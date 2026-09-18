package com.cherrybees;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CherryBeesMod implements ModInitializer {
    public static final String MOD_ID = "cherrybees";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final RegistryKey<ItemGroup> SPAWN_EGGS_GROUP =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("minecraft", "spawn_eggs"));

    public static final Item CHERRY_BEE_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "cherry_bee_spawn_egg"),
            new SpawnEggItem(new Item.Settings().spawnEgg(EntityType.BEE)));

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(SPAWN_EGGS_GROUP).register(entries -> {
            entries.add(new ItemStack(CHERRY_BEE_SPAWN_EGG));
        });

        LOGGER.info("Cherry Bees loaded - pink bees pollinate cherry blossoms!");
    }
}
