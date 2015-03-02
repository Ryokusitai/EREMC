package eremc.override;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.event.FMLInterModComms;

/**
 * 区別のためクラス名の語尾にのみEREMCがついています。
 * 追加したり書き換えた部分には説明を入れています。
 * このファイルは書き換えた部分を分かりやすく見るためにあるだけで(※1)、
 * 実際に使用されるファイルではありません。
 *
 * ※1 ASMで書き換えるとバイトコードで記述をしなくてはならないのでどのように書き換えたのか分からない。
 * ※これはProjectEのProjectEAPIクラスをNBTTagに対応させるため一部書き換えたものです。
 *   実際はこうなるようにProjectE上のクラスを直接ASMを利用して書き換えています。
 *
 */
public class ProjectEAPIEREMC
{
	//"condensernbtcopy"を"nbtwhitelist"に変更 それだけ 他のメソッドは変更していないので割愛
	public static void registerCondenserNBTException(ItemStack stack)
	{
		FMLInterModComms.sendMessage("ProjectE", "nbtwhitelist", stack);
	}

}
