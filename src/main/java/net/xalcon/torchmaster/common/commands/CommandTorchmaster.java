package net.xalcon.torchmaster.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.ModCaps;

public class CommandTorchmaster
{
    public enum SubCommands
    {
        DUMP_TORCHES("torchdump")
            {
                @Override
                public int execute(CommandContext<CommandSource> ctx)
                {
                    ctx.getSource()
                    CommandSource source = ctx.getSource();
                    MinecraftServer server = null;
                    if(source instanceof MinecraftServer)
                    {
                        server = (MinecraftServer)source;
                    }
                    else if(source instanceof Entity)
                    {
                        server = ((Entity)source).getServer();
                    }
                    else
                    {
                        throw new RuntimeException("Unknown command source: " + source.getClass().getName());
                    }
                    Torchmaster.Log.info("#################################");
                    Torchmaster.Log.info("# Torchmaster Torch Dump Start  #");
                    Torchmaster.Log.info("#################################");
                    for(World world: server.getWorlds())
                    {
                        world.getCapability(ModCaps.TEB_REGISTRY, Direction.DOWN).ifPresent(container ->
                        {
                            Torchmaster.Log.info("Torches in dimension {}:", world.getDimensionKey().getLocation());
                            for(TorchInfo torch: container.getEntries())
                                Torchmaster.Log.info("  {} @ {}", torch.getName(), torch.getPos());
                        });
                    }
                    Torchmaster.Log.info("#################################");
                    Torchmaster.Log.info("# Torchmaster Torch Dump End    #");
                    Torchmaster.Log.info("#################################");

                    source.sendMessage(new TranslationTextComponent(Torchmaster.MODID + ".command.torch_dump.completed"), false);
                    return 0;
                }
            },
        DUMP_ENTITIES("entitydump")
            {
                @Override
                public int execute(CommandContext<CommandSource> ctx)
                {
                    CommandSource source = ctx.getSource();
                    Torchmaster.Log.info("#################################");
                    Torchmaster.Log.info("# Torchmaster Entity Dump Start #");
                    Torchmaster.Log.info("#################################");
                    Torchmaster.Log.info("List of registered entities:");
                    for(ResourceLocation loc: ForgeRegistries.ENTITIES.getKeys())
                        Torchmaster.Log.info("  {}", loc);

                    Torchmaster.Log.info("Dread Lamp Registry Content:");
                    for(ResourceLocation loc: Torchmaster.DreadLampFilterRegistry.getRegisteredEntities())
                        Torchmaster.Log.info("  {}", loc);

                    Torchmaster.Log.info("Mega Torch Registry Content:");
                    for(ResourceLocation loc: Torchmaster.MegaTorchFilterRegistry.getRegisteredEntities())
                        Torchmaster.Log.info("  {}", loc);
                    Torchmaster.Log.info("#################################");
                    Torchmaster.Log.info("# Torchmaster Entity Dump End   #");
                    Torchmaster.Log.info("#################################");

                    source.sendMessage(new TranslationTextComponent(Torchmaster.MODID + ".command.entity_dump.completed"), false);
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

    public static void register(CommandDispatcher<CommandSource> dispatcher)
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
