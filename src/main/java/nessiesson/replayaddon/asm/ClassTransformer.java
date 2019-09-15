package nessiesson.replayaddon.asm;

import nessiesson.replayaddon.Reference;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.function.Consumer;

public class ClassTransformer implements IClassTransformer {
	private static final String ASM_HOOKS = NewMethods.class.getCanonicalName().replace('.', '/');
	public static Logger logger = LogManager.getLogger(Reference.NAME);

	@Override
	public byte[] transform(String name, String transformedName, byte[] clazz) {
		//logger.info(transformedName);
		switch (transformedName) {
			case "net.minecraft.client.renderer.RenderGlobal":
				return transformMethods(clazz, this::patchRenderEntities);
			case "net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher":
				return transformMethods(clazz, this::patchRender);
			case "net.minecraft.client.multiplayer.WorldClient":
				return transformMethods(clazz, this::patchSetWorldTime);
			case "net.minecraft.client.network.NetHandlerPlayClient":
				return transformMethods(clazz, this::patchSetWorldTimeCall);
			case "net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer":
				return transformMethods(clazz, this::patchRenderBeamSegment);
			case "net.minecraft.world.World":
				return transformMethods(clazz, this::patchGetRainStrength);
			default:
				return clazz;
		}
	}

	private byte[] transformMethods(byte[] bytes, Consumer<MethodNode> transformer) {
		ClassReader reader = new ClassReader(bytes);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);

		node.methods.forEach(transformer);

		ClassWriter writer = new ClassWriter(0);
		node.accept(writer);
		return writer.toByteArray();
	}

	private void patchRenderEntities(MethodNode method) {
		if (method.name.equals(MCPNames.method("func_180446_a")) && method.desc.equals("(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V")) {
			for (int i = 0; i < method.instructions.size(); i++) {
				AbstractInsnNode ain = method.instructions.get(i);

				if (ain instanceof MethodInsnNode && ain.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) ain).name.equals(MCPNames.method("func_178635_a")) && ((MethodInsnNode) ain).desc.equals("(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z")) {
					InsnList insert = new InsnList();
					insert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "overrideShouldRender", "(Lnet/minecraft/client/renderer/entity/RenderManager;Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z", false));
					method.instructions.insertBefore(ain, insert);
					method.instructions.remove(ain);
				}
			}
		}
	}

	private void patchRender(MethodNode method) {
		if (method.name.equals(MCPNames.method("func_180546_a")) && method.desc.equals("(Lnet/minecraft/tileentity/TileEntity;FI)V")) {
			for (int i = 0; i < method.instructions.size(); i++) {
				AbstractInsnNode ain = method.instructions.get(i);

				if (ain instanceof MethodInsnNode && ain.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) ain).name.equals(MCPNames.method("func_145835_a")) && ((MethodInsnNode) ain).desc.equals("(DDD)D")) {
					InsnList insert = new InsnList();
					insert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "overrideGetDistanceSq", "(Lnet/minecraft/tileentity/TileEntity;DDD)D", false));
					method.instructions.insert(ain, insert);
					method.instructions.remove(ain);
				}
			}
		}
	}

	private void patchSetWorldTime(MethodNode method) {
		if (method.name.equals(MCPNames.method("func_72877_b")) && method.desc.equals("(J)V")) {
			InsnList insert = new InsnList();
			insert.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insert.add(new VarInsnNode(Opcodes.LLOAD, 1));
			insert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "overrideSetWorldTime", "(Lnet/minecraft/client/multiplayer/WorldClient;J)V", false));
			insert.add(new InsnNode(Opcodes.RETURN));
			insert.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			method.instructions.insert(method.instructions.getFirst(), insert);
		}
	}

	private void patchSetWorldTimeCall(MethodNode method) {
		if (method.name.equals(MCPNames.method("func_147285_a")) && method.desc.equals("(Lnet/minecraft/network/play/server/SPacketTimeUpdate;)V")) {
			for (int i = 0; i < method.instructions.size(); i++) {
				AbstractInsnNode ain = method.instructions.get(i);

				if (ain instanceof MethodInsnNode && ain.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) ain).name.equals(MCPNames.method("func_72877_b")) && ((MethodInsnNode) ain).desc.equals("(J)V")) {
					InsnList insert = new InsnList();
					insert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "noopSetWorldTimeCall", "(J)V", false));
					method.instructions.insert(ain, insert);
					method.instructions.remove(ain);
				}
			}
		}
	}

	private void patchRenderBeamSegment(MethodNode method) {
		if (method.name.equals(MCPNames.method("func_188205_a")) && method.desc.equals("(DDDDDDII[FDD)V")) {
			InsnList insert = new InsnList();
			LabelNode label = new LabelNode();
			insert.add(new FieldInsnNode(Opcodes.GETSTATIC, "nessiesson/replayaddon/Configuration", "renderBeaconBeam", "Z"));
			insert.add(new JumpInsnNode(Opcodes.IFNE, label));
			insert.add(new InsnNode(Opcodes.RETURN));
			insert.add(label);
			insert.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			method.instructions.insert(method.instructions.getFirst(), insert);
		}
	}

	private void patchGetRainStrength(MethodNode method) {
		if (method.name.equals(MCPNames.method("func_72867_j")) && method.desc.equals("(F)F")) {
			InsnList insert = new InsnList();
			LabelNode label = new LabelNode();
			insert.add(new FieldInsnNode(Opcodes.GETSTATIC, "nessiesson/replayaddon/Configuration", "noRain", "Z"));
			insert.add(new JumpInsnNode(Opcodes.IFNE, label));
			insert.add(new InsnNode(Opcodes.FCONST_0));
			insert.add(new InsnNode(Opcodes.FRETURN));
			insert.add(label);
			insert.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			method.instructions.insert(method.instructions.getFirst(), insert);
		}
	}
}
