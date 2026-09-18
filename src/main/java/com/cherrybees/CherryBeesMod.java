package com.cherrybees;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CherryBeesMod implements ModInitializer {
    public static final String MOD_ID = "cherrybees";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Cherry Bees loaded - bees now pollinate cherry blossoms!");
    }
}

