package com.ish.ishrails.blocks;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.ish.ishrails.IshRails;
import com.ish.ishrails.state.properties.IshRailShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

import static com.ish.ishrails.IshRails.log;

public class IshRailState {
    private final World world;
    private final BlockPos pos;
    private final AbstractIshRailBlock block;
    private BlockState newState;
    private final boolean disableCorners;
    private final IshRailShape shape;
    private final List<BlockPos> connectedRails = Lists.newArrayList();
    private final boolean canMakeSlopes;

    public IshRailState(World worldIn, BlockPos pos, BlockState state) {
        this.world = worldIn;
        this.pos = pos;
        this.newState = state;
        this.block = (AbstractIshRailBlock) state.getBlock();
        this.shape = this.block.getRailDirection(newState, worldIn, pos, null);
        this.disableCorners = !this.block.isFlexibleRail(newState, worldIn, pos);
        this.canMakeSlopes = this.block.canMakeSlopes(newState, worldIn, pos);
        this.reset(this.shape);
    }

    public List<BlockPos> getConnectedRails() { return this.connectedRails;
    }

    private void reset(IshRailShape shape) {
        connectedRails.clear();
        switch(shape) {
            case NORTH_SOUTH:
                this.connectedRails.add(this.pos.north());
                this.connectedRails.add(this.pos.south());
            case EAST_WEST:
                this.connectedRails.add(this.pos.west());
                this.connectedRails.add(this.pos.east());
            case NW_SE:
                this.connectedRails.add(this.pos.north().west());
                this.connectedRails.add(this.pos.south().east());
            case NE_SW:
                this.connectedRails.add(this.pos.north().east());
                this.connectedRails.add(this.pos.south().west());
            case ASCENDING_EAST:
                this.connectedRails.add(this.pos.west());
                this.connectedRails.add(this.pos.east().up());
            case ASCENDING_WEST:
                this.connectedRails.add(this.pos.west().up());
                this.connectedRails.add(this.pos.east());
            case ASCENDING_NORTH:
                this.connectedRails.add(this.pos.north().up());
                this.connectedRails.add(this.pos.south());
            case ASCENDING_SOUTH:
                this.connectedRails.add(this.pos.north());
                this.connectedRails.add(this.pos.south().up());
            case SOUTH_NE:
                this.connectedRails.add(this.pos.north().east());
                this.connectedRails.add(this.pos.south());
            case SOUTH_NW:
                this.connectedRails.add(this.pos.north().west());
                this.connectedRails.add(this.pos.south());
            case NORTH_SW:
                this.connectedRails.add(this.pos.south().west());
                this.connectedRails.add(this.pos.north());
            case NORTH_SE:
                this.connectedRails.add(this.pos.south().east());
                this.connectedRails.add(this.pos.north());
            case WEST_NE:
                this.connectedRails.add(this.pos.north().east());
                this.connectedRails.add(this.pos.west());
            case WEST_SE:
                this.connectedRails.add(this.pos.south().east());
                this.connectedRails.add(this.pos.west());
            case EAST_NW:
                this.connectedRails.add(this.pos.north().west());
                this.connectedRails.add(this.pos.east());
            case EAST_SW:
                this.connectedRails.add(this.pos.south().west());
                this.connectedRails.add(this.pos.east());
        }

    }


    private boolean isConnected(IshRailState other) {

        for (int a: this.fetchConnections()) {
            for (int b: other.fetchConnections()) {

            if ((a + b) == 0) {return true;}
            }
        }
        return false;
    }


