package net.xalcon.torchmaster;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "torchmaster";
	public static final String MOD_NAME = "Torchmaster";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static ResourceLocation modLoc(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }
}