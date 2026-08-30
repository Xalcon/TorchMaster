package net.xalcon.torchmaster.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.client.gui.EntityBlockingLightSettingsScreen;

public class ClientProxy {
    // Hacky helper method to open screens on the client side without implementing a menu or custom network packets
    // this wrapper class allows the function to be used in code paths that can run on the server as well
    // The function should just always be guarded with an isClientSide check - which is why this is so hacky.
    public static void openClientSideOnlyScreen(ModClientSideOnlyScreen screen, Level level, BlockPos pos, BlockState state) {
        if(screen != ModClientSideOnlyScreen.EntityBlockingLightScreen) return;

        if(state.getBlock() instanceof EntityBlockingLightBlock block)
        {
            var lightType = block.getLightType();
            int range = lightType == LightType.DreadLamp ? Services.PLATFORM.getConfig().getDreadLampRadius()
                    : lightType == LightType.MegaTorch ? Services.PLATFORM.getConfig().getMegaTorchRadius()
                    : 0;

            Minecraft.getInstance().setScreen(new EntityBlockingLightSettingsScreen(pos, range));
        }
    }
}

