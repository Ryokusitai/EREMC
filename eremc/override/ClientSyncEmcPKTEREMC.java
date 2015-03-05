package eremc.override;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import moze_intel.projecte.emc.EMCMapper;
import moze_intel.projecte.emc.FuelMapper;
import moze_intel.projecte.emc.SimpleStack;
import moze_intel.projecte.playerData.Transmutation;
import moze_intel.projecte.utils.PELogger;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * これはEmc値の情報をClientに送信するためのクラスです
 *
 * 区別のためクラス名の語尾にのみEREMCがついています。
 * 追加したり書き換えた部分には説明を入れています。
 * このファイルは書き換えた部分を分かりやすく見るためにあるだけで(※1)、
 * 実際に使用されるファイルではありません。
 *
 * ※1 ASMで書き換えるとバイトコードで記述をしなくてはならないのでどのように書き換えたのか分からない。
 * ※これはProjectEのClientSyncEmcPKTクラスをNBTTagに対応させるため一部書き換えたものです。
 *   実際はこうなるようにProjectE上のクラスを直接ASMを利用して書き換えています。
 */
//*
public class ClientSyncEmcPKT implements IMessage, IMessageHandler<ClientSyncEmcPKT, IMessage>
{
	private int packetNum;
	private Object[] data;
	// 送信する/受信した値を格納するための配列
	private Object[] tag;

	public ClientSyncEmcPKT() {}

	public ClientSyncEmcPKT(int packetNum, ArrayList<Integer[]> arrayList)
	{
		this.packetNum = packetNum;
		data = arrayList.toArray();
	}

	// 引数にtag用配列追加したコンストラクタを作成
	public ClientSyncEmcPKT(int packetNum, ArrayList<Integer[]> arrayList, ArrayList<NBTTagCompound> tagList)
	{
		this.packetNum = packetNum;
		data = arrayList.toArray();
		// 引数のtag用配列をフィールドに格納
		tag  = tagList.toArray();
	}

	@Override
	public IMessage onMessage(ClientSyncEmcPKT pkt, MessageContext ctx)
	{
		if (pkt.packetNum == 0)
		{
			PELogger.logInfo("Receiving EMC data from server.");

			EMCMapper.emc.clear();
			EMCMapper.emc = new LinkedHashMap<SimpleStack, Integer>();
		}

		// 拡張forが使用されているので、カウントをするために追加
		int i = 0;
		for (Object obj : pkt.data)
		{
			Integer[] array = (Integer[]) obj;

			// 引数にtagを追加してインスタンス作成
			SimpleStack stack = new SimpleStack(array[0], array[1], array[2], (NBTTagCompound) pkt.tag[i++]);

			if (stack.isValid())
			{
				EMCMapper.emc.put(stack, array[3]);
			}
		}

		if (pkt.packetNum == -1)
		{
			PELogger.logInfo("Received all packets!");

			Transmutation.loadCompleteKnowledge();
			FuelMapper.loadMap();
		}

		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		packetNum = buf.readInt();
		int size = buf.readInt();
		data = new Object[size];
		// 送られてきた要素分のサイズに配列初期化
		tag = new Object[size];
		for (int i = 0; i < size; i++)
		{
			Integer[] array = new Integer[4];

			for (int j = 0; j < 4; j++)
			{
				array[j] = buf.readInt();
			}

			data[i] = array;

		}

		// ByteBufUtils.readTagメソッドを利用してNBTTagCompoundを取得し、配列に格納
		for (int i = 0; i < size; i++)
		{
			tag[i] = ByteBufUtils.readTag(buf);
		}

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(packetNum);
		buf.writeInt(data.length);

		for (Object obj : data)
		{
			Integer[] array = (Integer[]) obj;

			for (int i = 0; i < 4; i++)
			{
				buf.writeInt(array[i]);
			}
		}

		// ByteBufUtils.writeTagメソッドを利用して tag配列 の NBTTagCompound を buf に書き込む
		for (Object obj : tag)
		{
			ByteBufUtils.writeTag(buf, (NBTTagCompound) obj);
		}
	}
}
//*/