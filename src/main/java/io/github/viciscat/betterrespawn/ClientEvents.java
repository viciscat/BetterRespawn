package io.github.viciscat.betterrespawn;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void gameOverScreenInject(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();
        if (!(gui instanceof GuiGameOver)) return;
        GuiButton button = new GuiButton(999, gui.width / 2 - 100, gui.height / 4 + 96, "Random Respawn") {

            private float t = 0.f;

            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                super.drawButton(mc, mouseX, mouseY, partialTicks);
                t+=partialTicks;
                if (t >= 20.f && !enabled) enabled = true;
            }

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                if (super.mousePressed(mc, mouseX, mouseY)) {
                    BetterRespawnMod.NETWORK_WRAPPER.sendToServer(new RandomRespawnPacket());
                    mc.displayGuiScreen(null);
                    return true;
                }
                return false;
            }
        };
        button.enabled = false;
        event.getButtonList().add(1, button);
        for (int i = 2; i < event.getButtonList().size(); i++) {
            GuiButton button1 = event.getButtonList().get(i);
            button1.y += 24;
        }
    }
}
