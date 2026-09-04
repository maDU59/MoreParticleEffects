package fr.madu59.moreparticleeffects.client.mixin.block.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import fr.madu59.moreparticleeffects.client.interfaces.BlockInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity.AnimationStatus;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin {
    @Inject(method = "updateAnimation", at = @At("RETURN"))
    public void mpe$updateAnimationEnd(final Level level, final BlockPos pos, final BlockState state, CallbackInfo ci, @Share("status") LocalRef<AnimationStatus> animationStatusRef){
        ShulkerBoxBlockEntity entity = (ShulkerBoxBlockEntity) (Object) this;
        if(entity.getProgress(1) > 0){
            if(entity.getProgress(0) == 0){
                ((BlockInterface)state.getBlock()).onOpen(level, state.getBlock(), pos);
            }
        }
        else{
            if(entity.getProgress(0) > 0){
                if(entity.getProgress(1) == 0){
                    ((BlockInterface)state.getBlock()).onClose(level, state.getBlock(), pos);
                }
            }
        }
    }
}
