package itba.model;

import java.io.Serializable;

public class Airport implements Serializable {

    private String local;
    private String OACI;
    private String IATA;
    private String type;
    private String denomination;
    private String coordinates;
    private String latitude;
    private String longitude;
    private String uomElev;
    private String ref;
    private String refDistance;
    private String refDirection;
    private String condition;
    private String control;
    private String region;
    private String fir;
    private String use;
    private String traffic;
    private String sna;
    private String concessioned;
    private String province;
    private String inhab;

    public Airport(final String local, final String OACI, final String IATA, final String type, final String denomination,
                   final String coordinates, final String latitude, final String longitude, final String uomElev,
                   final String ref, final String refDistance, final String refDirection, final String condition,
                   final String control, final String region, final String fir, final String use, final String traffic,
                   final String sna, final String concessioned, final String province, final String inhab) {
        this.local = local;
        this.OACI = OACI;
        this.IATA = IATA;
        this.type = type;
        this.denomination = denomination;
        this.coordinates = coordinates;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uomElev = uomElev;
        this.ref = ref;
        this.refDistance = refDistance;
        this.refDirection = refDirection;
        this.condition = condition;
        this.control = control;
        this.region = region;
        this.fir = fir;
        this.use = use;
        this.traffic = traffic;
        this.sna = sna;
        this.concessioned = concessioned;
        this.province = province;
        this.inhab = inhab;
    }

    public String getLocal() {
        return local;
    }

    public String getOACI() {
        return OACI;
    }

    public String getIATA() {
        return IATA;
    }

    public String getType() {
        return type;
    }

    public String getDenomination() {
        return denomination;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUomElev() {
        return uomElev;
    }

    public String getRef() {
        return ref;
    }

    public String getRefDistance() {
        return refDistance;
    }

    public String getRefDirection() {
        return refDirection;
    }

    public String getCondition() {
        return condition;
    }

    public String getControl() {
        return control;
    }

    public String getRegion() {
        return region;
    }

    public String getFir() {
        return fir;
    }

    public String getUse() {
        return use;
    }

    public String getTraffic() {
        return traffic;
    }

    public String getSna() {
        return sna;
    }

    public String getConcessioned() {
        return concessioned;
    }

    public String getProvince() {
        return province;
    }

    public String getInhab() {
        return inhab;
    }
}
