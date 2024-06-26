package io.github.viciscat.betterrespawn.mixin;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.llamalad7.mixinextras.sugar.Local;
import com.tmtravlr.potioncore.potion.PotionMagicShield;
import com.tmtravlr.potioncore.potion.PotionRecoil;
import com.tmtravlr.potioncore.potion.PotionRevival;
import electroblob.wizardry.registry.WizardryItems;
import io.github.viciscat.betterrespawn.injected.EntityPlayerMPWrapper;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Unique
    private final Map<UUID, LongList> better_respawn$deathTimes = new HashMap<>();


    @Inject(method = "recreatePlayerEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;getChunkProvider()Lnet/minecraft/world/gen/ChunkProviderServer;"))
    public void randomizeSpawn(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd, CallbackInfoReturnable<EntityPlayerMP> cir, @Local(ordinal = 1) EntityPlayerMP entityplayermp) {
        EntityPlayerMPWrapper wrapped = EntityPlayerMPWrapper.of(playerIn);

        if (wrapped.better_respawn$getRandomRespawn()) {
            wrapped.better_respawn$setRandomRespawn(false);
            int rand = 70;
            int halfRand = rand / 2;
            Random random = entityplayermp.world.rand;
            BlockPos topSolidOrLiquidBlock = entityplayermp.world.getTopSolidOrLiquidBlock(playerIn.getPosition().add(
                    halfRand - random.nextInt(rand),
                    0,
                    halfRand - random.nextInt(rand)));
            entityplayermp.setPosition(topSolidOrLiquidBlock.getX() + 0.5, topSolidOrLiquidBlock.getY() + 0.5, topSolidOrLiquidBlock.getZ() + 0.5);
        }

        long timeMillis = System.currentTimeMillis();
        LongList longs = better_respawn$deathTimes.computeIfAbsent(entityplayermp.getUniqueID(), uuid -> new LongArrayList(5));
        longs.removeIf(l -> timeMillis - l > 1000 * 5 * 60);
        longs.add(timeMillis);


        if (longs.size() > 3) {
            longs.clear();
            EntityIronGolem entityIronGolem = new EntityIronGolem(entityplayermp.getServerWorld());
            entityIronGolem.setPosition(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ);
            entityplayermp.getServerWorld().spawnEntity(entityIronGolem);
            entityIronGolem.addPotionEffect(new PotionEffect(MobEffects.WITHER         , 30*20*60, 0, false, false));
            entityIronGolem.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE     , 30*20*60, 1, false, false));
            entityIronGolem.addPotionEffect(new PotionEffect(MobEffects.SPEED          , 30*20*60, 1, false, false));
            entityIronGolem.addPotionEffect(new PotionEffect(MobEffects.HASTE          , 30*20*60, 1, false, false));
            entityIronGolem.addPotionEffect(new PotionEffect(PotionRecoil.INSTANCE     , 30*20*60, 1, false, false));
            entityIronGolem.addPotionEffect(new PotionEffect(PotionMagicShield.INSTANCE, 30*20*60, 1, false, false));
            entityIronGolem.addPotionEffect(new PotionEffect(SRPPotions.EPEL_E         , 30*20*60, 0, false, false));

            entityplayermp.addPotionEffect(new PotionEffect(PotionRevival.INSTANCE, 60*20, 0, false, false));
            entityplayermp.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 15*20, 4, false, false));
            entityplayermp.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 60*20, 2, false, false));

            entityplayermp.inventory.armorInventory.set(0, new ItemStack(WizardryItems.spectral_boots));
            entityplayermp.inventory.armorInventory.set(1, new ItemStack(WizardryItems.spectral_leggings));
            entityplayermp.inventory.armorInventory.set(2, new ItemStack(WizardryItems.spectral_chestplate));
            entityplayermp.inventory.armorInventory.set(3, new ItemStack(WizardryItems.spectral_helmet));

            entityplayermp.sendMessage(new TextComponentString("<§k???§r> S§ke§rems you ne§ke§rd s§ko§rme §l§ehelp§r!"));

        }
    }
}
