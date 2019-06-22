package net.xalcon.torchmaster.common.init;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.blocks.BlockFeralFlareLantern;
import net.xalcon.torchmaster.common.blocks.BlockMegaTorch;
import net.xalcon.torchmaster.common.blocks.BlockTerrainLighter;
import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;
import net.xalcon.torchmaster.common.tiles.TileEntityMegaTorch;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

@ObjectHolder(Torchmaster.MODID)
public class ModTileEntities
{
    @ObjectHolder(BlockTerrainLighter.INTERNAL_NAME)
    public static TileEntityType<TileEntityTerrainLighter> TERRAIN_LIGHTER;

    @ObjectHolder(BlockMegaTorch.INTERNAL_NAME)
    public static TileEntityType<TileEntityMegaTorch> MEGA_TORCH;

    @ObjectHolder(BlockFeralFlareLantern.INTERNAL_NAME)
    public static TileEntityType<TileEntityFeralFlareLantern> FERAL_FLARE_LANTERN;

    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        /*Block[] blocks = new Block[]
                {
                        ModBlocks.MegaTorch,
                        ModBlocks.TerrainLighter,
                        ModBlocks.DreadLamp,
                        ModBlocks.FeralFlareLantern,
                        ModBlocks.InvisibleLight
                };

        Arrays.stream(blocks)
                .filter(block -> block instanceof IAutoRegisterTileEntity)
                .map(block -> (IAutoRegisterTileEntity)block)
                .forEach(block -> event.getRegistry().register());*/

        TERRAIN_LIGHTER = registerTileEntityType(event.getRegistry(),
                register(BlockTerrainLighter.INTERNAL_NAME, TileEntityType.Builder.create(TileEntityTerrainLighter::new)),
                BlockTerrainLighter.INTERNAL_NAME);

        MEGA_TORCH = registerTileEntityType(event.getRegistry(),
                register(BlockMegaTorch.INTERNAL_NAME, TileEntityType.Builder.create(TileEntityMegaTorch::new)),
                BlockMegaTorch.INTERNAL_NAME);

        FERAL_FLARE_LANTERN = registerTileEntityType(event.getRegistry(),
                register(BlockFeralFlareLantern.INTERNAL_NAME, TileEntityType.Builder.create(TileEntityFeralFlareLantern::new)),
                BlockFeralFlareLantern.INTERNAL_NAME);
    }

    protected static <T extends TileEntityType<?>> T registerTileEntityType(IForgeRegistry<TileEntityType<?>> registry, T tileEntityType, String name)
    {
        register(registry, tileEntityType, new ResourceLocation(Torchmaster.MODID, name));
        return tileEntityType;
    }

    protected static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T thing, ResourceLocation name)
    {
        thing.setRegistryName(name);
        registry.register(thing);
        return thing;
    }

    public static <T extends TileEntity> TileEntityType<T> register(String id, TileEntityType.Builder<T> builder)
    {
        Type<?> type = null;

        try
        {
            type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(1519)).getChoiceType(TypeReferences.BLOCK_ENTITY, id);
        }
        catch (IllegalArgumentException illegalstateexception)
        {
            if (SharedConstants.developmentMode)
            {
                throw illegalstateexception;
            }
        }

        TileEntityType<T> tileEntityType = builder.build(type);
        return tileEntityType;
    }
}
