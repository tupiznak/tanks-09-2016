package ru.steamtanks.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class ClientSnap {


    private @NotNull Way direction;
    private @NotNull Coords mouse;
    private boolean isFiring;
    private long frameTime;

    public @NotNull Way getDirection() {
        return direction;
    }

    public @NotNull Coords getMouse() {
        return mouse;
    }

    @JsonProperty("isFiring")
    public boolean isFiring() {
        return isFiring;
    }

    public long getFrameTime() {
        return frameTime;
    }

    public void setDirection(@NotNull Way direction) {
        this.direction = direction;
    }

    public void setMouse(@NotNull Coords mouse) {
        this.mouse = mouse;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }
}
