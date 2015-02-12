package eremc.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class EMCMapperTransformer implements IClassTransformer, Opcodes
{
	private static final String TARGETCLASSNAME = "moze_intel.projecte.emc.EMCMapper";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(!TARGETCLASSNAME.equals(transformedName)) {return basicClass;}

		try {
			EREMCCorePlugin.logger.info("-------------------------Start EREMC Transform--------------------------");
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cr.accept(new CustomVisitor(name, cw, transformedName), 8);
			basicClass = cw.toByteArray();
			EREMCCorePlugin.logger.info("-------------------------Finish EREMC Transform-------------------------");
		} catch (Exception e) {
			throw new RuntimeException("failed : EREMCTransformer loading", e);
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

		// EMCMapper
		static final String targetMethodName = "addIMCRegistration";
		static final String targetMethodName2 = "loadEmcFromIMC";

		/**
		 * ここでどのメソッドかを判断してそれぞれの書き換え処理に飛ばしている
		 */
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			/**
			 * EMCMApper書き換え2つめ
			 *
			 * Mapを書き換えたので、それにあわせてメソッドも書き換え
			 */
			if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [addIMCRegistration]");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideAIMCR(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);

			}
			if (targetMethodName2.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [loadEmcFromIMC]");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideLEFIMC(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);

			}

			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		/**
		 *  アイテムに振られるIDは準備が完了するまでに振りなおされることがあるようで、
		 *  最初にいきなりIDを取得＆登録してしまうと、後からIDを元に参照しようとした際には別のIDが割り振られてしまっていることが
		 *  あるようだ。そうなるとIDから正しいアイテムを取得することができず、EMC値の登録が出来ない。
		 *  なので最初はアイテムスタックを取得＆登録しておき、準備が完了しIDが固定されてから、アイテムスタックを元にIDを取得するように変更する。
		 *
		 *  そのための作業1つめ
		 *  	Mapの引数をSimpleStackからItemStackに変更。
		 */
		public FieldVisitor visitField(int access, String name, String desc,
	            String signature, Object value) {
			if (name.equals("IMCregistrations")) {
				return super.visitField(access, name, desc, "Ljava/util/LinkedHashMap<Lnet/minecraft/item/ItemStack;Ljava/lang/Integer;>;", value);
			}
			return super.visitField(access, name, desc, signature, value);
		}

		/**
		 * 強引なメソッド丸ごと書き換え
		 */
		private void overrideAIMCR(MethodVisitor mv)
		{
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(361, l0);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/emc/EMCMapper", "IMCregistrations", "Ljava/util/LinkedHashMap;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashMap", "containsKey", "(Ljava/lang/Object;)Z", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNE, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(363, l2);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/emc/EMCMapper", "IMCregistrations", "Ljava/util/LinkedHashMap;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
			mv.visitInsn(POP);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(364, l3);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(367, l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l4, 0);
			mv.visitLocalVariable("value", "I", null, l0, l4, 1);
			mv.visitMaxs(3, 2);
			mv.visitEnd();
		}

		private void overrideLEFIMC(MethodVisitor mv) {
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(518, l0);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/emc/EMCMapper", "IMCregistrations", "Ljava/util/LinkedHashMap;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashMap", "entrySet", "()Ljava/util/Set;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;", true);
			mv.visitVarInsn(ASTORE, 2);
			Label l1 = new Label();
			mv.visitJumpInsn(GOTO, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_FULL, 3, new Object[] {Opcodes.TOP, Opcodes.TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/util/Map$Entry");
			mv.visitVarInsn(ASTORE, 1);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(520, l3);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/emc/SimpleStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getKey", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/item/ItemStack");
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/emc/SimpleStack", "<init>", "(Lnet/minecraft/item/ItemStack;)V", false);
			mv.visitVarInsn(ASTORE, 0);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(521, l4);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
			Label l5 = new Label();
			mv.visitJumpInsn(IFGT, l5);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(523, l6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/emc/EMCMapper", "addToBlacklist", "(Lmoze_intel/projecte/emc/SimpleStack;)V", false);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(524, l7);
			mv.visitJumpInsn(GOTO, l1);
			mv.visitLabel(l5);
			mv.visitLineNumber(527, l5);
			mv.visitFrame(Opcodes.F_FULL, 3, new Object[] {"moze_intel/projecte/emc/SimpleStack", "java/util/Map$Entry", "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "moze_intel/projecte/emc/EMCMapper", "addMapping", "(Lmoze_intel/projecte/emc/SimpleStack;I)V", false);
			mv.visitLabel(l1);
			mv.visitLineNumber(518, l1);
			mv.visitFrame(Opcodes.F_FULL, 3, new Object[] {Opcodes.TOP, Opcodes.TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l2);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(531, l8);
			mv.visitInsn(RETURN);
			mv.visitLocalVariable("sStack", "Lmoze_intel/projecte/emc/SimpleStack;", null, l4, l1, 0);
			mv.visitLocalVariable("entry", "Ljava/util/Map$Entry;", "Ljava/util/Map$Entry<Lnet/minecraft/item/ItemStack;Ljava/lang/Integer;>;", l3, l1, 1);
			mv.visitMaxs(3, 3);
			mv.visitEnd();
		}

	}
}
