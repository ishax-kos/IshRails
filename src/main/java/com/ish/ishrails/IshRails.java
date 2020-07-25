package com.ish.ishrails;

import com.ish.ishrails.handlers.ClientEventHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ish.ishrails.handlers.Registry.registryInit;


@Mod(IshRails.MODID)
public class IshRails {
    public static final String MODID = "ishrails";
    public static Logger log = LogManager.getLogger();


    public IshRails() {
        registryInit();
        IEventBus bus= FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientEventHandler::onClientSetupEvent);
    }


    public static int log2(int n){
        if(n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }


    private void doClientEvents(final FMLClientSetupEvent event) {

    }
}
