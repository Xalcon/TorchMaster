package net.xalcon.torchmaster.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.TorchmasterConfig;

public class CommandTorchmasterEntityDump extends CommandBase
{
    @Override
    public String getName()
    {
        return "torchmaster_entity_dump";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/torchmaster_entity_dump";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        TorchMasterMod.Log.info("#################################");
        TorchMasterMod.Log.info("# Torchmaster Entity Dump Start #");
        TorchMasterMod.Log.info("#################################");
        TorchMasterMod.Log.info("List of registered entities:");
        for(ResourceLocation loc: EntityList.getEntityNameList())
            TorchMasterMod.Log.info("  {}", loc);

        TorchMasterMod.Log.info("Dread Lamp Registry Content:");
        for(ResourceLocation loc: TorchMasterMod.DreadLampFilterRegistry.getRegisteredEntities())
            TorchMasterMod.Log.info("  {}", loc);

        TorchMasterMod.Log.info("Mega Torch Registry Content:");
        for(ResourceLocation loc: TorchMasterMod.MegaTorchFilterRegistry.getRegisteredEntities())
            TorchMasterMod.Log.info("  {}", loc);
        TorchMasterMod.Log.info("#################################");
        TorchMasterMod.Log.info("# Torchmaster Entity Dump End   #");
        TorchMasterMod.Log.info("#################################");

        sender.sendMessage(new TextComponentTranslation(TorchMasterMod.MODID + ".command.entity_dump.completed"));
    }
}
