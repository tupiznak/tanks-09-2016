package ru.steamtanks.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.mechanics.base.ServerPlayerSnap;
import ru.steamtanks.models.UserProfile;

public class UserGameProfile {
    private final @NotNull UserProfile userProfile;
    private final Square square;

    public UserGameProfile(@NotNull UserProfile userProfile) {
        this.userProfile = userProfile;
        square = new Square();
    }

    public @NotNull UserProfile getUserProfile() {
        return userProfile;
    }

    public Square getSquare() {
        return square;
    }

    public @NotNull Integer getId() {
        return userProfile.getId();
    }

    public @NotNull ServerPlayerSnap generateSnap() {

        final ServerPlayerSnap result = new ServerPlayerSnap();
        result.setUserId(getId());
        result.setPlayerSquare(square.getSnap());
        return result;
    }
}
