package net.xalcon.torchmaster.compat;

import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.TorchMasterMod;

import java.util.Arrays;
import java.util.Objects;

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
		if(!TorchMasterMod.ConfigHandler.isLycaniteCompatEnabled) return;

		EntityList.getEntityNameList().stream()
				.filter(n -> Arrays.stream(MODS).anyMatch(n::contains))
				.map(EntityList.NAME_TO_CLASS::get)
				.filter(Objects::nonNull)
				.forEach(c -> event.getRegistry().registerEntity(c));
	}
}