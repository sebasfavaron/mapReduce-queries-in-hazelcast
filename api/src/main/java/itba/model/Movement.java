package itba.model;

import java.io.Serializable;
import java.util.Date;

public class Movement implements Serializable {
    private final Date date;
    private final Integer time;
    private final String flightType, classification, moveType, OACIOrigin, OACIDestiny, airline, plane;

    public Movement(Date date, Integer time, String flightType, String classification, String moveType, String OACIOrigin, String OACIDestiny, String airline, String plane) {
        this.date = date;
        this.time = time;
        this.flightType = flightType;
        this.classification = classification;
        this.moveType = moveType;
        this.OACIOrigin = OACIOrigin;
        this.OACIDestiny = OACIDestiny;
        this.airline = airline;
        this.plane = plane;
    }

    public Date getDate() {
        return date;
    }

    public Integer getTime() {
        return time;
    }

    public String getFlightType() {
        return flightType;
    }

    public String getClassification() {
        return classification;
    }

    public String getMoveType() {
        return moveType;
    }

    public String getOACIOrigin() {
        return OACIOrigin;
    }

    public String getOACIDestiny() {
        return OACIDestiny;
    }

    public String getAirline() {
        return airline;
    }

    public String getPlane() {
        return plane;
    }

    @Override
    public String toString() {
        return "Date: " + String.valueOf(date) + '\n' +
                "Time: " + time + '\n' +
                "FlightType: " + flightType + '\n' +
                "Classification: " + classification + '\n' +
                "MoveType: " + moveType + '\n' +
                "OACIOrigin: " + OACIOrigin + '\n' +
                "OACIDestiny: " + OACIDestiny + '\n' +
                "Airline: " + airline + '\n' +
                "Plane: " + plane;
    }
}
