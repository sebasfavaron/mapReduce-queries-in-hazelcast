package itba.model.query1;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import itba.model.MoveType;
import itba.model.Movement;

public class g10MovementsPerAirportMapper implements Mapper<String, Movement, String, Integer> {

    @Override
    public void map(String s, Movement movement, Context<String, Integer> context) {

        // Asumimos datos bien formados
        String OACI;
        if(movement.getMoveType().equals(MoveType.LANDING)) {
            OACI = movement.getOACIDestiny();
        } else {
            OACI = movement.getOACIOrigin();
        }

        context.emit(OACI, 1);
    }
}
