package fr.madu59.moreparticleeffects.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.madu59.moreparticleeffects.client.emitters.EmitterContext;
import fr.madu59.moreparticleeffects.client.emitters.EmitterEvent;
import fr.madu59.moreparticleeffects.client.interfaces.BlockInterface;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.HoeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(Block.class)
public class BlockMixin implements BlockInterface {
    @Override
    public void onBreak(LevelAccessor level, BlockState state, BlockPos pos) {
        EmitterEvent.ON_BREAK.call(EmitterContext.onBreak(level, state, pos));
    }

    @Override
    public void onPlace(LevelAccessor level, BlockState state, BlockPos pos) {
        EmitterEvent.ON_PLACE.call(EmitterContext.onPlace(level, state, pos));
    }

    @Override
    public void onFlatten(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos) {
        EmitterEvent.ON_FLATTEN.call(EmitterContext.onFlatten(level, oldBlock, newBlock, pos));
    }

    @Override
    public void onTill(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos) {
        EmitterEvent.ON_TILL.call(EmitterContext.onTill(level, oldBlock, newBlock, pos));
    }

    @Override
    public void onStrip(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos) {
        EmitterEvent.ON_STRIP.call(EmitterContext.onStrip(level, oldBlock, newBlock, pos));
    }

    @Override
    public void onAnimateTick(LevelAccessor level, BlockState state, BlockPos pos) {
        EmitterEvent.ON_ANIMATE_TICK.call(EmitterContext.onAnimateTick(level, state, pos));
    }

    @Override
    public void onBreakByPlayer(LevelAccessor level, BlockState state, BlockPos pos) {
        
    }

    @Override
    public void onStateChange(LevelAccessor level, BlockState oldState, BlockState newState, BlockPos pos) {
        onStateChangeInternal(level, oldState, newState, pos);
    }

    @Override
    public void onBlockChange(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos) {
        onBlockChangeInternal(level, oldBlock, newBlock, pos);
    }

    @Override
    public void onOpen(LevelAccessor level, Block block, BlockPos pos) {
        EmitterEvent.ON_OPEN.call(EmitterContext.onOpen(level, block, pos));
    }

    @Override
    public void onClose(LevelAccessor level, Block block, BlockPos pos) {
        EmitterEvent.ON_CLOSE.call(EmitterContext.onClose(level, block, pos));
    }

    @Unique
    private void onStateChangeInternal(LevelAccessor level, BlockState oldState, BlockState newState, BlockPos pos) {
        if(oldState.getBlock() != newState.getBlock()) {
            onBlockChange(level, oldState.getBlock(), newState.getBlock(), pos);
            if(newState.isAir()) {
                onBreak(level, oldState, pos);
            }
            else {
                onPlace(level, newState, pos);
            }
        }
    }

    @Unique 
    private void onBlockChangeInternal(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos) {
        if(AxeItemAccessor.getStrippables().containsKey(oldBlock) && AxeItemAccessor.getStrippables().get(oldBlock) == newBlock) {
            onStrip(level, oldBlock, newBlock, pos);
        }
        else if(HoeItemAccessor.getTillables().containsKey(oldBlock) && (newBlock == Blocks.FARMLAND || newBlock == Blocks.DIRT_PATH)) {
            onTill(level, oldBlock, newBlock, pos);
        }
        else if(ShovelItemAccessor.getFlattenables().containsKey(oldBlock) && ShovelItemAccessor.getFlattenables().get(oldBlock).getBlock() == newBlock) {
            onFlatten(level, oldBlock, newBlock, pos);
        }
    }

    @Inject(method = "animateTick", at = @At("HEAD"))
    private void mpe$animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random, CallbackInfo ci) {
        onAnimateTick(level, state, pos);
    }
}
