package com.org.util;


public enum Unit {

    SQM, CUM, MTR, NOS, KG;
    
    public static String[] names() {
        Unit[] states = values();
        String[] names = new String[states.length];

        for (int i = 0; i < states.length; i++) {
            names[i] = states[i].name();
        }

        return names;
    }
}
