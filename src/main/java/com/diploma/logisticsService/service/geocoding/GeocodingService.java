package com.diploma.logisticsService.service.geocoding;

import io.redlink.geocoding.Geocoder;
import io.redlink.geocoding.LatLon;
import io.redlink.geocoding.Place;
import io.redlink.geocoding.nominatim.NominatimGeocoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class GeocodingService {
    private static Geocoder osrmGeocoder;

    @PostConstruct
    private void init() {
        osrmGeocoder = NominatimGeocoder
                .configure()
                .create();
    }

    public LatLon geocode(String address) {
        //Проверяем является ли адрес набором координат
        LatLon latLon = fetchLatLon(address);
        if (latLon != null) return latLon;
        try {
            List<Place> places = osrmGeocoder.geocode("address");
            return places.get(0).getLatLon();
        } catch (Exception ex) {
            log.error("Unable to geocode address " + address, ex);
            return null;
        }
    }

    /**
     * parse coordinates as {latitude},{longitude}
     *
     * @param address - string
     * @return LatLon pair
     */
    private LatLon fetchLatLon(String address) {
        address = address.replaceAll("\\s+", "");
        String[] latLonValues;
        try {
            latLonValues = address.split(",");
            if (latLonValues.length != 2) return null;

            double lat = Double.parseDouble(latLonValues[0]);
            double lon = Double.parseDouble(latLonValues[1]);
            return LatLon.create(lat, lon);
        } catch (Exception ex) {
            return null;
        }
    }
}
