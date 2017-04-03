package io.varaga.apps.cutomer.inviter;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

import io.apps.events.model.CustomerPositionInfo;

/**
 * Customer Invite application.
 *
 * @author Varaga
 *
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String... args) throws JsonSyntaxException, IOException {
        //return sorted customers who need to be invited
        final Set<CustomerPositionInfo> invitedCustomers = new CustomerInviter(args).getCustomersWithinProximity(true);
        logger.info("Following Customers are invited for Lunch:");
        logger.info("###################################################################");
        for (final CustomerPositionInfo customer : invitedCustomers) {
            logger.info("UserId: {}, Name: {}", customer.getUserId(), customer.getName());
        }
        logger.info("###################################################################");
    }
}
