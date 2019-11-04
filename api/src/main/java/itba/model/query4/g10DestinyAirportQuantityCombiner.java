package itba.model.query4;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class g10DestinyAirportQuantityCombiner implements CombinerFactory<String, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(final String s) {
        return new DestinyCombiner();
    }

    private class DestinyCombiner extends Combiner<Integer, Integer> {
        private int destinyQuantity = 0;

        @Override
        public void combine(final Integer integer) {
            destinyQuantity += integer;
        }

        @Override
        public Integer finalizeChunk() {
            return destinyQuantity;
        }

        @Override
        public void reset() {
            destinyQuantity = 0;
        }
    }
}
