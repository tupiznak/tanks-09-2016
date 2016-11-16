package ru.steamtanks.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.mechanics.game.GameObject;
import ru.steamtanks.mechanics.game.GamePart;
import ru.steamtanks.mechanics.game.Snap;

import java.util.List;

public class Square extends GameObject {

    public Square() {
        addPart(PositionPart.class, new PositionPart());
        addPart(MousePart.class, new MousePart());
    }

    @Override
    public Snap<? extends GameObject> getSnap() {
        return new SquareSnap(this);
    }

    public static final class SquareSnap implements Snap<Square> {

        private final @NotNull List<Snap<? extends GamePart>> partSnaps;

        private final @NotNull Integer id;

        public SquareSnap(@NotNull Square square) {
            this.partSnaps = square.getPartSnaps();
            this.id = square.getId();
        }

        public @NotNull Integer getId() {
            return id;
        }

        @Override
        public @NotNull String getPartName() {
            return Square.class.getSimpleName();
        }

        public @NotNull List<Snap<? extends GamePart>> getPartSnaps() {
            return partSnaps;
        }
    }


}
