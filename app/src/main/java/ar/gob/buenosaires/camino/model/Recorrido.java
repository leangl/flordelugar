package ar.gob.buenosaires.camino.model;

import java.util.List;

/**
 * Created by Ignacio Saslavsky on 14/06/15.
 * correonano@gmail.com
 */
public class Recorrido {

    public int id;
    public String name;
    public String description;
    public float total_km;
    public int tiempo;
    public Conversion conversion;
    public int tipo;
    public int[] venues;
}
