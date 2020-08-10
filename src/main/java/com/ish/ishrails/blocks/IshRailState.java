package com.ish.ishrails.blocks;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.HashSet;
import javax.annotation.Nullable;
import com.ish.ishrails.state.properties.IshRailShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.ish.ishrails.IshRails.log;
import static java.lang.Math.abs;

public class IshRailState {
    private final World world;
    private final BlockPos pos;
    private final AbstractIshRailBlock block;
    private BlockState newState;
    private final boolean disableCorners;
    private final IshRailShape shape;
//    private final List<BlockPos> connectedRails = Lists.newArrayList();
    private final boolean canMakeSlopes;

    public IshRailState(World worldIn, BlockPos pos, BlockState state) {
        this.world = worldIn;
        this.pos = pos;
        this.newState = state;
        this.block = (AbstractIshRailBlock) state.getBlock();
        this.shape = this.block.getRailDirection(newState, worldIn, pos, null);
        this.disableCorners = !this.block.isFlexibleRail(newState, worldIn, pos);
        this.canMakeSlopes = this.block.canMakeSlopes(newState, worldIn, pos);
//        this.reset(ishrailshape);
    }

    public IshRailShape getShape() {
        return this.block.getRailDirection(this.newState, this.world, pos, null);
    }

//    public List<BlockPos> getConnectedRails() { return this.connectedRails;
//    }
//
//    private void refreshRail() {
//        this.connectedRails.clear();
//        switch(shape) {
//            case NORTH_SOUTH:
//                this.connectedRails.add(this.pos.north());
//                this.connectedRails.add(this.pos.south());
//            case EAST_WEST:
//                this.connectedRails.add(this.pos.west());
//                this.connectedRails.add(this.pos.east());
//            case NW_SE:
//                this.connectedRails.add(this.pos.north().west());
//                this.connectedRails.add(this.pos.south().east());
//            case NE_SW:
//                this.connectedRails.add(this.pos.north().east());
//                this.connectedRails.add(this.pos.south().west());
//            case ASCENDING_EAST:
//                this.connectedRails.add(this.pos.west());
//                this.connectedRails.add(this.pos.east().up());
//            case ASCENDING_WEST:
//                this.connectedRails.add(this.pos.west().up());
//                this.connectedRails.add(this.pos.east());
//            case ASCENDING_NORTH:
//                this.connectedRails.add(this.pos.north().up());
//                this.connectedRails.add(this.pos.south());
//            case ASCENDING_SOUTH:
//                this.connectedRails.add(this.pos.north());
//                this.connectedRails.add(this.pos.south().up());
//            case SOUTH_NE:
//                this.connectedRails.add(this.pos.north().east());
//                this.connectedRails.add(this.pos.south());
//            case SOUTH_NW:
//                this.connectedRails.add(this.pos.north().west());
//                this.connectedRails.add(this.pos.south());
//            case NORTH_SW:
//                this.connectedRails.add(this.pos.south().west());
//                this.connectedRails.add(this.pos.north());
//            case NORTH_SE:
//                this.connectedRails.add(this.pos.south().east());
//                this.connectedRails.add(this.pos.north());
//            case WEST_NE:
//                this.connectedRails.add(this.pos.north().east());
//                this.connectedRails.add(this.pos.west());
//            case WEST_SE:
//                this.connectedRails.add(this.pos.south().east());
//                this.connectedRails.add(this.pos.west());
//            case EAST_NW:
//                this.connectedRails.add(this.pos.north().west());
//                this.connectedRails.add(this.pos.east());
//            case EAST_SW:
//                this.connectedRails.add(this.pos.south().west());
//                this.connectedRails.add(this.pos.east());
//        }
//
//    }


