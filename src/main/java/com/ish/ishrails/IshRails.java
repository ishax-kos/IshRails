package com.ish.ishrails;

import com.ish.ishrails.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(IshRails.MOD_ID)
public class IshRails {
    public static final String MOD_ID = "ishrails";
    public static Logger log = LogManager.getLogger();
    public static CommonProxy proxy;

}