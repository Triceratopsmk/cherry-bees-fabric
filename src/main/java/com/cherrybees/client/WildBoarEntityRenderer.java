package com.cherrybees.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.state.PigEntityRenderState;
import net.minecraft.util.Identifier;

public class WildBoarEntityRenderer extends PigEntityRenderer {
    private static final Identifier BOAR_TEXTURE =
            Identifier.of("cherrybees", "textures/entity/wild_boar/wild_boar.png");

    public WildBoarEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PigEntityRenderState state) {
        return BOAR_TEXTURE;
    }
}
