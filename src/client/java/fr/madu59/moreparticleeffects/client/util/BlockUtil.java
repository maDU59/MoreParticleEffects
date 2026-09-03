package fr.madu59.moreparticleeffects.client.util;

import java.util.Iterator;

import com.google.gson.JsonElement;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

public class BlockUtil {

    public static boolean isValidBlock(Iterable<?> blockIterator, Block block) {
        for(Object blockEntry : blockIterator) {
            String blockId;
            if(blockEntry instanceof JsonElement) {
                blockId = ((JsonElement) blockEntry).getAsString();
            } else {
                blockId = blockEntry.toString();
            }

            if(isValidBlock(blockId, block)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidBlock(String entry, Block block) {
        if(entry == null || entry.isEmpty()) {
            System.err.println("Invalid block entry: " + entry);
            return false;
        }
        else if(entry.startsWith("#")) {
            Identifier tagId = Identifier.tryParse(entry.substring(1));

            TagKey<Block> tag = TagKey.create(
                Registries.BLOCK, 
                tagId
            );

            if(tagId != null && tag != null) {
                for(Holder<Block> blockHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
                    if(blockHolder.value() == block) {
                        return true;
                    }
                }
            } else {
                System.err.println("Invalid block tag entry: " + entry);
                return false;
            }
        }
        else if(entry.startsWith("$")) {
            if(entry.equals("$cauldron")) {
                return block instanceof AbstractCauldronBlock;
            }
            else if(entry.equals("$door")) {
                return block instanceof DoorBlock;
            }
            else if(entry.equals("$fence")) {
                return block instanceof FenceBlock;
            }
            else if(entry.equals("$fence_gate")) {
                return block instanceof FenceGateBlock;
            }
            else if(entry.equals("$slab")) {
                return block instanceof SlabBlock;
            }
            else if(entry.equals("$stairs")) {
                return block instanceof StairBlock;
            }
            else if(entry.equals("$wall")) {
                return block instanceof WallBlock;
            }
        }
        else if(entry.equals("*")) {
            return true;
        }
        else {
            Identifier blockId = Identifier.tryParse(entry);
            if(blockId != null && BuiltInRegistries.BLOCK.getKey(block) != null) {
                return BuiltInRegistries.BLOCK.getKey(block).equals(blockId);
            } else {
                System.err.println("Invalid block entry: " + entry);
                return false;
            }
        }
        return false;
    }

}
