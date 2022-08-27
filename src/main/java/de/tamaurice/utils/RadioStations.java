package de.tamaurice.utils;

import java.util.HashMap;
import java.util.Map;

public class RadioStations {
    Map<String, String> stations = new HashMap<>();

    public boolean addStation(String stationName, String stationLink) {
        try {
            stations.put(stationName, stationLink);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeStation(String stationName) {
        try {
            stations.remove(stationName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getStationLink(String stationName) {
        return stations.get(stationName);
    }

    public Map<String, String> getRadioStations() {
        return stations;
    }
}
