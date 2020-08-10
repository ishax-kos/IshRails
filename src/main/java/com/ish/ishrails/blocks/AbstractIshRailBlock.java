package com.ish.ishrails.blocks;

import com.ish.ishrails.state.properties.IshRailShape;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;

import static com.ish.ishrails.IshRails.log;
import static java.lang.StrictMath.floor;


public abstract class AbstractIshRailBlock extends Block implements IAbstractIshRailBlock  {
    protected static final VoxelShape FLAT_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    protected static final VoxelShape ASCENDING_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    private final boolean disableCorners;

    public static boolean isRail(World worldIn, BlockPos pos) {
        return isRail(worldIn.getBlockState(pos));
    }

    public static boolean isRail(BlockState state) {
        Block block = state.getBlock();
        return block instanceof AbstractRailBlock || block instanceof AbstractIshRailBlock;
    }

    protected AbstractIshRailBlock(boolean disableCorners, AbstractBlock.Properties blockProperties) {
        super(blockProperties);
        this.disableCorners = disableCorners;
    }

    public boolean areCornersDisabled() {
        return this.disableCorners;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        IshRailShape shape = state.isIn(this) ? state.get(this.getShapeProperty()) : null;
        IshRailShape shape2 = state.getBlock() == this ? getRailDirection(state, worldIn, pos, null) : null;
        return shape != null && shape.isAscending() ? ASCENDING_AABB : FLAT_AABB;
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return hasSolidSideOnTop(worldIn, pos.down());
    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.isIn(state.getBlock())) {
//            log.debug("peep");
            this.func_235327_a_(state, worldIn, pos, isMoving);
        }
    }

    protected BlockState func_235327_a_(BlockState blockState, World world, BlockPos pos, boolean isMoving) {
        blockState = this.getUpdatedState(world, pos, blockState, true);
        if (this.disableCorners) {
            blockState.neighborChanged(world, pos, this, pos, isMoving);
        }

        return blockState;
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            IshRailShape IshRailShape = getRailDirection(state, worldIn, pos, null);
            if (notValidForAscending(pos, worldIn, IshRailShape) && !worldIn.isAirBlock(pos)) {
                if (!isMoving) {
                    spawnDrops(state, worldIn, pos);
                }

                worldIn.removeBlock(pos, isMoving);
            } else {
//                this.updateState(state, worldIn, pos, blockIn);
            }

        }
    }
    
    
    private static boolean notValidForAscending(BlockPos pos, World world, IshRailShape shape) {
//        if (!hasSolidSideOnTop(world, pos.down())) {
//            return true;
//        }
//        else {
////            switch(shape) {
////                case ASCENDING_EAST:
////                    return !hasSolidSideOnTop(world, pos.east());
////                case ASCENDING_WEST:
////                    return !hasSolidSideOnTop(world, pos.west());
////                case ASCENDING_NORTH:
////                    return !hasSolidSideOnTop(world, pos.north());
////                case ASCENDING_SOUTH:
////                    return !hasSolidSideOnTop(world, pos.south());
////                default:
////                    return false;
////            }
//        }
        return !hasSolidSideOnTop(world, pos.down());
    }

    protected void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn) {
    }

    protected BlockState getUpdatedState(World worldIn, BlockPos pos, BlockState state, boolean placing) {
        if (worldIn.isRemote) {
            return state;
        } else {
//            IshRailShape shape = state.get(this.getShapeProperty());
            return (new IshRailState(worldIn, pos, state)).correctShape(worldIn.isBlockPowered(pos), placing).getNewState();
        }
    }

    /*
     * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
     */
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving) {
            super.onReplaced(state, worldIn, pos, newState, isMoving);
            if (getRailDirection(state, worldIn, pos, null).isAscending()) {
                worldIn.notifyNeighborsOfStateChange(pos.up(), this);
            }

            if (this.disableCorners) {
                worldIn.notifyNeighborsOfStateChange(pos, this);
                worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            }

        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
//        BlockState blockstate = super.getDefaultState();
        IshRailShape shape = IshRailShape.ASCENDING_SOUTH;
        int side = context.getFace().getIndex();
        World world = context.getWorld();
        BlockPos pos = context.getPos().offset(context.getFace(),-1);
        ;
        breakTo:{
            if (side >= 2) {
                if (world.getBlockState(pos).isSolidSide(EmptyBlockReader.INSTANCE, pos, context.getFace())) {
                    switch (side) {
                        case 2: shape = IshRailShape.ASCENDING_SOUTH; break;
                        case 3: shape = IshRailShape.ASCENDING_NORTH; break;
                        case 4: shape = IshRailShape.ASCENDING_EAST; break;
                        case 5: shape = IshRailShape.ASCENDING_WEST; break;
                    }
                    break breakTo;
                }
            }
            int direction = (int) floor((context.getPlacementYaw() + 22.5f) / 45.0f);
            switch (direction & 3) {
                case 0: shape = IshRailShape.NORTH_SOUTH; break;
                case 1: shape = IshRailShape.NE_SW; break;
                case 2: shape = IshRailShape.EAST_WEST; break;
                case 3: shape = IshRailShape.NW_SE; break;
            }
        }
//        else {
//            int direction = (int) floor((context.getPlacementYaw()) / 45.0f);
//            switch (direction & 4) {
//                case 0: shape = IshRailShape.NORTH_SE; break;
//                case 1: shape = IshRailShape.NORTH_SW; break;
//                case 2: shape = IshRailShape.EAST_SW; break;
//                case 3: shape = IshRailShape.EAST_NW; break;
//                case 4: shape = IshRailShape.SOUTH_NW; break;
//                case 5: shape = IshRailShape.SOUTH_NE; break;
//                case 6: shape = IshRailShape.WEST_NE; break;
//                case 7: shape = IshRailShape.WEST_SE; break;
//            }
//        }
        
        return super.getDefaultState().with(this.getShapeProperty(), shape);
    }

//    @Deprecated //Forge: Use getRailDirection(IBlockAccess, BlockPos, IBlockState, EntityMinecart) for enhanced ability
    public abstract Property<IshRailShape> getShapeProperty();

    /* ======================================== FORGE START =====================================*/

    @Override
    public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos) {
        return  !this.disableCorners;
    }

    @Override
    public IshRailShape getRailDirection(BlockState state, IBlockReader world, BlockPos pos, @javax.annotation.Nullable net.minecraft.entity.item.minecart.AbstractMinecartEntity cart) {
        return state.get(getShapeProperty());
    }
    /* ========================================= FORGE END ======================================*/
}