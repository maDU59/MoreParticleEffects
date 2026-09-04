package fr.madu59.moreparticleeffects.client.resources.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.madu59.moreparticleeffects.client.emitters.EmitterContext;
import fr.madu59.moreparticleeffects.client.emitters.EmitterDataBuilder;
import fr.madu59.moreparticleeffects.client.emitters.EmitterEvent;
import fr.madu59.moreparticleeffects.client.emitters.EmitterShape;
import fr.madu59.moreparticleeffects.client.registry.EmitterRegistry;
import fr.madu59.moreparticleeffects.client.util.BlockUtil;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class ParticleEmittersLoader extends SimpleJsonResourceReloadListener<JsonElement> {

    public ParticleEmittersLoader() {
        super(ExtraCodecs.JSON, FileToIdConverter.json("moreparticleeffects"));
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {

        EmitterRegistry.clear();

        for (Entry<Identifier, JsonElement> entry : prepared.entrySet()) {

            try{
    
                Identifier fileId = entry.getKey();

                if(!fileId.getPath().equals("particleemitters")){
                    continue;
                }
                
                JsonElement rootElement = entry.getValue();

                if (!rootElement.isJsonObject()) {
                    continue;
                }

                processParticleEmittersJSON(fileId, rootElement.getAsJsonObject());

            } catch (Exception e) {
                System.err.println("Error while loading particle emitters" + entry.getKey() + ": " + e.getMessage());
            }
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

        EmitterDataBuilder emitterDataBuilder = new EmitterDataBuilder(emitterId);

        if(emitterConfig.has("shape")) {
            parseShape(emitterConfig.get("shape").getAsJsonObject(), emitterDataBuilder);
        }

        for(Entry<String, JsonElement> conditionEntry : emitterConfig.getAsJsonObject("conditions").entrySet()) {
            EmitterEvent event = EmitterEvent.fromString(conditionEntry.getKey());

            if(event == null) {
                System.err.println("Invalid event type: " + conditionEntry.getKey() + " in emitter definition: " + emitterId);
                continue;
            }

            parseCondition(emitterId, event, conditionEntry.getValue().getAsJsonObject(), emitterDataBuilder);

            EmitterRegistry.registerEmitter(event, emitterDataBuilder.build());
        }
    }

    private EmitterDataBuilder parseCondition(Identifier emitterId, EmitterEvent event, JsonObject conditionConfig, EmitterDataBuilder emitterDataBuilder) {
        Predicate<EmitterContext> conditionPredicate = null;

        if(event == EmitterEvent.ON_TILL || event == EmitterEvent.ON_FLATTEN || event == EmitterEvent.ON_STRIP || event == EmitterEvent.ON_ANIMATE_TICK || event == EmitterEvent.ON_OPEN || event == EmitterEvent.ON_CLOSE) {
            if(conditionConfig.has("blocks")) {
                conditionPredicate = (ctx) -> {
                    return BlockUtil.isValidBlock(conditionConfig.get("blocks").getAsJsonArray(), ctx.getOldBlock());
                };
            }
            else if(conditionConfig.has("block")) {
                conditionPredicate = (ctx) -> {
                    return BlockUtil.isValidBlock(conditionConfig.get("block").getAsString(), ctx.getOldBlock());
                };
            }
        }
        if(conditionPredicate != null) {
            emitterDataBuilder.setPredicate(conditionPredicate);
        }

        if(conditionConfig.has("probability")) {
            emitterDataBuilder.setProbability(conditionConfig.get("probability").getAsFloat());
        }
        else{
            emitterDataBuilder.resetProbability();
        }

        return emitterDataBuilder;
    }

    private EmitterDataBuilder parseShape(JsonObject shapeElement, EmitterDataBuilder emitterDataBuilder) {
        if(shapeElement.has("type")) {
            String shapeType = shapeElement.get("type").getAsString();
            emitterDataBuilder.setShape(EmitterShape.fromString(shapeType));
        }
        if(shapeElement.has("size")) {
            if(shapeElement.get("size").isJsonArray()) {
                emitterDataBuilder.setSize(EmitterShape.getSizeFromIterable(shapeElement.get("size").getAsJsonArray()));
            }
            else if(shapeElement.get("size").isJsonPrimitive()) {
                emitterDataBuilder.setSize(shapeElement.get("size").getAsFloat());
            }
        }
        return emitterDataBuilder;
    }
}