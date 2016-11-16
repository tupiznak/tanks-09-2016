package ru.steamtanks.mechanics.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.steamtanks.mechanics.avatar.PositionPart;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public abstract class GameObject {
    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    private @NotNull Map<Class<?>, GamePart> parts = new HashMap<>();
    private @NotNull Integer id;

    private final @NotNull Set<String> partNameList = new HashSet<>();

    public GameObject() {
        this.id = Math.toIntExact(ID_GENERATOR.getAndIncrement());
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T extends GamePart> T getPart(@NotNull Class<T> clazz) {
        return (T)parts.get(clazz);
    }

    public @NotNull <T extends GamePart> T claimPart(@NotNull Class<T> clazz) {
        final @Nullable T part = getPart(clazz);
        if (part == null) {
            throw new NullPointerException("Claimed part should not be null");
        }
        return part;
    }

    public <T extends GamePart> void addPart(Class<T> clazz, T gamePart) {
        parts.put(clazz, gamePart);
    }

    @JsonProperty("id")
    public @NotNull Integer getId() {
        return id;
    }

    public @NotNull List<Snap<? extends GamePart>> getPartSnaps() {
        return parts.values().stream()
                .filter(GamePart::shouldBeSnaped)
                .map(GamePart::takeSnap)
                .collect(Collectors.toList());
    }

    public abstract @NotNull Snap<? extends GameObject> getSnap();
}
