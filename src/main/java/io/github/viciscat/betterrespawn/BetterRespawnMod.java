package io.github.viciscat.betterrespawn;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "better_respawn", useMetadata=true)
public class BetterRespawnMod {

    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("better_respawn");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        NETWORK_WRAPPER.registerMessage(RandomRespawnPacket.Handler.class, RandomRespawnPacket.class, 0, Side.SERVER);
    }
}
