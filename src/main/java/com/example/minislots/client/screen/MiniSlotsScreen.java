package com.example.minislots.client.screen;

import com.example.minislots.client.state.ModState;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIModel;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Random;

public class MiniSlotsScreen extends BaseOwoScreen<FlowLayout> {
    private static final List<String> SYMBOLS = List.of("Diamond", "Gold", "Emerald", "Coal");

    private final Random random = new Random();

    private LabelComponent coinLabel;
    private LabelComponent slot1;
    private LabelComponent slot2;
    private LabelComponent slot3;
    private LabelComponent slotsStatus;

    private FlowLayout miniPanel;
    private FlowLayout slotsPanel;
    private FlowLayout miniPlayArea;
    private ButtonComponent clickButton;

    @Override
    protected void build(FlowLayout rootComponent) {
        UIModel model = UIModel.load(Identifier.of("minislots", "owo/layout"));
        FlowLayout modelRoot = model.childById(FlowLayout.class, "root");
        rootComponent.child(modelRoot);

        ButtonComponent tabMini = model.childById(ButtonComponent.class, "tab-mini");
        ButtonComponent tabSlots = model.childById(ButtonComponent.class, "tab-slots");

        this.coinLabel = model.childById(LabelComponent.class, "coin-label");
        this.slot1 = model.childById(LabelComponent.class, "slot-1");
        this.slot2 = model.childById(LabelComponent.class, "slot-2");
        this.slot3 = model.childById(LabelComponent.class, "slot-3");
        this.slotsStatus = model.childById(LabelComponent.class, "slots-status");

        this.miniPanel = model.childById(FlowLayout.class, "mini-panel");
        this.slotsPanel = model.childById(FlowLayout.class, "slots-panel");
        this.miniPlayArea = model.childById(FlowLayout.class, "mini-play-area");
        this.clickButton = model.childById(ButtonComponent.class, "click-me");
        ButtonComponent spinButton = model.childById(ButtonComponent.class, "spin-button");

        miniPlayArea.sizing(Sizing.fixed(180), Sizing.fixed(100));
        miniPlayArea.padding(Insets.of(6));

        tabMini.onPress(button -> switchTab(true));
        tabSlots.onPress(button -> switchTab(false));

        clickButton.onPress(button -> {
            ModState.addCoin(1);
            updateCoinLabel();
            moveClickButtonRandomly();
        });

        spinButton.onPress(button -> spinSlots());

        switchTab(true);
        updateCoinLabel();
    }

    private void switchTab(boolean miniGameActive) {
        miniPanel.visible(miniGameActive);
        miniPanel.active(miniGameActive);
        slotsPanel.visible(!miniGameActive);
        slotsPanel.active(!miniGameActive);
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
