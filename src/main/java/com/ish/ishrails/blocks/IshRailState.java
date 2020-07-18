package com.ish.ishrails.blocks;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

import com.ish.ishrails.state.properties.IshRailShape;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IshRailState {
    private final World world;
    private final BlockPos pos;
    private final AbstractIshRailBlock block;
    private BlockState newState;
    private final boolean disableCorners;
    private final List<BlockPos> connectedRails = Lists.newArrayList();
    private final boolean canMakeSlopes;

    public IshRailState(World worldIn, BlockPos p_i47755_2_, BlockState p_i47755_3_) {
        this.world = worldIn;
        this.pos = p_i47755_2_;
        this.newState = p_i47755_3_;
        this.block = (AbstractIshRailBlock)p_i47755_3_.getBlock();
        IshRailShape ishrailshape = this.block.getRailDirection(newState, worldIn, p_i47755_2_, null);
        this.disableCorners = !this.block.isFlexibleRail(newState, worldIn, p_i47755_2_);
        this.canMakeSlopes = this.block.canMakeSlopes(newState, worldIn, p_i47755_2_);
        this.reset(ishrailshape);
    }

    public List<BlockPos> getConnectedRails() {
        return this.connectedRails;
    }

    private void reset(IshRailShape shape) {
        this.connectedRails.clear();
        switch(shape) {
            case NORTH_SOUTH:
                this.connectedRails.add(this.pos.north());
                this.connectedRails.add(this.pos.south());
                break;
            case EAST_WEST:
                this.connectedRails.add(this.pos.west());
                this.connectedRails.add(this.pos.east());
                break;
            case NW_SE:
                this.connectedRails.add(this.pos.north().west());
                this.connectedRails.add(this.pos.south().east());
                break;
            case NE_SW:
                this.connectedRails.add(this.pos.north().east());
                this.connectedRails.add(this.pos.south().west());
                break;
            case ASCENDING_EAST:
                this.connectedRails.add(this.pos.west());
                this.connectedRails.add(this.pos.east().up());
                break;
            case ASCENDING_WEST:
                this.connectedRails.add(this.pos.west().up());
                this.connectedRails.add(this.pos.east());
                break;
            case ASCENDING_NORTH:
                this.connectedRails.add(this.pos.north().up());
                this.connectedRails.add(this.pos.south());
                break;
            case ASCENDING_SOUTH:
                this.connectedRails.add(this.pos.north());
                this.connectedRails.add(this.pos.south().up());
                break;
            case SOUTH_NE:
                this.connectedRails.add(this.pos.north().east());
                this.connectedRails.add(this.pos.south());
                break;
            case SOUTH_NW:
                this.connectedRails.add(this.pos.north().west());
                this.connectedRails.add(this.pos.south());
                break;
            case NORTH_SW:
                this.connectedRails.add(this.pos.south().west());
                this.connectedRails.add(this.pos.north());
                break;
            case NORTH_SE:
                this.connectedRails.add(this.pos.south().east());
                this.connectedRails.add(this.pos.north());
                break;
            case WEST_NE:
                this.connectedRails.add(this.pos.north().east());
                this.connectedRails.add(this.pos.west());
                break;
            case WEST_SE:
                this.connectedRails.add(this.pos.south().east());
                this.connectedRails.add(this.pos.west());
                break;
            case EAST_NW:
                this.connectedRails.add(this.pos.north().west());
                this.connectedRails.add(this.pos.east());
                break;
            case EAST_SW:
                this.connectedRails.add(this.pos.south().west());
                this.connectedRails.add(this.pos.east());
        }

    }

    private void checkConnected() {
        for(int i = 0; i < this.connectedRails.size(); ++i) {
            IshRailState railstate = this.createForAdjacent(this.connectedRails.get(i));
            if (railstate != null && railstate.isConnectedTo(this)) {
                this.connectedRails.set(i, railstate.pos);
            } else {
                this.connectedRails.remove(i--);
            }
        }

    }

    private boolean isAdjacentRail(BlockPos p_196902_1_) {
        return AbstractRailBlock.isRail(this.world, p_196902_1_) || AbstractRailBlock.isRail(this.world, p_196902_1_.up()) || AbstractRailBlock.isRail(this.world, p_196902_1_.down());
    }

    @Nullable
    private IshRailState createForAdjacent(BlockPos p_196908_1_) {
        BlockState blockstate = this.world.getBlockState(p_196908_1_);
        if (AbstractRailBlock.isRail(blockstate)) {
            return new IshRailState(this.world, p_196908_1_, blockstate);
        } else {
            BlockPos lvt_2_1_ = p_196908_1_.up();
            blockstate = this.world.getBlockState(lvt_2_1_);
            if (AbstractRailBlock.isRail(blockstate)) {
                return new IshRailState(this.world, lvt_2_1_, blockstate);
            } else {
                lvt_2_1_ = p_196908_1_.down();
                blockstate = this.world.getBlockState(lvt_2_1_);
                return AbstractRailBlock.isRail(blockstate) ? new IshRailState(this.world, lvt_2_1_, blockstate) : null;
            }
        }
    }

    private boolean isConnectedTo(IshRailState p_196919_1_) {
        return this.isConnectedTo(p_196919_1_.pos);
    }

    private boolean isConnectedTo(BlockPos p_196904_1_) {
        for(int i = 0; i < this.connectedRails.size(); ++i) {
            BlockPos blockposN = this.connectedRails.get(i);
            if (blockposN.getX() == p_196904_1_.getX() && blockposN.getZ() == p_196904_1_.getZ()) {
                return true;
            }
        }

        return false;
    }

    protected int countAdjacentRails() {
        int i = 0;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.isAdjacentRail(this.pos.offset(direction))) {
                ++i;
            }
        }

        return i;
    }

    private boolean func_196905_c(IshRailState p_196905_1_) {
        return this.isConnectedTo(p_196905_1_) || this.connectedRails.size() != 2;
    }

    private void func_208510_c(IshRailState p_208510_1_) {
        this.connectedRails.add(p_208510_1_.pos);
        BlockPos blockposN = this.pos.north();
        BlockPos blockposNE = this.pos.north().east();
        BlockPos blockposE = this.pos.east();
        BlockPos blockposSE = this.pos.south().east();
        BlockPos blockposS = this.pos.south();
        BlockPos blockposSW = this.pos.south().west();
        BlockPos blockposW = this.pos.west();
        BlockPos blockposNW = this.pos.north().west();
        boolean flagN = this.isConnectedTo(blockposN);
        boolean flagNE = this.isConnectedTo(blockposNE);
        boolean flagE = this.isConnectedTo(blockposE);
        boolean flagSE = this.isConnectedTo(blockposSE);
        boolean flagS = this.isConnectedTo(blockposS);
        boolean flagSW = this.isConnectedTo(blockposSW);
        boolean flagW = this.isConnectedTo(blockposW);
        boolean flagNW = this.isConnectedTo(blockposNW);
        IshRailShape railshape = null;
        if (flagN || flagS) {
            railshape = IshRailShape.NORTH_SOUTH;
        }

        if (flagW || flagE) {
            railshape = IshRailShape.EAST_WEST;
        }

        if (!this.disableCorners) {
            if (flagS && !flagN) {
                if (flagNE && !flagNW) {
                    railshape = IshRailShape.SOUTH_NE;
                }
                if (flagNW && !flagNE) {
                    railshape = IshRailShape.SOUTH_NW;
                }
            }

            if (flagN && !flagS) {
                if (flagSE && !flagSW) {
                    railshape = IshRailShape.NORTH_SE;
                }
                if (flagSW && !flagSE) {
                    railshape = IshRailShape.NORTH_SW;
                }
            }

            if (flagE && !flagW) {
                if (flagNW && !flagSW) {
                    railshape = IshRailShape.EAST_NW;
                }
                if (flagSW && !flagNW) {
                    railshape = IshRailShape.EAST_SW;
                }
            }

            if (flagW && !flagE) {
                if (flagNE && !flagSE) {
                    railshape = IshRailShape.WEST_NE;
                }
                if (flagSE && !flagNE) {
                    railshape = IshRailShape.WEST_SE;
                }
            }
        }

        if (railshape == IshRailShape.NORTH_SOUTH && canMakeSlopes) {
            if (AbstractRailBlock.isRail(this.world, blockposN.up())) {
                railshape = IshRailShape.ASCENDING_NORTH;
            }

            if (AbstractRailBlock.isRail(this.world, blockposS.up())) {
                railshape = IshRailShape.ASCENDING_SOUTH;
            }
        }

        if (railshape == IshRailShape.EAST_WEST && canMakeSlopes) {
            if (AbstractRailBlock.isRail(this.world, blockposE.up())) {
                railshape = IshRailShape.ASCENDING_EAST;
            }

            if (AbstractRailBlock.isRail(this.world, blockposW.up())) {
                railshape = IshRailShape.ASCENDING_WEST;
            }
        }

        if (railshape == null) {
            railshape = IshRailShape.NORTH_SOUTH;
        }

        this.newState = this.newState.with(this.block.getShapeProperty(), railshape);
        this.world.setBlockState(this.pos, this.newState, 3);
    }

    private boolean func_208512_d(BlockPos p_208512_1_) {
        IshRailState railstate = this.createForAdjacent(p_208512_1_);
        if (railstate == null) {
            return false;
        } else {
            railstate.checkConnected();
            return railstate.func_196905_c(this);
        }
    }

    public IshRailState func_226941_a_(boolean p_226941_1_, boolean p_226941_2_, IshRailShape shape) {
        BlockPos blockposN = this.pos.north();
        BlockPos blockposNE = this.pos.north().east();
        BlockPos blockposE = this.pos.east();
        BlockPos blockposSE = this.pos.south().east();
        BlockPos blockposS = this.pos.south();
        BlockPos blockposSW = this.pos.south().west();
        BlockPos blockposW = this.pos.west();
        BlockPos blockposNW = this.pos.north().west();
        boolean flagN = this.isConnectedTo(blockposN);
        boolean flagNE = this.isConnectedTo(blockposNE);
        boolean flagE = this.isConnectedTo(blockposE);
        boolean flagSE = this.isConnectedTo(blockposSE);
        boolean flagS = this.isConnectedTo(blockposS);
        boolean flagSW = this.isConnectedTo(blockposSW);
        boolean flagW = this.isConnectedTo(blockposW);
        boolean flagNW = this.isConnectedTo(blockposNW);
        IshRailShape railshape = null;
        boolean flag4 = flagN || flagS;
        boolean flag5 = flagW || flagE;
        if (flag4 && !flag5) {
            railshape = IshRailShape.NORTH_SOUTH;
        }

        if (flag5 && !flag4) {
            railshape = IshRailShape.EAST_WEST;
        }

        boolean flagN_SE = flagN && flagSE;
        boolean flagN_SW = flagN && flagSW;
        boolean flagS_NE = flagS && flagNE;
        boolean flagS_NW = flagS && flagNW;
        boolean flagE_SW = flagE && flagSW;
        boolean flagE_NW = flagE && flagNW;
        boolean flagW_SE = flagW && flagSE;
        boolean flagW_NE = flagW && flagNE;
        if (!this.disableCorners) {
            if (flagS && !flagN) {
                if (flagNE && !flagNW) {
                    railshape = IshRailShape.SOUTH_NE;
                }
                if (flagNW && !flagNE) {
                    railshape = IshRailShape.SOUTH_NW;
                }
            }

            if (flagN && !flagS) {
                if (flagSE && !flagSW) {
                    railshape = IshRailShape.NORTH_SE;
                }
                if (flagSW && !flagSE) {
                    railshape = IshRailShape.NORTH_SW;
                }
            }

            if (flagE && !flagW) {
                if (flagNW && !flagSW) {
                    railshape = IshRailShape.EAST_NW;
                }
                if (flagSW && !flagNW) {
                    railshape = IshRailShape.EAST_SW;
                }
            }

            if (flagW && !flagE) {
                if (flagNE && !flagSE) {
                    railshape = IshRailShape.WEST_NE;
                }
                if (flagSE && !flagNE) {
                    railshape = IshRailShape.WEST_SE;
                }
            }
        }

        if (railshape == null) {
            if (flag4 && flag5) {
                railshape = shape;
            } else if (flag4) {
                railshape = IshRailShape.NORTH_SOUTH;
            } else if (flag5) {
                railshape = IshRailShape.EAST_WEST;
            }

//            if (!this.disableCorners) {
//                if (p_226941_1_) {
//                    if (flag6) {
//                        railshape = IshRailShape.SOUTH_EAST;
//                    }
//
//                    if (flag7) {
//                        railshape = IshRailShape.SOUTH_WEST;
//                    }
//
//                    if (flag8) {
//                        railshape = IshRailShape.NORTH_EAST;
//                    }
//
//                    if (flag9) {
//                        railshape = IshRailShape.NORTH_WEST;
//                    }
//                } else {
//                    if (flag9) {
//                        railshape = IshRailShape.NORTH_WEST;
//                    }
//
//                    if (flag8) {
//                        railshape = IshRailShape.NORTH_EAST;
//                    }
//
//                    if (flag7) {
//                        railshape = IshRailShape.SOUTH_WEST;
//                    }
//
//                    if (flag6) {
//                        railshape = IshRailShape.SOUTH_EAST;
//                    }
//                }
//            }
        }

        if (railshape == IshRailShape.NORTH_SOUTH && canMakeSlopes) {
            if (AbstractRailBlock.isRail(this.world, blockposN.up())) {
                railshape = IshRailShape.ASCENDING_NORTH;
            }

            if (AbstractRailBlock.isRail(this.world, blockposS.up())) {
                railshape = IshRailShape.ASCENDING_SOUTH;
            }
        }

        if (railshape == IshRailShape.EAST_WEST && canMakeSlopes) {
            if (AbstractRailBlock.isRail(this.world, blockposE.up())) {
                railshape = IshRailShape.ASCENDING_EAST;
            }

            if (AbstractRailBlock.isRail(this.world, blockposW.up())) {
                railshape = IshRailShape.ASCENDING_WEST;
            }
        }

        if (railshape == null) {
            railshape = shape;
        }

        this.reset(railshape);
        this.newState = this.newState.with(this.block.getShapeProperty(), railshape);
        if (p_226941_2_ || this.world.getBlockState(this.pos) != this.newState) {
            this.world.setBlockState(this.pos, this.newState, 3);

            for(int i = 0; i < this.connectedRails.size(); ++i) {
                IshRailState railstate = this.createForAdjacent(this.connectedRails.get(i));
                if (railstate != null) {
                    railstate.checkConnected();
                    if (railstate.func_196905_c(this)) {
                        railstate.func_208510_c(this);
                    }
                }
            }
        }

        return this;
    }

    public BlockState getNewState() {
        return this.newState;
    }
}