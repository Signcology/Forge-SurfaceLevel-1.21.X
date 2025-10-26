package com.signcology.surfacelevel.event;

import com.signcology.surfacelevel.SurfaceLevel;
import com.signcology.surfacelevel.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = SurfaceLevel.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvent {
    static int updateDistance = 3;
    static int updateDistanceExplosives = 5;
    static int coverDistance = 1;

    private static boolean canBeSeen(Level level, BlockPos pPos) {
        for(int x = -coverDistance; x <= coverDistance; x++) {
            for(int y = -coverDistance; y <= coverDistance; y++) {
                for(int z = -coverDistance; z <= coverDistance; z++) {
                    BlockPos pos = new BlockPos(pPos.getX() + x, pPos.getY() + y, pPos.getZ() + z);
                    if (!level.getBlockState(pos).canOcclude()) {
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

        for(int x = -updateDistance; x <= updateDistance; x++) {
            for(int y = -updateDistance; y <= updateDistance; y++) {
                for(int z = -updateDistance; z <= updateDistance; z++) {
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

        for(int x = -updateDistanceExplosives; x <= updateDistanceExplosives; x++) {
            for(int y = -updateDistanceExplosives; y <= updateDistanceExplosives; y++) {
                for(int z = -updateDistanceExplosives; z <= updateDistanceExplosives; z++) {
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

        for(int x = -updateDistanceExplosives; x <= updateDistanceExplosives; x++) {
            for(int y = -updateDistanceExplosives; y <= updateDistanceExplosives; y++) {
                for(int z = -updateDistanceExplosives; z <= updateDistanceExplosives; z++) {
                    BlockPos pos = new BlockPos((int) explosion.center().x + x, (int) explosion.center().y + y, (int) explosion.center().z + z);
                    degenerateHardBlock(level, level.getBlockState(pos), pos);
                }
            }
        }
    }


}
