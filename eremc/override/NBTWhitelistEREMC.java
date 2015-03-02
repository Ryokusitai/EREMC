package eremc.override;

import java.util.ArrayList;
import java.util.List;

import moze_intel.projecte.emc.SimpleStack;
import net.minecraft.item.ItemStack;

/**
 * 区別のためクラス名の語尾にのみEREMCがついています。
 * 追加したり書き換えた部分には説明を入れています。
 * このファイルは書き換えた部分を分かりやすく見るためにあるだけで(※1)、
 * 実際に使用されるファイルではありません。
 *
 * ※1 ASMで書き換えるとバイトコードで記述をしなくてはならないのでどのように書き換えたのか分からない。
 * ※これはProjectEのNBTWhitelistクラスをNBTTagに対応させるため一部書き換えたものです。
 *   実際はこうなるようにProjectE上のクラスを直接ASMを利用して書き換えています。
 *
 */
public class NBTWhitelistEREMC
{
	//private static final List<SimpleStack> LIST = new ArrayList<SimpleStack>();
	private static final List<ItemStack> LIST = new ArrayList<ItemStack>();

	public static boolean register(ItemStack stack)
	{
		// 暫定としてItemStackに変更
		ItemStack s = stack;
		// ItemStackではisValid()が使えないのでnull判定に変更
		if (s == null)
		{
			return false;
		}
		//ダメージ値の操作は出来ない?ので消去

		if (!LIST.contains(s))
		{
			LIST.add(s);
			return true;
		}

		return false;
	}

	public static boolean shouldDupeWithNBT(ItemStack stack)
	{
		SimpleStack s = new SimpleStack(stack);

		if (!s.isValid())
		{
			return false;
		}
		// containsは使えないのでforループで比較 もしかするとこの影響でcontainsより動作が重くなる？
		SimpleStack s2;
		for (ItemStack item : LIST) {
			s2 = new SimpleStack(item);
			if (s.equals(s2)) {
				return true;
			}
		}
		return false;
	}

}
