package ru.steamtanks.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.steamtanks.mechanics.base.Coords;
import ru.steamtanks.mechanics.base.Direction;
import ru.steamtanks.mechanics.base.Way;
import ru.steamtanks.mechanics.game.GamePart;
import ru.steamtanks.mechanics.game.Snap;

import java.util.ArrayList;
import java.util.List;

public class PositionPart implements GamePart {
    private @NotNull Coords body;

    private @NotNull Direction movingTo;

    private final @NotNull List<Coords> desirablePath = new ArrayList<>();

    public PositionPart() {
        body = new Coords(0.0f, 0.0f);

        movingTo = Way.None.getRadial();
    }

    public void executeMovement() {
        if(!desirablePath.isEmpty()) {
            body = desirablePath.get(desirablePath.size() - 1);
        }
        desirablePath.clear();
    }

    public @NotNull List<Coords> getDesirablePath() {
        return desirablePath;
    }

    public @NotNull Coords getLastDesirablePoint() {
        if (desirablePath.isEmpty()) {
            return body;
        }
        return desirablePath.get(desirablePath.size() - 1);
    }

    public void addDesirableCoords(@Nullable Coords desirableCoords) {
        desirablePath.add(desirableCoords);
    }

    public @NotNull Coords getBody() {
        return body;
    }

    public void setBody(@NotNull Coords body) {
        this.body = body;
    }

    public @NotNull Direction getMovingTo() {
        return movingTo;
    }

    public void setMovingTo(@NotNull Direction movingTo) {
        this.movingTo = movingTo;
    }

    @Override
    public boolean shouldBeSnaped() {
        return true;
    }

    @Override
    public PositionSnap takeSnap() {
        return new PositionSnap(this);
    }

    public static final class PositionSnap implements Snap<PositionPart> {
        private final @NotNull Coords body;
        private final @NotNull Direction movingTo;

        public PositionSnap(@NotNull PositionPart positionPart) {
            body = positionPart.body;
            movingTo = positionPart.movingTo;
        }

        @Override
        public @NotNull String getPartName() {
            return PositionPart.class.getSimpleName();
        }

        public @NotNull Coords getBody() {
            return body;
        }
        public @NotNull Direction getMovingTo() {
            return movingTo;
        }
    }
}
