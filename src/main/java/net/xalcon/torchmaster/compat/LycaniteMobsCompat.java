package net.xalcon.torchmaster.compat;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.common.TorchmasterConfig;

import java.util.Arrays;

@Mod.EventBusSubscriber()
public class LycaniteMobsCompat
{
	private static final String[] MODS =
	{
			"arcticmobs",
			"demonmobs",
			"desertmobs",
			"forestmobs",
			"freshwatermobs",
			"infernomobs",
			"junglemobs",
			"mountainmobs",
			"plainsmobs",
			"saltwatermobs",
			"shadowmobs",
			"swampmobs"
	};

	@SubscribeEvent
	public static void registerTorchEntities(EntityFilterRegisterEvent.MegaTorch event)
	{
		if(!TorchmasterConfig.LycanitesMobsBlockAll) return;

		ForgeRegistries.ENTITIES.getEntries().stream()
				.filter(n -> Arrays.stream(MODS).anyMatch(m -> m.equals(n.getKey().getNamespace())))
				.map(n -> n.getValue().getEntityClass())
				.forEach(c -> event.getRegistry().registerEntity(c));
	}
}