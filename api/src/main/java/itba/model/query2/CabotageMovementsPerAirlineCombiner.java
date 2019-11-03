package itba.model.query2;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;
import itba.model.query1.MovementsPerAirportCombiner;

public class CabotageMovementsPerAirlineCombiner implements CombinerFactory<String, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(final String s) {
        return new CabotageMovementsCombiner();
    }

    private static class CabotageMovementsCombiner extends Combiner<Integer, Integer> {
        private int cabotageMovementQuantity = 0;

        @Override
        public void combine(final Integer integer) {
            cabotageMovementQuantity += integer;
        }

        @Override
        public Integer finalizeChunk() {
            return cabotageMovementQuantity;
        }

        @Override
        public void reset() {
            cabotageMovementQuantity = 0;
        }
    }
}
