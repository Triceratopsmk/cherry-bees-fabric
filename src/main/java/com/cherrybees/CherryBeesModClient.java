package com.cherrybees;

import com.cherrybees.client.CherryBeeEntityRenderer;
import com.cherrybees.client.WildBoarEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CherryBeesModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(CherryBeesMod.CHERRY_BEE, CherryBeeEntityRenderer::new);
        EntityRendererRegistry.register(CherryBeesMod.WILD_BOAR, WildBoarEntityRenderer::new);
    }
}
