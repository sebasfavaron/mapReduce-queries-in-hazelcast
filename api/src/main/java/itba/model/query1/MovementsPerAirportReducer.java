package itba.model.query1;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MovementsPerAirportReducer implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(final String s) {
        return new MovementReducer();
    }

    private class MovementReducer extends Reducer<Integer, Integer> {
        private volatile int movementsQuantity;

        @Override
        public void beginReduce() {
            movementsQuantity = 0;
        }

        @Override
        public void reduce(final Integer integer) {
            movementsQuantity += integer;
        }

        @Override
        public Integer finalizeReduce() {
            return movementsQuantity;
        }

    }
}
