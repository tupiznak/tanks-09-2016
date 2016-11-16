package ru.steamtanks.mechanics.services.implementation;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.steamtanks.mechanics.Config;
import ru.steamtanks.mechanics.GameSession;
import ru.steamtanks.mechanics.avatar.MousePart;
import ru.steamtanks.mechanics.avatar.PositionPart;
import ru.steamtanks.mechanics.avatar.Square;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.mechanics.base.ClientSnap;
import ru.steamtanks.mechanics.base.Coords;
import ru.steamtanks.mechanics.base.Way;

import java.util.*;

import static ru.steamtanks.mechanics.base.Way.Left;

@Service
public class ClientSnapshotsService {

    private final Map<Integer, List<ClientSnap>> snaps = new HashMap<>();

    private final @NotNull MovementService movementService;

    public ClientSnapshotsService(@NotNull MovementService movementService) {
        this.movementService = movementService;
    }

    public void pushClientSnap(@NotNull Integer user, @NotNull ClientSnap snap) {
        this.snaps.putIfAbsent(user, new ArrayList<>());
        final List<ClientSnap> clientSnaps = snaps.get(user);
        clientSnaps.add(snap);
    }

    public @NotNull List<ClientSnap> getSnapForUser(@NotNull Integer user) {
        return snaps.getOrDefault(user, Collections.emptyList());
    }

    public void processSnapshotsFor(@NotNull GameSession gameSession) {
        final Collection<UserGameProfile> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (UserGameProfile player : players) {
            final List<ClientSnap> playerSnaps = getSnapForUser(player.getId());
            if (playerSnaps.isEmpty()) {
                continue;
            }
            for (ClientSnap snap : playerSnaps) {
                processMovement(player, snap.getDirection(), snap.getFrameTime());
            }
            final ClientSnap lastSnap = playerSnaps.get(playerSnaps.size() - 1);
            processMouseMove(player, lastSnap.getMouse());

            //TODO:Firing
        }
    }

    private void processMovement(UserGameProfile gameUser, @NotNull Way way, long frameTime) {
        final PositionPart positionPart = gameUser.getSquare().claimPart(PositionPart.class);
        final Coords body = positionPart.getBody();
        switch (way) {
            case Left: {
                moveSquareBy(gameUser.getSquare(), - Config.SQUARE_SPEED * frameTime, 0);
                break;
            }
            case Right: {
                moveSquareBy(gameUser.getSquare(), Config.SQUARE_SPEED * frameTime, 0);
                break;
            }
            case Up: {
                moveSquareBy(gameUser.getSquare(), 0, - Config.SQUARE_SPEED * frameTime);
                break;
            }
            case Down: {
                moveSquareBy(gameUser.getSquare(), 0, Config.SQUARE_SPEED * frameTime);
                break;
            }
            case None: {
                break;
            }
        }
    }

    private void moveSquareBy(@NotNull Square square, double dx, double dy) {
        final PositionPart positionPart = square.claimPart(PositionPart.class);
        final Coords lastDesirablePoint = positionPart.getLastDesirablePoint();
        final double newX = Math.min(Config.PLAYGROUND_WIDTH - Config.SQUARE_SIZE, lastDesirablePoint.x + dx);
        final double newY = Math.min(Config.PLAYGROUND_HEIGHT - Config.SQUARE_SIZE, lastDesirablePoint.y + dy);
        positionPart.addDesirableCoords(new Coords(newX, newY));
        movementService.registerObjectToMove(square);
    }

    private void processMouseMove(UserGameProfile gameUser, @NotNull Coords mouse) {
        gameUser.getSquare().claimPart(MousePart.class).setMouse(mouse);
    }


    public void clear() {
        snaps.clear();
    }

}
