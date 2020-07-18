package com.ish.ishrails;

import com.ish.ishrails.blocks.IshRailBlock;
import com.ish.ishrails.proxy.CommonProxy;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(IshRails.MODID)
public class IshRails {
    public static final String MODID = "ishrails";
    public static Logger log = LogManager.getLogger();
    public static CommonProxy proxy;

    public IshRails() {
        registryInit();
    }

//    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegistryEvents { }

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    /** BUKKAKS **/
    private static final RegistryObject<Block> ISH_RAIL = BLOCKS.register("rail", () -> new IshRailBlock());
    private static final RegistryObject<Item> ISH_RAIL_ITEM = ITEMS.register("rail", () -> new BlockItem(ISH_RAIL.get(), new Item.Properties().group(ItemGroup.TRANSPORTATION)));

    public static void registryInit() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}