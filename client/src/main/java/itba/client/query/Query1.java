package itba.client.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import itba.model.Airport;
import itba.model.Movement;
import itba.model.query1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query1 implements Query {

    private HazelcastInstance hazelcastInstance;
    private IList<Airport> airports;
    private IList<Movement> movements;
    private Logger logger;

    public Query1(final HazelcastInstance hazelcastInstance, final IList<Airport> airports, final IList<Movement> movements) {
        this.hazelcastInstance = hazelcastInstance;
        this.airports = airports;
        this.movements = movements;
        this.logger = LoggerFactory.getLogger(Query.class);
    }

    @Override
    public void run() throws InterruptedException, ExecutionException {

        JobTracker jobTracker = hazelcastInstance.getJobTracker("Query1");

        KeyValueSource<String, Movement> source = KeyValueSource.fromList(movements);
        Job<String, Movement> job = jobTracker.newJob(source);

        ICompletableFuture<Map<String, Integer>> completableFuture = job
                .mapper(new MovementsPerAirportMapper())
                .combiner(new MovementsPerAirportCombiner())
                .reducer(new MovementsPerAirportReducer())
                .submit();

        Map<String, Integer> oaciMap = completableFuture.get();

        // todo: Ordenar y guardar el resultado de oaciMap en el archivo de salida.

        System.out.println();

    }
}
