package me.dniym.utils;

public enum AttribLimit {


    InstantHealth("HEAL", 6, 124),
    Leaping("Jump", 8, 3);

    private String name;
    private int oldId;
    private int maxValue;
    private int defaultValue;

    AttribLimit(String name, int oldId, int maxValue) {

    }

    public static AttribLimit get(String name) {

        return null;
    }

}
