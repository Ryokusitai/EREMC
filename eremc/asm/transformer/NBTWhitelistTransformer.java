package eremc.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.*;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import eremc.asm.EREMCCorePlugin;
import eremc.asm.transformer.SimpleStackTransformer.CustomMethodVisitor;
import eremc.asm.transformer.SimpleStackTransformer.CustomMethodVisitor2;
import eremc.asm.transformer.SimpleStackTransformer.CustomMethodVisitor3;
import eremc.asm.transformer.SimpleStackTransformer.CustomMethodVisitor4;
import eremc.asm.transformer.SimpleStackTransformer.CustomVisitor;

public class NBTWhitelistTransformer implements IClassTransformer, Opcodes
{
	private static final String TARGETCLASSNAME = "moze_intel.projecte.utils.NBTWhitelist";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!TARGETCLASSNAME.equals(transformedName)) { return basicClass;}

		try {
			EREMCCorePlugin.logger.info("-------------------------Start NBTWhitelist Transform--------------------------");
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cr.accept(new CustomVisitor(name, cw), 8);
			basicClass = cw.toByteArray();
			EREMCCorePlugin.logger.info("------------------------Finish NBTWhitelist Transform--------------------------");
		} catch (Exception e) {
			throw new RuntimeException("failed : NBTWhitelistTransformer Loading", e);
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

		static final String targetMethodName = "register";
		static final String targetMethodName2 = "shouldDupeWithNBT";

		/**
		 * ここでどのメソッドかを判断してそれぞれの書き換え処理に飛ばしている
		 */
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [register]");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideRegister(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);

			}
			if (targetMethodName2.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [shouldDupeWithNBT]");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideShouldDupeWithNBT(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);

			}

			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc,
	            String signature, Object value) {
			if (signature.equals("Ljava/util/List<Lmoze_intel/projecte/emc/SimpleStack;>;")) {
				EREMCCorePlugin.logger.info("Transform method [ArrayList]");
				return super.visitField(access, name, desc, "Ljava/util/List<Lnet/minecraft/item/ItemStack;>;", value);
			}
			return super.visitField(access, name, desc, signature, value);
		}

		public void overrideRegister(MethodVisitor mv) {
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(18, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ASTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(19, l1);
			mv.visitVarInsn(ALOAD, 1);
			Label l2 = new Label();
			mv.visitJumpInsn(IFNONNULL, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(21, l3);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(25, l2);
			mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {"net/minecraft/item/ItemStack"}, 0, null);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/utils/NBTWhitelist", "LIST", "Ljava/util/List;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "contains", "(Ljava/lang/Object;)Z", true);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNE, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(27, l5);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/utils/NBTWhitelist", "LIST", "Ljava/util/List;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(28, l6);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(32, l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l7, 0);
			mv.visitLocalVariable("s", "Lnet/minecraft/item/ItemStack;", null, l1, l7, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}

		public void overrideShouldDupeWithNBT(MethodVisitor mv) {
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(37, l0);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/emc/SimpleStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/emc/SimpleStack", "<init>", "(Lnet/minecraft/item/ItemStack;)V", false);
			mv.visitVarInsn(ASTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(39, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "isValid", "()Z", false);
			Label l2 = new Label();
			mv.visitJumpInsn(IFNE, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(41, l3);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(44, l2);
			mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {"moze_intel/projecte/emc/SimpleStack"}, 0, null);
			mv.visitFieldInsn(GETSTATIC, "moze_intel/projecte/utils/NBTWhitelist", "LIST", "Ljava/util/List;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "iterator", "()Ljava/util/Iterator;", true);
			mv.visitVarInsn(ASTORE, 4);
			Label l4 = new Label();
			mv.visitJumpInsn(GOTO, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_FULL, 5, new Object[] {"net/minecraft/item/ItemStack", "moze_intel/projecte/emc/SimpleStack", Opcodes.TOP, Opcodes.TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/item/ItemStack");
			mv.visitVarInsn(ASTORE, 3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(45, l6);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/emc/SimpleStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/emc/SimpleStack", "<init>", "(Lnet/minecraft/item/ItemStack;)V", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(46, l7);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "equals", "(Ljava/lang/Object;)Z", false);
			mv.visitJumpInsn(IFEQ, l4);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(47, l8);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(44, l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l5);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(50, l9);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l10, 0);
			mv.visitLocalVariable("s", "Lmoze_intel/projecte/emc/SimpleStack;", null, l1, l10, 1);
			mv.visitLocalVariable("s2", "Lmoze_intel/projecte/emc/SimpleStack;", null, l7, l4, 2);
			mv.visitLocalVariable("item", "Lnet/minecraft/item/ItemStack;", null, l6, l4, 3);
			mv.visitMaxs(3, 5);
			mv.visitEnd();
		}
	}

}