    private List<Byte> fetchEmptyConnections() {
        byte[] connections = this.fetchConnections();
        List<Byte> emptyConnections = null;
        for (byte i: connections) {
            if (this.isConnected(this.createForAdjacent(this.posFromConnection(i)))) {
                emptyConnections.add(i);
            }
        }
        return emptyConnections;
    }

    
    private BlockPos posFromConnection(int connection) {
        switch (connection) {
            case  1: return this.pos.north();
            case  2: return this.pos.north().east();
            case  3: return this.pos.east();
            case  4: return this.pos.south().east();
            case -1: return this.pos.south();
            case -2: return this.pos.south().west();
            case -3: return this.pos.west();
            case -4: return this.pos.north().west();
            default: return null;
        }
    }
    
    
//    private List<BlockPos> getActualConnections() {
//        List<BlockPos> connects = Lists.newArrayList();
//        for (BlockPos p: this.getOutlets(this.pos,this.shape)) {
//            if (this.pointsAt(p)) {
//                connects.add(p);
//            }
//        }
//        return connects;
//    }


    private byte[] fetchConnections(BlockPos blockPos) {
        IshRailState railstate = createForAdjacent(blockPos);
        if (railstate != null) {
            return fetchConnections(railstate.shape);
        }
        else {
            return new byte[]{};
        }
    }
    private byte[] fetchConnections() {return fetchConnections(this.shape);}
    private byte[] fetchConnections(IshRailShape railShape) {
        // 1, 2, 3, 4, -1, -2, -3, -4 (when arranged around a circle the opposites add up to 0)
        switch(railShape) {
            case NORTH_SOUTH: case ASCENDING_NORTH: case ASCENDING_SOUTH:
                return new byte[]{1, -1};
            case NE_SW:
                return new byte[]{2, -2};
            case EAST_WEST:  case ASCENDING_EAST: case ASCENDING_WEST:
                return new byte[]{3, -3};
            case NW_SE:
                return new byte[]{4, -4};

            case NORTH_SE:
                return new byte[]{1, 4};
            case NORTH_SW:
                return new byte[]{1, -2};
            case SOUTH_NE:
                return new byte[]{-1, 2};
            case SOUTH_NW:
                return new byte[]{-1, -4};
            case EAST_SW:
                return new byte[]{3, -2};
            case EAST_NW:
                return new byte[]{3, -4};
            case WEST_NE:
                return new byte[]{-3, 2};
            case WEST_SE:
                return new byte[]{-3, 4};
            default:
                return new byte[]{};
        }
    }


    private boolean isAdjacentRail(BlockPos pos) { return AbstractIshRailBlock.isRail(this.world, pos) || AbstractIshRailBlock.isRail(this.world, pos.up()) || AbstractIshRailBlock.isRail(this.world, pos.down());
    }


    @Nullable
    private IshRailState createForAdjacent(BlockPos blockPos) {
//        log.debug("createForAdjacent");
        BlockState blockstate = this.world.getBlockState(blockPos);
        if (AbstractIshRailBlock.isRail(blockstate)) { return new IshRailState(this.world, blockPos, blockstate);
        } else {

            BlockPos pos2 = blockPos.up();
            blockstate = this.world.getBlockState(pos2);
            if (AbstractIshRailBlock.isRail(blockstate)) { return new IshRailState(this.world, pos2, blockstate);
            } else {
                pos2 = blockPos.down();
                blockstate = this.world.getBlockState(pos2);
                if (AbstractIshRailBlock.isRail(blockstate)) {

                    return new IshRailState(this.world, pos2, blockstate);
                }
//                log.debug("failed");
                return null;
            }
        }
    }


    protected int countAdjacentRails() {
        int i = 0;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.isAdjacentRail(this.pos.offset(direction))) {
                ++i;
            }
        } return i;
    }

    private boolean pointsAt(BlockPos blockPos, int direction) {return pointsAt(blockPos, (byte) direction);}
    private boolean pointsAt(BlockPos blockPos, byte direction) {
        IshRailState railstate = createForAdjacent(blockPos);
        if (railstate != null) {
            return pointsAt(railstate.shape, direction);
        }
        else {
            return false;
        }
    }
    private boolean pointsAt(IshRailShape railShape, byte direction) {
        byte[] connections = fetchConnections(railShape);
        for (int i: connections) {
            if (direction + i == 0) {return true;}
        }
        return false;
    }


