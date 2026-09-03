package fr.madu59.moreparticleeffects.client.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockInterface {
    void onBreak(LevelAccessor level, BlockState state, BlockPos pos);
    void onPlace(LevelAccessor level, BlockState state, BlockPos pos);
    void onTill(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos);
    void onStrip(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos);
    void onFlatten(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos);
    void onBreakByPlayer(LevelAccessor level, BlockState state, BlockPos pos);
    void onStateChange(LevelAccessor level, BlockState oldState, BlockState newState, BlockPos pos);
    void onBlockChange(LevelAccessor level, Block oldBlock, Block newBlock, BlockPos pos);
}
