package itba.model.query4;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import itba.model.MoveType;
import itba.model.Movement;

public class g10DestinyAirportQuantityMapper implements Mapper<String, Movement, String, Integer> {

    private String originOACI;

    public g10DestinyAirportQuantityMapper(final String originOACI) {
        this.originOACI = originOACI;
    }

    @Override
    public void map(final String s, final Movement movement, final Context<String, Integer> context) {
        if (movement.getMoveType().equals(MoveType.TAKE_OF) && movement.getOACIOrigin().equals(originOACI)) {
            context.emit(movement.getOACIDestiny(), 1);
        }
    }
}
