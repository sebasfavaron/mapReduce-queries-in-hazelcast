package itba.server;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IdGenerator;
import itba.model.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("mapReduce Server Starting ...");

        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();

        //Map example in hz
        Map<Long, String> map = hzInstance.getMap("mapExample");
        IdGenerator idGenerator = hzInstance.getIdGenerator("newid");
        for (int i = 0; i < 10; i++) {
            map.put(idGenerator.newId(), "message" + i);
        }

        //Load movements
        List<Movement> movements = hzInstance.getList("movements");
        CsvLoader csvLoader = new CsvLoader();
        csvLoader.loadMovement(movements, "movimientos.csv");

    }
}
