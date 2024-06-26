package io.github.viciscat.betterrespawn.mixin;

import io.github.viciscat.betterrespawn.injected.EntityPlayerMPWrapper;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(EntityPlayerMP.class)
class EntityPlayerMPMixin implements EntityPlayerMPWrapper {

    @Unique
    private boolean better_respawn$randomRespawn = false;


    @Override
    public void better_respawn$setRandomRespawn(boolean randomRespawn) {
        this.better_respawn$randomRespawn = randomRespawn;
    }

    @Override
    public boolean better_respawn$getRandomRespawn() {
        return better_respawn$randomRespawn;
    }
}
