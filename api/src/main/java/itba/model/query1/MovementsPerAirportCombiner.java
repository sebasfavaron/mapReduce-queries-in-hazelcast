package itba.model.query1;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class MovementsPerAirportCombiner implements CombinerFactory<String, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(final String s) {
        return new MovementsCombiner();
    }

    private static class MovementsCombiner extends Combiner<Integer, Integer> {
        private int movementsQuantity = 0;

        @Override
        public void combine(final Integer integer) {
            movementsQuantity += integer;
        }

        @Override
        public Integer finalizeChunk() {
            return movementsQuantity;
        }

        @Override
        public void reset() {
            movementsQuantity = 0;
        }
    }
}
