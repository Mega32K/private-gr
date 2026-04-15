package com.mega.revelationfix.util.asm;

import com.mega.endinglib.coremod.forge.IClassProcessor;
import com.mega.endinglib.util.asm.injection.InjectionFinder;
import com.mega.revelationfix.util.MCMapping;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class CompatClassNodeProcessor implements IClassProcessor {
    private static final String LIVING_ENTITY_CLASS = "net/minecraft/world/entity/LivingEntity";
    public static IClassProcessor INSTANCE = new CompatClassNodeProcessor();
    public static Logger LOGGER = RevelationFixMixinPlugin.LOGGER;

    @Override
    public void processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type type, AtomicBoolean modified) {
        if (phase == ILaunchPluginService.Phase.BEFORE) {
            String name = classNode.name;
            if (LIVING_ENTITY_CLASS.equals(name)) {
                classNode.methods.forEach(eachMethod -> {
                    if (MCMapping.LivingEntity$METHOD$heal.equalsMethodNode(eachMethod)) {
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        list.add(new VarInsnNode(Opcodes.FLOAD, 1));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mega/revelationfix/util/entity/EntityRedirectUtils", "quietusHealingAbility", "(Lnet/minecraft/world/entity/LivingEntity;F)F", false));
                        list.add(new VarInsnNode(Opcodes.FSTORE, 1));
                        InjectionFinder.injectHead(eachMethod, list);
                        modified.set(true);
                    } else if (MCMapping.LivingEntity$METHOD$getDamageAfterArmorAbsorb.equalsMethodNode(eachMethod)) {
                        eachMethod.instructions.forEach(abstractInsnNode -> {
                            if (abstractInsnNode instanceof MethodInsnNode mNode && MCMapping.CombatRules$METHOD$getDamageAfterAbsorb.equalsMethodNode(mNode)) {
                                InsnList list = new InsnList();
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new VarInsnNode(Opcodes.FLOAD, 2));
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new FieldInsnNode(Opcodes.GETFIELD, LIVING_ENTITY_CLASS, "revelationfix$runtimeDamageSource", "Lnet/minecraft/world/damagesource/DamageSource;"));

                                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mega/revelationfix/util/entity/EntityRedirectUtils", "quietusArmorAbility", "(FLnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;)F", false));
                                eachMethod.instructions.insert(mNode, list);
                                modified.set(true);
                            }
                        });
                    } else if (MCMapping.LivingEntity$METHOD$getDamageAfterMagicAbsorb.equalsMethodNode(eachMethod)) {
                        eachMethod.instructions.forEach(abstractInsnNode -> {
                            if (abstractInsnNode instanceof MethodInsnNode mNode && MCMapping.CombatRules$METHOD$getDamageAfterMagicAbsorb.equalsMethodNode(mNode)) {
                                InsnList list = new InsnList();
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new VarInsnNode(Opcodes.FLOAD, 2));
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new FieldInsnNode(Opcodes.GETFIELD, LIVING_ENTITY_CLASS, "revelationfix$runtimeDamageSource", "Lnet/minecraft/world/damagesource/DamageSource;"));
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new FieldInsnNode(Opcodes.GETFIELD, LIVING_ENTITY_CLASS, "revelationfix$runtimeCorrectDamageBeforeResistance", "Lit/unimi/dsi/fastutil/Pair;"));

                                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mega/revelationfix/util/entity/EntityRedirectUtils", "quietusEnchantmentAbility", "(FLnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;Lit/unimi/dsi/fastutil/Pair;)F", false));
                                eachMethod.instructions.insert(mNode, list);
                                modified.set(true);
                            }
                        });
                    }
                });
            }
            if (name.endsWith("CommonConfigMixin") && classNode.methods != null) {
                final MethodNode[] toRemove = new MethodNode[1];
                containsAnnotation(classNode, "Lorg/spongepowered/asm/mixin/Mixin;", (cn, an) -> {
                    if (containsClassArgs(an, "com.mega.revelationfix.common.config.CommonConfig")) {
                        cn.methods.forEach(methodNode -> {
                            if (methodNode.name.toLowerCase().contains("inwhitelist") && methodNode.desc.startsWith("(Lnet/minecraft/world/item/Item;")) {
                                toRemove[0] = methodNode;
                            }
                        });
                    }
                });
                if (toRemove[0] != null) {
                    classNode.methods.remove(toRemove[0]);
                    modified.set(true);
                }
            }
        }
    }

    static void containsAnnotation(ClassNode classNode, String desc, BiConsumer<ClassNode, AnnotationNode> consumer) {
        AnnotationNode node = null;
        if (classNode.invisibleAnnotations != null) {
            for (AnnotationNode entry : classNode.invisibleAnnotations) {
                if (entry.desc.equals(desc)) {
                    node = entry;
                }
            }
        }
        if (classNode.visibleAnnotations != null) {
            for (AnnotationNode entry : classNode.visibleAnnotations) {
                if (entry.desc.equals(desc)) {
                    node = entry;
                }
            }
        }
        if (node != null) {
            consumer.accept(classNode, node);
        }
    }

    public static boolean containsClassArgs(AnnotationNode an, String className) {
        if (an == null || an.values == null || className == null || className.isEmpty()) {
            return false;
        }
        String internal = className.replace('.', '/');
        String desc = "L" + internal + ";";
        for (int i = 1; i < an.values.size(); i += 2) {
            if (matchClassArgValue(an.values.get(i), internal, desc)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchClassArgValue(Object value, String internal, String desc) {
        if (value == null) {
            return false;
        }
        if (value instanceof Type typeValue) {
            return internal.equals(typeValue.getInternalName()) || desc.equals(typeValue.getDescriptor());
        }
        if (value instanceof String stringValue) {
            return stringValue.contains(internal) || stringValue.contains(desc);
        }
        if (value instanceof String[] stringArray) {
            for (String stringValue : stringArray) {
                if (stringValue != null && (stringValue.contains(internal) || stringValue.contains(desc))) {
                    return true;
                }
            }
            return false;
        }
        if (value instanceof List<?> listValue) {
            for (Object element : listValue) {
                if (matchClassArgValue(element, internal, desc)) {
                    return true;
                }
            }
        }
        return false;
    }
}
