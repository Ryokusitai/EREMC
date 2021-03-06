package eremc.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.*;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import eremc.asm.EREMCCorePlugin;
import eremc.asm.MethodNameList;

public class PacketHandlerTransformer implements IClassTransformer, Opcodes
{
	private static final String TARGETCLASSNAME = "moze_intel.projecte.network.PacketHandler";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!TARGETCLASSNAME.equals(transformedName)) { return basicClass;}

		try {
			EREMCCorePlugin.logger.info("---------------------Start PacketHandler Transform----------------------");
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cr.accept(new CustomVisitor(name, cw), 8);
			basicClass = cw.toByteArray();
			EREMCCorePlugin.logger.info("--------------------Finish PacketHandler Transform----------------------");
		} catch (Exception e) {
			throw new RuntimeException("failed : PacketHandlerTransformer Loading", e);
		}
		return basicClass;
	}

	class CustomVisitor extends ClassVisitor
	{
		String owner;
		public CustomVisitor(String owner, ClassVisitor cv)
		{
			super(Opcodes.ASM4, cv);
			this.owner = owner;
		}

		static final String targetMethodName = "sendFragmentedEmcPacket";
		static final String targetMethodName2 = "sendFragmentedEmcPacketToAll";

		/**
		 * ここでどのメソッドかを判断してそれぞれの書き換え処理に飛ばしている
		 * とりあえず考える手間を省くため全書き換え
		 */
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [sendFragmentedEmcPacket]");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideSendFragmentedEmcPacket(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);
			}
			if (targetMethodName2.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [sendFragmentedEmcPacketToAll]");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideSendFragmentedEmcPacketToAll(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);
			}

			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		private void overrideSendFragmentedEmcPacket(MethodVisitor mv)
		{
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(48, l0);
			mv.visitTypeInsn(NEW, "java/util/ArrayList");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
			mv.visitVarInsn(ASTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(49, l1);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 2);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(50, l2);
			mv.visitTypeInsn(NEW, "java/util/ArrayList");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
			mv.visitVarInsn(ASTORE, 3);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(52, l3);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/emc/EMCMapper", "emc", "Ljava/util/LinkedHashMap;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashMap", "entrySet", "()Ljava/util/Set;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;", true);
			mv.visitVarInsn(ASTORE, 5);
			Label l4 = new Label();
			mv.visitJumpInsn(GOTO, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"net/minecraft/entity/player/EntityPlayerMP", "java/util/ArrayList", Opcodes.INTEGER, "java/util/ArrayList", Opcodes.TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/util/Map$Entry");
			mv.visitVarInsn(ASTORE, 4);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(54, l6);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getKey", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "moze_intel/projecte/emc/SimpleStack");
			mv.visitVarInsn(ASTORE, 6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(56, l7);
			mv.visitVarInsn(ALOAD, 6);
			Label l8 = new Label();
			mv.visitJumpInsn(IFNONNULL, l8);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(58, l9);
			mv.visitJumpInsn(GOTO, l4);
			mv.visitLabel(l8);
			mv.visitLineNumber(61, l8);
			mv.visitFrame(Opcodes.F_FULL, 7, new Object[] {"net/minecraft/entity/player/EntityPlayerMP", "java/util/ArrayList", Opcodes.INTEGER, "java/util/ArrayList", "java/util/Map$Entry", "java/util/Iterator", "moze_intel/projecte/emc/SimpleStack"}, 0, new Object[] {});
			mv.visitInsn(ICONST_4);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Integer");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "id", "I");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "qnty", "I");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_2);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "damage", "I");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_3);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
			mv.visitInsn(AASTORE);
			mv.visitVarInsn(ASTORE, 7);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(62, l10);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
			mv.visitInsn(POP);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(63, l11);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "tag", "Lnet/minecraft/nbt/NBTTagCompound;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
			mv.visitInsn(POP);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(65, l12);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I", false);
			mv.visitIntInsn(SIPUSH, 256);
			mv.visitJumpInsn(IF_ICMPLT, l4);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(67, l13);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/network/packets/ClientSyncEmcPKT");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "<init>", "(ILjava/util/ArrayList;Ljava/util/ArrayList;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/network/PacketHandler", "sendTo", "(Lcpw/mods/fml/common/network/simpleimpl/IMessage;Lnet/minecraft/entity/player/EntityPlayerMP;)V", false);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLineNumber(68, l14);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "clear", "()V", false);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(69, l15);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "clear", "()V", false);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(70, l16);
			mv.visitIincInsn(2, 1);
			mv.visitLabel(l4);
			mv.visitLineNumber(52, l4);
			mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"net/minecraft/entity/player/EntityPlayerMP", "java/util/ArrayList", Opcodes.INTEGER, "java/util/ArrayList", Opcodes.TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l5);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(74, l17);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I", false);
			Label l18 = new Label();
			mv.visitJumpInsn(IFLE, l18);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitLineNumber(76, l19);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/network/packets/ClientSyncEmcPKT");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_M1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "<init>", "(ILjava/util/ArrayList;Ljava/util/ArrayList;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/network/PacketHandler", "sendTo", "(Lcpw/mods/fml/common/network/simpleimpl/IMessage;Lnet/minecraft/entity/player/EntityPlayerMP;)V", false);
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitLineNumber(77, l20);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "clear", "()V", false);
			Label l21 = new Label();
			mv.visitLabel(l21);
			mv.visitLineNumber(78, l21);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "clear", "()V", false);
			Label l22 = new Label();
			mv.visitLabel(l22);
			mv.visitLineNumber(79, l22);
			mv.visitIincInsn(2, 1);
			mv.visitLabel(l18);
			mv.visitLineNumber(82, l18);
			mv.visitFrame(Opcodes.F_FULL, 4, new Object[] {"net/minecraft/entity/player/EntityPlayerMP", "java/util/ArrayList", Opcodes.INTEGER, "java/util/ArrayList"}, 0, new Object[] {});
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Sent EMC data packets to: ");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayerMP", MethodNameList.getName("getCommandSenderName"), "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/utils/PELogger", "logInfo", "(Ljava/lang/String;)V", false);
			Label l23 = new Label();
			mv.visitLabel(l23);
			mv.visitLineNumber(83, l23);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Total packets: ");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/utils/PELogger", "logDebug", "(Ljava/lang/String;)V", false);
			Label l24 = new Label();
			mv.visitLabel(l24);
			mv.visitLineNumber(84, l24);
			mv.visitInsn(RETURN);
			Label l25 = new Label();
			mv.visitLabel(l25);
			mv.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayerMP;", null, l0, l25, 0);
			mv.visitLocalVariable("list", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<[Ljava/lang/Integer;>;", l1, l25, 1);
			mv.visitLocalVariable("counter", "I", null, l2, l25, 2);
			mv.visitLocalVariable("tag", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Lnet/minecraft/nbt/NBTTagCompound;>;", l3, l25, 3);
			mv.visitLocalVariable("entry", "Ljava/util/Map$Entry;", "Ljava/util/Map$Entry<Lmoze_intel/projecte/emc/SimpleStack;Ljava/lang/Integer;>;", l6, l4, 4);
			mv.visitLocalVariable("stack", "Lmoze_intel/projecte/emc/SimpleStack;", null, l7, l4, 6);
			mv.visitLocalVariable("data", "[Ljava/lang/Integer;", null, l10, l4, 7);
			mv.visitMaxs(5, 8);
			mv.visitEnd();
		}

		private void overrideSendFragmentedEmcPacketToAll(MethodVisitor mv)
		{
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(88, l0);
			mv.visitTypeInsn(NEW, "java/util/ArrayList");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
			mv.visitVarInsn(ASTORE, 0);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(89, l1);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(90, l2);
			mv.visitTypeInsn(NEW, "java/util/ArrayList");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(92, l3);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/emc/EMCMapper", "emc", "Ljava/util/LinkedHashMap;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashMap", "entrySet", "()Ljava/util/Set;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;", true);
			mv.visitVarInsn(ASTORE, 4);
			Label l4 = new Label();
			mv.visitJumpInsn(GOTO, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_FULL, 5, new Object[] {"java/util/ArrayList", Opcodes.INTEGER, "java/util/ArrayList", Opcodes.TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/util/Map$Entry");
			mv.visitVarInsn(ASTORE, 3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(94, l6);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getKey", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "moze_intel/projecte/emc/SimpleStack");
			mv.visitVarInsn(ASTORE, 5);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(96, l7);
			mv.visitVarInsn(ALOAD, 5);
			Label l8 = new Label();
			mv.visitJumpInsn(IFNONNULL, l8);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(98, l9);
			mv.visitJumpInsn(GOTO, l4);
			mv.visitLabel(l8);
			mv.visitLineNumber(101, l8);
			mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"java/util/ArrayList", Opcodes.INTEGER, "java/util/ArrayList", "java/util/Map$Entry", "java/util/Iterator", "moze_intel/projecte/emc/SimpleStack"}, 0, new Object[] {});
			mv.visitInsn(ICONST_4);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Integer");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "id", "I");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "qnty", "I");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_2);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "damage", "I");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_3);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
			mv.visitInsn(AASTORE);
			mv.visitVarInsn(ASTORE, 6);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(102, l10);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
			mv.visitInsn(POP);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(103, l11);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "tag", "Lnet/minecraft/nbt/NBTTagCompound;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
			mv.visitInsn(POP);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(105, l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I", false);
			mv.visitIntInsn(SIPUSH, 256);
			mv.visitJumpInsn(IF_ICMPLT, l4);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(107, l13);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/network/packets/ClientSyncEmcPKT");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "<init>", "(ILjava/util/ArrayList;Ljava/util/ArrayList;)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/network/PacketHandler", "sendToAll", "(Lcpw/mods/fml/common/network/simpleimpl/IMessage;)V", false);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLineNumber(108, l14);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "clear", "()V", false);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(109, l15);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "clear", "()V", false);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(110, l16);
			mv.visitIincInsn(1, 1);
			mv.visitLabel(l4);
			mv.visitLineNumber(92, l4);
			mv.visitFrame(Opcodes.F_FULL, 5, new Object[] {"java/util/ArrayList", Opcodes.INTEGER, "java/util/ArrayList", Opcodes.TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l5);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(114, l17);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I", false);
			Label l18 = new Label();
			mv.visitJumpInsn(IFLE, l18);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitLineNumber(116, l19);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/network/packets/ClientSyncEmcPKT");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_M1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "<init>", "(ILjava/util/ArrayList;Ljava/util/ArrayList;)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/network/PacketHandler", "sendToAll", "(Lcpw/mods/fml/common/network/simpleimpl/IMessage;)V", false);
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitLineNumber(117, l20);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "clear", "()V", false);
			Label l21 = new Label();
			mv.visitLabel(l21);
			mv.visitLineNumber(118, l21);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "clear", "()V", false);
			Label l22 = new Label();
			mv.visitLabel(l22);
			mv.visitLineNumber(119, l22);
			mv.visitIincInsn(1, 1);
			mv.visitLabel(l18);
			mv.visitLineNumber(122, l18);
			mv.visitFrame(Opcodes.F_FULL, 3, new Object[] {"java/util/ArrayList", Opcodes.INTEGER, "java/util/ArrayList"}, 0, new Object[] {});
			mv.visitLdcInsn("Sent EMC data packets to all players.");
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/utils/PELogger", "logInfo", "(Ljava/lang/String;)V", false);
			Label l23 = new Label();
			mv.visitLabel(l23);
			mv.visitLineNumber(123, l23);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Total packets per player: ");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/utils/PELogger", "logDebug", "(Ljava/lang/String;)V", false);
			Label l24 = new Label();
			mv.visitLabel(l24);
			mv.visitLineNumber(124, l24);
			mv.visitInsn(RETURN);
			Label l25 = new Label();
			mv.visitLabel(l25);
			mv.visitLocalVariable("list", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<[Ljava/lang/Integer;>;", l1, l25, 0);
			mv.visitLocalVariable("counter", "I", null, l2, l25, 1);
			mv.visitLocalVariable("tag", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Lnet/minecraft/nbt/NBTTagCompound;>;", l3, l25, 2);
			mv.visitLocalVariable("entry", "Ljava/util/Map$Entry;", "Ljava/util/Map$Entry<Lmoze_intel/projecte/emc/SimpleStack;Ljava/lang/Integer;>;", l6, l4, 3);
			mv.visitLocalVariable("stack", "Lmoze_intel/projecte/emc/SimpleStack;", null, l7, l4, 5);
			mv.visitLocalVariable("data", "[Ljava/lang/Integer;", null, l10, l4, 6);
			mv.visitMaxs(5, 7);
			mv.visitEnd();
		}
	}

}
