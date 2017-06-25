package net.xalcon.torchmaster.compat;

import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.TorchMasterMod;

import java.util.Objects;

@Mod.EventBusSubscriber()
public class MoCreaturesCompat
{
	@SubscribeEvent
	public static void registerTorchEntities(EntityFilterRegisterEvent.MegaTorch event)
	{
		if(!TorchMasterMod.ConfigHandler.isMoCreaturesCompatEnabled) return;

		EntityList.getEntityNameList().stream()
				.filter(n -> "mocreatures".equals(n.getResourceDomain()))
				.map(EntityList::getClass)
				.filter(Objects::nonNull)
				.forEach(c -> event.getRegistry().registerEntity(c));
	}
}