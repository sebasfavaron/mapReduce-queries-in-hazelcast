package itba.client.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import itba.client.Writer;
import itba.model.Airport;
import itba.model.Movement;
import itba.model.query2.CabotageMovementsPerAirlineCombiner;
import itba.model.query2.CabotageMovementsPerAirlineMapper;
import itba.model.query2.CabotageMovementsPerAirlineReducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Query2 implements Query {

    private static final Logger LOGGER = LoggerFactory.getLogger(Query.class);

    private HazelcastInstance hazelcastInstance;
    private IList<Airport> airports;
    private IList<Movement> movements;
    private Integer n;
    private Writer writer;

    public Query2(final HazelcastInstance hazelcastInstance, final IList<Airport> airports, final IList<Movement> movements, final Integer n) {
        this.hazelcastInstance = hazelcastInstance;
        this.airports = airports;
        this.movements = movements;
        this.n = n;
        this.writer = new Writer("query2.csv");
    }

    @Override
    public void run() throws InterruptedException, ExecutionException {

        JobTracker jobTracker = hazelcastInstance.getJobTracker("Query2");

        KeyValueSource<String, Movement> source = KeyValueSource.fromList(movements);
        Job<String, Movement> job = jobTracker.newJob(source);

        ICompletableFuture<Map<String, Integer>> completableFuture = job
                .mapper(new CabotageMovementsPerAirlineMapper())
                .combiner(new CabotageMovementsPerAirlineCombiner())
                .reducer(new CabotageMovementsPerAirlineReducer())
                .submit();

        Map<String, Integer> cabotageMovementsPerAirline = completableFuture.get();

        AtomicInteger totalCabotageFlights = getTotalCabotageFlights(cabotageMovementsPerAirline);

        printResults(cabotageMovementsPerAirline, totalCabotageFlights.get());
    }

    private AtomicInteger getTotalCabotageFlights(Map<String, Integer> cabotageMovementsPerAirline) {
        AtomicInteger totalCabotageFlights = new AtomicInteger();

        cabotageMovementsPerAirline.forEach((key, value) -> {
            totalCabotageFlights.addAndGet(value);
        });

        return totalCabotageFlights;
    }

    private void printResults(Map<String, Integer> airlanes, Integer totalCabotageFlights) {
        AtomicInteger topFlights = new AtomicInteger();

        final Map<String, Integer> topAirlines = airlanes.entrySet()
                .stream()
                .filter(airline -> !airline.getKey().equals("N/A") && !airline.getKey().equals(""))
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed().thenComparing(Map.Entry::getKey))
                .limit(this.n)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        topAirlines.forEach((key, value) -> {
            topFlights.addAndGet(value);
            double percentage = (double) value / totalCabotageFlights;
            printLine(key, percentage * 100);
        });

        if (topFlights.get() != totalCabotageFlights) {
            double percentage = (double) topFlights.get() / totalCabotageFlights;
            printLine("Otros", (1 - percentage) * 100);
        }
    }

    private void printLine(String airlane, Double percentage) {
        writer.writeString(airlane + ";" + String.format("%.2f", percentage) + "%\n");
    }
}
