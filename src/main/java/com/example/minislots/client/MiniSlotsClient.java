package com.example.minislots.client;

import com.example.minislots.client.screen.MiniSlotsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class MiniSlotsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("minislots")
                        .executes(context -> {
                            MinecraftClient client = context.getSource().getClient();
                            if (client != null) {
                                client.execute(() -> client.setScreen(new MiniSlotsScreen()));
                                context.getSource().sendFeedback(Text.literal("Opened Mini Slots UI"));
                            }
                            return 1;
                        })));
    }
}
