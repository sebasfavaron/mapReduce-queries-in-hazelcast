package itba.client.query;

import com.hazelcast.core.IList;
import itba.model.Airport;
import itba.model.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class Query1 implements Query {

    private IList<Airport> airports;
    private IList<Movement> movements;
    private Logger logger;

    public Query1(final IList<Airport> airports, final IList<Movement> movements) {
        this.airports = airports;
        this.movements = movements;
        this.logger = LoggerFactory.getLogger(Query.class);
    }

    private class MovementCount {
        private String OACI, denomination;
        private Integer count;

        public MovementCount(String OACI, String denomination) {
            this.OACI = OACI;
            this.denomination = denomination;
            this.count = 1;
        }

        public void incrementMovements() {
            this.count++;
        }

        @Override
        public String toString() {
            return OACI + ";" + denomination + ";" + count;
        }
    }

    @Override
    public void run() throws InterruptedException, ExecutionException {
        List<MovementCount> movementCounts = new ArrayList<>();

        movements.forEach(movement -> {
            String OACI;
            if(movement.getMoveType().equals("Aterrizaje")) {
                OACI = movement.getOACIDestiny();
            } else {
                OACI = movement.getOACIOrigin();
            }

            Optional<MovementCount> movementCount = movementCounts.stream().filter(m -> m.OACI.equals(OACI)).findFirst();
            if(movementCount.isPresent()) {
                movementCount.get().incrementMovements();
            } else {
                Optional<Airport> airport = airports.stream().filter(a -> a.getOACI().equals(OACI)).findFirst();
                if(!airport.isPresent()) {
                    logger.error("OACI code " + OACI + " not found on airport database");
                } else {
                    movementCounts.add(new MovementCount(OACI, airport.get().getDenomination()));
                }
            }
        });

        movementCounts.forEach(System.out::println); //TODO: imprimir esto mismo ordenado como piden en 'outPath'/query1.csv
    }
}
