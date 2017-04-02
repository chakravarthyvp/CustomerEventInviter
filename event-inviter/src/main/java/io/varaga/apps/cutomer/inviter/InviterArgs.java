package io.varaga.apps.cutomer.inviter;

import java.io.*;

import org.kohsuke.args4j.Option;

/**
 * Bean with the command args line needed by the Customer Invite application.
 */
public class InviterArgs {

    @Option(name = "-proximityInKm", usage = "proximity to the event location,in KMs. Customers within this distance are invited, Defaults to 100 KM")
    private int proximityInKm = 100;

    @Option(name = "-customersFile", usage = "absolute path to file with customer and their position info, if not provided then its loaded from the classpath")
    private String customersFile;

    @Option(name = "-invitingLocationLat", usage = "Required: event location latitude, if not, 53.3393 is taken latitude")
    private double invitingLocationLat = 53.3393;

    @Option(name = "-invitingLocationLong", usage = "Required: event location longitude, if not, -6.2576841 is taken to be longitude")
    private double invitingLocationLong = -6.2576841;

    public int getProximityInKm() {
        return proximityInKm;
    }

    public void setProximityInKm(final int distanceInKm) {
        this.proximityInKm = distanceInKm;
    }

    public String getCustomersFile() {
        return this.customersFile;
    }

    public void setCustomersFile(final String customersFile) {
        this.customersFile = customersFile;
    }

    public double getInvitingLocationLat() {
        return invitingLocationLat;
    }

    public void setInvitingLocationLat(final double invitingLocationLat) {
        this.invitingLocationLat = invitingLocationLat;
    }

    public void setInvitingLocationLong(final double invitingLocationLong) {
        this.invitingLocationLong = invitingLocationLong;
    }

    public double getInvitingLocationLong() {
        return invitingLocationLong;
    }

    /**
     * Returns an Input Stream to the specified customers file.
     *
     * @return The given argument for customersFile wrapped as an {@link InputStream} or, if none is given, customersFile is loaded from the
     *         classpath.
     */
    public InputStream getCustomersFileInputStream() {
        try {
            return getCustomersFile() != null ? new FileInputStream(getCustomersFile()) : getDefaultCustomersFile();
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getDefaultCustomersFile() {
        return this.getClass().getResourceAsStream("/CustomerFile.json");
    }

}