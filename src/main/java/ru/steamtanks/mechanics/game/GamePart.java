package ru.steamtanks.mechanics.game;

public interface GamePart {

    boolean shouldBeSnaped();

    Snap<? extends GamePart> takeSnap();
}
