/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package io.varaga.apps.customer.inviter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import io.apps.events.model.CustomerPositionInfo;
import io.varaga.apps.cutomer.inviter.CustomerInviter;

/**
 * Tests to verify te customer inviter application.
 */
public class CustomerInviterTest {

    private String testFilePath;

    @Before
    public void setUpTest() throws IOException {
        final File file = new File("src/test/resources/test-customers.json");
        testFilePath = file.getAbsolutePath();
    }

    @Test
    public void testVerifyDefaultFileReturnsRightCustomers() {
        try {
            final String[] args = {};
            final Set<CustomerPositionInfo> invitedCustomers = new CustomerInviter(args).getCustomersWithinProximity(true);
            assertThat("File in resource path not loaded?", invitedCustomers.size() == 9);
        } catch (final Exception e) {
            fail("Unexpected exception: " + e);
        }

    }

    @Test
    public void testVerifyCustomersFileLoadsRightCustomers() {
        try {
            final String[] args = { "-customersFile", testFilePath };
            final Set<CustomerPositionInfo> invitedCustomers = new CustomerInviter(args).getCustomersWithinProximity(true);
            assertThat("test-customers file not readable or loaded?", invitedCustomers.size() == 2);
        } catch (final Exception e) {
            fail("Unexpected exception: " + e);
        }

    }

    @Test
    public void testVerifyCustomersAreSorted() {
        try {
            final String[] args = { "-customersFile", testFilePath };
            final Set<CustomerPositionInfo> invitedCustomers = new CustomerInviter(args).getCustomersWithinProximity(true);
            assertThat("test-customers file not readable or loaded?", invitedCustomers.size() == 2);

            final List<CustomerPositionInfo> custVerificationList = getCustomerList(invitedCustomers);
            assertThat("Has order changed", custVerificationList.get(0).getUserId() == 6);
            assertThat("has order changed", custVerificationList.get(1).getUserId() == 12);
        } catch (final Exception e) {
            fail("Unexpected exception: " + e);
        }

    }

    @Test
    public void testVerifyCustomersAreRandom() {
        try {
            final String[] args = { "-customersFile", testFilePath };
            final Set<CustomerPositionInfo> invitedCustomers = new CustomerInviter(args).getCustomersWithinProximity(false);
            assertThat("test-customers file not readable or loaded?", invitedCustomers.size() == 2);

            final List<CustomerPositionInfo> custVerificationList = getCustomerList(invitedCustomers);
            assertThat("", (custVerificationList.get(0).getUserId() == 6) || custVerificationList.get(0).getUserId() == 12);
        } catch (final Exception e) {
            fail("Unexpected exception: " + e);
        }

    }

    @Test
    public void testVerifyCustomersAreNotInvitedIfEventIsBeyondProximity() {
        try {
            final String germanyLat = "52.533951";
            final String germanyLong = "13.405519";

            final String[] args = { "-customersFile", testFilePath, "-invitingLocationLat", germanyLat, "-invitingLocationLong", germanyLong };
            final Set<CustomerPositionInfo> invitedCustomers = new CustomerInviter(args).getCustomersWithinProximity(false);
            assertThat("No customers should have been found within this grid", invitedCustomers.size() == 0);

        } catch (final Exception e) {
            fail("Unexpected exception: " + e);
        }

    }

    private List<CustomerPositionInfo> getCustomerList(final Set<CustomerPositionInfo> customersSet) {
        final List<CustomerPositionInfo> custList = new ArrayList<CustomerPositionInfo>();
        for (final CustomerPositionInfo customer : customersSet) {
            custList.add(customer);
        }
        return custList;
    }
}
