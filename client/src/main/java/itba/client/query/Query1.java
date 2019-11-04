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
import itba.model.query1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query1 implements Query {

    private static final Logger LOGGER = LoggerFactory.getLogger(Query.class);

    private HazelcastInstance hazelcastInstance;
    private IList<Airport> airports;
    private IList<Movement> movements;
    private Writer writer;

    public Query1(final HazelcastInstance hazelcastInstance, final IList<Airport> airports, final IList<Movement> movements, final String outPath) {
        this.hazelcastInstance = hazelcastInstance;
        this.airports = airports;
        this.movements = movements;
        this.writer = new Writer(outPath + "query1.csv");
    }

    @Override
    public void run() throws InterruptedException, ExecutionException {

        JobTracker jobTracker = hazelcastInstance.getJobTracker("Query1");

        KeyValueSource<String, Movement> source = KeyValueSource.fromList(movements);
        Job<String, Movement> job = jobTracker.newJob(source);

        ICompletableFuture<Map<String, Integer>> completableFuture = job
                .mapper(new g10MovementsPerAirportMapper())
                .combiner(new g10MovementsPerAirportCombiner())
                .reducer(new g10MovementsPerAirportReducer())
                .submit();

        Map<String, Integer> oaciMap = completableFuture.get();
        List<AirportWithResult> sortedAirportWithResultList = sortAndFilterResult(oaciMap);

        printResults(sortedAirportWithResultList);
    }

    private List<AirportWithResult> sortAndFilterResult(final Map<String, Integer> oaciMap) {

        List<AirportWithResult> airportWithResultList = convertFromOACIMapToAirportWithResultList(oaciMap);

        airportWithResultList.sort(AirportWithResult::compareTo);

        airportWithResultList = airportWithResultList.stream().filter(airportWithResult -> airportWithResult.result != 0)
                .collect(Collectors.toList());

        return airportWithResultList;
    }

    private List<AirportWithResult> convertFromOACIMapToAirportWithResultList(final Map<String, Integer> oaciMap) {
        List<AirportWithResult> airportWithResultList = new LinkedList<>();

        airports.forEach(airport -> airportWithResultList.add(new AirportWithResult(airport,
                oaciMap.get(airport.getOACI()) == null ? 0 : oaciMap.get(airport.getOACI()))));

        return airportWithResultList;
    }

    private void printResults(List<AirportWithResult> sortedAirportWithResultList){
        writer.writeString("OACI;Denominación;Movimientos\n");

        sortedAirportWithResultList.forEach(airportWithResult -> writer.writeString(airportWithResult.airport.getOACI()
                + ";" + airportWithResult.airport.getDenomination() + ";" + airportWithResult.result + "\n"));
    }

    private static class AirportWithResult implements Comparable<AirportWithResult> {
        private Airport airport;
        private int result;

        AirportWithResult(final Airport airport, final int result) {
            this.airport = airport;
            this.result = result;
        }

        @Override
        public int compareTo(final AirportWithResult airportWithResult) {
            if (result == airportWithResult.result) {
                return airport.getOACI().compareTo(airportWithResult.airport.getOACI());
            }

            return airportWithResult.result - result;
        }
    }
}