    private boolean pointsAt(BlockPos fromPos, BlockPos toPos) {
        BlockState blockstate = this.world.getBlockState(fromPos);
        IshRailState rState;
        if (AbstractIshRailBlock.isRail(blockstate)) { rState = new IshRailState(this.world, fromPos, blockstate);
        } else {
            BlockPos pos2 = fromPos.down();
            blockstate = this.world.getBlockState(pos2);
            rState = AbstractIshRailBlock.isRail(blockstate) ? new IshRailState(this.world, pos2, blockstate) : null;
        }
//        log.debug(rState);
        if (rState == null) {return false;}
        boolean result = pointsAt(fromPos, toPos, rState.newState.get(rState.block.getShapeProperty()));
//        log.debug(result);
        return result;
    }

//    private boolean pointsAt(BlockPos fromPos, BlockPos toPos, IshRailState state) {
//        //
//        IshRailShape shape = state.newState.get(state.block.getShapeProperty());
//        return pointsAt(fromPos, toPos, shape);
//    }
    private boolean pointsAtReversed(BlockPos toPos) {return pointsAt(this.pos, toPos, this.getShape().reverse());}
    private boolean pointsAt(BlockPos toPos) {return pointsAt(this.pos, toPos, this.getShape());}
    private boolean pointsAt(BlockPos toPos, IshRailShape shape) {return pointsAt(this.pos, toPos, shape);}

    private boolean pointsAt(BlockPos fromPos, BlockPos toPos, IshRailShape shape) {
        try {
        switch(shape) {
            case NORTH_SOUTH:       return (toPos.equals(fromPos.north())        || toPos.equals(fromPos.south()));
            case EAST_WEST:         return (toPos.equals(fromPos.west())         || toPos.equals(fromPos.east()));
            case NW_SE:             return (toPos.equals(fromPos.north().west()) || toPos.equals(fromPos.south().east()));
            case NE_SW:             return (toPos.equals(fromPos.north().east()) || toPos.equals(fromPos.south().west()));
            case ASCENDING_EAST:    return (toPos.equals(fromPos.west())         || toPos.equals(fromPos.east().up()));
            case ASCENDING_WEST:    return (toPos.equals(fromPos.west().up())    || toPos.equals(fromPos.east()));
            case ASCENDING_NORTH:   return (toPos.equals(fromPos.north().up())   || toPos.equals(fromPos.south()));
            case ASCENDING_SOUTH:   return (toPos.equals(fromPos.north())        || toPos.equals(fromPos.south().up()));
            case NORTH_SW :          return (toPos.equals(fromPos.north().east()) || toPos.equals(fromPos.south()));
            case NORTH_SE:          return (toPos.equals(fromPos.north().west()) || toPos.equals(fromPos.south()));
            case SOUTH_NE:          return (toPos.equals(fromPos.south().west()) || toPos.equals(fromPos.north()));
            case SOUTH_NW:          return (toPos.equals(fromPos.south().east()) || toPos.equals(fromPos.north()));
            case EAST_SW:           return (toPos.equals(fromPos.north().east()) || toPos.equals(fromPos.west()));
            case EAST_NW:           return (toPos.equals(fromPos.south().east()) || toPos.equals(fromPos.west()));
            case WEST_SE:           return (toPos.equals(fromPos.north().west()) || toPos.equals(fromPos.east()));
            case WEST_NE:           return (toPos.equals(fromPos.south().west()) || toPos.equals(fromPos.east()));
            default: return false;
        }}
        catch (NullPointerException e) {return false;}
    }


    private boolean isAdjacentRail(BlockPos pos) { return AbstractIshRailBlock.isRail(this.world, pos) || AbstractIshRailBlock.isRail(this.world, pos.up()) || AbstractIshRailBlock.isRail(this.world, pos.down());
    }

