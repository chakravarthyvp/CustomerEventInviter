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

import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import io.apps.events.model.CustomerPositionInfo;
import io.varaga.apps.cutomer.inviter.InviterArgs;
import io.varaga.apps.utils.file.CustomerFileReaderUtils;

public class InviterArgsTest {

    @Test
    public void testInviterArgsReturnsDefaultArgs() {
        final InviterArgs args = new InviterArgs();
        assertThat("Default Proximity in Km is not loaded", args.getProximityInKm() == 100);
        assertThat("Default Customers File should be null", args.getCustomersFile() == null);
        assertThat("Default latitude is unexpected", args.getInvitingLocationLat() == 53.3393);
        assertThat("Default longitude is unexpected", args.getInvitingLocationLong() == -6.2576841);
    }

    @Test
    public void testInviterArgsLoadsFileInClasspath() throws IOException {
        final InviterArgs args = new InviterArgs();
        assertThat("Default Customers File should be null", args.getCustomersFile() == null);
        InputStream inputFile = null;
        try {
            inputFile = args.getCustomersFileInputStream();
            assertThat("Empty file from the resource loader is unexpected", inputFile.available() > 0);
        } finally {
            if (null != inputFile) {
                inputFile.close();
            }
        }
    }

    @Test
    public void testInviterArgsLoadsCustomerFileAsExpected() throws IOException {
        final int expectedCustomerCount = 32;
        final InviterArgs args = new InviterArgs();
        assertThat("Default Customers File should be null", args.getCustomersFile() == null);
        InputStream inputFile = null;
        try {
            inputFile = args.getCustomersFileInputStream();
            assertThat("Empty file from the resource loader is unexpected", inputFile.available() > 0);

            final List<CustomerPositionInfo> customersPositions = CustomerFileReaderUtils.readCustomerPositionFromJsonFile(inputFile);
            assertThat("Customer Json file from the resource path is not loaded:", customersPositions.size() == expectedCustomerCount);

        } finally {
            if (null != inputFile) {
                inputFile.close();
            }
        }
    }

}
