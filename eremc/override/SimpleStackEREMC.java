package eremc.override;

import cpw.mods.fml.common.network.ByteBufUtils;
import moze_intel.projecte.emc.SimpleStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

/**
 * 区別のためクラス名の語尾にのみEREMCがついています。
 * 追加したり書き換えた部分には説明を入れています。
 * このファイルは書き換えた部分を分かりやすく見るためにあるだけで(※1)、
 * 実際に使用されるファイルではありません。
 *
 * ※1 ASMで書き換えるとバイトコードで記述をしなくてはならないのでどのように書き換えたのか分からない。
 * ※これはProjectEのSimpleStackクラスをNBTTagに対応させるため一部書き換えたものです。
 *   実際はこうなるようにProjectE上のクラスを直接ASMを利用して書き換えています。
 *
 */
//*
public class SimpleStack
{
	public int id;
	public int damage;
	public int qnty;
	// BCのゲートなどtagで区別しているアイテムにも対応できるように tag を格納するフィールドを追加
	public NBTTagCompound tag;

	// なくてもいいのかもしれませんが this.tag = null; の一文を追加
	public SimpleStack(int id, int qnty, int damage)
	{
		this.id = id;
		this.qnty = qnty;
		this.damage = damage;
		this.tag = null;
	}

	// 引数にNBTTagを追加したコンストラクタ
	public SimpleStack(int id, int qnty, int damage, NBTTagCompound tag)
	{
		this.id = id;
		this.qnty = qnty;
		this.damage = damage;
		this.tag = tag;
	}

	public SimpleStack(ItemStack stack)
	{
		if (stack == null)
		{
			id = -1;
		}
		else
		{
			id = Item.itemRegistry.getIDForObject(stack.getItem());
			damage = stack.getItemDamage();
			qnty = stack.stackSize;
			// tagを取得してフィールドに格納する
			tag = stack.stackTagCompound;
		}
	}

	public boolean isValid()
	{
		return id != -1;
	}

	public ItemStack toItemStack()
	{
		if (isValid())
		{
			Item item = Item.getItemById(id);

			if (item != null)
			{
				ItemStack stack = new ItemStack(Item.getItemById(id), qnty, damage);
				// ItemStackに変換するときもtagをセットして返す
				stack.stackTagCompound = tag;
				return stack;
			}
		}

		return null;
	}

	public SimpleStack copy()
	{
		// tagも送る
		return new SimpleStack(id, qnty, damage, tag);
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	//**
	// * returnの判断条件に
	// * ItemStack.areItemStacksEqual(this.toItemStack(), other.toItemStack() を追加
	// *-/
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof SimpleStack)
		{
			SimpleStack other = (SimpleStack) obj;

			if (this.damage == OreDictionary.WILDCARD_VALUE || other.damage == OreDictionary.WILDCARD_VALUE)
			{
				//return this.id == other.id;
				return this.qnty == other.qnty && this.id == other.id && ItemStack.areItemStacksEqual(this.toItemStack(), other.toItemStack()); /////
			}

			//return this.id == other.id && this.damage == other.damage;
			return this.id == other.id && this.qnty == other.qnty && this.damage == other.damage && ItemStack.areItemStacksEqual(this.toItemStack(), other.toItemStack()); /////
		}
		return false;
	}

	//**
	// * 「" " + tag」を追加
	// *-/
	@Override
	public String toString()
	{
		Object obj = Item.itemRegistry.getObjectById(id);

		if (obj != null)
		{
			return Item.itemRegistry.getNameForObject(obj) + " " + qnty + " " + damage + " " + tag;/////
		}

		return "id:" + id + " damage:" + damage + " qnty:" + qnty + " " + tag;/////
	}
}
//*/