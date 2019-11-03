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
    private static Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private static String AIRPORTS_CSV = "aeropuertos.csv";
    private static String MOVEMENTS_CSV = "movimientos.csv";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LOGGER.info("mapReduce Client Starting ...");

        // todo: arreglar tema de parámetros
        ArgumentParser parser = new ArgumentParser();
        ClientArguments arguments = parser.parse(args);

        ClientConfig config = new ClientConfig();
        arguments.getAddresses()
                .forEach(address -> config.getNetworkConfig().addAddress(address.getHost() + ":" + address.getPort()));
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev");
        groupConfig.setPassword("dev-pass");

        HazelcastInstance hzClient = HazelcastClient.newHazelcastClient(config);

        // Load airports
        IList<Airport> airports = hzClient.getList("airports");

        //Load movements
        IList<Movement> movements = hzClient.getList("movements");

        // todo: checkear que exista el archivo (como? hay IOException adentro)
        // todo: arreglar tema de parámetros (agregar el path completo al principio de aeropuertos.csv)
        CsvLoader csvLoader = new CsvLoader();
        csvLoader.loadAirports(airports, arguments.getInPath() + AIRPORTS_CSV);
        csvLoader.loadMovements(movements, arguments.getInPath() + MOVEMENTS_CSV);

        // todo: switch entre todas las queries
        Query query = query(arguments.getQueryNumber(), hzClient, airports, movements);
        query.run();

        airports.destroy();
        movements.destroy();
        hzClient.shutdown();
    }

    private static Query query(final int queryNumber, final HazelcastInstance hazelcastInstance,
                               final IList<Airport> airports, final IList<Movement> movements) {

        Query query = null;

        // Nunca va a entrar al caso default ni va a ser null porque viene desde los .sh, no desde el usuario
        switch (queryNumber) {
            case 1:
                query = new Query1(hazelcastInstance, airports, movements);
        }

        return query;
    }
}
