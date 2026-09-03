package fr.madu59.moreparticleeffects.client.registry;

import java.util.HashMap;
import java.util.Map;

import fr.madu59.moreparticleeffects.client.emitters.EmitterData;
import fr.madu59.moreparticleeffects.client.emitters.EmitterEvent;
import net.minecraft.resources.Identifier;

public class EmitterRegistry {
    private static Map<EmitterEvent, Map<Identifier, EmitterData>> emitters = new HashMap<>();

    public static void registerEmitter(EmitterEvent event, EmitterData data) {
        if (!emitters.containsKey(event)) {
            emitters.put(event, new HashMap<>());
        }
        emitters.get(event).put(data.getId(), data);
    }

    public static Map<Identifier, EmitterData> getEmitters(EmitterEvent event) {
        return emitters.getOrDefault(event, new HashMap<>());
    }
}