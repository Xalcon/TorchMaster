package net.xalcon.torchmaster.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.logic.entityblocking.TorchInfo;

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
                    Torchmaster.LOG.info("#################################");
                    Torchmaster.LOG.info("# Torchmaster Torch Dump Start  #");
                    Torchmaster.LOG.info("#################################");
                    for(var level: server.getAllLevels())
                    {
                        Torchmaster.getRegistryForLevel(level).ifPresent(container ->
                        {
                            Torchmaster.LOG.info("Torches in dimension {}:", level.dimension().registry());
                            for(TorchInfo torch: container.getEntries())
                                Torchmaster.LOG.info("  {} @ {}", torch.getName(), torch.getPos());
                        });
                    }
                    Torchmaster.LOG.info("#################################");
                    Torchmaster.LOG.info("# Torchmaster Torch Dump End    #");
                    Torchmaster.LOG.info("#################################");

                    source.sendSuccess(() -> Component.translatable(Constants.MOD_ID + ".command.torch_dump.completed"), false);
                    return 0;
                }
            },
        DUMP_ENTITIES("entitydump")
            {
                @Override
                public int execute(CommandContext<CommandSourceStack> ctx)
                {
                    var source = ctx.getSource();
                    Torchmaster.LOG.info("#################################");
                    Torchmaster.LOG.info("# Torchmaster Entity Dump Start #");
                    Torchmaster.LOG.info("#################################");
                    Torchmaster.LOG.info("List of registered entities:");
                    BuiltInRegistries.ENTITY_TYPE.stream().map(BuiltInRegistries.ENTITY_TYPE::getKey).forEach(loc ->
                            Torchmaster.LOG.info("  {}", loc));

                    Torchmaster.LOG.info("Dread Lamp Registry Content:");
                    for(ResourceLocation loc: Torchmaster.DreadLampFilterRegistry.getEntities())
                        Torchmaster.LOG.info("  {}", loc);

                    Torchmaster.LOG.info("Mega Torch Registry Content:");
                    for(ResourceLocation loc: Torchmaster.MegaTorchFilterRegistry.getEntities())
                        Torchmaster.LOG.info("  {}", loc);
                    Torchmaster.LOG.info("#################################");
                    Torchmaster.LOG.info("# Torchmaster Entity Dump End   #");
                    Torchmaster.LOG.info("#################################");
                    source.sendSuccess(() -> Component.translatable(Constants.MOD_ID + ".command.entity_dump.completed"), false);
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("torchmaster");
        for (SubCommands subCommand : SubCommands.values())
        {
            command.then(Commands.literal(subCommand.getTranslationKey()).executes(subCommand::execute));
        }

        dispatcher.register(
            (LiteralArgumentBuilder) ((LiteralArgumentBuilder) command.requires((cmdSrc) -> cmdSrc.hasPermission(2)))
                .executes((ctx) -> 0));
    }
}
