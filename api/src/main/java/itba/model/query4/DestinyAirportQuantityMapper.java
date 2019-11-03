package itba.model.query4;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import itba.model.MoveType;
import itba.model.Movement;

public class DestinyAirportQuantityMapper implements Mapper<String, Movement, String, Integer> {

    private String destinyOACI;

    public DestinyAirportQuantityMapper(final String destinyOACI) {
        this.destinyOACI = destinyOACI;
    }

    @Override
    public void map(final String s, final Movement movement, final Context<String, Integer> context) {
        if (movement.getMoveType().equals(MoveType.LANDING) && movement.getOACIDestiny().equals(destinyOACI)) {
            context.emit(movement.getOACIOrigin(), 1);
        }
    }
}
