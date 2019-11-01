package itba.client;

public class ArgumentParser {
    public ClientArguments parse(final String[] args) {
        String Daddresses = "-Daddresses=", DinPath = "-DinPath=", DoutPath = "-DoutPath=", Doaci = "-Doaci=", Dn = "-Dn=", Dmin = "-Dmin=";
        ClientArguments clientArguments = new ClientArguments();
        for (String arg : args) {
            if (arg.startsWith(Daddresses)) {
                String val = getArgumentValue(arg, Daddresses);
                String[] address = val.split(":");
                clientArguments.setHost(address[0]);
                clientArguments.setPort(address[1]);
            } else if (arg.startsWith(DinPath)) {
                clientArguments.setInPath(getArgumentValue(arg, DinPath));
            } else if (arg.startsWith(DoutPath)) {
                clientArguments.setOutPath(getArgumentValue(arg, DoutPath));
            } else if (arg.startsWith(Doaci)) {
                clientArguments.setOACI(getArgumentValue(arg, Doaci));
            } else if (arg.startsWith(Dn)) {
                clientArguments.setN(getArgumentValue(arg, Dn));
            } else if (arg.startsWith(Dmin)) {
                clientArguments.setMin(getArgumentValue(arg, Dmin));
            } else {
                System.out.println("\"" + arg + "\" is not a valid argument");
                System.exit(-1);
            }
        }

        return clientArguments;
    }

    private String getArgumentValue(final String arg, final String key) {
        return arg.substring(key.length());
    }
}
