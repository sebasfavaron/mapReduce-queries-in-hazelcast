package itba.model.query3;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.ArrayList;
import java.util.List;

public class g10ThounsandMovementsPerAirportReducer implements ReducerFactory<Integer, String, List<String>> {
    @Override
    public Reducer<String, List<String>> newReducer(Integer integer) {

        return new ThousandMovementsPerAirportReducer();
    }

    private class ThousandMovementsPerAirportReducer extends Reducer<String, List<String>> {
        private volatile List<String> oaciList;

        @Override
        public void beginReduce() {
            oaciList = new ArrayList<>();
        }

        @Override
        public void reduce(String oaci) {
            oaciList.add(oaci);
        }

        @Override
        public List<String> finalizeReduce() {
            if (oaciList.size() > 1){
                return oaciList;
            }
            return null;
        }
    }
}

