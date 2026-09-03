package fr.madu59.moreparticleeffects.client.resources.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class ParticleEmittersLoader extends SimpleJsonResourceReloadListener<JsonElement> {

    public ParticleEmittersLoader() {
        super(ExtraCodecs.JSON, FileToIdConverter.json("moreparticleeffects"));
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {
        // clear()

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
            JsonElement emitterConfig = entry.getValue();

            processParticleSpawnerDefinition(id, emitterName, emitterConfig);
        }
    }

    private void processParticleSpawnerDefinition(Identifier id, String emitterName, JsonElement emitterConfig) {
        Identifier emitterId = Identifier.tryBuild(id.getNamespace(), emitterName);
    }
}