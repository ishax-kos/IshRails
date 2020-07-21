package com.ish.ishrails.handlers;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEventHandler {
//    @SubscribeEvent
    public static void onClientSetupEvent(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Registry.ISH_RAIL.get(), RenderType.getCutout());
    }
}
