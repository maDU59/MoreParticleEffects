package fr.madu59.moreparticleeffects.client.resources.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.madu59.moreparticleeffects.client.emitters.EmitterData;
import fr.madu59.moreparticleeffects.client.emitters.EmitterEvent;
import fr.madu59.moreparticleeffects.client.registry.EmitterRegistry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.Map.Entry;

public class ParticleEmittersLoader extends SimpleJsonResourceReloadListener<JsonElement> {

    public ParticleEmittersLoader() {
        super(ExtraCodecs.JSON, FileToIdConverter.json("moreparticleeffects"));
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {
        EmitterRegistry.clear();

        for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
      
            Identifier fileId = entry.getKey();

            if(!fileId.getPath().equals("particleemitters")){
                continue;
            }
            
            JsonElement rootElement = entry.getValue();

            if (!rootElement.isJsonObject()) {
                continue;
            }

            processParticleEmittersJSON(fileId, rootElement.getAsJsonObject());
        }
    }

    private void processParticleEmittersJSON(Identifier id, JsonObject rootNode) {
        for(Map.Entry<String, JsonElement> entry : rootNode.entrySet()) {
            String emitterName = entry.getKey();
            JsonObject emitterConfig = entry.getValue().getAsJsonObject();

            processParticleSpawnerDefinition(id, emitterName, emitterConfig);
        }
    }

    private void processParticleSpawnerDefinition(Identifier id, String emitterName, JsonObject emitterConfig) {
        
        Identifier emitterId = Identifier.tryBuild(id.getNamespace(), emitterName);

        if (emitterId == null) {
            System.err.println("Invalid emitter name: " + emitterName);
            return;
        }
        
        if(!emitterConfig.has("particle")) {
            System.err.println("Missing required fields 'particle' in emitter definition: " + emitterId);
            return;
        }

        if(!emitterConfig.has("conditions")) {
            System.err.println("Missing required fields 'conditions' in emitter definition: " + emitterId);
            return;
        }

        for(Entry<String, JsonElement> conditionEntry : emitterConfig.getAsJsonObject("conditions").entrySet()) {
            EmitterData emitterData = new EmitterData(emitterId, (ctx) -> true);
            EmitterEvent event = EmitterEvent.fromString(conditionEntry.getKey());

            if(event != null) {
                EmitterRegistry.registerEmitter(event, emitterData);
            }
        }
    }
}