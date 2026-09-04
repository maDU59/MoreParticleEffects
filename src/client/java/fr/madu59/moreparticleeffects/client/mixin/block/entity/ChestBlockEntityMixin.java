package fr.madu59.moreparticleeffects.client.mixin.block.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.madu59.moreparticleeffects.client.interfaces.BlockInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin {
    @Inject(method = "lidAnimateTick", at = @At("RETURN"))
    private static void mpe$lidAnimateTick(final Level level, final BlockPos pos, final BlockState state, final ChestBlockEntity entity, CallbackInfo ci) {
        if(entity.getOpenNess(1) > 0){
            if(entity.getOpenNess(0) == 0){
                ((BlockInterface)state.getBlock()).onOpen(level, state.getBlock(), pos);
            }
        }
        else{
            if(entity.getOpenNess(0) > 0){
                if(entity.getOpenNess(1) == 0){
                    ((BlockInterface)state.getBlock()).onClose(level, state.getBlock(), pos);
                }
            }
        }
    }
}
