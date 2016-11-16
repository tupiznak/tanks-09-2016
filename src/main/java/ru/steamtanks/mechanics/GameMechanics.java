package ru.steamtanks.mechanics;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.steamtanks.exceptions.RemotePointService.RPSCloseSocketSessionException;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.mechanics.base.ClientSnap;
import ru.steamtanks.mechanics.services.implementation.*;
import ru.steamtanks.models.UserProfile;
import ru.steamtanks.services.implementation.AccountService;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameMechanics implements AbstractGameMechanics {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameMechanics.class);

    private @NotNull AccountService accountService;

    private @NotNull ClientSnapshotsService clientSnapshotsService;

    private @NotNull ServerSnapshotService serverSnapshotService;

    private @NotNull RemotePointService remotePointService;

    private @NotNull MovementService movementService;

    private @NotNull GameSessionService gameSessionService;

    private @NotNull Set<Integer> playingUsers = new HashSet<>();

    private ConcurrentLinkedQueue<@NotNull Integer> waiters = new ConcurrentLinkedQueue<>();

    private final @NotNull Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    @SuppressWarnings("LongLine")
    public GameMechanics(@NotNull AccountService accountService,
                         @NotNull ServerSnapshotService serverSnapshotService,
                         @NotNull RemotePointService remotePointService,
                         @NotNull MovementService movementService,
                         @NotNull GameSessionService gameSessionService) {
        this.accountService = accountService;
        this.serverSnapshotService = serverSnapshotService;
        this.remotePointService = remotePointService;
        this.movementService = movementService;
        this.gameSessionService = gameSessionService;
        this.clientSnapshotsService = new ClientSnapshotsService(movementService);
    }

    @Override
    public void addUser(@NotNull Integer userId) {
        if (gameSessionService.isPlaying(userId)) {
            return;
        }
        waiters.add(userId);
    }

    private void tryStartGames() {
        final Set<UserProfile> matchedPlayers = new LinkedHashSet<>();

        while (waiters.size() >= 2 || waiters.size() >= 1 && matchedPlayers.size() >= 1) {
            final Integer candidate = waiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(accountService.getUser(candidate));
            if(matchedPlayers.size() == 2) {
                final Iterator<UserProfile> iterator = matchedPlayers.iterator();
                gameSessionService.startGame(iterator.next(), iterator.next());
                matchedPlayers.clear();
            }
        }
        matchedPlayers.stream().map(UserProfile::getId).forEach(waiters::add);

    }

    private boolean insureCandidate(@NotNull Integer candidate) {
        return remotePointService.isConnected(candidate) &&
                accountService.getUser(candidate) != null;
    }

    @Override
    public void gmStep(long frameTime) {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch (RuntimeException ex) {
                    LOGGER.error("Cant handle game task", ex);
                }
            }
        }

        for (GameSession session : gameSessionService.getSessions()) {
            clientSnapshotsService.processSnapshotsFor(session);
        }

        movementService.executeMoves();

        final Iterator<GameSession> iterator = gameSessionService.getSessions().iterator();
        final List<GameSession> sessionsToTerminate = new ArrayList<>();
        while (iterator.hasNext()) {
            final GameSession session = iterator.next();
            try {
                serverSnapshotService.sendSnapshotsFor(session, frameTime);
            } catch (RuntimeException ex) {
                LOGGER.error("Failed send snapshots, terminating the session", ex);
                sessionsToTerminate.add(session);
            }
        }
        sessionsToTerminate.forEach(elem -> {
            try {
                gameSessionService.notifyGameIsOver(elem);
            } catch (RPSCloseSocketSessionException e) {
                e.printStackTrace();
            }
        });

        tryStartGames();
        clientSnapshotsService.clear();
        movementService.clear();
    }


    @Override
    public void addClientSnapshot(@NotNull Integer userId, @NotNull ClientSnap clientSnap) {
        tasks.add(() -> clientSnapshotsService.pushClientSnap(userId, clientSnap));
    }

    @Override
    public void reset() {

    }
}
