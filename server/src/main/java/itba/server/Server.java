package itba.server;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(final String[] args) {
        logger.info("mapReduce Server Starting ...");

        // Los archivos se tienen que procesar cada vez que se usa una query,
        // por eso no hay que inicializar nada más acá
        Config config = new Config();
        NetworkConfig network = config.getNetworkConfig();
        network.setPort(5701).setPortCount(50);
        network.setPortAutoIncrement(true);
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig()
                .addMember("localhost")
                .setEnabled(true);
        HazelcastInstance hzServer = Hazelcast.newHazelcastInstance(config);
    }
}
