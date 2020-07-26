package net.xalcon.torchmaster.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.logic.entityblocking.ITEBLightRegistry;

public class CommandTorchmaster
{
    public enum SubCommands
    {
        DUMP_TORCHES("torchdump")
            {
                @Override
                public int execute(CommandContext<CommandSource> ctx)
                {
                    CommandSource source = ctx.getSource();
                    MinecraftServer server = source.getServer();
                    Torchmaster.Log.info("#################################");
                    Torchmaster.Log.info("# Torchmaster Torch Dump Start  #");
                    Torchmaster.Log.info("#################################");
                    for(World world: server.getWorlds())
                    {
                        world.getCapability(ModCaps.TEB_REGISTRY, Direction.DOWN).ifPresent(container ->
                        {
                            Torchmaster.Log.info("Torches in dimension {}:", world.func_234923_W_().func_240901_a_());
                            for(TorchInfo torch: container.getEntries())
                                Torchmaster.Log.info("  {} @ {}", torch.getName(), torch.getPos());
                        });
                    }
                    Torchmaster.Log.info("#################################");
                    Torchmaster.Log.info("# Torchmaster Torch Dump End    #");
                    Torchmaster.Log.info("#################################");

                    source.sendFeedback(new TranslationTextComponent(Torchmaster.MODID + ".command.torch_dump.completed"), false);
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

                    source.sendFeedback(new TranslationTextComponent(Torchmaster.MODID + ".command.entity_dump.completed"), false);
                    return 0;
                }
            };

        private final String translationKey;

        SubCommands(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public abstract int execute(CommandContext<CommandSource> ctx);

        public String getTranslationKey()
        {
            return translationKey;
        }
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        LiteralArgumentBuilder<CommandSource> command = Commands.literal("torchmaster");
        for (SubCommands subCommand : SubCommands.values())
        {
            command.then(Commands.literal(subCommand.getTranslationKey()).executes(subCommand::execute));
        }

        dispatcher.register(
            (LiteralArgumentBuilder) ((LiteralArgumentBuilder) command.requires((cmdSrc) -> cmdSrc.hasPermissionLevel(2)))
                .executes((ctx) ->
                {
                    return 0;
                }));
    }
}
