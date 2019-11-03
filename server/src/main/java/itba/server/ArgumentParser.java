package itba.server;

public class ArgumentParser {
    public ServerArguments parse(final String[] args) {
        String Dport = "-Dport=";
        ServerArguments serverArguments = new ServerArguments();
        for (String arg : args) {
            if (arg.startsWith(Dport)) {
                serverArguments.setPort(Integer.parseInt(getArgumentValue(arg, Dport)));
            }
        }

        return serverArguments;
    }

    private String getArgumentValue(final String arg, final String key) {
        return arg.substring(key.length());
    }
}