//    private BlockPos[] getOutlets(BlockPos fromPos, IshRailShape shape) {
//        switch(shape) {
//            case NORTH_SOUTH:       return new BlockPos[]{fromPos.south()        , fromPos.north()};
//            case EAST_WEST:         return new BlockPos[]{fromPos.east()         , fromPos.west()};
//            case NW_SE:             return new BlockPos[]{fromPos.south().east() , fromPos.north().west()};
//            case NE_SW:             return new BlockPos[]{fromPos.south().west() , fromPos.north().east()};
//            case ASCENDING_EAST:    return new BlockPos[]{fromPos.east()         , fromPos.west().up()};
//            case ASCENDING_WEST:    return new BlockPos[]{fromPos.east().up()    , fromPos.west()};
//            case ASCENDING_NORTH:   return new BlockPos[]{fromPos.south().up()   , fromPos.north()};
//            case ASCENDING_SOUTH:   return new BlockPos[]{fromPos.south()        , fromPos.north().up()};
//            case SOUTH_NE:          return new BlockPos[]{fromPos.south().west() , fromPos.north()};
//            case SOUTH_NW:          return new BlockPos[]{fromPos.south().east() , fromPos.north()};
//            case NORTH_SW:          return new BlockPos[]{fromPos.north().east() , fromPos.south()};
//            case NORTH_SE:          return new BlockPos[]{fromPos.north().west() , fromPos.south()};
//            case WEST_NE:           return new BlockPos[]{fromPos.south().west() , fromPos.east()};
//            case WEST_SE:           return new BlockPos[]{fromPos.north().west() , fromPos.east()};
//            case EAST_NW:           return new BlockPos[]{fromPos.south().east() , fromPos.west()};
//            case EAST_SW:           return new BlockPos[]{fromPos.north().east() , fromPos.west()};
//            default: return new BlockPos[]{};
//        }
//    }
//
//    private boolean pointsAt(BlockPos fromPos, BlockPos toPos, IshRailShape shape) {
////        boolean result = false;
////        for (int i = 0; i < connectionCount(shape); i++) {
////            if (getOutlets(fromPos, shape).equals(toPos)) {return true;}
////        }
////        return false;
//
//        switch(shape) {
//            case NORTH_SOUTH:       return (toPos.equals(fromPos.south())        || toPos.equals(fromPos.north()));
//            case EAST_WEST:         return (toPos.equals(fromPos.east())         || toPos.equals(fromPos.west()));
//            case NW_SE:             return (toPos.equals(fromPos.south().east()) || toPos.equals(fromPos.north().west()));
//            case NE_SW:             return (toPos.equals(fromPos.south().west()) || toPos.equals(fromPos.north().east()));
//            case ASCENDING_EAST:    return (toPos.equals(fromPos.east())         || toPos.equals(fromPos.west().up()));
//            case ASCENDING_WEST:    return (toPos.equals(fromPos.east().up())    || toPos.equals(fromPos.west()));
//            case ASCENDING_NORTH:   return (toPos.equals(fromPos.south().up())   || toPos.equals(fromPos.north()));
//            case ASCENDING_SOUTH:   return (toPos.equals(fromPos.south())        || toPos.equals(fromPos.north().up()));
//            case SOUTH_NE:          return (toPos.equals(fromPos.south().west()) || toPos.equals(fromPos.north()));
//            case SOUTH_NW:          return (toPos.equals(fromPos.south().east()) || toPos.equals(fromPos.north()));
//            case NORTH_SW:          return (toPos.equals(fromPos.north().east()) || toPos.equals(fromPos.south()));
//            case NORTH_SE:          return (toPos.equals(fromPos.north().west()) || toPos.equals(fromPos.south()));
//            case WEST_NE:           return (toPos.equals(fromPos.south().west()) || toPos.equals(fromPos.east()));
//            case WEST_SE:           return (toPos.equals(fromPos.north().west()) || toPos.equals(fromPos.east()));
//            case EAST_NW:           return (toPos.equals(fromPos.south().east()) || toPos.equals(fromPos.west()));
//            case EAST_SW:           return (toPos.equals(fromPos.north().east()) || toPos.equals(fromPos.west()));
//            default: return false;
//        }
//    }
//
//    private int outletCount(IshRailShape shape) {
//        switch(shape) {
////            case something new:       return more;
//            default: return 2;
//        }
//    }


