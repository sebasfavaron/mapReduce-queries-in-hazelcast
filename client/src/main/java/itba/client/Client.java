package itba.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import itba.client.query.Query;
import itba.client.query.Query1;
import itba.client.query.Query2;
import itba.model.Airport;
import itba.model.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutionException;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("mapReduce Client Starting ...");

        ArgumentParser parser = new ArgumentParser();
        ClientArguments arguments = parser.parse(args);

        ClientConfig config = new ClientConfig();
        arguments.getAddresses().forEach(address -> config.getNetworkConfig().addAddress(address.getHost() + ":" + address.getPort()));
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev");
        groupConfig.setPassword("dev-pass");
        HazelcastInstance hzClient = HazelcastClient.newHazelcastClient(config);

        // Load airports
        IList<Airport> airports = hzClient.getList("airports");

        //Load movements
        IList<Movement> movements = hzClient.getList("movements");

        // todo: checkear que exista el archivo (como? hay IOException adentro)
        CsvLoader csvLoader = new CsvLoader();
        csvLoader.loadAirports(airports, arguments.getInPath()+"aeropuertos.csv");
        csvLoader.loadMovements(movements, arguments.getInPath()+"movimientos.csv");
        
        // todo: switch entre todas las queries
        Query query = new Query2(airports, movements, 5);
        query.run();

        movements.destroy();
        hzClient.shutdown();
    }
}
