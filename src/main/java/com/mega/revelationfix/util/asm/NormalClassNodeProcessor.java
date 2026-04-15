package com.mega.revelationfix.util.asm;

import com.mega.endinglib.coremod.forge.IClassProcessor;
import com.mega.endinglib.util.asm.injection.InjectionFinder;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class NormalClassNodeProcessor implements IClassProcessor {
    public static final String SELF_AT_ITF = "Lcom/mega/revelationfix/util/java/Self;";
    public static final String DYNAMIC_UTIL_CLASS = "com/mega/revelationfix/util/DynamicUtil";
    public static final String GR_ITEMS_CLASS = "com/mega/revelationfix/common/init/GRItems";
    public static NormalClassNodeProcessor INSTANCE = new NormalClassNodeProcessor();
    public static Logger LOGGER = RevelationFixMixinPlugin.LOGGER;

    public static boolean hasRuntimeVisibleAnnotation(FieldNode field, String annotationDescriptor) {
        if (field.visibleAnnotations == null) {
            return false;
        }

        for (AnnotationNode ann : field.visibleAnnotations) {
            if (annotationDescriptor.equals(ann.desc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type type, AtomicBoolean atomicBoolean) {
        if (phase == ILaunchPluginService.Phase.BEFORE) {
            String name = classNode.name;
            if (name.equals(GR_ITEMS_CLASS)) {
                classNode.methods.forEach(methodNode -> {
                    if ("init".equals(methodNode.name) && "(Lnet/minecraftforge/registries/DeferredRegister;)V".equals(methodNode.desc)) {
                        InsnList insnNodes = new InsnList();
                        classNode.fields.forEach(fieldNode -> {
                            if (fieldNode.desc.equals("Lnet/minecraftforge/registries/RegistryObject;")
                                    && (fieldNode.visibleAnnotations == null || fieldNode.visibleAnnotations.isEmpty() || !hasRuntimeVisibleAnnotation(fieldNode, SELF_AT_ITF))) {
                                insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, GR_ITEMS_CLASS, "CREATIVE_TAB_ITEMS", "Ljava/util/List;"));
                                insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, GR_ITEMS_CLASS, fieldNode.name, fieldNode.desc));
                                insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true));
                                insnNodes.add(new InsnNode(Opcodes.POP));
                            }
                        });
                        InjectionFinder.injectTail(methodNode, insnNodes);
                        atomicBoolean.set(true);
                    }
                });
            } else if (name.equals("com/mega/revelationfix/common/capability/entity/MegaCapability")) {
                classNode.methods.forEach(methodNode -> {
                    if ("getData".equals(methodNode.name) && "()F".equals(methodNode.desc)) {
                        methodNode.instructions.clear();
                        InsnList insnNodes = new InsnList();
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, "com/mega/revelationfix/common/capability/entity/MegaCapability", "dataManager", "Lcom/mega/endinglib/api/capability/SynchedCapabilityData;"));
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, "com/mega/revelationfix/common/capability/entity/MegaCapability", "DATA", "Lcom/mega/endinglib/api/capability/CapabilityEntityData;"));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/mega/endinglib/api/capability/SynchedCapabilityData", "getValue", "(Lcom/mega/endinglib/api/capability/CapabilityEntityData;)Ljava/lang/Object;", false));
                        insnNodes.add(new TypeInsnNode(Opcodes.CHECKCAST, "com/mega/revelationfix/common/data/FloatWrapped"));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/mega/revelationfix/common/data/FloatWrapped", "getValue", "()F", false));
                        insnNodes.add(new InsnNode(Opcodes.FRETURN));
                        methodNode.instructions.add(insnNodes);
                        atomicBoolean.set(true);
                    } else if ("setData".equals(methodNode.name) && "(F)V".equals(methodNode.desc)) {
                        methodNode.instructions.clear();
                        InsnList insnNodes = new InsnList();
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, "com/mega/revelationfix/common/capability/entity/MegaCapability", "dataManager", "Lcom/mega/endinglib/api/capability/SynchedCapabilityData;"));
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, "com/mega/revelationfix/common/capability/entity/MegaCapability", "DATA", "Lcom/mega/endinglib/api/capability/CapabilityEntityData;"));
                        insnNodes.add(new TypeInsnNode(Opcodes.NEW, "com/mega/revelationfix/common/data/FloatWrapped"));
                        insnNodes.add(new InsnNode(Opcodes.DUP));
                        insnNodes.add(new VarInsnNode(Opcodes.FLOAD, 1));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/mega/revelationfix/common/data/FloatWrapped", "<init>", "(F)V", false));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/mega/endinglib/api/capability/SynchedCapabilityData", "setValue", "(Lcom/mega/endinglib/api/capability/CapabilityEntityData;Ljava/lang/Object;)V", false));
                        insnNodes.add(new InsnNode(Opcodes.RETURN));
                        methodNode.instructions.add(insnNodes);
                        atomicBoolean.set(true);
                    }
                });
            } else if (name.equals("com/mega/revelationfix/Hi")) {
                classNode.methods.forEach(methodNode -> {
                    if ("youWillKnowIt".equals(methodNode.name) && "()V".equals(methodNode.desc)) {
                        methodNode.instructions.clear();
                        InsnList insnNodes = new InsnList();
                        insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                        insnNodes.add(new LdcInsnNode("\u4f60\u662f\u54ea\u4f4d\uff1f"));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
                        insnNodes.add(new InsnNode(Opcodes.RETURN));
                        methodNode.instructions.add(insnNodes);
                        atomicBoolean.set(true);
                    }
                });
            }
        }
    }
}