public IshRailState correctShape(boolean isPowered, boolean justPlaced, IshRailShape shape) {
        BlockPos blockposN  = this.pos.north();
        BlockPos blockposNE = this.pos.north().east();
        BlockPos blockposE  = this.pos.east();
        BlockPos blockposSE = this.pos.south().east();
        BlockPos blockposS  = this.pos.south();
        BlockPos blockposSW = this.pos.south().west();
        BlockPos blockposW  = this.pos.west();
        BlockPos blockposNW = this.pos.north().west();
        boolean flagN  = pointsAt(blockposN, 1);
        boolean flagNE = pointsAt(blockposNE, 2);
        boolean flagE  = pointsAt(blockposE, 3);
        boolean flagSE = pointsAt(blockposSE, 4);
        boolean flagS  = pointsAt(blockposS, -1);
        boolean flagSW = pointsAt(blockposSW, -2);
        boolean flagW  = pointsAt(blockposW, -3);
        boolean flagNW = pointsAt(blockposNW, -4);
        IshRailShape railshape = null;

        if (!this.disableCorners && !this.shape.isAscending()) { //
            if (flagS && !flagN) {
                if (this.shape == IshRailShape.NE_SW) {
                    railshape = IshRailShape.SOUTH_NE;
                }
                if (this.shape == IshRailShape.NW_SE) {
                    railshape = IshRailShape.SOUTH_NW;
                }
            }

            if (flagN && !flagS) {
                if (this.shape == IshRailShape.NW_SE) {
                    railshape = IshRailShape.NORTH_SE;
                }
                if (this.shape == IshRailShape.NE_SW) {
                    railshape = IshRailShape.NORTH_SW;
                }
            }


            if (flagE && !flagW) {
                if (this.shape == IshRailShape.NW_SE) {
                    railshape = IshRailShape.EAST_NW;
                }
                if (this.shape == IshRailShape.NE_SW) {
                    railshape = IshRailShape.EAST_SW;
                }
            }

            if (flagW && !flagE) {
                if (this.shape == IshRailShape.NE_SW) {
                    railshape = IshRailShape.WEST_NE;
                }
                if (this.shape == IshRailShape.NW_SE) {
                    railshape = IshRailShape.WEST_SE;
                }
            }


            if (flagSE && !flagNW) {
                if (this.shape == IshRailShape.NORTH_SOUTH) {
                    railshape = IshRailShape.NORTH_SE;
                }
                if (this.shape == IshRailShape.EAST_WEST) {
                    railshape = IshRailShape.WEST_SE;
                }
            }

            if (flagNW && !flagSE) {
                if (this.shape == IshRailShape.NORTH_SOUTH) {
                    railshape = IshRailShape.SOUTH_NW;
                }
                if (this.shape == IshRailShape.EAST_WEST) {
                    railshape = IshRailShape.EAST_NW;
                }
            }


            if (flagNE && !flagSW) {
                if (this.shape == IshRailShape.NORTH_SOUTH) {
                    railshape = IshRailShape.SOUTH_NE;
                }
                if (this.shape == IshRailShape.EAST_WEST) {
                    railshape = IshRailShape.WEST_NE;
                }
            }

            if (flagSW && !flagNE) {
                if (this.shape == IshRailShape.NORTH_SOUTH) {
                    railshape = IshRailShape.NORTH_SW;
                }
                if (this.shape == IshRailShape.EAST_WEST) {
                    railshape = IshRailShape.EAST_SW;
                }
            }
        }

        if (railshape == null) {
            railshape = this.shape;
        }

        this.newState = this.newState.with(this.block.getShapeProperty(), railshape);
        if (justPlaced || this.world.getBlockState(this.pos) != this.newState) {
            this.world.setBlockState(this.pos, this.newState, 3);
            byte[] myCon = fetchConnections(railshape);
            for(byte i: myCon) {
                IshRailState railstate = this.createForAdjacent(posFromConnection(i));
                List<Byte> openCon = railstate.fetchEmptyConnections();
                if (openCon.size() != 0) {
                    this.correctAdjacentShape(railstate, i);
                }
            }
        }
        return this;
    }

    
