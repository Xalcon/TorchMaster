package net.xalcon.torchasm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.xalcon.torchmaster.TorchMasterMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

public class TorchTransformer implements IClassTransformer
{
    private static Logger log = LogManager.getLogger("torchmaster_core");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if("net.minecraft.world.WorldServer".equals(name))
        {
            return patchWorldServer(basicClass);
        }
        return basicClass;
    }

    /**
     * Torchmasters MegaTorch uses the CheckSpawn event to control spawning of entities near torches.
     * The problem is, the CheckSpawn event is used by the WorldSpawner as well as by the vanilla
     * MobSpawner. If the MegaTorch just blocks the event, mob spawners wouldnt be able to work
     * inside the mega torch radius. This is bad for 2 reasons:
     * 1.) Players cant use MobSpawners in their base/close to a mega torch
     * 2.) The torch would trivialize dungeons that use mobspawners to spawn monster
     * To avoid this, we need some way to detect if the spawn is an actual WorldSpawner spawn or an
     * artificial one.
     * This patch modifies the WorldServer.tick() method to set a field to false at the start of the tick() method
     * and then back to true at the end.
     * WorldSpawn happens inside the tick() method. TileEntities tick outside of it.
     * This allows us to simply check the field if we are in the correct runstate
     * @param basicClass the bytes of the "unmodified" class
     */
    private static byte[] patchWorldServer(byte[] basicClass)
    {
        ClassReader cr = new ClassReader(basicClass);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        TorchCoreClassVisitor cv = new TorchCoreClassVisitor(Opcodes.ASM5, cw, "tick", "()V", TorchTransformer::sanityCheckCallback);
        cr.accept(cv, ClassReader.EXPAND_FRAMES); // this will apply our class+method visitor

        // do the sanity check. We dont do anything to class if the patch wasnt successful
        if(patchCount != 2)
        {
            log.fatal("[Torchmaster] Something went wrong while patching WorldServer.tick() method! Expected 2 patches, did " + patchCount + "; reverting changes!");
            log.fatal("[Torchmaster] MegaTorch will not be able to distinguish between world and mobspawner spawns! Report this bug to the author!");
            return basicClass;
        }

        // we are good, lets ship it
        byte[] bytes = cw.toByteArray();
        log.info("[Torchmaster] tick state hook successfully installed. Yey!");
        TorchMasterMod.isWorldHookInstalled = true;
        return bytes;
    }

    private static int patchCount = 0;

    /**
     * This method is calley each time the FieldToggleMethodAdapter adds a patch
     */
    private static void sanityCheckCallback()
    {
        patchCount++;
    }

    /**
     * Custom ClassVisitor to allow our custom MethodVisitor to work on certain Methods (currently only WorldServer.tick())
     */
    private static class TorchCoreClassVisitor extends ClassVisitor
    {
        private final String methodName;
        private final String methodDesc;
        private final Runnable sanityCheckCallback;

        /**
         * Custom Class visitor constructor, duh.
         * @param api the API Version, 5 is fine
         * @param cv the class visitor (class writer)
         * @param methodName the method we want to patch with our custom method visitor
         * @param methodDesc the description of the method we want to patch
         * @param sanityCheckCallback this callback will be passed to the custom method visitor
         */
        TorchCoreClassVisitor(int api, ClassVisitor cv, String methodName, String methodDesc, Runnable sanityCheckCallback)
        {
            super(api, cv);
            this.methodName = methodName;
            this.methodDesc = methodDesc;
            this.sanityCheckCallback = sanityCheckCallback;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
        {
            MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
            // If the method, that passed to this ClassVisitor is the one that is going to be visited next
            // use our custom method visitor instead of the default one
            if(name.equals(this.methodName) && desc.equals(this.methodDesc))
                return new FieldToggleMethodAdapter(this.api, mv, access, name, desc, this.sanityCheckCallback);
            return mv;
        }
    }

    /**
     * Specialized MethodVisitor that is only applied to the WorldServer.tick() method
     * This MethodVisitor adds `TorchMasterMod.isNotInWorldTick = false;` at the start of the tick() method
     * and `TorchMasterMod.isNotInWorldTick = true` at the end.
     * This allows us to check if the CheckSpawn event is called from inside the tick() method
     * or from outside. Since the WorldSpawner only runs inside the WorldServer.tick(), this allows
     * us to make a really fast check if a given mob is spawned naturally or by a mobspawner.
     * The logic for isNotInWorldTick is also inverted (i.e. we are not using `isInWorldTick`) since this allows
     * the mega torch to still work even if the patch wasnt applied. The only thing is, it can no longer
     * distinguish between mobspawner spawns and worldspawns and just blocks everything.
     */
    private static class FieldToggleMethodAdapter extends AdviceAdapter
    {
        private final Runnable sanityCheckCallback;

        FieldToggleMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc, Runnable sanityCheckCallback)
        {
            super(api, mv, access, name, desc);
            this.sanityCheckCallback = sanityCheckCallback;
        }

        @Override
        protected void onMethodEnter()
        {
            // load 1 (true)
            this.mv.visitInsn(Opcodes.ICONST_0);
            // write loaded value to field
            this.mv.visitFieldInsn(Opcodes.PUTSTATIC, "net/xalcon/torchmaster/TorchMasterMod", "isNotInWorldTick", "Z");
            log.info("[Torchmaster] Applied patch to WorldServer.Tick() @ method enter");
            this.sanityCheckCallback.run();
        }

        @Override
        protected void onMethodExit(int opcode)
        {
            // load 0 (false)
            this.mv.visitInsn(Opcodes.ICONST_1);
            // write loaded value to field
            this.mv.visitFieldInsn(Opcodes.PUTSTATIC, "net/xalcon/torchmaster/TorchMasterMod", "isNotInWorldTick", "Z");
            log.info("[Torchmaster] Applied patch to WorldServer.Tick() @ method exit");
            this.sanityCheckCallback.run();
        }
    }
}
