package com.signcology.surfacelevel.util;

import com.signcology.surfacelevel.SurfaceLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Item> HARD_BLOCK = createTag("hard_block");
        public static final TagKey<Item> STONE_BLOCK = createTag("stone_block");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SurfaceLevel.MODID, name));
        }
    }



    public static class Items {

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SurfaceLevel.MODID, name));
        }
    }
}
