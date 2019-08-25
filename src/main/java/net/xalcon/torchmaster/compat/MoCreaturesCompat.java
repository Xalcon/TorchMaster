package net.xalcon.torchmaster.compat;

import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.TorchmasterConfig;

import java.util.Arrays;
import java.util.Objects;

@Mod.EventBusSubscriber()
public class MoCreaturesCompat
{
	@SubscribeEvent
	public static void registerTorchEntities(EntityFilterRegisterEvent.MegaTorch event)
	{
		if(!TorchmasterConfig.MoCreaturesBlockAll) return;

		EntityList.getEntityNameList().stream()
			.filter(n -> "mocreatures".equals(n.getNamespace()))
			.map(rl -> new EntityInfoWrapper(rl, EntityList.getClass(rl)))
			.filter(e -> e.getEntityClass() != null)
			.forEach(e -> event.getRegistry().registerEntity(e.getEntityName()));
	}
}