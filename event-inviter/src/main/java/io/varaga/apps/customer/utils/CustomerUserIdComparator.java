package io.varaga.apps.customer.utils;

import java.io.Serializable;
import java.util.Comparator;

import io.apps.events.model.CustomerPositionInfo;

/**
 * Class that compares the userId of {@link CustomerPositionInfo}. This ascending order comparator is intended to be used in calling functions that
 * sorts based on the user id of the customers.
 *
 */
public class CustomerUserIdComparator implements Comparator<CustomerPositionInfo>, Serializable {

    private static final long serialVersionUID = 4790050481877212768L;

    /**
     * Compares the userId with customers {@link CustomerPositionInfo}.
     *
     */
    public int compare(final CustomerPositionInfo o1, final CustomerPositionInfo o2) {
        final int userId1 = o1.getUserId();
        final long userId2 = o2.getUserId();
        if (userId1 == -1) {
            return 1;
        } else if (userId2 == -1) {
            return -1;
        }
        if (userId1 < userId2) {
            return -1;
        }
        return 1;

    }

}
