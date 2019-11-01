package itba.client.query;

import com.hazelcast.core.IList;
import itba.model.Airport;
import itba.model.Movement;

import java.util.concurrent.ExecutionException;

public class Query1 implements Query {

    private IList<Airport> airports;
    private IList<Movement> movements;

    public Query1(final IList<Airport> airports, final IList<Movement> movements) {
        this.airports = airports;
        this.movements = movements;
    }

    @Override
    public void run() throws InterruptedException, ExecutionException {
        for (int i = 0; i < 10; i++) {
            System.out.println(airports.get(i).getDenomination());
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(movements.get(i).getAirline());
        }
    }
}