//    private List<BlockPos> getActualConnections() {
//        List<BlockPos> connects = Lists.newArrayList();
//        for (BlockPos p: this.getOutlets(this.pos,this.shape)) {
//            if (this.pointsAt(p)) {
//                connects.add(p);
//            }
//        }
//        return connects;
//    }


    public void correctAdjacentShape(IshRailState state, byte direction) {
//        log.debug(state.pointsAt(fromPos));
        if (pointsAt(this.pos, direction)) {
            IshRailShape railshape = null;
            if (!state.shape.isAscending()) {
                if (state.pos.equals(this.pos.south())) {
                    if (state.shape == IshRailShape.NW_SE) {
                        railshape = IshRailShape.NORTH_SE;
                    }
                    if (state.shape == IshRailShape.NE_SW) {
                        railshape = IshRailShape.NORTH_SW;
                    }
                } else if (state.pos.equals(this.pos.south().west())) {
                    if (state.shape == IshRailShape.NORTH_SOUTH) {
                        railshape = IshRailShape.SOUTH_NE;
                    }
                    if (state.shape == IshRailShape.EAST_WEST) {
                        railshape = IshRailShape.WEST_NE;
                    }
                } else if (state.pos.equals(this.pos.west())) {
                    if (state.shape == IshRailShape.NW_SE) {
                        railshape = IshRailShape.EAST_NW;
                    }
                    if (state.shape == IshRailShape.NE_SW) {
                        railshape = IshRailShape.EAST_SW;
                    }
                } else if (state.pos.equals(this.pos.north().west())) {
                    if (state.shape == IshRailShape.NORTH_SOUTH) {
                        railshape = IshRailShape.NORTH_SE;
                    }
                    if (state.shape == IshRailShape.EAST_WEST) {
                        railshape = IshRailShape.WEST_SE;
                    }
                } else if (state.pos.equals(this.pos.north())) {
                    if (state.shape == IshRailShape.NE_SW) {
                        railshape = IshRailShape.SOUTH_NE;
                    }
                    if (state.shape == IshRailShape.NW_SE) {
                        railshape = IshRailShape.SOUTH_NW;
                    }
                } else if (state.pos.equals(this.pos.north().east())) {
                    if (state.shape == IshRailShape.NORTH_SOUTH) {
                        railshape = IshRailShape.NORTH_SW;
                    }
                    if (state.shape == IshRailShape.EAST_WEST) {
                        railshape = IshRailShape.EAST_SW;
                    }
                } else if (state.pos.equals(this.pos.east())) {
                    if (state.shape == IshRailShape.NE_SW) {
                        railshape = IshRailShape.WEST_NE;
                    }
                    if (state.shape == IshRailShape.NW_SE) {
                        railshape = IshRailShape.WEST_SE;
                    }
                } else if (state.pos.equals(this.pos.south().east())) {
                    if (state.shape == IshRailShape.NORTH_SOUTH) {
                        railshape = IshRailShape.SOUTH_NW;
                    }
                    if (state.shape == IshRailShape.EAST_WEST) {
                        railshape = IshRailShape.EAST_NW;
                    }
                }
            }

            if (railshape == null) {
                railshape = state.shape;
            }

            state.newState = state.newState.with(state.block.getShapeProperty(), railshape);
            state.world.setBlockState(state.pos, state.newState, 3);
        }
    }

    public BlockState getNewState() { return this.newState;
    }
}