package eremc.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import eremc.asm.EREMCCorePlugin;
import eremc.asm.transformer.SimpleStackTransformer.CustomMethodVisitor;
import eremc.asm.transformer.SimpleStackTransformer.CustomMethodVisitor2;
import eremc.asm.transformer.SimpleStackTransformer.CustomMethodVisitor3;
import eremc.asm.transformer.SimpleStackTransformer.CustomMethodVisitor4;

public class ProjectEAPITransformer implements IClassTransformer, Opcodes
{
	private static final String TARGETCLASSNAME = "moze_intel.projecte.api.ProjectEAPI";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!TARGETCLASSNAME.equals(transformedName)) { return basicClass;}

		try {
			EREMCCorePlugin.logger.info("---------------------Start ProjectEAPI Transform----------------------");
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cr.accept(new CustomVisitor(name, cw), 8);
			basicClass = cw.toByteArray();
			EREMCCorePlugin.logger.info("--------------------Finish ProjectEAPI Transform----------------------");
		} catch (Exception e) {
			throw new RuntimeException("failed : ProjectEAPITransformer Loading", e);
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

		static final String targetMethodName = "registerCondenserNBTException";

		/**
		 * ここでどのメソッドかを判断してそれぞれの書き換え処理に飛ばしている
		 */
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))) {
				EREMCCorePlugin.logger.info("Transform method [registerCondenserNBTException]");
				return new CustomMethodVisitor(this.api, super.visitMethod(access, name, desc, signature, exceptions));

			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}

	class CustomMethodVisitor extends MethodVisitor {
		public CustomMethodVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}

		public void visitLdcInsn(Object cst) {
			if (cst.toString().equals("condensernbtcopy")) {
				super.visitLdcInsn("nbtwhitelist");
				return;
			}
			super.visitLdcInsn(cst);
		}
	}

}
