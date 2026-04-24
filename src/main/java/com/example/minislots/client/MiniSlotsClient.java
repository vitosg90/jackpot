package com.example.minislots.client;

import com.example.minislots.client.screen.MiniSlotsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class MiniSlotsClient implements ClientModInitializer {

    private static boolean openScreenNextTick = false;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("minislots")
                        .executes(context -> {
                            openScreenNextTick = true;
                            context.getSource().sendFeedback(Text.literal("Opened Mini Slots UI"));
                            return 1;
                        })));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!openScreenNextTick) return;

            openScreenNextTick = false;
            if (client.player == null) return;

            client.setScreen(new MiniSlotsScreen());
        });
    }
}
