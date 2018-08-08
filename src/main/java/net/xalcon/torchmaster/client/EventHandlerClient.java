package net.xalcon.torchmaster.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.logic.ITorchRegistryContainer;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = TorchMasterMod.MODID)
public class EventHandlerClient
{
    @SubscribeEvent
    public static void OnPlaySoundEvent(PlaySoundEvent event)
    {
        if("ambient.cave".equals(event.getName()))
        {
            ITorchRegistryContainer container = Minecraft.getMinecraft().world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER, null);
            if(container == null) return;

            ISound sound = event.getSound();
            if(sound == null) return;

            if(container.getMegaTorchRegistry().isPositionInTorchRange(sound.getXPosF(), sound.getYPosF(), sound.getZPosF()))
            {
                System.out.println("BLOCKING AMBIENT CAVE SOUND");
                event.setResultSound(null);
            }
            else
            {
                System.out.println("ALLOWING AMBIENT CAVE SOUND");
            }
        }
    }
}
