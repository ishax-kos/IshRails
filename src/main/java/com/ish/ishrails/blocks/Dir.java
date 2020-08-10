package com.ish.ishrails.blocks;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public enum Dir {
    SW,// (-3),
    W,//  (-2),
    NW,// (-1),
    N,//  (0),
    NE,// (1),
    E,//  (2),
    SE,// (3),
    S,//  (4),
    ;


    public static Dir fromIndex(int index) {
        return fromOrd(index+3);
    }


    public int getIndex() {
        return this.ordinal()-3;
    }


    public static Dir fromOrd(int index) {
        return Dir.values()[index&7];
    }


    public Dir rotate(int rotate) {
        return fromOrd(this.ordinal() + rotate);
    }


    public Dir flip() {
        return fromOrd(this.ordinal() + 4);
    }


    public int angleDiff(Dir other) {
        int val = 3 + other.ordinal() - this.ordinal();
        return (val & 7) - 3;
    }


    public BlockPos fromPos(BlockPos pos) {
        switch (this) {
            case N:  return pos.north();
            case NE: return pos.north().east();
            case E:  return pos.east();
            case SE: return pos.south().east();
            case S:  return pos.south();
            case SW: return pos.south().west();
            case W:  return pos.west();
            case NW: return pos.north().west();
            default: return null;
        }
    }

}
