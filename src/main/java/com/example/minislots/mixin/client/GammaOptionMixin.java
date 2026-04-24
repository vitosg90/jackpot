package com.example.minislots.mixin.client;

import com.example.minislots.client.state.ModState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleOption.class)
public class GammaOptionMixin {

    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    private void minislots$forceGammaOnJackpot(CallbackInfoReturnable<Object> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.options == null) return;

        SimpleOption<?> gamma = client.options.getGamma();
        if ((Object) this != gamma) return;

        if (ModState.isJackpotActive()) {
            cir.setReturnValue(10.0D);
        }
    }
}
