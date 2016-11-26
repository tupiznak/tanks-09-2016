package ru.steamtanks.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.mechanics.base.ClientSnap;

public interface AbstractGameMechanics {

    void addClientSnapshot(@NotNull Integer userId, @NotNull ClientSnap clientSnap);

    void gmStep(long frameTime);

    void reset();
}
