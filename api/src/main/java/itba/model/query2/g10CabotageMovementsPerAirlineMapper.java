package itba.model.query2;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import itba.model.Movement;

public class g10CabotageMovementsPerAirlineMapper implements Mapper<String, Movement, String, Integer> {

    @Override
    public void map(String s, Movement movement, Context<String, Integer> context) {

        if (movement.getClassification().equals("Cabotaje")) {
            context.emit(movement.getAirline(), 1);
        }
    }
}
