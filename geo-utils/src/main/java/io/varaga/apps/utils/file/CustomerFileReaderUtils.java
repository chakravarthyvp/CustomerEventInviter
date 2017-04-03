package io.varaga.apps.utils.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.apps.events.model.CustomerPositionInfo;

/**
 * Class is responsible to read the customer json file and dynamically bind the model POJO classes in {@link io.apps.events.model.*}
 *
 * @TODO - This class can refactor to read the file and update a cache dynamically, listen for changes in the file system and load new files etc.,
 */
public class CustomerFileReaderUtils<T> {

    private CustomerFileReaderUtils() {
    }

    /**
     * Given a Customer Position file wrapped as an {@link InputStream}, it will read all the lines and will return a list with all the lat/long.
     *
     * @param customerPositionFileStream
     * @return A List of {@link CustomerPositionInfo}.
     * @throws IOException
     *             - if file could not be read
     * @throws IllegalArgumentException
     *             If file could not be read.
     */
    public static List<CustomerPositionInfo> readCustomerPositionFromJsonFile(final InputStream customerPositionFileStream) throws IOException {
        BufferedReader br = null;

        final ObjectMapper mapper = new ObjectMapper();
        final List<CustomerPositionInfo> customerPositionList = new ArrayList<CustomerPositionInfo>();
        try {
            br = new BufferedReader(new InputStreamReader(customerPositionFileStream, "UTF-8"));
            String line = null;
            while (null != (line = br.readLine())) {
                final CustomerPositionInfo cPInfo = mapper.readValue(line, CustomerPositionInfo.class);
                if (null != cPInfo) {
                    customerPositionList.add(cPInfo);
                }
            }
        } finally {
            if (null != br) {
                br.close();
            }
        }
        return customerPositionList;
    }
}
