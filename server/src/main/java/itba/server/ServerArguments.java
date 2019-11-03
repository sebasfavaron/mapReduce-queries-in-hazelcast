package itba.server;

public class ServerArguments {

    private int port = 5701;

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

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public void setPort(final String port) {
        this.port = stringToInt(port, "Not a valid number");
    }
}
