package com.ish.ishrails.state.properties;

import net.minecraft.util.IStringSerializable;

public enum IshRailShape implements IStringSerializable {
    /**Straight Rails**/
    NORTH_SOUTH("north_south"),
    EAST_WEST("east_west"),
    NE_SW("ne_sw"),
    NW_SE("nw_se"),

    /**Ascending Rails**/
    ASCENDING_EAST("ascending_east"),
    ASCENDING_WEST("ascending_west"),
    ASCENDING_NORTH("ascending_north"),
    ASCENDING_SOUTH("ascending_south"),

    /**Curved Rails**/
    SOUTH_NE("south_ne"),
    SOUTH_NW("south_nw"),
    NORTH_SW("north_sw"),
    NORTH_SE("north_se"),
    WEST_SE ("west_se" ),
    WEST_NE ("west_ne" ),
    EAST_SW ("east_sw" ),
    EAST_NW ("east_nw" );

    private final String name;

    private IshRailShape(String name) {
        this.name = name;
    }


    public String toString() {
        return this.name;
    }

    public boolean isAscending() {
        return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
    }

    public String func_176610_l() {
        return this.name;
    }
}