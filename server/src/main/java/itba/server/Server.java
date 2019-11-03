package itba.server;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import itba.model.HazelcastConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(final String[] args) {

        LOGGER.info("Inicializando servidor ...");

        ArgumentParser parser = new ArgumentParser();
        ServerArguments arguments = parser.parse(args);

        // Los archivos se tienen que procesar cada vez que se usa una query,
        // por eso no hay que inicializar nada más acá
        Config config = new Config();
        config.getGroupConfig().setName(HazelcastConfig.CLUSTER_NAME);
        NetworkConfig network = config.getNetworkConfig();
        network.setPort(arguments.getPort()).setPortCount(50);
        network.setPortAutoIncrement(true);
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig()
                .addMember("localhost")
                .setEnabled(true);

        Hazelcast.newHazelcastInstance(config);
    }
}
