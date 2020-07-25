package com.ish.ishrails.blocks;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

import com.ish.ishrails.state.properties.IshRailShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.block.RailState;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.ish.ishrails.IshRails.log;

public class IshRailState {
    private final World world;
    private final BlockPos pos;
    private final AbstractIshRailBlock block;
    private BlockState newState;
    private final boolean disableCorners;
//    private final List<BlockPos> connectedRails = Lists.newArrayList();
    private final boolean canMakeSlopes;

    public IshRailState(World worldIn, BlockPos pos, BlockState state) {
        this.world = worldIn;
        this.pos = pos;
        this.newState = state;
        this.block = (AbstractIshRailBlock) state.getBlock();
        IshRailShape ishrailshape = this.block.getRailDirection(newState, worldIn, pos, null);
        this.disableCorners = !this.block.isFlexibleRail(newState, worldIn, pos);
        this.canMakeSlopes = this.block.canMakeSlopes(newState, worldIn, pos);
//        this.reset(ishrailshape);
    }

//    public IshRailShape getShape(BlockPos pos) {
//        return this.block.getRailDirection(newState, world, pos, null);
//    }

//    public List<BlockPos> getConnectedRails() { return this.connectedRails;
//    }
//
//    private void reset(IshRailShape shape) {
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

    private boolean pointsAt(BlockPos fromPos, BlockPos toPos, IshRailShape shape) {
//        log.debug(fromPos.north().getX());
//        log.debug(fromPos.north().getY());
//        log.debug(fromPos.north().getZ());
//        log.debug(toPos.equals(fromPos.north()));
//        log.debug(toPos.getX());
//        log.debug(toPos.getY());
//        log.debug(toPos.getZ());
        switch(shape) {
            case NORTH_SOUTH:       return (toPos.equals(fromPos.north())        || toPos.equals(fromPos.south()));
            case EAST_WEST:         return (toPos.equals(fromPos.west())         || toPos.equals(fromPos.east()));
            case NW_SE:             return (toPos.equals(fromPos.north().west()) || toPos.equals(fromPos.south().east()));
            case NE_SW:             return (toPos.equals(fromPos.north().east()) || toPos.equals(fromPos.south().west()));
            case ASCENDING_EAST:    return (toPos.equals(fromPos.west())         || toPos.equals(fromPos.east().up()));
            case ASCENDING_WEST:    return (toPos.equals(fromPos.west().up())    || toPos.equals(fromPos.east()));
            case ASCENDING_NORTH:   return (toPos.equals(fromPos.north().up())   || toPos.equals(fromPos.south()));
            case ASCENDING_SOUTH:   return (toPos.equals(fromPos.north())        || toPos.equals(fromPos.south().up()));
            case SOUTH_NE:          return (toPos.equals(fromPos.north().east()) || toPos.equals(fromPos.south()));
            case SOUTH_NW:          return (toPos.equals(fromPos.north().west()) || toPos.equals(fromPos.south()));
            case NORTH_SW:          return (toPos.equals(fromPos.south().west()) || toPos.equals(fromPos.north()));
            case NORTH_SE:          return (toPos.equals(fromPos.south().east()) || toPos.equals(fromPos.north()));
            case WEST_NE:           return (toPos.equals(fromPos.north().east()) || toPos.equals(fromPos.west()));
            case WEST_SE:           return (toPos.equals(fromPos.south().east()) || toPos.equals(fromPos.west()));
            case EAST_NW:           return (toPos.equals(fromPos.north().west()) || toPos.equals(fromPos.east()));
            case EAST_SW:           return (toPos.equals(fromPos.south().west()) || toPos.equals(fromPos.east()));
            default: return false;
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

    private boolean pointsAtMe(BlockPos blockPos) {
        IshRailState state = this.createForAdjacent(blockPos);
        log.debug(state);
        if (state == null) {
            return false;
        } else {
            IshRailShape railDirection = state.block.getRailDirection(state.newState, this.world, blockPos, null);
            boolean result = pointsAt(this.pos, blockPos, railDirection);
            log.debug(result);
            return result;
        }
    }

//    public RailState func_226941_a_(boolean p_226941_1_, boolean p_226941_2_, RailShape p_226941_3_) {
//
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
        boolean flagN  = this.pointsAtMe(blockposN);
        boolean flagNE = this.pointsAtMe(blockposNE);
        boolean flagE  = this.pointsAtMe(blockposE);
        boolean flagSE = this.pointsAtMe(blockposSE);
        boolean flagS  = this.pointsAtMe(blockposS);
        boolean flagSW = this.pointsAtMe(blockposSW);
        boolean flagW  = this.pointsAtMe(blockposW);
        boolean flagNW = this.pointsAtMe(blockposNW);
        IshRailShape railshape = null;

        if (true && !shape.isAscending()) { //!this.disableCorners
            if (flagS && !flagN) {
                if (shape == IshRailShape.NE_SW) {
                    railshape = IshRailShape.SOUTH_NE;
                }
                if (shape == IshRailShape.NW_SE) {
                    railshape = IshRailShape.SOUTH_NW;
                }
            }

            if (flagN && !flagS) {
                if (shape == IshRailShape.NW_SE) {
                    railshape = IshRailShape.NORTH_SE;
                }
                if (shape == IshRailShape.NE_SW) {
                    railshape = IshRailShape.NORTH_SW;
                }
            }


            if (flagE && !flagW) {
                if (shape == IshRailShape.NW_SE) {
                    railshape = IshRailShape.EAST_NW;
                }
                if (shape == IshRailShape.NE_SW) {
                    railshape = IshRailShape.EAST_SW;
                }
            }

            if (flagW && !flagE) {
                if (shape == IshRailShape.NE_SW) {
                    railshape = IshRailShape.WEST_NE;
                }
                if (shape == IshRailShape.NW_SE) {
                    railshape = IshRailShape.WEST_SE;
                }
            }
        }

        if (railshape == null) {
            railshape = shape;
        }

        this.newState = this.newState.with(this.block.getShapeProperty(), railshape);
        if (justPlaced || this.world.getBlockState(this.pos) != this.newState) {
            this.world.setBlockState(this.pos, this.newState, 3);
//
//            for(int i = 0; i < this.connectedRails.size(); ++i) {
//                IshRailState railstate = this.createForAdjacent(this.connectedRails.get(i));
//                if (railstate != null) {
//                    railstate.checkConnected();
//                    if (railstate.canConnectTo(this)) {
//                        railstate.correctShapeNeighbor(this, shape);
//                    }
//                }
//            }
        } return this;
    }

    public BlockState getNewState() { return this.newState;
    }
}