package eremc.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import eremc.asm.EREMCCorePlugin;

public class ClientSyncEmcPKTTransformer implements IClassTransformer, Opcodes
{
	private static final String TARGETCLASSNAME = "moze_intel.projecte.network.packets.ClientSyncEmcPKT";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!TARGETCLASSNAME.equals(transformedName)) { return basicClass;}

		try {
			EREMCCorePlugin.logger.info("---------------------Start ClientSyncEmcPKT Transform----------------------");
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cr.accept(new CustomVisitor(name, cw, transformedName), 8);
			basicClass = cw.toByteArray();
			EREMCCorePlugin.logger.info("--------------------Finish ClientSyncEmcPKT Transform----------------------");
		} catch (Exception e) {
			throw new RuntimeException("failed : TransformerClientSyncEmcPKT Loading", e);
		}
		return basicClass;
	}

	class CustomVisitor extends ClassVisitor
	{
		String owner;
		String transformedName;
		public CustomVisitor(String owner, ClassVisitor cv, String transformedName)
		{
			super(Opcodes.ASM4, cv);
			this.owner = owner;
			this.transformedName = transformedName;
		}

		static final String targetMethodName = "<init>";
		static final String targetMethodName2 = "onMessage";
		static final String targetMethodName3 = "fromBytes";
		static final String targetMethodName4 = "toBytes";
		boolean finishOverride;
		/**
		 * ここでどのメソッドかを判断してそれぞれの書き換え処理に飛ばしている
		 */
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			// コンストラクタを追加するだけ
			if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [<init>]");
				createConstructor();
			}
			// 強引
			if (targetMethodName2.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				if (!finishOverride) {
					finishOverride = true;
					EREMCCorePlugin.logger.info("Transform method [onMessage]");
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					overrideOnMessage(mv);
					return super.visitMethod(access, name + "Old", desc, signature, exceptions);

				}
			}
			if (targetMethodName3.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [fromBytes]");
				//return new CustomMethodVisitor(this.api, super.visitMethod(access, name, desc, signature, exceptions));
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideFromBytes(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);
			}
			if (targetMethodName4.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [toBytes]");
				//return new CustomMethodVisitor(this.api, super.visitMethod(access, name, desc, signature, exceptions));
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideToBytes(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);
			}

			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		int cnt = 0;
		/**
		 * フィールドの追加
		 * private Object[] tag;
		 */
		@Override
		public FieldVisitor visitField(int access, String name, String desc,
	            String signature, Object value) {
			if (cnt == 0) {
				FieldVisitor fv = super.visitField(ACC_PRIVATE, "tag", "[Ljava/lang/Object;", null, null);
				fv.visitEnd();
				cnt++;
			}

			return super.visitField(access, name, desc, signature, value);
	    }

		/**
		 * public ClientSyncEmcPKT(int packetNum, ArrayList<Integer[]> arrayList, ArrayList<NBTTagCompound> tagList)
		 * を新たに作成
		 */
		boolean isCreate;	// 作成済みかどうか
		public void createConstructor() {
			if (isCreate)	{return;}

			isCreate = true;
			MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>", "(ILjava/util/ArrayList;Ljava/util/ArrayList;)V", "(ILjava/util/ArrayList<[Ljava/lang/Integer;>;Ljava/util/ArrayList<Lnet/minecraft/nbt/NBTTagCompound;>;)V", null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(35, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "<init>", "(ILjava/util/ArrayList;)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(36, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "toArray", "()[Ljava/lang/Object;", false);
			mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "tag", "[Ljava/lang/Object;");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(37, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/network/packets/ClientSyncEmcPKT;", null, l0, l3, 0);
			mv.visitLocalVariable("packetNum", "I", null, l0, l3, 1);
			mv.visitLocalVariable("arrayList", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<[Ljava/lang/Integer;>;", l0, l3, 2);
			mv.visitLocalVariable("tagList", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Lnet/minecraft/nbt/NBTTagCompound;>;", l0, l3, 3);
			mv.visitMaxs(3, 4);
			mv.visitEnd();
		}

		/**
		 * 一部書き換えだと上手く動作してくれず、原因もよく分からなかったので全書き換え
		 */
		private void overrideOnMessage(MethodVisitor mv)
		{
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(142, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "packetNum", "I");
			Label l1 = new Label();
			mv.visitJumpInsn(IFNE, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(144, l2);
			mv.visitLdcInsn("Receiving EMC data from server.");
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/utils/PELogger", "logInfo", "(Ljava/lang/String;)V", false);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(146, l3);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/emc/EMCMapper", "emc", "Ljava/util/LinkedHashMap;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashMap", "clear", "()V", false);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(147, l4);
			mv.visitTypeInsn(NEW, "java/util/LinkedHashMap");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/LinkedHashMap", "<init>", "()V", false);
			mv.visitFieldInsn(PUTSTATIC, "moze_intel/projecte/emc/EMCMapper", "emc", "Ljava/util/LinkedHashMap;");
			mv.visitLabel(l1);
			mv.visitLineNumber(150, l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 3);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(151, l5);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "data", "[Ljava/lang/Object;");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 7);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitVarInsn(ISTORE, 6);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 5);
			Label l6 = new Label();
			mv.visitJumpInsn(GOTO, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitFrame(Opcodes.F_FULL, 8, new Object[] {"moze_intel/projecte/network/packets/ClientSyncEmcPKT", "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "cpw/mods/fml/common/network/simpleimpl/MessageContext", Opcodes.INTEGER, Opcodes.TOP, Opcodes.INTEGER, Opcodes.INTEGER, "[Ljava/lang/Object;"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 4);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(153, l8);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Integer;");
			mv.visitVarInsn(ASTORE, 8);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(155, l9);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/emc/SimpleStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitInsn(ICONST_2);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "tag", "[Ljava/lang/Object;");
			mv.visitVarInsn(ILOAD, 3);
			mv.visitIincInsn(3, 1);
			mv.visitInsn(AALOAD);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/nbt/NBTTagCompound");
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/emc/SimpleStack", "<init>", "(IIILnet/minecraft/nbt/NBTTagCompound;)V", false);
			mv.visitVarInsn(ASTORE, 9);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(157, l10);
			mv.visitVarInsn(ALOAD, 9);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "isValid", "()Z", false);
			Label l11 = new Label();
			mv.visitJumpInsn(IFEQ, l11);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(159, l12);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/emc/EMCMapper", "emc", "Ljava/util/LinkedHashMap;");
			mv.visitVarInsn(ALOAD, 9);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitInsn(ICONST_3);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
			mv.visitInsn(POP);
			mv.visitLabel(l11);
			mv.visitLineNumber(151, l11);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitIincInsn(5, 1);
			mv.visitLabel(l6);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitVarInsn(ILOAD, 6);
			mv.visitJumpInsn(IF_ICMPLT, l7);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(163, l13);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "packetNum", "I");
			mv.visitInsn(ICONST_M1);
			Label l14 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l14);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(165, l15);
			mv.visitLdcInsn("Received all packets!");
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/utils/PELogger", "logInfo", "(Ljava/lang/String;)V", false);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(167, l16);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/playerData/Transmutation", "loadCompleteKnowledge", "()V", false);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(168, l17);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/emc/FuelMapper", "loadMap", "()V", false);
			mv.visitLabel(l14);
			mv.visitLineNumber(171, l14);
			mv.visitFrame(Opcodes.F_FULL, 4, new Object[] {"moze_intel/projecte/network/packets/ClientSyncEmcPKT", "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "cpw/mods/fml/common/network/simpleimpl/MessageContext", Opcodes.INTEGER}, 0, new Object[] {});
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			Label l18 = new Label();
			mv.visitLabel(l18);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/network/packets/ClientSyncEmcPKT;", null, l0, l18, 0);
			mv.visitLocalVariable("pkt", "Lmoze_intel/projecte/network/packets/ClientSyncEmcPKT;", null, l0, l18, 1);
			mv.visitLocalVariable("ctx", "Lcpw/mods/fml/common/network/simpleimpl/MessageContext;", null, l0, l18, 2);
			mv.visitLocalVariable("i", "I", null, l5, l18, 3);
			mv.visitLocalVariable("obj", "Ljava/lang/Object;", null, l8, l11, 4);
			mv.visitLocalVariable("array", "[Ljava/lang/Integer;", null, l9, l11, 8);
			mv.visitLocalVariable("stack", "Lmoze_intel/projecte/emc/SimpleStack;", null, l10, l11, 9);
			mv.visitMaxs(7, 10);
			mv.visitEnd();
		}

		/**
		 * 宣言した日からだいぶ遅れているのでとりあえず全書き換え。
		 */
		private void overrideFromBytes(MethodVisitor mv)
		{
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(77, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "packetNum", "I");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(78, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitVarInsn(ISTORE, 2);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(79, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "data", "[Ljava/lang/Object;");
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(80, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "tag", "[Ljava/lang/Object;");
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(82, l4);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 3);
			Label l5 = new Label();
			mv.visitLabel(l5);
			Label l6 = new Label();
			mv.visitJumpInsn(GOTO, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(84, l7);
			mv.visitFrame(Opcodes.F_APPEND,2, new Object[] {Opcodes.INTEGER, Opcodes.INTEGER}, 0, null);
			mv.visitInsn(ICONST_4);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Integer");
			mv.visitVarInsn(ASTORE, 4);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(86, l8);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 5);
			Label l9 = new Label();
			mv.visitLabel(l9);
			Label l10 = new Label();
			mv.visitJumpInsn(GOTO, l10);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(88, l11);
			mv.visitFrame(Opcodes.F_APPEND,2, new Object[] {"[Ljava/lang/Integer;", Opcodes.INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(86, l12);
			mv.visitIincInsn(5, 1);
			mv.visitLabel(l10);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(ICONST_4);
			mv.visitJumpInsn(IF_ICMPLT, l11);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(91, l13);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "data", "[Ljava/lang/Object;");
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitInsn(AASTORE);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLineNumber(82, l14);
			mv.visitIincInsn(3, 1);
			mv.visitLabel(l6);
			mv.visitFrame(Opcodes.F_CHOP,2, null, 0, null);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitJumpInsn(IF_ICMPLT, l7);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(95, l15);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 3);
			Label l16 = new Label();
			mv.visitLabel(l16);
			Label l17 = new Label();
			mv.visitJumpInsn(GOTO, l17);
			Label l18 = new Label();
			mv.visitLabel(l18);
			mv.visitLineNumber(97, l18);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "tag", "[Ljava/lang/Object;");
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/network/ByteBufUtils", "readTag", "(Lio/netty/buffer/ByteBuf;)Lnet/minecraft/nbt/NBTTagCompound;", false);
			mv.visitInsn(AASTORE);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitLineNumber(95, l19);
			mv.visitIincInsn(3, 1);
			mv.visitLabel(l17);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitJumpInsn(IF_ICMPLT, l18);
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitLineNumber(100, l20);
			mv.visitInsn(RETURN);
			Label l21 = new Label();
			mv.visitLabel(l21);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/network/packets/ClientSyncEmcPKT;", null, l0, l21, 0);
			mv.visitLocalVariable("buf", "Lio/netty/buffer/ByteBuf;", null, l0, l21, 1);
			mv.visitLocalVariable("size", "I", null, l2, l21, 2);
			mv.visitLocalVariable("i", "I", null, l5, l15, 3);
			mv.visitLocalVariable("array", "[Ljava/lang/Integer;", null, l8, l14, 4);
			mv.visitLocalVariable("j", "I", null, l9, l13, 5);
			mv.visitLocalVariable("i", "I", null, l16, l20, 3);
			mv.visitMaxs(3, 6);
			mv.visitEnd();
		}

		private void overrideToBytes(MethodVisitor mv)
		{
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(105, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "packetNum", "I");
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "writeInt", "(I)Lio/netty/buffer/ByteBuf;", false);
			mv.visitInsn(POP);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(106, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "data", "[Ljava/lang/Object;");
			mv.visitInsn(ARRAYLENGTH);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "writeInt", "(I)Lio/netty/buffer/ByteBuf;", false);
			mv.visitInsn(POP);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(108, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "data", "[Ljava/lang/Object;");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 5);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitVarInsn(ISTORE, 4);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 3);
			Label l3 = new Label();
			mv.visitJumpInsn(GOTO, l3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"moze_intel/projecte/network/packets/ClientSyncEmcPKT", "io/netty/buffer/ByteBuf", Opcodes.TOP, Opcodes.INTEGER, Opcodes.INTEGER, "[Ljava/lang/Object;"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 2);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(110, l5);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Integer;");
			mv.visitVarInsn(ASTORE, 6);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(112, l6);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 7);
			Label l7 = new Label();
			mv.visitLabel(l7);
			Label l8 = new Label();
			mv.visitJumpInsn(GOTO, l8);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(114, l9);
			mv.visitFrame(Opcodes.F_FULL, 8, new Object[] {"moze_intel/projecte/network/packets/ClientSyncEmcPKT", "io/netty/buffer/ByteBuf", "java/lang/Object", Opcodes.INTEGER, Opcodes.INTEGER, "[Ljava/lang/Object;", "[Ljava/lang/Integer;", Opcodes.INTEGER}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitVarInsn(ILOAD, 7);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "writeInt", "(I)Lio/netty/buffer/ByteBuf;", false);
			mv.visitInsn(POP);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(112, l10);
			mv.visitIincInsn(7, 1);
			mv.visitLabel(l8);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 7);
			mv.visitInsn(ICONST_4);
			mv.visitJumpInsn(IF_ICMPLT, l9);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(108, l11);
			mv.visitIincInsn(3, 1);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"moze_intel/projecte/network/packets/ClientSyncEmcPKT", "io/netty/buffer/ByteBuf", Opcodes.TOP, Opcodes.INTEGER, Opcodes.INTEGER, "[Ljava/lang/Object;"}, 0, new Object[] {});
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitJumpInsn(IF_ICMPLT, l4);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(118, l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/network/packets/ClientSyncEmcPKT", "tag", "[Ljava/lang/Object;");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 5);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitVarInsn(ISTORE, 4);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 3);
			Label l13 = new Label();
			mv.visitJumpInsn(GOTO, l13);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 2);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(120, l15);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/nbt/NBTTagCompound");
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/network/ByteBufUtils", "writeTag", "(Lio/netty/buffer/ByteBuf;Lnet/minecraft/nbt/NBTTagCompound;)V", false);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(118, l16);
			mv.visitIincInsn(3, 1);
			mv.visitLabel(l13);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitJumpInsn(IF_ICMPLT, l14);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(122, l17);
			mv.visitInsn(RETURN);
			Label l18 = new Label();
			mv.visitLabel(l18);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/network/packets/ClientSyncEmcPKT;", null, l0, l18, 0);
			mv.visitLocalVariable("buf", "Lio/netty/buffer/ByteBuf;", null, l0, l18, 1);
			mv.visitLocalVariable("obj", "Ljava/lang/Object;", null, l5, l11, 2);
			mv.visitLocalVariable("array", "[Ljava/lang/Integer;", null, l6, l11, 6);
			mv.visitLocalVariable("i", "I", null, l7, l11, 7);
			mv.visitLocalVariable("obj", "Ljava/lang/Object;", null, l15, l16, 2);
			mv.visitMaxs(3, 8);
			mv.visitEnd();
		}

	}
}
