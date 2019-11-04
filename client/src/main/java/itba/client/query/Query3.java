package itba.client.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import itba.client.Writer;
import itba.model.Movement;
import itba.model.query1.g10MovementsPerAirportCombiner;
import itba.model.query1.g10MovementsPerAirportMapper;
import itba.model.query1.g10MovementsPerAirportReducer;
import itba.model.query3.g10ThounsandMovementsPerAirportMapper;
import itba.model.query3.g10ThounsandMovementsPerAirportReducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query3 implements Query {

    private static final Logger LOGGER = LoggerFactory.getLogger(Query.class);

    private HazelcastInstance hazelcastInstance;
    private IList<Movement> movements;
    private Writer writer;

    public Query3(final HazelcastInstance hazelcastInstance, final IList<Movement> movements, final String outPath) {
        this.hazelcastInstance = hazelcastInstance;
        this.movements = movements;
        this.writer = new Writer(outPath + "query3.csv");
    }

    @Override
    public void run() throws InterruptedException, ExecutionException {
        JobTracker jobTracker = hazelcastInstance.getJobTracker("Query3");

        KeyValueSource<String, Movement> source = KeyValueSource.fromList(movements);
        Job<String, Movement> job = jobTracker.newJob(source);

        ICompletableFuture<Map<String, Integer>> completableFuture = job
                .mapper(new g10MovementsPerAirportMapper())
                .combiner(new g10MovementsPerAirportCombiner())
                .reducer(new g10MovementsPerAirportReducer())
                .submit();

        Map<String, Integer> oaciMovementsMap = completableFuture.get();

        IMap<String, Integer> hzOaciMovementsMap = hazelcastInstance.getMap("thounsandsMovements");
        hzOaciMovementsMap.putAll(oaciMovementsMap);

        KeyValueSource<String, Integer> newSource = KeyValueSource.fromMap(hzOaciMovementsMap);
        Job<String, Integer> newJob = jobTracker.newJob(newSource);

        ICompletableFuture<Map<Integer, List<String>>> newCompletableFuture = newJob
                .mapper(new g10ThounsandMovementsPerAirportMapper())
                .reducer(new g10ThounsandMovementsPerAirportReducer())
                .submit();

        Map<Integer, List<String>> thousandMovementsPerAirport = newCompletableFuture.get();
        hzOaciMovementsMap.destroy();

        List<QueryResults> queryResults = getQueryResults(thousandMovementsPerAirport);

        printResults(queryResults);
    }

    private List<QueryResults> getQueryResults(Map<Integer, List<String>> thousandMovementsPerAirport) {
        List<QueryResults> queryResults = new ArrayList<>();
        List<String> oaciAddedList = new ArrayList<>();

        thousandMovementsPerAirport.forEach((thounsands, oaciList) -> {
            Collections.sort(oaciList);

            oaciList.forEach(oaci1 -> {
                oaciAddedList.add(oaci1);
                oaciList.forEach(oaci2 -> {
                    if (!oaci1.equals(oaci2) && !oaciAddedList.contains(oaci2)){
                        queryResults.add(new QueryResults(thounsands, oaci1, oaci2));
                    }
                });
            });
        });

        Collections.sort(queryResults);

        return queryResults;
    }

    private void printResults(List<QueryResults> queryOutput) {
        writer.writeString("Grupo;Aeropuerto A;Aeropuerto B\n");
        for(QueryResults result : queryOutput) {
            writer.writeString(result.toString());
        }
    }

    private class QueryResults implements Comparable<QueryResults> {
        private final Integer thousandMovements;
        private final String oaci1;
        private final String oaci2;

        public QueryResults(int thousandMovements, String oaci1, String oaci2) {
            this.thousandMovements = thousandMovements;
            this.oaci1 = oaci1;
            this.oaci2 = oaci2;
        }

        @Override
        public String toString() {
            return thousandMovements + ";" + oaci1 + ";" + oaci2 + "\n";
        }

        @Override
        public int compareTo(QueryResults q) {
            return q.thousandMovements - thousandMovements;
        }
    }
}
