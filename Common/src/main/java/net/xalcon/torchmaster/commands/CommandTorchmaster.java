package net.xalcon.torchmaster.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.logic.entityblocking.TorchInfo;
import net.xalcon.torchmaster.platform.Services;

public class CommandTorchmaster
{
    public enum SubCommands
    {
        DUMP_TORCHES("torchdump")
            {
                @Override
                public int execute(CommandContext<CommandSourceStack> ctx)
                {
                    var source = ctx.getSource();
                    MinecraftServer server = source.getServer();
                    Constants.LOG.info("#################################");
                    Constants.LOG.info("# Torchmaster Torch Dump Start  #");
                    Constants.LOG.info("#################################");
                    for(var level: server.getAllLevels())
                    {
                        Services.PLATFORM.getRegistryForLevel(level).ifPresent(container ->
                        {
                            Constants.LOG.info("Torches in dimension {}:", level.dimension().registry());
                            for(TorchInfo torch: container.getEntries())
                                Constants.LOG.info("  {} @ {}", torch.getName(), torch.getPos());
                        });
                    }
                    Constants.LOG.info("#################################");
                    Constants.LOG.info("# Torchmaster Torch Dump End    #");
                    Constants.LOG.info("#################################");

                    source.sendSuccess(Component.translatable(Constants.MOD_ID + ".command.torch_dump.completed"), false);
                    return 0;
                }
            },
        DUMP_ENTITIES("entitydump")
            {
                @Override
                public int execute(CommandContext<CommandSourceStack> ctx)
                {
                    var source = ctx.getSource();
                    Constants.LOG.info("#################################");
                    Constants.LOG.info("# Torchmaster Entity Dump Start #");
                    Constants.LOG.info("#################################");
                    Constants.LOG.info("List of registered entities:");
                    Registry.ENTITY_TYPE.stream().map(Registry.ENTITY_TYPE::getKey).forEach(loc ->
                            Constants.LOG.info("  {}", loc));

                    Constants.LOG.info("Dread Lamp Registry Content:");
                    for(ResourceLocation loc: Torchmaster.DreadLampFilterRegistry.getRegisteredEntities())
                        Constants.LOG.info("  {}", loc);

                    Constants.LOG.info("Mega Torch Registry Content:");
                    for(ResourceLocation loc: Torchmaster.MegaTorchFilterRegistry.getRegisteredEntities())
                        Constants.LOG.info("  {}", loc);
                    Constants.LOG.info("#################################");
                    Constants.LOG.info("# Torchmaster Entity Dump End   #");
                    Constants.LOG.info("#################################");

                    source.sendSuccess(Component.translatable(Constants.MOD_ID + ".command.entity_dump.completed"), false);
                    return 0;
                }
            };

        private final String translationKey;

        SubCommands(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public abstract int execute(CommandContext<CommandSourceStack> ctx);

        public String getTranslationKey()
        {
            return translationKey;
        }
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("torchmaster");
        for (SubCommands subCommand : SubCommands.values())
        {
            command.then(Commands.literal(subCommand.getTranslationKey()).executes(subCommand::execute));
        }

        dispatcher.register(
            (LiteralArgumentBuilder) ((LiteralArgumentBuilder) command.requires((cmdSrc) -> cmdSrc.hasPermission(2)))
                .executes((ctx) ->
                {
                    return 0;
                }));
    }
}
