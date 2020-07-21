package com.ish.ishrails.blocks;

import com.google.common.eventbus.Subscribe;
import com.ish.ishrails.state.properties.IshRailShape;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.MaterialLibrary;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.minecraft.client.renderer.RenderTypeLookup.setRenderLayer;

public class IshRailBlock extends AbstractIshRailBlock {
    public static final EnumProperty<IshRailShape> SHAPE = EnumProperty.create("shape", IshRailShape.class);

    public IshRailBlock() {
        super(false, AbstractBlock.Properties
                .create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.7F)
                .sound(SoundType.METAL));
        this.setDefaultState(this.stateContainer.getBaseState().with(SHAPE, IshRailShape.NORTH_SOUTH));
    }


    protected void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (blockIn.getDefaultState().canProvidePower() && (new IshRailState(worldIn, pos, state)).countAdjacentRails() == 3) {
            this.getUpdatedState(worldIn, pos, state, false);
        }

    }


    public Property<IshRailShape> getShapeProperty() {
        return SHAPE;
    }

//    /**
//     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
//     * blockstate.
//     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
//     * fine.
//     */
    public BlockState rotate(BlockState state, Rotation rot) {
        switch(rot) {
            case CLOCKWISE_180:
                switch((IshRailShape)state.get(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.with(SHAPE, IshRailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.with(SHAPE, IshRailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.with(SHAPE, IshRailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.with(SHAPE, IshRailShape.ASCENDING_NORTH);
                    case SOUTH_NE:
                        return state.with(SHAPE, IshRailShape.NORTH_SW);
                    case SOUTH_NW:
                        return state.with(SHAPE, IshRailShape.NORTH_SE);
                    case NORTH_SW:
                        return state.with(SHAPE, IshRailShape.SOUTH_NE);
                    case NORTH_SE:
                        return state.with(SHAPE, IshRailShape.SOUTH_NW);
                    case WEST_NE:
                        return state.with(SHAPE, IshRailShape.EAST_SW);
                    case EAST_NW:
                        return state.with(SHAPE, IshRailShape.WEST_SE);
                    case EAST_SW:
                        return state.with(SHAPE, IshRailShape.WEST_NE);
                    case WEST_SE:
                        return state.with(SHAPE, IshRailShape.EAST_NW);
                }
            case COUNTERCLOCKWISE_90:
                switch((IshRailShape)state.get(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.with(SHAPE, IshRailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.with(SHAPE, IshRailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.with(SHAPE, IshRailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.with(SHAPE, IshRailShape.ASCENDING_EAST);
                    case SOUTH_NE:
                        return state.with(SHAPE, IshRailShape.EAST_NW);
                    case SOUTH_NW:
                        return state.with(SHAPE, IshRailShape.EAST_SW);
                    case NORTH_SW:
                        return state.with(SHAPE, IshRailShape.WEST_SE);
                    case NORTH_SE:
                        return state.with(SHAPE, IshRailShape.WEST_NE);
                    case WEST_NE:
                        return state.with(SHAPE, IshRailShape.SOUTH_NW);
                    case EAST_NW:
                        return state.with(SHAPE, IshRailShape.NORTH_SW);
                    case EAST_SW:
                        return state.with(SHAPE, IshRailShape.NORTH_SE);
                    case WEST_SE:
                        return state.with(SHAPE, IshRailShape.SOUTH_NE);
                    case NORTH_SOUTH:
                        return state.with(SHAPE, IshRailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.with(SHAPE, IshRailShape.NORTH_SOUTH);
                    case NW_SE:
                        return state.with(SHAPE, IshRailShape.NE_SW);
                    case NE_SW:
                        return state.with(SHAPE, IshRailShape.NW_SE);
                }
            case CLOCKWISE_90:
                switch((IshRailShape)state.get(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.with(SHAPE, IshRailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.with(SHAPE, IshRailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.with(SHAPE, IshRailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.with(SHAPE, IshRailShape.ASCENDING_WEST);
                    case SOUTH_NE:
                        return state.with(SHAPE, IshRailShape.WEST_SE);
                    case SOUTH_NW:
                        return state.with(SHAPE, IshRailShape.WEST_NE);
                    case NORTH_SW:
                        return state.with(SHAPE, IshRailShape.EAST_NW);
                    case NORTH_SE:
                        return state.with(SHAPE, IshRailShape.EAST_SW);
                    case WEST_NE:
                        return state.with(SHAPE, IshRailShape.NORTH_SE);
                    case EAST_NW:
                        return state.with(SHAPE, IshRailShape.SOUTH_NE);
                    case EAST_SW:
                        return state.with(SHAPE, IshRailShape.SOUTH_NW);
                    case WEST_SE:
                        return state.with(SHAPE, IshRailShape.NORTH_SW);
                    case NORTH_SOUTH:
                        return state.with(SHAPE, IshRailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.with(SHAPE, IshRailShape.NORTH_SOUTH);
                    case NW_SE:
                        return state.with(SHAPE, IshRailShape.NE_SW);
                    case NE_SW:
                        return state.with(SHAPE, IshRailShape.NW_SE);
                }
            default:
                return state;
        }
    }

//    /**
//     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
//     * blockstate.
//     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
//     */
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        IshRailShape railshape = state.get(SHAPE);
        switch(mirrorIn) {
            case LEFT_RIGHT:
                switch(railshape) {
                    case ASCENDING_NORTH:
                        return state.with(SHAPE, IshRailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.with(SHAPE, IshRailShape.ASCENDING_NORTH);
                    case SOUTH_NE:
                        return state.with(SHAPE, IshRailShape.NORTH_SE);
                    case SOUTH_NW:
                        return state.with(SHAPE, IshRailShape.NORTH_SW);
                    case NORTH_SW:
                        return state.with(SHAPE, IshRailShape.SOUTH_NE);
                    case NORTH_SE:
                        return state.with(SHAPE, IshRailShape.SOUTH_NW);
                    case WEST_NE:
                        return state.with(SHAPE, IshRailShape.WEST_SE);
                    case EAST_NW:
                        return state.with(SHAPE, IshRailShape.EAST_SW);
                    case EAST_SW:
                        return state.with(SHAPE, IshRailShape.EAST_NW);
                    case WEST_SE:
                        return state.with(SHAPE, IshRailShape.WEST_NE);
                    case NW_SE:
                        return state.with(SHAPE, IshRailShape.NE_SW);
                    case NE_SW:
                        return state.with(SHAPE, IshRailShape.NW_SE);
                    default:
                        return super.mirror(state, mirrorIn);
                }
            case FRONT_BACK:
                switch(railshape) {
                    case ASCENDING_EAST:
                        return state.with(SHAPE, IshRailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.with(SHAPE, IshRailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_NE:
                        return state.with(SHAPE, IshRailShape.SOUTH_NW);
                    case SOUTH_NW:
                        return state.with(SHAPE, IshRailShape.SOUTH_NE);
                    case NORTH_SW:
                        return state.with(SHAPE, IshRailShape.NORTH_SE);
                    case NORTH_SE:
                        return state.with(SHAPE, IshRailShape.NORTH_SW);
                    case WEST_NE:
                        return state.with(SHAPE, IshRailShape.EAST_NW);
                    case EAST_NW:
                        return state.with(SHAPE, IshRailShape.WEST_NE);
                    case EAST_SW:
                        return state.with(SHAPE, IshRailShape.WEST_SE);
                    case WEST_SE:
                        return state.with(SHAPE, IshRailShape.EAST_SW);
                    case NW_SE:
                        return state.with(SHAPE, IshRailShape.NE_SW);
                    case NE_SW:
                        return state.with(SHAPE, IshRailShape.NW_SE);
                }
        }

        return super.mirror(state, mirrorIn);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE);
    }
}