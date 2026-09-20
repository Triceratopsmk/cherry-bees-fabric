package com.cherrybees;

import com.cherrybees.entity.CherryBeeEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CherryBeesMod implements ModInitializer {
    public static final String MOD_ID = "cherrybees";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final RegistryKey<ItemGroup> SPAWN_EGGS_GROUP =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("minecraft", "spawn_eggs"));
    private static final RegistryKey<ItemGroup> FOOD_AND_DRINK_GROUP =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("minecraft", "food_and_drink"));
    private static final RegistryKey<ItemGroup> FUNCTIONAL_GROUP =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("minecraft", "functional"));

    private static final RegistryKey<EntityType<?>> CHERRY_BEE_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, "cherry_bee"));

    public static final EntityType<CherryBeeEntity> CHERRY_BEE = Registry.register(
            Registries.ENTITY_TYPE,
            CHERRY_BEE_KEY,
            EntityType.Builder.create(CherryBeeEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.7F, 0.6F)
                    .maxTrackingRange(8)
                    .trackingTickInterval(3)
                    .build(CHERRY_BEE_KEY));

    private static final Identifier CHERRY_BEE_SPAWN_EGG_ID = Identifier.of(MOD_ID, "cherry_bee_spawn_egg");

    public static final Item CHERRY_BEE_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            CHERRY_BEE_SPAWN_EGG_ID,
            new SpawnEggItem(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, CHERRY_BEE_SPAWN_EGG_ID))
                    .spawnEgg(CHERRY_BEE)));

    private static final Identifier GIANT_EFFECT_ID = Identifier.of(MOD_ID, "giant");

    public static final RegistryEntry.Reference<StatusEffect> GIANT_EFFECT = Registry.registerReference(
            Registries.STATUS_EFFECT,
            GIANT_EFFECT_ID,
            new GiantStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9B30D9)
                    .addAttributeModifier(
                            EntityAttributes.SCALE,
                            Identifier.of(MOD_ID, "giant_effect"),
                            1.0,
                            EntityAttributeModifier.Operation.ADD_VALUE));

    private static final Identifier PURPLE_HONEY_ID = Identifier.of(MOD_ID, "purple_honey_bottle");

    private static final FoodComponent PURPLE_HONEY_FOOD = new FoodComponent.Builder()
            .nutrition(6)
            .saturationModifier(0.6F)
            .build();

    private static final ConsumableComponent PURPLE_HONEY_CONSUMABLE = ConsumableComponents.drink()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(GIANT_EFFECT, 200, 0)))
            .build();

    public static final Item PURPLE_HONEY_BOTTLE = Registry.register(
            Registries.ITEM,
            PURPLE_HONEY_ID,
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, PURPLE_HONEY_ID))
                    .maxCount(16)
                    .useRemainder(Items.GLASS_BOTTLE)
                    .food(PURPLE_HONEY_FOOD, PURPLE_HONEY_CONSUMABLE)));

    private static final Identifier CHERRY_BEEHIVE_ID = Identifier.of(MOD_ID, "cherry_beehive");

    public static final Block CHERRY_BEEHIVE = Registry.register(
            Registries.BLOCK,
            CHERRY_BEEHIVE_ID,
            new BeehiveBlock(AbstractBlock.Settings.create()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, CHERRY_BEEHIVE_ID))
                    .strength(0.6F)
                    .sounds(BlockSoundGroup.WOOD)));

    public static final Item CHERRY_BEEHIVE_ITEM = Registry.register(
            Registries.ITEM,
            CHERRY_BEEHIVE_ID,
            new BlockItem(CHERRY_BEEHIVE, new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, CHERRY_BEEHIVE_ID))));

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(CHERRY_BEE, BeeEntity.createBeeAttributes());

        ItemGroupEvents.modifyEntriesEvent(SPAWN_EGGS_GROUP).register(entries -> {
            entries.add(new ItemStack(CHERRY_BEE_SPAWN_EGG));
        });

        ItemGroupEvents.modifyEntriesEvent(FOOD_AND_DRINK_GROUP).register(entries -> {
            entries.add(new ItemStack(PURPLE_HONEY_BOTTLE));
        });

        ItemGroupEvents.modifyEntriesEvent(FUNCTIONAL_GROUP).register(entries -> {
            entries.add(new ItemStack(CHERRY_BEEHIVE_ITEM));
        });

        // Cherry groves get cherry bees instead of normal bees; every other biome keeps vanilla bees untouched.
        BiomeModifications.create(Identifier.of(MOD_ID, "cherry_grove_bees"))
                .add(ModificationPhase.REPLACEMENTS,
                        BiomeSelectors.includeByKey(BiomeKeys.CHERRY_GROVE),
                        context -> {
                            context.getSpawnSettings().removeSpawnsOfEntityType(EntityType.BEE);
                            context.getSpawnSettings().addSpawn(
                                    SpawnGroup.CREATURE,
                                    new SpawnSettings.SpawnEntry(CHERRY_BEE, 2, 3),
                                    10);
                        });

        LOGGER.info("Cherry Bees loaded - cherry bees are a separate species with their own pink hive, and pollinate only fallen cherry petals!");
    }
}
