package io.github.viciscat.betterrespawn.injected;

import net.minecraft.entity.player.EntityPlayerMP;

public interface EntityPlayerMPWrapper {

    void better_respawn$setRandomRespawn(boolean randomRespawn);

    boolean better_respawn$getRandomRespawn();

    static EntityPlayerMPWrapper of(EntityPlayerMP playerMP) {
        return (EntityPlayerMPWrapper) playerMP;
    }
}
