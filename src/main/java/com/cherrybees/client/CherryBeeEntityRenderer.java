package com.cherrybees.client;

import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.BeeEntityRenderState;
import net.minecraft.util.Identifier;

public class CherryBeeEntityRenderer extends BeeEntityRenderer {
    private static final Identifier CHERRY_TEXTURE =
            Identifier.of("cherrybees", "textures/entity/cherry_bee/cherry_bee.png");
    private static final Identifier CHERRY_ANGRY_TEXTURE =
            Identifier.of("cherrybees", "textures/entity/cherry_bee/cherry_bee_angry.png");
    private static final Identifier NECTAR_TEXTURE =
            Identifier.of("minecraft", "textures/entity/bee/bee_nectar.png");
    private static final Identifier ANGRY_NECTAR_TEXTURE =
            Identifier.of("minecraft", "textures/entity/bee/bee_angry_nectar.png");

    public CherryBeeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BeeEntityRenderState state) {
        if (state.hasNectar) {
            return state.angry ? ANGRY_NECTAR_TEXTURE : NECTAR_TEXTURE;
        }
        return state.angry ? CHERRY_ANGRY_TEXTURE : CHERRY_TEXTURE;
    }
}

