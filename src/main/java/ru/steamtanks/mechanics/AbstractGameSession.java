package ru.steamtanks.mechanics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.models.UserProfile;

import java.util.Set;

public interface AbstractGameSession {
    @NotNull Set<UserGameProfile> getEnemy(@NotNull UserGameProfile user);

    @Nullable UserGameProfile getSelf(@NotNull Integer userId);

    void addUser(UserProfile userProfile);

    void delUser(UserGameProfile userGameProfile);

    Integer getMaxCountOfUsers();

    Integer getNowCountOfUsers();

    Integer getIdGameSession();

}