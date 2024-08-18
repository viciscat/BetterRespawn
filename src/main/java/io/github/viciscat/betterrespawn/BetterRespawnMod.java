package io.github.viciscat.betterrespawn;

import com.dhanantry.scapeandrunparasites.entity.monster.hijacked.EntityHiGolem;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.srpcotesia.capability.CapabilityParasitePlayer;
import com.srpcotesia.capability.ParasitePlayer;
import com.tmtravlr.potioncore.potion.PotionMagicShield;
import com.tmtravlr.potioncore.potion.PotionRecoil;
import com.tmtravlr.potioncore.potion.PotionRevival;
import electroblob.wizardry.registry.WizardryItems;
import io.github.viciscat.wither.DecayPotion;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Mod;
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

    public static EntityLiving getGolem(EntityPlayerMP entity) {
        ParasitePlayer capability = entity.getCapability(CapabilityParasitePlayer.PARASITE_PLAYER_CAPABILITY, null);
        if (capability == null || !capability.isParasite()) {
            return new EntityIronGolem(entity.getServerWorld());
        } else {
            return new EntityHiGolem(entity.getServerWorld());
        }
    }

    public static void addEffectsToGolem(EntityLivingBase entityLiving) {
        entityLiving.addPotionEffect(new PotionEffect(DecayPotion.INSTANCE      , 30*20*60, 0, false, false));
        entityLiving.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE     , 30*20*60, 1, false, false));
        entityLiving.addPotionEffect(new PotionEffect(MobEffects.SPEED          , 30*20*60, 1, false, false));
        entityLiving.addPotionEffect(new PotionEffect(MobEffects.HASTE          , 30*20*60, 1, false, false));
        entityLiving.addPotionEffect(new PotionEffect(PotionRecoil.INSTANCE     , 30*20*60, 1, false, false));
        entityLiving.addPotionEffect(new PotionEffect(PotionMagicShield.INSTANCE, 30*20*60, 1, false, false));
        entityLiving.addPotionEffect(new PotionEffect(SRPPotions.EPEL_E         , 30*20*60, 0, false, false));
    }
    public static void addEffectsToPlayer(EntityPlayerMP entityLiving) {
        entityLiving.addPotionEffect(new PotionEffect(PotionRevival.INSTANCE, 60*20, 0, false, false));
        entityLiving.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 15*20, 4, false, false));
        entityLiving.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 60*20, 2, false, false));

        if (entityLiving.inventory.armorInventory.get(0).isEmpty()) entityLiving.inventory.armorInventory.set(0, new ItemStack(WizardryItems.spectral_boots));
        if (entityLiving.inventory.armorInventory.get(1).isEmpty()) entityLiving.inventory.armorInventory.set(1, new ItemStack(WizardryItems.spectral_leggings));
        if (entityLiving.inventory.armorInventory.get(2).isEmpty()) entityLiving.inventory.armorInventory.set(2, new ItemStack(WizardryItems.spectral_chestplate));
        if (entityLiving.inventory.armorInventory.get(3).isEmpty()) entityLiving.inventory.armorInventory.set(3, new ItemStack(WizardryItems.spectral_helmet));
    }
}
