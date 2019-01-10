package net.xalcon.torchmaster.compat;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.common.TorchmasterConfig;

@Mod.EventBusSubscriber()
public class MoCreaturesCompat
{
	@SubscribeEvent
	public static void registerTorchEntities(EntityFilterRegisterEvent.MegaTorch event)
	{
		if(!TorchmasterConfig.MoCreaturesBlockAll) return;

		ForgeRegistries.ENTITIES.getEntries().stream()
				.filter(n -> "mocreatures".equals(n.getKey().getNamespace()))
				.map(n -> n.getValue().getEntityClass())
				.forEach(c -> event.getRegistry().registerEntity(c));
	}
}