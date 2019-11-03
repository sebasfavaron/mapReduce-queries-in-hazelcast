package itba.model.query3;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class ThounsandMovementsPerAirportMapper implements Mapper<String, Integer, Integer, String> {

    @Override
    public void map(String oaci, Integer movements, Context<Integer,String> context) {
        int thoundsandMovements = movements / 1000;
        thoundsandMovements *= 1000;

        if (thoundsandMovements >= 1000){
            context.emit(thoundsandMovements, oaci);
        }
    }
}