package fr.madu59.moreparticleeffects.client;

import fr.madu59.moreparticleeffects.client.resources.loader.ParticleEmittersLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public class MoreParticleEffectsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(Identifier.tryParse("moreparticleeffects:particleemitters"), new ParticleEmittersLoader());
	}
}