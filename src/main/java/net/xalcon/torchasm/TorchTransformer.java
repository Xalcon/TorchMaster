package net.xalcon.torchasm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;

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
     * This patch modifies the WorldServer.tick() method to set a field to true at the start of the tick() method
     * and then back to false at the end.
     * WorldSpawn happens inside the tick() method. TileEntities tick outside of it.
     * This allows us to simply check the field if we are in the correct runstate
     * @param basicClass
     */
    private static byte[] patchWorldServer(byte[] basicClass)
    {
        ClassReader cr = new ClassReader(basicClass);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        ClassNode cn = new ClassNode(Opcodes.ASM5);
        cr.accept(cn, ClassReader.EXPAND_FRAMES); // Expand frames if the are compressed

        // Search for the tick() method
        MethodNode method = cn.methods.stream().filter(m -> "tick".equals(m.name) && "()V".equals(m.desc)).findFirst().orElse(null);

        if(method == null)
        {
            log.fatal("Torchmaster was unable to find the WorldServer.tick() method! Block mob spawns will not work!");
            return basicClass;
        }

        TorchCoreClassVisitor cv = new TorchCoreClassVisitor(Opcodes.ASM5, cw, method);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);

        byte[] bytes = cw.toByteArray();
        dump("d:\\WorldServer.class", bytes);
        return bytes;
    }

    private static void dump(String path, byte[] data)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(data);
            fos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static class TorchCoreClassVisitor extends ClassVisitor
    {
        private final MethodNode mn;

        TorchCoreClassVisitor(int api, ClassVisitor cv, MethodNode mn)
        {
            super(api, cv);
            this.mn = mn;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
        {
            MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
            if(name.equals(mn.name) && desc.equals(mn.desc))
                return new FieldToggleMethodAdapter(this.api, mv, access, name, desc);
            return mv;
        }
    }

    private static class FieldToggleMethodAdapter extends AdviceAdapter
    {
        FieldToggleMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc)
        {
            super(api, mv, access, name, desc);
        }

        @Override
        protected void onMethodEnter()
        {
            // load 1 (true)
            this.mv.visitInsn(Opcodes.ICONST_1);
            // write loaded value to field
            this.mv.visitFieldInsn(Opcodes.PUTSTATIC, "net/xalcon/torchmaster/TorchMasterMod", "isInWorldTick", "Z");

        }

        @Override
        protected void onMethodExit(int opcode)
        {
            // load 0 (false)
            this.mv.visitInsn(Opcodes.ICONST_0);
            // write loaded value to field
            this.mv.visitFieldInsn(Opcodes.PUTSTATIC, "net/xalcon/torchmaster/TorchMasterMod", "isInWorldTick", "Z");
        }
    }
}
