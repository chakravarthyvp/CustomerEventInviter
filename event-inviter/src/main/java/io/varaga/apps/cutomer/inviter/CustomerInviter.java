package io.varaga.apps.cutomer.inviter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.kohsuke.args4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

import io.apps.events.model.CustomerPositionInfo;
import io.varaga.apps.customer.utils.CustomerUserIdComparator;
import io.varaga.apps.utils.file.CustomerFileReaderUtils;
import io.varaga.apps.utils.geo.GeoLocation;
import io.varaga.apps.utils.geo.GeoUtils;

/**
 * This class is resonsible to find those customers that are within the certain distance as specified by the calling application. The class performs
 * multiple key functions. It maps the customers to a grid based on hash key. An event location is mapped to a grid key. Event's grid key is looked up in the cache, to find those customers
 * that are within this grid. Distance computation applied with the subset of customers in the grid. It sorts those customers based on userId.
 *
 */
public class CustomerInviter {

    private static final Logger logger = LoggerFactory.getLogger(CustomerInviter.class);

    /**
     * Maps a list customers to a grid key. Used internally for distributing customers based on a Hashed Group key (called as Grid Key).
     */
    private final Map<String, List<CustomerPositionInfo>> customerGridGroup = new ConcurrentHashMap<String, List<CustomerPositionInfo>>();

    /**
     * Argument supplied in the command line.
     */
    private final InviterArgs inviterArgs;

    public CustomerInviter(final String... args) {
        inviterArgs = getInviterArgs(args);
    }

    /**
     * Gets the customers within a proximity.
     *
     * @param sortedCustomers
     *            - if true, the customers are sorted. Sorting is based on userId of this customer {@link CustomerUserIdComparator}
     * @return - A set of customers within the proximity as specied by {@link InviterArgs}
     * @throws JsonSyntaxException
     *             - thrown if the customers file could not be parsed.
     * @throws IOException
     *             - if the file does not exists or could not be read.
     */
    public Set<CustomerPositionInfo> getCustomersWithinProximity(final boolean sortedCustomers) throws JsonSyntaxException, IOException {

        final List<CustomerPositionInfo> customersPositions = CustomerFileReaderUtils
                .readCustomerPositionFromJsonFile(inviterArgs.getCustomersFileInputStream());
        // map the customer into a grid.
        mapCustomersToGridKey(customersPositions);
        logger.info("Customers are grouped");
        //get the event location (GPS coordinate)
        final GeoLocation eventLocation = GeoLocation.fromDegrees(inviterArgs.getInvitingLocationLat(), inviterArgs.getInvitingLocationLong());
        logger.info("Event Location determined: {}" + eventLocation);

        //Generate a grid key for the event location that matches 100 KM boundary hashes
        final String hashForCenterLocation = GeoUtils.getCellHashGroupFor100KMs(eventLocation.getLatitudeInDegrees(),
                eventLocation.getLongitudeInDegrees());
        //Now call the customers within this grid
        return getCustomersWithinThisGrid(hashForCenterLocation, sortedCustomers, eventLocation);
    }

    /**
     * This method groups the customers into a respective grid key based on the radius as is supplied in {@link InviterArgs}. This logical grouping is
     * used to hash & process ONLY those customers that are within the proximity.
     *
     * @param customerList
     *            - the list of customers that are to be determined for the invite.
     */
    private void mapCustomersToGridKey(final List<CustomerPositionInfo> customerList) {
        for (final CustomerPositionInfo custPosition : customerList) {
            // get the grid key, bounding box with 100 KM, for this customer.
            final String hash = GeoUtils.getCellHashGroupFor100KMs(custPosition.getLatitude(), custPosition.getLongitude());
            List<CustomerPositionInfo> customersGroupList = customerGridGroup.get(hash);
            if (null == customersGroupList) {
                customersGroupList = new ArrayList<CustomerPositionInfo>();
                //get the customer list that belong to the same grid key. If the customer list does not exists load one.
                customerGridGroup.put(hash, customersGroupList);
            }
            // Load the customer into the list grouped into the grid key.
            customerGridGroup.get(hash).add(custPosition);
            logger.debug("CustomerId: {} hashed into grid key: {}", custPosition.getUserId(), hash);
        }
    }

    /**
     * This method gets the list of customers, that are within the grid, and calculates to confirm the distance if within the expected distance from
     * the event location.
     *
     * @param gridKey
     *            - hashed grid key to look up the customers group
     * @param eventLocation
     *            - GeoLocation encompassing the latitude and longitude of the event
     * @param customerSorted
     *            - if true, sorts the returned set.
     * @return - Set of customers within the range as in {@link InviterArgs}, empty Set if no customers belong to this grid key
     */
    private Set<CustomerPositionInfo> getCustomersWithinThisGrid(final String gridKey, final boolean customerSorted,
                                                                 final GeoLocation eventLocation) {
        logger.debug("Event Location Grid Key: " + gridKey);
        final List<CustomerPositionInfo> customersGroupList = customerGridGroup.get(gridKey);

        //Customers who needs to be invited are Sorted using a comparator based on the user ID
        Set<CustomerPositionInfo> invitedCustomers = null;
        if (customerSorted) {
            invitedCustomers = new TreeSet<CustomerPositionInfo>(new CustomerUserIdComparator());
        } else {
            invitedCustomers = new HashSet<CustomerPositionInfo>();
        }

        //if we don't find any customer within this grid key... we return an empty customer group
        if (null == customersGroupList) {
            return invitedCustomers;
        }
        for (final CustomerPositionInfo customer : customersGroupList) {
            // calculate distance between customer location & event location
            final double customerDistanceFromEvent = GeoUtils.getDistanceInKMBetween(customer.getLatitude(), customer.getLongitude(),
                    eventLocation.getLatitudeInDegrees(), eventLocation.getLongitudeInDegrees());

            if (customerDistanceFromEvent <= inviterArgs.getProximityInKm()) {
                //customer is within the proximity, lets add him to the invite set.
                invitedCustomers.add(customer);
            }
        }
        return invitedCustomers;
    }

    private InviterArgs getInviterArgs(final String... args) {
        final InviterArgs inviterArgs = new InviterArgs();
        final CmdLineParser parser = getCmdLineParserFor(inviterArgs);
        try {
            parser.parseArgument(Arrays.copyOfRange(args, 0, args.length));
        } catch (final CmdLineException e) {
            printErrorAndUsage(e, parser);
        }
        return inviterArgs;
    }

    private void printErrorAndUsage(final CmdLineException e, final CmdLineParser parser) {
        logger.error(e.getMessage());
        logger.info("Usage:");
        parser.printUsage(System.out);
    }

    private CmdLineParser getCmdLineParserFor(final Object bean) {
        final ParserProperties properties = ParserProperties.defaults().withUsageWidth(120);
        return new CmdLineParser(bean, properties);
    }
}
