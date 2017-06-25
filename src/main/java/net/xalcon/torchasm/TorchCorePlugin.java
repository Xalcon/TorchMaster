package net.xalcon.torchasm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

public class TorchCorePlugin implements IFMLLoadingPlugin
{
    public static boolean isTickHookInstalled;

    /**
     * Return a list of classes that implements the IClassTransformer interface
     *
     * @return a list of classes that implements the IClassTransformer interface
     */
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{TorchTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass()
    {
        return TorchDummyCore.class.getName();
    }

    @Nullable
    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
