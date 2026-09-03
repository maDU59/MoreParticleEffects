package fr.madu59.moreparticleeffects.client.resources.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.madu59.moreparticleeffects.client.emitters.EmitterContext;
import fr.madu59.moreparticleeffects.client.emitters.EmitterDataBuilder;
import fr.madu59.moreparticleeffects.client.emitters.EmitterEvent;
import fr.madu59.moreparticleeffects.client.registry.EmitterRegistry;
import fr.madu59.moreparticleeffects.client.util.BlockUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

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

        for(Entry<String, JsonElement> conditionEntry : emitterConfig.getAsJsonObject("conditions").entrySet()) {
            EmitterEvent event = EmitterEvent.fromString(conditionEntry.getKey());

            Predicate<EmitterContext> conditionPredicate = getConditionPredicate(event, conditionEntry.getValue().getAsJsonObject());

            if(event != null) {
                emitterDataBuilder.setPredicate(conditionPredicate);
                EmitterRegistry.registerEmitter(event, emitterDataBuilder.build());
            }
        }
    }

    public Predicate<EmitterContext> getConditionPredicate(EmitterEvent event, JsonObject conditionConfig) {
        if(event == EmitterEvent.ON_TILL || event == EmitterEvent.ON_FLATTEN || event == EmitterEvent.ON_STRIP) {
            if(conditionConfig.has("blocks")) {
                return (ctx) -> {
                    return BlockUtil.isValidBlock(conditionConfig.get("blocks").getAsJsonArray(), ctx.getOldBlock());
                };
            }
            else if(conditionConfig.has("block")) {
                return (ctx) -> {
                    return BlockUtil.isValidBlock(conditionConfig.get("block").getAsString(), ctx.getOldBlock());
                };
            }
        }
        return (ctx) -> true;
    }
}