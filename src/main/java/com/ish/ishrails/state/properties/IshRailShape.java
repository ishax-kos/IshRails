package com.ish.ishrails.state.properties;

import com.ish.ishrails.blocks.Dir;
import com.ish.ishrails.blocks.IshRailState;
import net.minecraft.util.IStringSerializable;

import java.util.HashSet;

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
    EAST_NW ("east_nw" ),

    CROSSING ("xing" ),
    CROSSING_DIAGONAL ("xing_diag" );

    private final String name;

    private IshRailShape(String name) {
        this.name = name;
    }


    public IshRailShape reverse() {
        switch (this) {
            case SOUTH_NE: return NORTH_SW;
            case SOUTH_NW: return NORTH_SE;
            case NORTH_SE: return SOUTH_NW;
            case NORTH_SW: return SOUTH_NE;
            case WEST_NE: return EAST_SW;
            case WEST_SE: return EAST_NW;
            case EAST_NW: return WEST_SE;
            case EAST_SW: return WEST_NE;

            case ASCENDING_EAST: return ASCENDING_WEST;
            case ASCENDING_WEST: return ASCENDING_EAST;
            case ASCENDING_SOUTH: return ASCENDING_NORTH;
            case ASCENDING_NORTH: return ASCENDING_SOUTH;
            default: return this;
        }
    }


    public String toString() {
        return this.name;
    }

    public boolean isAscending() {
        return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
    }

    public boolean hasNorth() {
        return this == NORTH_SOUTH || this == NORTH_SE || this == NORTH_SW || this == ASCENDING_SOUTH;
    }

    public boolean hasNE() {
        return this == NE_SW || this == WEST_NE || this == SOUTH_NE;
    }

    public boolean hasEast() {
        return this == EAST_WEST || this == EAST_NW || this == EAST_SW || this == ASCENDING_WEST;
    }

    public boolean hasSE() {
        return this == NW_SE || this == WEST_SE || this == NORTH_SE;
    }

    public boolean hasSouth() {
        return this == NORTH_SOUTH || this == SOUTH_NE || this == SOUTH_NW || this == ASCENDING_NORTH;
    }

    public boolean hasSW() {
        return this == NE_SW || this == EAST_SW || this == NORTH_SW;
    }

    public boolean hasWest() {
        return this == EAST_WEST || this == WEST_NE || this == WEST_SE || this == ASCENDING_EAST;
    }

    public boolean hasNW() {
        return this == NW_SE || this == EAST_NW || this == SOUTH_NW;
    }

    public static IshRailShape formShape(HashSet<Dir> directions) {
        assert (directions.size() >= 2);
        if (directions.contains(Dir.N)) {
            if (directions.contains(Dir.SE)) {
                return NORTH_SE;
            }
            if (directions.contains(Dir.S)) {
                return NORTH_SOUTH;
            }
            if (directions.contains(Dir.SW)) {
                return NORTH_SW;
            }
        }
        if (directions.contains(Dir.NE)) {
            if (directions.contains(Dir.S)) {
                return SOUTH_NE;
            }
            if (directions.contains(Dir.SW)) {
                return NE_SW;
            }
            if (directions.contains(Dir.W)) {
                return WEST_NE;
            }
        }
        if (directions.contains(Dir.E)) {
            if (directions.contains(Dir.SW)) {
                return EAST_SW;
            }
            if (directions.contains(Dir.W)) {
                return EAST_WEST;
            }
            if (directions.contains(Dir.NW)) {
                return EAST_NW;
            }
        }
        if (directions.contains(Dir.SE)) {
            if (directions.contains(Dir.W)) {
                return WEST_SE;
            }
            if (directions.contains(Dir.NW)) {
                return NW_SE;
            }
        }
        if (directions.contains(Dir.S)) {
            if (directions.contains(Dir.NW)) {
                return SOUTH_NW;
            }
        }
        return null;
    }

    public boolean isDiagonal() {
        return this == NE_SW || this == NW_SE;
    }

    public String func_176610_l() {
        return this.name;
    }
}