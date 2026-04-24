package com.example.minislots.client.state;

public final class ModState {
    private static int coins = 0;
    private static boolean jackpotActive = false;

    private ModState() {
    }

    public static int getCoins() {
        return coins;
    }

    public static void addCoin(int amount) {
        coins += amount;
        if (coins < 0) coins = 0;
    }

    public static boolean spendCoin() {
        if (coins <= 0) return false;
        coins--;
        return true;
    }

    public static boolean isJackpotActive() {
        return jackpotActive;
    }

    public static void setJackpotActive(boolean jackpotActive) {
        ModState.jackpotActive = jackpotActive;
    }
}
