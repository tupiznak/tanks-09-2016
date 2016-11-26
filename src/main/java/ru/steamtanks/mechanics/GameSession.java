package ru.steamtanks.mechanics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.models.UserProfile;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;


public class GameSession implements AbstractGameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final int maxCountOfUsers;
    private final @NotNull Integer sessionId;
    private Set<@NotNull UserGameProfile> userGameProfileSet;

    public GameSession(int maxCountOfUsers) {
        this.sessionId = Math.toIntExact(ID_GENERATOR.getAndIncrement());
        this.maxCountOfUsers = maxCountOfUsers;
        this.userGameProfileSet = new HashSet<>();
    }

    @Override
    public @NotNull Set<UserGameProfile> getEnemy(@NotNull UserGameProfile user) {
        Set<@NotNull UserGameProfile> userGameProfileSetResponse = userGameProfileSet;
        userGameProfileSetResponse.remove(user);
        return userGameProfileSetResponse;
    }

    @Override
    public @Nullable UserGameProfile getSelf(@NotNull Integer userId) {
        for (UserGameProfile userGameProfile : userGameProfileSet){
            if (userGameProfile.getId().equals(userId)){
                return userGameProfile;
            }
        }
        return null;
    }

    @Override
    public void addUser(UserProfile userProfile){
        UserGameProfile userGameProfile = new UserGameProfile(userProfile);
        userGameProfileSet.add(userGameProfile);
    }

    @Override
    public void delUser(@Nullable UserGameProfile userGameProfile){
        userGameProfileSet.remove(userGameProfile);
    }

        @Override
    public Integer getMaxCountOfUsers(){
        return maxCountOfUsers;
    }

    @Override
    public Integer getNowCountOfUsers(){
        return userGameProfileSet.size();
    }

    @Override
    public Integer getIdGameSession(){
        return ID_GENERATOR.intValue();
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSession that = (GameSession) o;

        return sessionId.equals(that.sessionId);

    }
*/

/*
    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
*/
}
