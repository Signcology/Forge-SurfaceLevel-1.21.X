package com.signcology.surfacelevel.event;

import com.signcology.surfacelevel.Config;
import com.signcology.surfacelevel.SurfaceLevel;
import com.signcology.surfacelevel.block.ModBlocks;
import com.signcology.surfacelevel.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SurfaceLevel.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvent {

    private static boolean canBeSeen(Level level, BlockPos pPos) {
        for(int x = -Config.airSearchDistance; x <= Config.airSearchDistance; x++) {
            for(int y = -Config.airSearchDistance; y <= Config.airSearchDistance; y++) {
                for(int z = -Config.airSearchDistance; z <= Config.airSearchDistance; z++) {
                    BlockPos pos = new BlockPos(pPos.getX() + x, pPos.getY() + y, pPos.getZ() + z);
                    if (!level.getBlockState(pos).is(ModTags.Blocks.STONE_BLOCK)) {
                        //!level.getBlockState(pos).canOcclude()
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void generateHardBlock(Level level,BlockState block, BlockPos pos) {
        if(block.getBlock() == Blocks.STONE && !canBeSeen(level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.HARDSTONE.get().defaultBlockState());
        } else if(block.getBlock() == Blocks.DEEPSLATE && !canBeSeen(level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.HARDSLATE.get().defaultBlockState());
        } else if(block.getBlock() == Blocks.NETHERRACK && !canBeSeen(level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.HARDRACK.get().defaultBlockState());
        }
    }
    private static void generateHardBlockExplosion(Level level,BlockState block, BlockPos pos) {
        if(block.getBlock() == Blocks.STONE) {
            level.setBlockAndUpdate(pos, ModBlocks.HARDSTONE.get().defaultBlockState());
        } else if(block.getBlock() == Blocks.DEEPSLATE) {
            level.setBlockAndUpdate(pos, ModBlocks.HARDSLATE.get().defaultBlockState());
        } else if(block.getBlock() == Blocks.NETHERRACK) {
            level.setBlockAndUpdate(pos, ModBlocks.HARDRACK.get().defaultBlockState());
        }
    }

    private static void degenerateHardBlock(Level level,BlockState block, BlockPos pos) {
        if(block.getBlock() == ModBlocks.HARDSTONE.get()) {
            level.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
        } else if(block.getBlock() == ModBlocks.HARDSLATE.get()) {
            level.setBlockAndUpdate(pos, Blocks.DEEPSLATE.defaultBlockState());
        } else if(block.getBlock() == ModBlocks.HARDRACK.get()) {
            level.setBlockAndUpdate(pos, Blocks.NETHERRACK.defaultBlockState());
        }
    }

    @SubscribeEvent
    public static void onHardBlockCreate(BlockEvent.BreakEvent event) {
        Level level = event.getPlayer().level();

        for(int x = -Config.updateDistance; x <= Config.updateDistance; x++) {
            for(int y = -Config.updateDistance; y <= Config.updateDistance; y++) {
                for(int z = -Config.updateDistance; z <= Config.updateDistance; z++) {
                    BlockPos pos = new BlockPos(event.getPos().getX() + x, event.getPos().getY() + y, event.getPos().getZ() + z);
                    generateHardBlock(level, level.getBlockState(pos), pos);
                    //System.out.println("Attempt Generation");
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExploisonHardBlockCreate(ExplosionEvent.Start event) {
        Level level = event.getLevel();
        Explosion explosion = event.getExplosion();

        for(int x = -Config.updateExplosionDistance; x <= Config.updateExplosionDistance; x++) {
            for(int y = -Config.updateExplosionDistance; y <= Config.updateExplosionDistance; y++) {
                for(int z = -Config.updateExplosionDistance; z <= Config.updateExplosionDistance; z++) {
                    BlockPos pos = new BlockPos((int) explosion.center().x + x, (int) explosion.center().y + y, (int) explosion.center().z + z);
                    generateHardBlockExplosion(level, level.getBlockState(pos), pos);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onExploisonHardBlockCreate(ExplosionEvent.Detonate event) {
        Level level = event.getLevel();
        Explosion explosion = event.getExplosion();

        for(int x = -Config.updateExplosionDistance; x <= Config.updateExplosionDistance; x++) {
            for(int y = -Config.updateExplosionDistance; y <= Config.updateExplosionDistance; y++) {
                for(int z = -Config.updateExplosionDistance; z <= Config.updateExplosionDistance; z++) {
                    BlockPos pos = new BlockPos((int) explosion.center().x + x, (int) explosion.center().y + y, (int) explosion.center().z + z);
                    degenerateHardBlock(level, level.getBlockState(pos), pos);
                }
            }
        }
    }


}
