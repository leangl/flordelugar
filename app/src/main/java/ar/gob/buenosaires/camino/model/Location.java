package ar.gob.buenosaires.camino.model;

import java.io.Serializable;

/**
 * Created by Ignacio Saslavsky on 13/06/15.
 * correonano@gmail.com
 */
public class Location implements Serializable {

//    "lat": -34.586544,
//            "lon": -58.391874,
//            "city": "CABA",
//            "state": "Buenos Aires",
//            "neighborhood": "Recoleta",
//            "comuna": 2

    public double lat;
    public double lon;
    public String city;
    public String state;
    public String neighborhood;
    public int comuna;

}
