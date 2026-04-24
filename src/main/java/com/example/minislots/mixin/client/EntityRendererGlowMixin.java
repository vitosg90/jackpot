package com.example.minislots.mixin.client;

import com.example.minislots.client.state.ModState;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererGlowMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void minislots$forceRenderAndOutline(Entity entity, net.minecraft.client.render.Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (!ModState.isJackpotActive()) return;

        // Client-side entity reference: enable glowing through vanilla flag for outline pass.
        entity.setGlowing(true);
        cir.setReturnValue(true);
    }
}
