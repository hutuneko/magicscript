package com.hutuneko.magicscript.api;

import com.hutuneko.magicscript.item.MagicItems;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

public class MagicScriptAPI {
    public static String pageJson(ItemStack stack){
        if (stack.getItem() == MagicItems.MAGIC_SCRIPTBOOK.get()
                || stack.getItem() == Items.WRITABLE_BOOK
                || stack.getItem() == Items.WRITTEN_BOOK) {

            ListTag pages = stack.getOrCreateTag().getList("pages", Tag.TAG_STRING);
            List<String> stringList = new ArrayList<>();

            for (int i = 0; i < pages.size(); i++) {
                String jsonPage = pages.getString(i);

                // JSON → Component に戻す
                Component component = Component.Serializer.fromJson(jsonPage);

                // Component → 純粋な文字列に
                String page = null;
                if (component != null) {
                    page = component.getString();
                }

                stringList.add(page);
            }

            return String.join("\n---\n", stringList); // 複数ページの場合など
        }

        return null;
    }

    public static String page(ItemStack stack) {

        if (stack.getItem() == MagicItems.MAGIC_SCRIPTBOOK.get()
                || stack.getItem() == Items.WRITABLE_BOOK
                || stack.getItem() == Items.WRITTEN_BOOK) {

            ListTag pages = stack.getOrCreateTag().getList("pages", Tag.TAG_STRING);

            if (pages.isEmpty()) return null;

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < pages.size(); i++) {
                String raw = pages.getString(i);

                // JSONでなくてもそのまま使う
                sb.append(raw).append("\n");
            }

            String result = sb.toString().trim();
            return result.isEmpty() ? null : result;
        }

        return null;
    }

}
