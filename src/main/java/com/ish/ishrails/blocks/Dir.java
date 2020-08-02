package com.ish.ishrails.blocks;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public enum Dir {
    N  (1),
    NE (2),
    E  (3),
    SE (4),
    S  (-1),
    SW (-2),
    W  (-3),
    NW (-4),
    ;
    public int index;

    private Dir(int index) {
        this.index = index;
    }

    private int Dir() {
        return this.index;
    }



    public BlockPos fromPos(BlockPos pos) {
        switch (this) {
            case N: return pos.north();
            case NE: return pos.north().east();
            case E: return pos.east();
            case SE: return pos.south().east();
            case S: return pos.south();
            case SW: return pos.south().west();
            case W: return pos.west();
            case NW: return pos.north().west();
            default: return null;
        }
    }

}
