package com.ish.ishrails.handlers;

import com.ish.ishrails.IshRails;
import com.ish.ishrails.blocks.IshRailBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.ish.ishrails.IshRails.MODID;

public class Registry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    /** BUKKAKS **/
    public static final RegistryObject<Block> ISH_RAIL = BLOCKS.register("rail", () -> new IshRailBlock());
    public static final RegistryObject<Item> ISH_RAIL_ITEM = ITEMS.register("rail", () -> new BlockItem(ISH_RAIL.get(), new Item.Properties().group(ItemGroup.TRANSPORTATION)));


    public static void registryInit() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
