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
		static final String targetMethodName6 = "isValid";		// reloadId()追加
		static final String targetMethodName7 = "hashCode";		// reliadId()追加
		boolean finishedAddMethod;		// reloadId()とgetId()の2つのメソッドが追加されたかどうかの判断用

		/**
		 * ここでどのメソッドかを判断してそれぞれの書き換え処理に飛ばしている
		 */
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			// 先頭にもってこないと他のif内でreturnされて終わり
			if (!this.finishedAddMethod) {
				EREMCCorePlugin.logger.info("Create method [reloadId() & createGetId()]");
				createReloadId();
				createGetId();
				finishedAddMethod = true;
			}
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
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				ovverrideEquals(mv);
				return super.visitMethod(access, name + "Old", desc, signature, exceptions);
				//return new CustomMethodVisitor3(this.api, super.visitMethod(access, name, desc, signature, exceptions));
			}
			if (targetMethodName5.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [toString]");
				return new CustomMethodVisitor4(this.api, super.visitMethod(access, name, desc, signature, exceptions));
			}
			// これはメソッド全体の書き換え(置き換え)
			if (targetMethodName6.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [isValid]");
				return new CustomMethodVisitor5(this.api, super.visitMethod(access, name, desc, signature, exceptions));
			}
			if (targetMethodName7.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [hashCode]");
				return new CustomMethodVisitor6(this.api, super.visitMethod(access, name, desc, signature, exceptions));
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
				// Item item;を追加
				fv = super.visitField(ACC_PUBLIC, "item", "Lnet/minecraft/item/Item;", null, null);
				fv.visitEnd();
				cnt++;
			}

			return super.visitField(access, name, desc, signature, value);
	    }

		boolean finishedCreateConst;
		/**
		 * public SimpleStack(int id, int qnty, int damage, NBTTagCompound tag)
		 * を新たに作成
		 */
		public void createConstructor() {
			if (finishedCreateConst)	{return;}

			finishedCreateConst = true;
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
		 * reloadId()の呼び出しを追加
		 */
		public void overrideCopy(MethodVisitor mv) {
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(74, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "reloadId", "()V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(75, l1);
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
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/emc/SimpleStack;", null, l0, l2, 0);
			mv.visitMaxs(6, 1);
			mv.visitEnd();
		}

		/**
		 * とても長い
		 */
		public void ovverrideEquals(MethodVisitor mv)
		{
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(88, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(INSTANCEOF, "moze_intel/projecte/emc/SimpleStack");
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(90, l2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, "moze_intel/projecte/emc/SimpleStack");
			mv.visitVarInsn(ASTORE, 2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(92, l3);
			mv.visitTypeInsn(NEW, "net/minecraft/nbt/NBTTagCompound");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/nbt/NBTTagCompound", "<init>", "()V", false);
			mv.visitVarInsn(ASTORE, 3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(93, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "toString", "()Ljava/lang/String;", false);
			mv.visitLdcInsn("BuildCraft");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "indexOf", "(Ljava/lang/String;)I", false);
			mv.visitInsn(ICONST_M1);
			Label l5 = new Label();
			mv.visitJumpInsn(IF_ICMPEQ, l5);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(94, l6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "toItemStack", "()Lnet/minecraft/item/ItemStack;", false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "toItemStack", "()Lnet/minecraft/item/ItemStack;", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/item/ItemStack", MethodNameList.getName("areItemStackTagsEqual"), "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false);
			mv.visitJumpInsn(IFNE, l5);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(95, l7);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l5);
			mv.visitLineNumber(98, l5);
			mv.visitFrame(Opcodes.F_APPEND,2, new Object[] {"moze_intel/projecte/emc/SimpleStack", "net/minecraft/nbt/NBTTagCompound"}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "damage", "I");
			mv.visitIntInsn(SIPUSH, 32767);
			Label l8 = new Label();
			mv.visitJumpInsn(IF_ICMPEQ, l8);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "damage", "I");
			mv.visitIntInsn(SIPUSH, 32767);
			Label l9 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l9);
			mv.visitLabel(l8);
			mv.visitLineNumber(101, l8);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "qnty", "I");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "qnty", "I");
			Label l10 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l10);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "getId", "()I", false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "getId", "()I", false);
			mv.visitJumpInsn(IF_ICMPNE, l10);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l10);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l9);
			mv.visitLineNumber(105, l9);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "getId", "()I", false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "getId", "()I", false);
			Label l11 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "qnty", "I");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "qnty", "I");
			mv.visitJumpInsn(IF_ICMPNE, l11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "damage", "I");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "damage", "I");
			mv.visitJumpInsn(IF_ICMPNE, l11);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l11);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(107, l1);
			mv.visitFrame(Opcodes.F_CHOP,2, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/emc/SimpleStack;", null, l0, l12, 0);
			mv.visitLocalVariable("obj", "Ljava/lang/Object;", null, l0, l12, 1);
			mv.visitLocalVariable("other", "Lmoze_intel/projecte/emc/SimpleStack;", null, l3, l1, 2);
			mv.visitLocalVariable("newTag", "Lnet/minecraft/nbt/NBTTagCompound;", null, l4, l1, 3);
			mv.visitMaxs(2, 4);
			mv.visitEnd();
		}

		boolean finishedCreateRelId;
		/**
		 * reloadId()メソッド作成
		 */
		public void createReloadId() {
			if (finishedCreateRelId) { return;}

			finishedCreateRelId = true;
			MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "reloadId", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(124, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "item", "Lnet/minecraft/item/Item;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/item/Item", MethodNameList.getName("getIdFromItem"), "(Lnet/minecraft/item/Item;)I", false);
			mv.visitVarInsn(ISTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(125, l1);
			mv.visitVarInsn(ILOAD, 1);
			Label l2 = new Label();
			mv.visitJumpInsn(IFEQ, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(126, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/emc/SimpleStack", "id", "I");
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(127, l4);
			mv.visitInsn(RETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(129, l2);
			mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {Opcodes.INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_M1);
			mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/emc/SimpleStack", "id", "I");
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(130, l5);
			mv.visitInsn(RETURN);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/emc/SimpleStack;", null, l0, l6, 0);
			mv.visitLocalVariable("id", "I", null, l1, l6, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}

		boolean finishedCreateGetId;
		/**
		 * getId()メソッド作成
		 */
		public void createGetId() {
			if (finishedCreateGetId) { return;}

			finishedCreateGetId = true;
			MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "getId", "()I", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(133, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "reloadId", "()V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(134, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "moze_intel/projecte/emc/SimpleStack", "id", "I");
			mv.visitInsn(IRETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lmoze_intel/projecte/emc/SimpleStack;", null, l0, l2, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
	}

	// 自分が何回呼ばれたのかを数える
	// 0のとき this.tag = null; と this.item = Item.getItemById(id); の追加処理
	// 2のとき tag = stack.stackTagCompound; と this.item = stack.getItem(); の追加処理
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
				// たぶん this.item = Item.getItemById(id); って意味
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ILOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/item/Item", MethodNameList.getName("getItemById"), "(I)Lnet/minecraft/item/Item;", false);
				mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/emc/SimpleStack", "item", "Lnet/minecraft/item/Item;");
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
				// this.item = stack.getItem(); も追加

				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", MethodNameList.getName("getItem"), "()Lnet/minecraft/item/Item;", false);
				mv.visitFieldInsn(PUTFIELD, "moze_intel/projecte/emc/SimpleStack", "item", "Lnet/minecraft/item/Item;");


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

	private boolean finishedCMV5Addition;		// 追加済みかどうか
	/**
	 * isValidメソッドに「reloadId();」追加
	 */
	class CustomMethodVisitor5 extends MethodVisitor {

		public CustomMethodVisitor5(int api, MethodVisitor mv) {
			super(api, mv);
		}

		public void visitLineNumber(int line, Label start) {
			super.visitLineNumber(line, start);
			if (!finishedCMV5Addition) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "reloadId", "()V", false);
				finishedCMV5Addition = true;
			}
		}
	}

	private boolean finishedCMV6Addition;		// 追加済みかどうか
	/**
	 * hashCodeメソッドに「reloadId();」追加
	 */
	class CustomMethodVisitor6 extends MethodVisitor {

		public CustomMethodVisitor6(int api, MethodVisitor mv) {
			super(api, mv);
		}

		public void visitLineNumber(int line, Label start) {
			super.visitLineNumber(line, start);
			if (!finishedCMV6Addition) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, "moze_intel/projecte/emc/SimpleStack", "reloadId", "()V", false);
				finishedCMV6Addition = true;
			}
		}
	}

}
