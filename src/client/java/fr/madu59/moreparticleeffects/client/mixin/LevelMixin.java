package fr.madu59.moreparticleeffects.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.madu59.moreparticleeffects.client.interfaces.BlockInterface;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block.UpdateFlags;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ClientLevel.class)
public class LevelMixin {
    @Inject(method ="sendBlockUpdated", at = @At("HEAD"))
    private void mpe$setBlock(final BlockPos pos, final BlockState old, final BlockState current, final @UpdateFlags int updateFlags, CallbackInfo ci) {
        if(((Level)(Object)this).isClientSide()) {
            ((BlockInterface)old.getBlock()).onStateChange((Level)(Object)this, old, current, pos);
        }
    }
}
