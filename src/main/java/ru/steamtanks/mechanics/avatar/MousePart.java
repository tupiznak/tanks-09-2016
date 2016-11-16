package ru.steamtanks.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.mechanics.base.Coords;
import ru.steamtanks.mechanics.game.GamePart;
import ru.steamtanks.mechanics.game.Snap;

public class MousePart implements GamePart {
    private @NotNull Coords mouse;


    public MousePart() {
        this.mouse = new Coords(0.0f, 0.0f);
    }

    @Override
    public boolean shouldBeSnaped() {
        return true;
    }

    public @NotNull Coords getMouse() {
        return mouse;
    }

    public void setMouse(@NotNull Coords mouse) {
        this.mouse = mouse;
    }

    @Override
    public MouseSnap takeSnap() {
        return new MouseSnap(this);
    }

    public static final class MouseSnap implements Snap<MousePart> {

        private final Coords mouse;

        public MouseSnap(MousePart mouse) {
            this.mouse = mouse.getMouse();
        }

        @Override
        public @NotNull String getPartName() {
            return MousePart.class.getSimpleName();
        }

        public Coords getMouse() {
            return mouse;
        }
    }
}
