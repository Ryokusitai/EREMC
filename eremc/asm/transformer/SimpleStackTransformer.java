package eremc.asm.transformer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.nbt.NBTTagCompound;

import org.objectweb.asm.*;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import eremc.asm.EREMCCorePlugin;
import eremc.asm.MethodNameList;

public class SimpleStackTransformer implements IClassTransformer, Opcodes
{
	private static final String TARGETCLASSNAME = "moze_intel.projecte.emc.SimpleStack";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!TARGETCLASSNAME.equals(transformedName)) { return basicClass;}

		try {
			EREMCCorePlugin.logger.info("-------------------------Start SimpleStack Transform--------------------------");
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cr.accept(new CustomVisitor(name, cw), 8);
			basicClass = cw.toByteArray();
			EREMCCorePlugin.logger.info("------------------------Finish SimpleStack Transform--------------------------");
		} catch (Exception e) {
			throw new RuntimeException("failed : SimpleStackTransformer Loading", e);
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

		static final String targetMethodName = "<init>";
		static final String targetMethodName2 = "toItemStack";
		static final String targetMethodName3 = "copy";
		static final String targetMethodName4 = "equals";
		static final String targetMethodName5 = "toString";

		/**
		 * ここでどのメソッドかを判断してそれぞれの書き換え処理に飛ばしている
		 */
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [<init>]");
				createConstructor();
				return new CustomMethodVisitor(this.api, super.visitMethod(access, name, desc, signature, exceptions));

			}
			if (targetMethodName2.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [toItemStack]");
				return new CustomMethodVisitor2(this.api, super.visitMethod(access, name, desc, signature, exceptions));

			}
			// これはメソッド全体の書き換え(置き換え)
			if (targetMethodName3.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [copy]");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				overrideCopy(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);

			}
			if (targetMethodName4.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [equals]");
				return new CustomMethodVisitor3(this.api, super.visitMethod(access, name, desc, signature, exceptions));

			}
			if (targetMethodName5.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [toString]");
				return new CustomMethodVisitor4(this.api, super.visitMethod(access, name, desc, signature, exceptions));

			}

			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		int cnt = 0;
		@Override
		public FieldVisitor visitField(int access, String name, String desc,
	            String signature, Object value) {
			if (cnt == 0) {
				FieldVisitor fv = super.visitField(ACC_PUBLIC, "tag", "Lnet/minecraft/nbt/NBTTagCompound;", null, null);
				fv.visitEnd();
				cnt++;
			}

			return super.visitField(access, name, desc, signature, value);
	    }

		/**
		 * public SimpleStack(int id, int qnty, int damage, NBTTagCompound tag)
		 * を新たに作成
		 */
		boolean isCreate;
		public void createConstructor() {
			if (isCreate)	{return;}

			isCreate = true;
			MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>", "(IIILnet/minecraft/nbt/NBTTagCompound;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(26, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/emc/SimpleStack", "<init>", "(III)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(27, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/emc/SimpleStack", "tag", "Lnet/minecraft/nbt/NBTTagCompound;");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(28, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/emc/SimpleStack;", null, l0, l3, 0);
			mv.visitLocalVariable("id", "I", null, l0, l3, 1);
			mv.visitLocalVariable("qnty", "I", null, l0, l3, 2);
			mv.visitLocalVariable("damage", "I", null, l0, l3, 3);
			mv.visitLocalVariable("tag", "Lnet/minecraft/nbt/NBTTagCompound;", null, l0, l3, 4);
			mv.visitMaxs(4, 5);
			mv.visitEnd();
		}

		/**
		 * mv.visitMaxsの値が違ったりするとどうなるのかよく分からなかったため
		 * 一部書き換えではなくまるごと置き換え
		 */
		public void overrideCopy(MethodVisitor mv) {
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(71, l0);
			mv.visitTypeInsn(NEW, "moze_intel/projecte/emc/SimpleStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "id", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "qnty", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "damage", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "tag", "Lnet/minecraft/nbt/NBTTagCompound;");
			mv.visitMethodInsn(INVOKESPECIAL, "moze_intel/projecte/emc/SimpleStack", "<init>", "(IIILnet/minecraft/nbt/NBTTagCompound;)V", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/emc/SimpleStack;", null, l0, l1, 0);
			mv.visitMaxs(6, 1);
			mv.visitEnd();
		}
	}

	// 自分が何回呼ばれたのかを数える
	// 0のとき this.tag = null; 追加処理
	// 2のとき tag = stack.stackTagCompound; 追加処理
	private int cmvflag = 0;

	class CustomMethodVisitor extends MethodVisitor {


		public CustomMethodVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}

		public void visitMethodInsn(int opcode, String owner, String name,
	            String desc, boolean itf) {
			super.visitMethodInsn(opcode, owner, name, desc, itf);

			if (cmvflag == 0) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/emc/SimpleStack", "tag", "Lnet/minecraft/nbt/NBTTagCompound;");
			}
			if (owner.equals("java/lang/Object")) {
				cmvflag++;
			}

		}

		// public SimpleStack(ItemStack stack)に
		// tag = stack.stackTagCompound;
		public void visitFieldInsn(int opcode, String owner, String name,
				String desc) {
			// 引数が一致していて、書き換えたいコンストラクタからの呼び出しなら書き換える
			if (opcode == PUTFIELD && owner.equals("moze_intel/projecte/emc/SimpleStack") && name.equals("qnty") &&
					cmvflag == 2) {
				super.visitFieldInsn(opcode, owner, name, desc);

				// tag追加
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitFieldInsn(GETFIELD, "net/minecraft/item/ItemStack", MethodNameList.getName("stackTagCompound"), "Lnet/minecraft/nbt/NBTTagCompound;");
				mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/emc/SimpleStack", "tag", "Lnet/minecraft/nbt/NBTTagCompound;");

				cmvflag = -1;
				return;
			}
			super.visitFieldInsn(opcode, owner, name, desc);
		}
	}

	/**
	 * toItemStackメソッド書き換え用
	 * return new ItemStack(Item.getItemById(id), qnty, damage);
	 * を
	 * ItemStack stack = new ItemStack(Item.getItemById(id), qnty, damage);
	 * stack.stackTagCompound = tag;/////
	 * return stack;
	 * に書き換える
	 */
	class CustomMethodVisitor2 extends MethodVisitor {

		public CustomMethodVisitor2(int api, MethodVisitor mv) {
			super(api, mv);
		}

		public void visitMethodInsn(int opcode, String owner, String name,
	            String desc, boolean itf) {		// stackTagCompound = field_77990_d
			super.visitMethodInsn(opcode, owner, name, desc, itf);
			if (owner.equals("net/minecraft/item/ItemStack")) {
				mv.visitVarInsn(ASTORE, 2);
				Label l5 = new Label();
				mv.visitLabel(l5);
				mv.visitLineNumber(60, l5);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "tag", "Lnet/minecraft/nbt/NBTTagCompound;");
				mv.visitFieldInsn(PUTFIELD, "net/minecraft/item/ItemStack", MethodNameList.getName("stackTagCompound"), "Lnet/minecraft/nbt/NBTTagCompound;");
				Label l6 = new Label();
				mv.visitLabel(l6);
				mv.visitLineNumber(61, l6);
				mv.visitVarInsn(ALOAD, 2);
			}
		}

	}

	// 現在のラベルを記録する
	Label lbl;
	/**
	 * equals メソッドに追加
	 */
	class CustomMethodVisitor3 extends MethodVisitor {

		public CustomMethodVisitor3(int api, MethodVisitor mv) {
			super(api, mv);
		}

		// ラベル番号を記録
		public void visitJumpInsn(int opcode, Label label) {
	        super.visitJumpInsn(opcode, label);
	        lbl = label;
	    }

		public void visitInsn(int opcode) {
			if (opcode == ICONST_1) {
				addTag(lbl);
			}
	        super.visitInsn(opcode);
	    }

		public void addTag(Label lbl) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "toItemStack", "()Lnet/minecraft/item/ItemStack;", false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "toItemStack", "()Lnet/minecraft/item/ItemStack;", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/item/ItemStack", MethodNameList.getName("areItemStacksEqual"), "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false);
			mv.visitJumpInsn(IFEQ, lbl);
		}
	}

	/**
	 * toStringメソッドに追加
	 */
	class CustomMethodVisitor4 extends MethodVisitor {

		public CustomMethodVisitor4(int api, MethodVisitor mv) {
			super(api, mv);
		}

		public void visitMethodInsn(int opcode, String owner, String name,
	            String desc, boolean itf) {
			if (name.equals("toString")) {
				// tag判定を追加
				mv.visitLdcInsn(" ");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "tag", "Lnet/minecraft/nbt/NBTTagCompound;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
			}
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

}
