package eremc.override;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import moze_intel.projecte.emc.EMCMapper;
import moze_intel.projecte.emc.SimpleStack;
import moze_intel.projecte.network.packets.*;
import moze_intel.projecte.utils.PELogger;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Map;

/**
 * 区別のためクラス名の語尾にのみEREMCがついています。
 * 追加したり書き換えた部分には説明を入れています。
 * このファイルは書き換えた部分を分かりやすく見るためにあるだけで(※1)、
 * 実際に使用されるファイルではありません。
 *
 * ※1 ASMで書き換えるとバイトコードで記述をしなくてはならないのでどのように書き換えたのか分からない。
 * ※これはProjectEのPacketHandlerクラスをNBTTagに対応させるため一部書き換えたものです。
 *   実際はこうなるようにProjectE上のクラスを直接ASMを利用して書き換えています。
 *
 */
//*
public final class PacketHandler
{
	private static final int MAX_PKT_SIZE = 256;
	private static final SimpleNetworkWrapper HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("projecte");

	public static void register()
	{
		HANDLER.registerMessage(ClientSyncEmcPKT.class, ClientSyncEmcPKT.class, 0, Side.CLIENT);
		HANDLER.registerMessage(KeyPressPKT.class, KeyPressPKT.class, 1, Side.SERVER);
		HANDLER.registerMessage(ParticlePKT.class, ParticlePKT.class, 2, Side.CLIENT);
		HANDLER.registerMessage(SwingItemPKT.class, SwingItemPKT.class, 3, Side.CLIENT);
		HANDLER.registerMessage(StepHeightPKT.class, StepHeightPKT.class, 4, Side.CLIENT);
		HANDLER.registerMessage(SetFlyPKT.class, SetFlyPKT.class, 5, Side.CLIENT);
		HANDLER.registerMessage(ClientKnowledgeSyncPKT.class, ClientKnowledgeSyncPKT.class, 6, Side.CLIENT);
		HANDLER.registerMessage(ClientTableSyncPKT.class, ClientTableSyncPKT.class, 7, Side.CLIENT);
		HANDLER.registerMessage(CondenserSyncPKT.class, CondenserSyncPKT.class, 8, Side.CLIENT);
		HANDLER.registerMessage(CollectorSyncPKT.class, CollectorSyncPKT.class, 9, Side.CLIENT);
		HANDLER.registerMessage(RelaySyncPKT.class, RelaySyncPKT.class, 10, Side.CLIENT);
		HANDLER.registerMessage(ClientCheckUpdatePKT.class, ClientCheckUpdatePKT.class, 11, Side.CLIENT);
		HANDLER.registerMessage(ClientSyncBagDataPKT.class, ClientSyncBagDataPKT.class, 12, Side.CLIENT);
		HANDLER.registerMessage(SearchUpdatePKT.class, SearchUpdatePKT.class, 13, Side.SERVER);
		HANDLER.registerMessage(ClientKnowledgeClearPKT.class, ClientKnowledgeClearPKT.class, 14, Side.CLIENT);
		HANDLER.registerMessage(ClientOrientationSyncPKT.class, ClientOrientationSyncPKT.class, 15, Side.CLIENT);
		HANDLER.registerMessage(UpdateGemModePKT.class, UpdateGemModePKT.class, 16, Side.SERVER);
		HANDLER.registerMessage(ClientSyncTableEMCPKT.class, ClientSyncTableEMCPKT.class, 17, Side.CLIENT);
	}

	public static void sendFragmentedEmcPacket(EntityPlayerMP player)
	{
		ArrayList<Integer[]> list = new ArrayList<Integer[]>();
		int counter = 0;
		// tagを送信するためにtag用配列追加
		ArrayList<NBTTagCompound> tag = new ArrayList<NBTTagCompound>();

		for (Map.Entry<SimpleStack, Integer> entry : EMCMapper.emc.entrySet())
		{
			SimpleStack stack = entry.getKey();

			if (stack == null)
			{
				continue;
			}

			Integer[] data = new Integer[] {stack.id, stack.qnty, stack.damage, entry.getValue()};
			list.add(data);
			// tag用配列にtag情報をセット
			tag.add(stack.tag);

			if (list.size() >= MAX_PKT_SIZE)
			{
				PacketHandlerEREMC.sendTo(new ClientSyncEmcPKT(counter, list, tag), player);
				list.clear();
				// list.clear();にならってtagもクリア 一回で送信しきれずデータを複数に分割して送るとき用
				tag.clear();
				counter++;
			}
		}

		if (list.size() > 0)
		{
			PacketHandlerEREMC.sendTo(new ClientSyncEmcPKT(-1, list, tag), player);
			list.clear();
			// 配列のデータをクリア
			tag.clear();
			counter++;
		}

		PELogger.logInfo("Sent EMC data packets to: " + player.getCommandSenderName());
		PELogger.logDebug("Total packets: " + counter);
	}

	public static void sendFragmentedEmcPacketToAll()
	{
		ArrayList<Integer[]> list = new ArrayList<Integer[]>();
		int counter = 0;
		// 上に同じ
		ArrayList<NBTTagCompound> tag = new ArrayList<NBTTagCompound>();

		for (Map.Entry<SimpleStack, Integer> entry : EMCMapper.emc.entrySet())
		{
			SimpleStack stack = entry.getKey();

			if (stack == null)
			{
				continue;
			}

			Integer[] data = new Integer[] {stack.id, stack.qnty, stack.damage, entry.getValue()};
			list.add(data);
			// 上に同じ
			tag.add(stack.tag);

			if (list.size() >= MAX_PKT_SIZE)
			{
				PacketHandlerEREMC.sendToAll(new ClientSyncEmcPKT(counter, list, tag));
				list.clear();
				// 上に同じ
				tag.clear();
				counter++;
			}
		}

		if (list.size() > 0)
		{
			PacketHandlerEREMC.sendToAll(new ClientSyncEmcPKT(-1, list, tag));
			list.clear();
			// 上に同じ
			tag.clear();
			counter++;
		}

		PELogger.logInfo("Sent EMC data packets to all players.");
		PELogger.logDebug("Total packets per player: " + counter);
	}

	//**
	// * Sends a packet to the server.<br>
	// * Must be called Client side.
	// *-/
	public static void sendToServer(IMessage msg)
	{
		HANDLER.sendToServer(msg);
	}

	//**
	// * Sends a packet to all the clients.<br>
	// * Must be called Server side.
	// *-/
	public static void sendToAll(IMessage msg)
	{
		HANDLER.sendToAll(msg);
	}

	//**
	// * Send a packet to all players around a specific point.<br>
	// * Must be called Server side.
	// *-/
	public static void sendToAllAround(IMessage msg, TargetPoint point)
	{
		HANDLER.sendToAllAround(msg, point);
	}

	//**
	// * Send a packet to a specific player.<br>
	// * Must be called Server side.
	// *-/
	public static void sendTo(IMessage msg, EntityPlayerMP player)
	{
		HANDLER.sendTo(msg, player);
	}

	//**
	// * Send a packet to all the players in the specified dimension.<br>
	// *  Must be called Server side.
	// *-/
	public static void sendToDimension(IMessage msg, int dimension)
	{
		HANDLER.sendToDimension(msg, dimension);
	}
}
//*/