    @Nullable
    private IshRailState createForAdjacent(BlockPos blockPos) {
//        log.debug("createForAdjacent");
        BlockState blockstate = this.world.getBlockState(blockPos);
        if (AbstractIshRailBlock.isRail(blockstate)) { return new IshRailState(this.world, blockPos, blockstate);
        } else {
//            BlockPos pos2 = blockPos.up();
//            blockstate = this.world.getBlockState(pos2);
//            if (AbstractIshRailBlock.isRail(blockstate)) { return new IshRailState(this.world, pos2, blockstate);
//            } else { }
            BlockPos pos2 = blockPos.down();
            blockstate = this.world.getBlockState(pos2);
            if (AbstractIshRailBlock.isRail(blockstate)
            && this.block.getRailDirection(blockstate, this.world, pos2, null).isAscending()) {
                ;
                return new IshRailState(this.world, pos2, blockstate);
            }
//                log.debug("failed");
            return null;
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

    private boolean pointsAtMe(BlockPos blockPos) {
        IshRailState state = this.createForAdjacent(blockPos);
        if (state == null) {
            return false;
        } else {
            IshRailShape railDirection = state.block.getRailDirection(state.newState, this.world, blockPos, null);
            boolean result = pointsAt(this.pos, blockPos, railDirection);
            return result;
        }
    }
    
    private HashSet<Dir> getOutletDirections() {
        Dir[] list = null;
        switch(this.shape) {
            case NORTH_SOUTH:case ASCENDING_NORTH:case ASCENDING_SOUTH:  list = new Dir[]{Dir.N, Dir.S}; break;
            case EAST_WEST:  case ASCENDING_EAST: case ASCENDING_WEST:   list = new Dir[]{Dir.E, Dir.W}; break;
            case NW_SE:             list = new Dir[]{Dir.SE , Dir.NW}; break;
            case NE_SW:             list = new Dir[]{Dir.SW , Dir.NE}; break;
            case NORTH_SW:          list = new Dir[]{Dir.SW , Dir.N}; break;
            case NORTH_SE:          list = new Dir[]{Dir.SE , Dir.N}; break;
            case SOUTH_NE:          list = new Dir[]{Dir.NE , Dir.S}; break;
            case SOUTH_NW:          list = new Dir[]{Dir.NW , Dir.S}; break;
            case EAST_SW:           list = new Dir[]{Dir.SW , Dir.E}; break;
            case EAST_NW:           list = new Dir[]{Dir.NW , Dir.E}; break;
            case WEST_SE:           list = new Dir[]{Dir.SE , Dir.W}; break;
            case WEST_NE:           list = new Dir[]{Dir.NE , Dir.W}; break;
            default: list = new Dir[]{}; break;
        }
        return new HashSet<Dir>(Arrays.asList(list));
    }
        
    private HashSet<Dir> getActualConnections() {
        HashSet<Dir> connects = new HashSet<Dir>();
        BlockPos p;
        for (Dir d: this.getOutletDirections()) {
            p = d.fromPos(this.pos);
            IshRailState s = createForAdjacent(p);
            if (s != null) {
                boolean b = s.pointsAtReversed(this.pos);
                if (b) {
                    connects.add(d);
                }
            }
//            else //log.debug("null");
        }
        return connects;
    }

//    private List<BlockPos> getActualConnections(BlockPos pos, IshRailShape shape) {
//        List<BlockPos> connects = Lists.newArrayList();
//        for (BlockPos p: getOutlets(pos, shape)) {
//            IshRailState s = createForAdjacent(p);
//            if (s != null && createForAdjacent(p).pointsAt(pos)) {
//                connects.add(p);
//            }
//        }
//        return connects;
//    }


    public HashSet<Dir> getOpenConnections() {
        HashSet<Dir> connects = new HashSet<Dir>();
        for (Dir d: this.getOutletDirections()) {
            IshRailState other = createForAdjacent(d.fromPos(this.pos));
            if (other == null) {
                connects.add(d);
            } else {
                //log.debug("a");
                Dir newDir = d.flip();
                //log.debug("b");
                IshRailShape s = other.shape;
                //log.debug("c");
                //log.debug(s);
                //log.debug(newDir);
                if (!s.hasDir(newDir)) {
                    //log.debug("d");
                    connects.add(d);
                    //log.debug("e");
                }
            }
        }
        return connects;
    }

    public Dir directionTo(IshRailState other) {
        for (Dir i: Dir.values()) {
            if (i.fromPos(this.pos).equals(other.pos)) {
                return i;
            }
        }
        return null;
    }

//    List<BlockPos> getOpenConnections(IshRailState state) {
//        List<BlockPos> connects = Lists.newArrayList();
//        for (BlockPos p: state.getOutlets()) {
//            if (!this.pointsAt(p)) {
//                connects.add(p);
//            }
//        }
//        return connects;
//    }


    public BlockPos[] getOutlets() {return getOutlets(this.pos, this.getShape());}
    public BlockPos[] getOutlets(BlockPos fromPos, IshRailShape shape) {
        switch(shape) {
            case NORTH_SOUTH:       return new BlockPos[]{fromPos.south()        , fromPos.north()};
            case EAST_WEST:         return new BlockPos[]{fromPos.east()         , fromPos.west()};
            case NW_SE:             return new BlockPos[]{fromPos.south().east() , fromPos.north().west()};
            case NE_SW:             return new BlockPos[]{fromPos.south().west() , fromPos.north().east()};
            case ASCENDING_EAST:    return new BlockPos[]{fromPos.east()         , fromPos.west().up()};
            case ASCENDING_WEST:    return new BlockPos[]{fromPos.east().up()    , fromPos.west()};
            case ASCENDING_NORTH:   return new BlockPos[]{fromPos.south().up()   , fromPos.north()};
            case ASCENDING_SOUTH:   return new BlockPos[]{fromPos.south()        , fromPos.north().up()};
            case NORTH_SW:          return new BlockPos[]{fromPos.south().west() , fromPos.north()};
            case NORTH_SE:          return new BlockPos[]{fromPos.south().east() , fromPos.north()};
            case SOUTH_NE:          return new BlockPos[]{fromPos.north().east() , fromPos.south()};
            case SOUTH_NW:          return new BlockPos[]{fromPos.north().west() , fromPos.south()};
            case EAST_SW:           return new BlockPos[]{fromPos.south().west() , fromPos.east()};
            case EAST_NW:           return new BlockPos[]{fromPos.north().west() , fromPos.east()};
            case WEST_SE:           return new BlockPos[]{fromPos.south().east() , fromPos.west()};
            case WEST_NE:           return new BlockPos[]{fromPos.north().east() , fromPos.west()};
            default: return new BlockPos[]{};
        }
    }


    public IshRailState correctShape(boolean isPowered, boolean justPlaced) {
        IshRailShape shape = this.getShape();
        IshRailShape railshape = null;
        if (!shape.isAscending()) {
            BlockPos blockposN = this.pos.north();
            BlockPos blockposNE = this.pos.north().east();
            BlockPos blockposE = this.pos.east();
            BlockPos blockposSE = this.pos.south().east();
            BlockPos blockposS = this.pos.south();
            BlockPos blockposSW = this.pos.south().west();
            BlockPos blockposW = this.pos.west();
            BlockPos blockposNW = this.pos.north().west();
            boolean flagN = this.pointsAtMe(blockposN);
            boolean flagNE = this.pointsAtMe(blockposNE);
            boolean flagE = this.pointsAtMe(blockposE);
            boolean flagSE = this.pointsAtMe(blockposSE);
            boolean flagS = this.pointsAtMe(blockposS);
            boolean flagSW = this.pointsAtMe(blockposSW);
            boolean flagW = this.pointsAtMe(blockposW);
            boolean flagNW = this.pointsAtMe(blockposNW);



            if (flagN && flagS && shape == IshRailShape.NORTH_SOUTH) {
                railshape = IshRailShape.NORTH_SOUTH;
            }
            if (flagNE && flagSW && shape == IshRailShape.NE_SW) {
                railshape = IshRailShape.NE_SW;
            }
            if (flagE && flagW && shape == IshRailShape.EAST_WEST) {
                railshape = IshRailShape.EAST_WEST;
            }
            if (flagSE && flagNW && shape == IshRailShape.NE_SW) {
                railshape = IshRailShape.NE_SW;
            }

            if (!this.disableCorners && !shape.isAscending()) { //
                if (flagS && !flagN) {
                    if (shape == IshRailShape.NE_SW && !flagSW) {
                        railshape = IshRailShape.SOUTH_NE;
                    }
                    if (shape == IshRailShape.NW_SE && !flagSE) {
                        railshape = IshRailShape.SOUTH_NW;
                    }
                }

                if (flagN && !flagS) {
                    if (shape == IshRailShape.NW_SE && !flagNW) {
                        railshape = IshRailShape.NORTH_SE;
                    }
                    if (shape == IshRailShape.NE_SW && !flagNE) {
                        railshape = IshRailShape.NORTH_SW;
                    }
                }


                if (flagE && !flagW) {
                    if (shape == IshRailShape.NW_SE && !flagSE) {
                        railshape = IshRailShape.EAST_NW;
                    }
                    if (shape == IshRailShape.NE_SW && !flagNE) {
                        railshape = IshRailShape.EAST_SW;
                    }
                }

                if (flagW && !flagE) {
                    if (shape == IshRailShape.NE_SW && !flagSW) {
                        railshape = IshRailShape.WEST_NE;
                    }
                    if (shape == IshRailShape.NW_SE && !flagNW) {
                        railshape = IshRailShape.WEST_SE;
                    }
                }


                if (flagSE && !flagNW) {
                    if (shape == IshRailShape.NORTH_SOUTH && !flagS) {
                        railshape = IshRailShape.NORTH_SE;
                    }
                    if (shape == IshRailShape.EAST_WEST && !flagE) {
                        railshape = IshRailShape.WEST_SE;
                    }
                }

                if (flagNW && !flagSE) {
                    if (shape == IshRailShape.NORTH_SOUTH && !flagN) {
                        railshape = IshRailShape.SOUTH_NW;
                    }
                    if (shape == IshRailShape.EAST_WEST && !flagW) {
                        railshape = IshRailShape.EAST_NW;
                    }
                }


                if (flagNE && !flagSW) {
                    if (shape == IshRailShape.NORTH_SOUTH && !flagN) {
                        railshape = IshRailShape.SOUTH_NE;
                    }
                    if (shape == IshRailShape.EAST_WEST && !flagE) {
                        railshape = IshRailShape.WEST_NE;
                    }
                }

                if (flagSW && !flagNE) {
                    if (shape == IshRailShape.NORTH_SOUTH && !flagS) {
                        railshape = IshRailShape.NORTH_SW;
                    }
                    if (shape == IshRailShape.EAST_WEST && !flagW) {
                        railshape = IshRailShape.EAST_SW;
                    }
                }
            }
        }

        if (railshape == null) {
            railshape = shape;
        }

        this.newState = this.newState.with(this.block.getShapeProperty(), railshape);
        if (justPlaced || this.world.getBlockState(this.pos) != this.newState) {
            this.world.setBlockState(this.pos, this.newState, 3);

//            log.debug("1");
            BlockPos[] outlets = getOutlets(this.pos, railshape);
            for(BlockPos outlet: outlets) {
//                log.debug("2");
                IshRailState railstate = createForAdjacent(outlet);
//                log.debug(outlet);
                if (railstate != null) {
//                    log.debug("3");
                    HashSet<Dir> openConn = railstate.getOpenConnections();
//                    for(BlockPos p: openConn) {log.debug(p.toString());}
                    if (openConn.size() != 0) {
//                        log.debug("found open");
                        railstate.correctAdjacentShape(this, openConn);
                    }
                }
            }
        }
    //log.debug("");
    return this;
    }

    public void addAndCheckAngle(Dir newDir, HashSet<Dir> set) {addAndCheckAngle(newDir, set, true);}
    public void addAndCheckAngle(Dir newDir, HashSet<Dir> set, boolean isDominant) {
        for (Dir incumbant: (HashSet<Dir>) set.clone()){
            //log.debug("A");
            int distance = abs(incumbant.angleDiff(newDir));
            //log.debug("B");
            if (distance <= 2) {
                if (isDominant) {
                    //log.debug("C.1");
                    set.remove(incumbant); // if there are conflicts, remove them.
                } else {
                    //log.debug("C.2");
                    return; // if there are conflicts, quit.
                }
            }
        }
        //log.debug("D");
        set.add(newDir);
        //log.debug("E");
    }


    public void correctAdjacentShape(IshRailState centralState, HashSet<Dir> openConn) {
        //log.debug("found sattelite");

        IshRailShape railshape = null;
        HashSet<Dir> connections = getOpenConnections();
        //log.debug(connections);
        addAndCheckAngle(directionTo(centralState), connections);
        //log.debug(connections);
        for (Dir d: getActualConnections()) {
            addAndCheckAngle(d, connections);
        }
        railshape = IshRailShape.formShape(connections);

        //log.debug(connections);


        //log.debug("railshape:");
        //log.debug(railshape);
//        if (railshape == null && connections.size() < 2) {
//            addAndCheckAngle(directionTo(centralState),connections);
//            railshape = IshRailShape.formShape(connections);
//        }
        if (railshape == null) {
            railshape = this.getShape();
        }

        this.newState = this.newState.with(this.block.getShapeProperty(), railshape);
        this.world.setBlockState(this.pos, this.newState, 3);
    }


    public BlockState getNewState() { return this.newState;
    }
}