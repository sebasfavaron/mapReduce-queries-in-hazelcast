package itba.client;

import java.util.ArrayList;
import java.util.List;

public class ClientArguments {

    private int queryNumber;
    private List<Address> addresses;
    private String inPath, outPath, OACI;
    private Integer n, min;

    public ClientArguments() {
        this.addresses = new ArrayList<>();
        this.inPath = null;
        this.outPath = null;
        this.OACI = null;
        this.n = null;
        this.min = null;
    }

    public class Address {
        private String host;
        private Integer port;

        public Address(String host, String port) {
            if (!host.matches("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|localhost")) {
                System.out.println("Host is not valid");
                System.exit(-1);
            }
            this.host = host;

            this.port = stringToInt(port, "Port is not valid");
        }

        public String getHost() {
            return host;
        }

        public Integer getPort() {
            return port;
        }
    }

    private Integer stringToInt(final String str, final String errorMsg) {
        if (str == null) {
            System.out.println(errorMsg);
            System.exit(-1);
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println(errorMsg);
            System.exit(-1);
        }
        return null;
    }

    public int getQueryNumber() {
        return queryNumber;
    }

    public void setQueryNumber(final int queryNumber) {
        this.queryNumber = queryNumber;
    }

    public String getInPath() {
        return inPath;
    }

    public void setInPath(final String inPath) {
        this.inPath = inPath;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(final String outPath) {
        this.outPath = outPath;
    }

    public String getOACI() {
        return OACI;
    }

    public void setOACI(final String OACI) {
        this.OACI = OACI;
    }

    public Integer getN() {
        return n;
    }

    public void setN(final String n) {
        this.n = stringToInt(n, "Not a valid number");
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(final String min) {
        this.min = stringToInt(min, "Not a valid minimum number");
    }

    public void addAddress(String host, String port) {
        addresses.add(new Address(host, port));
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
