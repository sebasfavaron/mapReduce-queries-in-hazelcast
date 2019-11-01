package itba.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import itba.client.query.Query;
import itba.client.query.Query1;
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
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev");
        groupConfig.setPassword("dev-pass");
        HazelcastInstance hzClient = HazelcastClient.newHazelcastClient(config);

        CsvLoader csvLoader = new CsvLoader();

        // Load airports
        IList<Airport> airports = hzClient.getList("airports");

        //Load movements
        IList<Movement> movements = hzClient.getList("movements");

        // todo: checkear que exista el archivo
        csvLoader.loadAirport(airports, "aeropuertos.csv");
        csvLoader.loadMovement(movements, "movimientos.csv");

        // todo: switch entre todas las queries
        Query query = new Query1(airports, movements);
        query.run();

        movements.destroy();
        hzClient.shutdown();
    }
}
