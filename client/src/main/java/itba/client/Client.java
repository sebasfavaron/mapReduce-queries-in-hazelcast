package itba.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import itba.client.query.Query;
import itba.client.query.Query1;
import itba.client.query.Query2;
import itba.client.query.Query4;
import itba.model.Airport;
import itba.model.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

public class Client {
    private static Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private static String AIRPORTS_CSV = "aeropuertos.csv";
    private static String MOVEMENTS_CSV = "movimientos.csv";
    private static String TIMESTAMP_FILENAME = "timestamp.txt";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LOGGER.info("mapReduce Client Starting ...");

        Writer timestamp = new Writer(TIMESTAMP_FILENAME);

        // todo: arreglar tema de parámetros
        ArgumentParser parser = new ArgumentParser();
        ClientArguments arguments = parser.parse(args);

        ClientConfig config = new ClientConfig();
        ClientNetworkConfig networkConfig = config.getNetworkConfig();
        arguments.getAddresses().forEach(address -> networkConfig.addAddress(address.getAddress()));
        networkConfig.setSmartRouting(false);
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev");
        groupConfig.setPassword("dev-pass");

        HazelcastInstance hzClient = HazelcastClient.newHazelcastClient(config);

        logToWriter(timestamp, "Inicio de la lectura del archivo");

        // Load airports
        IList<Airport> airports = hzClient.getList("airports");

        //Load movements
        IList<Movement> movements = hzClient.getList("movements");

        // todo: checkear que exista el archivo (como? hay IOException adentro)
        // todo: arreglar tema de parámetros (agregar el path completo al principio de aeropuertos.csv)
        CsvLoader csvLoader = new CsvLoader();
        csvLoader.loadAirports(airports, arguments.getInPath() + AIRPORTS_CSV);
        csvLoader.loadMovements(movements, arguments.getInPath() + MOVEMENTS_CSV);

        logToWriter(timestamp, "Fin la lectura del archivo");

        // todo: switch entre todas las queries
        Query query = query(arguments.getQueryNumber(), hzClient, airports, movements, arguments);

        logToWriter(timestamp, "Inicio del trabajo map/reduce");

        query.run();

        logToWriter(timestamp, "Fin del trabajo map/reduce");

        airports.destroy();
        movements.destroy();
        hzClient.shutdown();
    }

    private static Query query(final int queryNumber, final HazelcastInstance hazelcastInstance,
                               final IList<Airport> airports, final IList<Movement> movements,
                               final ClientArguments clientArguments) {

        Query query = null;

        // Nunca va a entrar al caso default ni va a ser null porque viene desde los .sh, no desde el usuario
        switch (queryNumber) {
            case 1:
                query = new Query1(hazelcastInstance, airports, movements, clientArguments.getOutPath());
                break;

            case 2:
                query = new Query2(hazelcastInstance, airports, movements, 5, clientArguments.getOutPath());
                break;

            case 4:
                query = new Query4(hazelcastInstance, airports, movements, clientArguments.getOACI(), clientArguments.getN(), clientArguments.getOutPath());
                break;
        }

        return query;
    }

    private static void logToWriter(final Writer writer, final String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:S");
        writer.writeString(LocalDateTime.now().format(formatter) + " INFO " + string + "\n");
    }
}
