package itba.client.query;

import com.hazelcast.core.IList;
import itba.model.Airport;
import itba.model.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class Query2 implements Query{

    private IList<Airport> airports;
    private IList<Movement> movements;
    private Integer n;
    private Logger logger;

    public Query2(final IList<Airport> airports, final IList<Movement> movements, final Integer n) {
        this.airports = airports;
        this.movements = movements;
        this.n = n;
        this.logger = LoggerFactory.getLogger(Query.class);
    }

    @Override
    public void run() throws InterruptedException, ExecutionException {
        HashMap<String, Integer> airlanes = new HashMap<>();
        AtomicReference<Integer> totalCabotageFlights = new AtomicReference<>(0);

        //Load airlanes and count of cabotage flights
        movements.forEach(movement -> {
            if (movement.getClassification().equals("Cabotaje")){
                totalCabotageFlights.getAndSet(totalCabotageFlights.get() + 1);

                if (airlanes.containsKey(movement.getAirline())){
                    airlanes.put(movement.getAirline(), airlanes.get(movement.getAirline()) + 1);
                } else {
                    airlanes.put(movement.getAirline(), 1);
                }
            }
        });

        printResults(airlanes, totalCabotageFlights);
    }

    private void printResults(HashMap<String, Integer> airlanes, AtomicReference<Integer> totalCabotageFlights){
        Double acumTotalPercentage = 0.0;

        for (Integer n = this.n; n > 0; n--){
            String maxAirlanePercentage = Collections.max(airlanes.entrySet(), Map.Entry.comparingByValue()).getKey();
            acumTotalPercentage += (double) (airlanes.get(maxAirlanePercentage) / totalCabotageFlights.get());
            printLine(maxAirlanePercentage, (double) (airlanes.get(maxAirlanePercentage) / totalCabotageFlights.get()) * 100);
            airlanes.remove(maxAirlanePercentage);
        }

        if (acumTotalPercentage != 1){
            printLine("Otros", (1 - acumTotalPercentage) * 100);
        }
    }

    private void printLine(String airlane, Double percentage){
        System.out.println(airlane + ";" + percentage + "%");
    }
}
