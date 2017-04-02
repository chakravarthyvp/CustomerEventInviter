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
package io.varaga.apps.customer.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import io.apps.events.model.CustomerPositionInfo;

/**
 * Tests comparisons on the {@link CustomerUserIdComparator}
 */
public class CustomerUserIdComparatorTest {

    @Test
    public void testUserIdsAreComparable() {
        final CustomerUserIdComparator comparator = new CustomerUserIdComparator();
        final CustomerPositionInfo customer1 = new CustomerPositionInfo();
        final CustomerPositionInfo customer2 = new CustomerPositionInfo();
        //1st userid is lesser
        customer1.setUserId(1);
        customer2.setUserId(2);
        int id = comparator.compare(customer1, customer2);
        assertEquals("userid1 should be lesser ", true, id < 1);

        //2nd userid is lesser
        customer1.setUserId(4);
        customer2.setUserId(2);
        id = comparator.compare(customer1, customer2);
        assertEquals("userid2 should be lesser ", true, id == 1);

        //both are equal
        customer1.setUserId(2);
        customer2.setUserId(2);
        id = comparator.compare(customer1, customer2);
        assertEquals("userid2 should be lesser ", true, id == 1);
    }

}
