package itba.model.query4;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class DestinyAirportQuantityReducer implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(final String s) {
        return new DestinyReducer();
    }

    private class DestinyReducer extends Reducer<Integer, Integer> {
        private volatile int destinyQuantity;

        @Override
        public void beginReduce() {
            destinyQuantity = 0;
        }

        @Override
        public void reduce(final Integer integer) {
            destinyQuantity += integer;
        }

        @Override
        public Integer finalizeReduce() {
            return destinyQuantity;
        }
    }
}
