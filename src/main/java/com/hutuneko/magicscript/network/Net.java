package com.hutuneko.magicscript.network;

import com.hutuneko.magicscript.Magicscript;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Net {

    // チャンネルは 1 本化（"main"）
    private static final String PROTOCOL = "1"; // 旧 "0" から上げてもOK。左右一致が必須
    public static SimpleChannel CHANNEL;
    private static int id = 0;

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CHANNEL = NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(Magicscript.MODID, "main"),
                    () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals
            );

            id = 0;

            // --- Client 向け（サーバ→クライアント）
//            CHANNEL.messageBuilder(.class, id++, NetworkDirection.PLAY_TO_CLIENT)
//                    .encoder(::encode)
//                    .decoder(::decode)
//                    .consumerMainThread(::handle)
//                    .add();

            // --- Server 向け（クライアント→サーバ）
            CHANNEL.messageBuilder(SaveScriptPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                    .encoder(SaveScriptPacket::encode)
                    .decoder(SaveScriptPacket::decode)
                    .consumerMainThread(SaveScriptPacket::handle)
                    .add();
        });
    }

}