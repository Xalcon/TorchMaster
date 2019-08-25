package net.xalcon.torchmaster.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.logic.ITorchRegistryContainer;
import net.xalcon.torchmaster.common.logic.TorchRegistryContainer;

public class CommandTorchmasterTorchList extends CommandBase
{
    @Override
    public String getName()
    {
        return "torchmaster_torch_list";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/torchmaster_torch_list";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        TorchMasterMod.Log.info("#################################");
        TorchMasterMod.Log.info("# Torchmaster Torch Dump Start  #");
        TorchMasterMod.Log.info("#################################");
        for(World world: server.worlds)
        {
            ITorchRegistryContainer container = world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER, null);
            if(container == null) continue;
            TorchMasterMod.Log.info("Mega torches in dimension {} (Id: {}):", world.provider.getDimensionType(), world.provider.getDimension());
            for(BlockPos pos: container.getMegaTorchRegistry().getEntries())
                TorchMasterMod.Log.info("  {}", pos);

            TorchMasterMod.Log.info("Dread Lamps in dimension {} (Id: {}):", world.provider.getDimensionType(), world.provider.getDimension());
            for(BlockPos pos: container.getDreadLampRegistry().getEntries())
                TorchMasterMod.Log.info("  {}", pos);

        }
        TorchMasterMod.Log.info("#################################");
        TorchMasterMod.Log.info("# Torchmaster Torch Dump End    #");
        TorchMasterMod.Log.info("#################################");

        sender.sendMessage(new TextComponentTranslation(TorchMasterMod.MODID + ".command.torch_list.completed"));
    }
}
