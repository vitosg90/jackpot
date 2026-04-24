package com.example.minislots.client.screen;

import com.example.minislots.client.state.ModState;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Random;

public class MiniSlotsScreen extends BaseUIModelScreen<FlowLayout> {

    private static final List<String> SYMBOLS = List.of("Diamond", "Gold", "Emerald", "Coal");
    private final Random random = new Random();

    private LabelComponent coinLabel;
    private LabelComponent slot1;
    private LabelComponent slot2;
    private LabelComponent slot3;
    private LabelComponent slotsStatus;

    private FlowLayout miniPlayArea;
    private ButtonComponent clickButton;

    private boolean miniTabActive = true;

    public MiniSlotsScreen() {
        super(FlowLayout.class, DataSource.asset(Identifier.of("minislots", "layout")));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        ButtonComponent tabMini = rootComponent.childById(ButtonComponent.class, "tab-mini");
        ButtonComponent tabSlots = rootComponent.childById(ButtonComponent.class, "tab-slots");

        this.coinLabel = rootComponent.childById(LabelComponent.class, "coin-label");
        this.slot1 = rootComponent.childById(LabelComponent.class, "slot-1");
        this.slot2 = rootComponent.childById(LabelComponent.class, "slot-2");
        this.slot3 = rootComponent.childById(LabelComponent.class, "slot-3");
        this.slotsStatus = rootComponent.childById(LabelComponent.class, "slots-status");

        this.miniPlayArea = rootComponent.childById(FlowLayout.class, "mini-play-area");
        this.clickButton = rootComponent.childById(ButtonComponent.class, "click-me");
        ButtonComponent spinButton = rootComponent.childById(ButtonComponent.class, "spin-button");

        miniPlayArea.sizing(Sizing.fixed(180), Sizing.fixed(100));
        miniPlayArea.padding(Insets.of(6));

        tabMini.onPress(button -> {
            miniTabActive = true;
            slotsStatus.text(Text.literal("Mini-game tab active"));
        });

        tabSlots.onPress(button -> {
            miniTabActive = false;
            slotsStatus.text(Text.literal("Slots tab active"));
        });

        clickButton.onPress(button -> {
            if (!miniTabActive) return;
            ModState.addCoin(1);
            updateCoinLabel();
            moveClickButtonRandomly();
        });

        spinButton.onPress(button -> {
            if (miniTabActive) return;
            spinSlots();
        });

        updateCoinLabel();
        slotsStatus.text(Text.literal("Mini-game tab active"));
    }

    private void updateCoinLabel() {
        coinLabel.text(Text.literal("Coins: " + ModState.getCoins()));
    }

    private void moveClickButtonRandomly() {
        int maxX = Math.max(0, miniPlayArea.width() - clickButton.width() - 12);
        int maxY = Math.max(0, miniPlayArea.height() - clickButton.height() - 12);

        int x = random.nextInt(maxX + 1);
        int y = random.nextInt(maxY + 1);
        clickButton.positioning(Positioning.absolute(x + 6, y + 6));
    }

    private void spinSlots() {
        if (!ModState.spendCoin()) {
            slotsStatus.text(Text.literal("Not enough coins"));
            return;
        }

        updateCoinLabel();

        String s1 = SYMBOLS.get(random.nextInt(SYMBOLS.size()));
        String s2 = SYMBOLS.get(random.nextInt(SYMBOLS.size()));
        String s3 = SYMBOLS.get(random.nextInt(SYMBOLS.size()));

        slot1.text(Text.literal(s1));
        slot2.text(Text.literal(s2));
        slot3.text(Text.literal(s3));

        boolean jackpot = "Diamond".equals(s1) && "Diamond".equals(s2) && "Diamond".equals(s3);
        ModState.setJackpotActive(jackpot);

        slotsStatus.text(jackpot
                ? Text.literal("JACKPOT! Fullbright + ESP enabled")
                : Text.literal("Try again"));
    }
}
