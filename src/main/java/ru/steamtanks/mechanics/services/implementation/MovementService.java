package ru.steamtanks.mechanics.services.implementation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.steamtanks.mechanics.avatar.PositionPart;
import ru.steamtanks.mechanics.avatar.Square;
import ru.steamtanks.mechanics.game.GameObject;

import java.util.LinkedHashSet;
import java.util.Set;

/**Movement of all game objects occures here. Collisions is comming...
 * Created by Solovyev on 06/11/2016.
 */
@Service
public class MovementService {
    private final Set<GameObject> objectsToMove = new LinkedHashSet<GameObject>();

    public void registerObjectToMove(@NotNull Square gameObject) {
        objectsToMove.add(gameObject);
    }

    public void executeMoves() {
        for (GameObject gameObject : objectsToMove) {
            @Nullable final PositionPart part = gameObject.getPart(PositionPart.class);
            if (part == null) {
                continue;
            }
            part.setBody(part.getLastDesirablePoint());
        }

    }


    public void clear() {
        objectsToMove.clear();
    }
}
