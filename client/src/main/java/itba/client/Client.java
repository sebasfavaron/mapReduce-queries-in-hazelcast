package itba.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import itba.model.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        logger.info("mapReduce Client Starting ...");

        ArgumentParser parser = new ArgumentParser();
        ClientArguments arguments = parser.parse(args);

        ClientConfig config = new ClientConfig();
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev");
        groupConfig.setPassword("dev-pass");
        HazelcastInstance hzClient = HazelcastClient.newHazelcastClient(config);

        //Movements retrieval example in hz
        IList<Movement> movements = hzClient.getList("movements");
        System.out.println(movements.get(0));
    }
}
