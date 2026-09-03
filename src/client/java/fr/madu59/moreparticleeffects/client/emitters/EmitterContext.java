package fr.madu59.moreparticleeffects.client.emitters;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EmitterContext {
    public BlockState oldState;
    public BlockState newState;
    public Block oldBlock;
    public Block newBlock;
    public BlockPos pos;

    public static EmitterContext onBlockChange(LevelAccessor level, Block oldBlock, Block newBlock) {
        EmitterContext context = new EmitterContext();
        context.oldBlock = oldBlock;
        context.newBlock = newBlock;
        return context;
    }

    public static EmitterContext onBreak(LevelAccessor level, BlockState oldState, BlockPos pos) {
        EmitterContext context = new EmitterContext();
        context.oldState = oldState;
        context.pos = pos;
        return context;
    }
}
