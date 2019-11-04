package itba.model.query2;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class g10CabotageMovementsPerAirlineReducer implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(final String s) {
        return new CabotageMovementReducer();
    }

    private class CabotageMovementReducer extends Reducer<Integer, Integer> {
        private volatile int cabotageMovementQuantity;

        @Override
        public void beginReduce() {
            cabotageMovementQuantity = 0;
        }

        @Override
        public void reduce(final Integer integer) {
            cabotageMovementQuantity += integer;
        }

        @Override
        public Integer finalizeReduce() {
            return cabotageMovementQuantity;
        }

    }
}

