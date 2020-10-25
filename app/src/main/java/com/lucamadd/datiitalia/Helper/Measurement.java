package com.lucamadd.datiitalia.Helper;

import java.util.*;

public class Measurement {
    public static final Map<Integer,String> prefixes;
    static {
        Map<Integer,String> tempPrefixes = new HashMap<Integer,String>();
        tempPrefixes.put(0,"");
        tempPrefixes.put(3,"k");
        tempPrefixes.put(6,"M");
        tempPrefixes.put(9,"G");
        tempPrefixes.put(12,"T");
        tempPrefixes.put(-3,"m");
        tempPrefixes.put(-6,"u");
        prefixes = Collections.unmodifiableMap(tempPrefixes);
    }

    double value;

    public Measurement(double value) {
        this.value = value;
    }

    public String toString() {
        double tval = value;
        int order = 0;
        while(tval > 1000.0) {
            tval /= 1000.0;
            order += 3;
        }
        while(tval < 1.0) {
            tval *= 1000.0;
            order -= 3;
        }
        return Double.toString(tval).substring(0,5).replace(".",",") + prefixes.get(order);
    }


}