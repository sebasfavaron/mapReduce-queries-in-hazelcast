package itba.model.query1;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import itba.model.Movement;

public class MovementsPerAirportMapper implements Mapper<String, Movement, String, Integer> {

    @Override
    public void map(String s, Movement movement, Context<String, Integer> context) {

        // Asumimos datos bien formados
        String OACI;
        if(movement.getMoveType().equals("Aterrizaje")) {
            OACI = movement.getOACIDestiny();
        } else {
            OACI = movement.getOACIOrigin();
        }

        context.emit(OACI, 1);
    }
}
