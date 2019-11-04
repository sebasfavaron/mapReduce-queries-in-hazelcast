package itba.client.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import itba.client.Writer;
import itba.model.Movement;
import itba.model.query4.g10DestinyAirportQuantityCombiner;
import itba.model.query4.g10DestinyAirportQuantityMapper;
import itba.model.query4.g10DestinyAirportQuantityReducer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query4 implements Query {

    private HazelcastInstance hazelcastInstance;
    private IList<Movement> movements;
    private String destinyOACI;
    private int n;
    private Writer writer;

    public Query4(final HazelcastInstance hazelcastInstance, final IList<Movement> movements, final String destinyOACI,
                  final int n, final String outPath) {
        this.hazelcastInstance = hazelcastInstance;
        this.movements = movements;
        this.destinyOACI = destinyOACI;
        this.n = n;
        this.writer = new Writer(outPath + "query4.csv");
    }

    @Override
    public void run() throws InterruptedException, ExecutionException {

        JobTracker jobTracker = hazelcastInstance.getJobTracker("Query4");

        KeyValueSource<String, Movement> source = KeyValueSource.fromList(movements);
        Job<String, Movement> job = jobTracker.newJob(source);

        ICompletableFuture<Map<String, Integer>> completableFuture = job
                .mapper(new g10DestinyAirportQuantityMapper(destinyOACI))
                .combiner(new g10DestinyAirportQuantityCombiner())
                .reducer(new g10DestinyAirportQuantityReducer())
                .submit();

        Map<String, Integer> destinyAirportQuantityMap = completableFuture.get();

        List<DestinyAirport> destinyAirports = sortAndFilterResult(destinyAirportQuantityMap);

        printResults(destinyAirports);
    }

    private List<DestinyAirport> sortAndFilterResult(final Map<String, Integer> destinyAirportQuantityMap) {
        List<DestinyAirport> ret = new LinkedList<>();

        destinyAirportQuantityMap.forEach((k, v) -> ret.add(new DestinyAirport(k, v)));

        ret.sort(DestinyAirport::compareTo);

        return ret.stream().limit(n).collect(Collectors.toList());
    }

    private void printResults(List<DestinyAirport> destinyAirports){
        writer.writeString("OACI;Despegues\n");

        destinyAirports.forEach(destinyAirport -> writer.writeString(destinyAirport.oaci + ";"
                + destinyAirport.quantity + "\n"));
    }

    private static class DestinyAirport implements Comparable<DestinyAirport> {

        private String oaci;
        private int quantity;

        DestinyAirport(final String oaci, final int quantity) {
            this.oaci = oaci;
            this.quantity = quantity;
        }

        @Override
        public int compareTo(final DestinyAirport destinyAirport) {
            if (quantity == destinyAirport.quantity) {
                return oaci.compareTo(destinyAirport.oaci);
            }

            return destinyAirport.quantity - quantity;
        }
    }
}
