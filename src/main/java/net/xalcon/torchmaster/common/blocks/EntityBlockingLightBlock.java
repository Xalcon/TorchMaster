package net.xalcon.torchmaster.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.network.ModMessageHandler;
import net.xalcon.torchmaster.common.network.volume.VolumeDisplayMessage;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityBlockingLightBlock extends Block
{
    protected static final VoxelShape SHAPE =  Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    private Function<BlockPos, String> keyFactory;
    private Function<BlockPos, IEntityBlockingLight> lightFactory;
    private float flameOffsetY;
    private final VoxelShape shape;
    private Supplier<Integer> range;

    public EntityBlockingLightBlock(Properties properties, Function<BlockPos, String> keyFactory, Function<BlockPos, IEntityBlockingLight> lightFactory, float flameOffsetY, VoxelShape shape, Supplier<Integer> rangeSupplier)
    {
        super(properties);
        this.keyFactory = keyFactory;
        this.lightFactory = lightFactory;
        this.flameOffsetY = flameOffsetY;
        this.shape = shape;
        this.range = rangeSupplier;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        return this.shape;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(!level.isClientSide())
        {
            var show = !player.isShiftKeyDown();
            var color = 0xA0FF20;

            var itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(itemStack.getItem() instanceof DyeItem item)
            {
                color = item.getDyeColor().getTextColor();
            }

            player.displayClientMessage(Component.translatable(show ? "torchmaster.torch_volume.on_show" : "torchmaster.torch_volume.on_hide"), true);

            var msg = VolumeDisplayMessage.create(pos, range.get(), color, show, show);
            ModMessageHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), msg);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, result);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random rand)
    {
        double d0 = (double)pos.getX() + 0.5f;
        double d1 = (double)pos.getY() + this.flameOffsetY;
        double d2 = (double)pos.getZ() + 0.5f;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, world, pos, oldState, moving);
        world.getCapability(ModCaps.TEB_REGISTRY)
            .ifPresent(reg ->
            {
                reg.registerLight(this.keyFactory.apply(pos), this.lightFactory.apply(pos));
            });
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        world.getCapability(ModCaps.TEB_REGISTRY)
            .ifPresent(reg ->
            {
                reg.unregisterLight(this.keyFactory.apply(pos));
            });
        super.onRemove(state, world, pos, oldState, moving);
    }
}
