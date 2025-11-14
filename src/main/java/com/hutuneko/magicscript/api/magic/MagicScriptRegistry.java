package com.hutuneko.magicscript.api.magic;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.lang.reflect.Modifier;

public class MagicScriptRegistry {

    public static void scan(String modid) {

        // --- Forge の ModFile 情報を取得 ---
        ModFileInfo modFile = (ModFileInfo) ModList.get().getModFileById(modid);
        if (modFile == null) {
            System.out.println("[MagicScript] No mod file found for id: " + modid);
            return;
        }

        // --- ASM でスキャンされたアノテーション情報を取得 ---
        ModFileScanData scanData = modFile.getFile().getScanResult();

        for (ModFileScanData.AnnotationData ann : scanData.getAnnotations()) {

            // MagicScript アノテーションを持つクラスだけ処理
            if (!ann.annotationType().getClassName().equals(MagicScript.class.getName()))
                continue;

            String className = ann.clazz().getClassName();

            try {
                Class<?> cls = Class.forName(className);

                // --- IMagicFunction を実装しているクラスだけ登録 ---
                if (!IMagicFunction.class.isAssignableFrom(cls))
                    continue;

                // abstract class は除外
                if (Modifier.isAbstract(cls.getModifiers()))
                    continue;

                // --- MagicScript("名前") を取得 ---
                MagicScript scriptAnn = cls.getAnnotation(MagicScript.class);
                String name = scriptAnn.value();

                // --- インスタンス生成して登録 ---
                IMagicFunction instance =
                        (IMagicFunction) cls.getDeclaredConstructor().newInstance();

                MagicAPIRegistry.register(name, instance);

                System.out.println("[MagicScript] Registered spell (ASM): "
                        + name + " -> " + className);

            } catch (Throwable t) {
                System.out.println("[MagicScript] Failed to load class: " + className);
                t.printStackTrace();
            }
        }
    }
}
