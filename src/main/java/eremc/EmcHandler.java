package eremc;

import java.lang.reflect.Method;

import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class EmcHandler
{
	private static boolean isLoadBCTp = false;
	private static boolean isLoadBCSl = false;
	private static boolean isLoadIC2 = false;
	private static boolean isLoadEUtil = false;

	/**
	 * ic2のAPIを参考にしました。
	 *
	 */
	public static void registerEMC() {
		checkLoad();

		// バニラの板ガラスとエンドストーンにEMCを追加
		ProjectEAPI.registerCustomEMC(new ItemStack(Blocks.glass_pane, 1), 1);
		ProjectEAPI.registerCustomEMC(new ItemStack(Blocks.end_stone), 64);
		// ExtraUtilitiesの関係で石のハーフブロックに登録
		ProjectEAPI.registerCustomEMC(new ItemStack(Blocks.stone_slab, 1), 1);
		if (EmcHandler.isLoadBCTp) {
			try {
				IndefiniteClassLoader bcTp = new IndefiniteClassLoader("buildcraft.BuildCraftTransport");
				Item wire = bcTp.getItem("pipeWire");
				Item pipeItemStone = bcTp.getItem("pipeItemsStone");
				Item pipeItemsCobblestone = bcTp.getItem("pipeItemsCobblestone");
				// メソッド取得
				IndefiniteClassLoader gateDefinition = new IndefiniteClassLoader("buildcraft.transport.gates.GateDefinition");
				IndefiniteClassLoader bcItemGate = new IndefiniteClassLoader("buildcraft.transport.gates.ItemGate");
				Method makeGateItem = bcItemGate.getMethod("makeGateItem", gateDefinition.getEnumAsClass("GateMaterial"), gateDefinition.getEnumAsClass("GateLogic"));

				// ワイヤー
				ProjectEAPI.registerCustomEMC(new ItemStack(wire, 1, 0), 840);
				ProjectEAPI.registerCustomEMC(new ItemStack(wire, 1, 1), 2960);
				ProjectEAPI.registerCustomEMC(new ItemStack(wire, 1, 2), 840);
				ProjectEAPI.registerCustomEMC(new ItemStack(wire, 1, 3), 840);
				// 石と丸石のパイプ
				ProjectEAPI.registerCustomEMC(new ItemStack(pipeItemStone), 1);
				ProjectEAPI.registerCustomEMC(new ItemStack(pipeItemsCobblestone), 1);
				// 一体型,タイマー型,コンパレータ型などはaddGateExpansionメソッドで設定できるのだと思う
				ItemStack rs = (ItemStack) makeGateItem.invoke(null, gateDefinition.getEnumField("GateMaterial", "REDSTONE"), gateDefinition.getEnumField("GateLogic", "AND"));
				ItemStack iand = (ItemStack) makeGateItem.invoke(null, gateDefinition.getEnumField("GateMaterial", "IRON"), gateDefinition.getEnumField("GateLogic", "AND"));
				ItemStack ior = (ItemStack) makeGateItem.invoke(null, gateDefinition.getEnumField("GateMaterial", "IRON"), gateDefinition.getEnumField("GateLogic", "OR"));
				ItemStack gand = (ItemStack) makeGateItem.invoke(null, gateDefinition.getEnumField("GateMaterial", "GOLD"), gateDefinition.getEnumField("GateLogic", "AND"));
				ItemStack gor = (ItemStack) makeGateItem.invoke(null, gateDefinition.getEnumField("GateMaterial", "GOLD"), gateDefinition.getEnumField("GateLogic", "OR"));
				ItemStack dand = (ItemStack) makeGateItem.invoke(null, gateDefinition.getEnumField("GateMaterial", "DIAMOND"), gateDefinition.getEnumField("GateLogic", "AND"));
				ItemStack dor = (ItemStack) makeGateItem.invoke(null, gateDefinition.getEnumField("GateMaterial", "DIAMOND"), gateDefinition.getEnumField("GateLogic", "OR"));
				ProjectEAPI.registerCustomEMC(rs, 5296);
				ProjectEAPI.registerCustomEMC(iand, 12240);
				ProjectEAPI.registerCustomEMC(ior , 12240);
				ProjectEAPI.registerCustomEMC(gand, 21088);
				ProjectEAPI.registerCustomEMC(gor , 21088);
				ProjectEAPI.registerCustomEMC(dand, 47344);
				ProjectEAPI.registerCustomEMC(dor , 47344);

				// NBTもちゃんとコピーするように設定
				ProjectEAPI.registerCondenserNBTException(rs);
				ProjectEAPI.registerCondenserNBTException(iand);
				ProjectEAPI.registerCondenserNBTException(ior);
				ProjectEAPI.registerCondenserNBTException(gand);
				ProjectEAPI.registerCondenserNBTException(gor);
				ProjectEAPI.registerCondenserNBTException(dand);
				ProjectEAPI.registerCondenserNBTException(dor);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (EmcHandler.isLoadBCSl) {
			try {
				Class bcSl = Class.forName("buildcraft.BuildCraftSilicon");

				Item item = null;
				Object ret = bcSl.getField("redstoneChipset").get(null);
				if (ret instanceof Item)
					item = (Item)ret;

				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 0), 1808);
				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 1), 2320);
				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 2), 5904);
				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 3), 18192);
				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 4), 9640);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (EmcHandler.isLoadIC2) {
			try {
				IndefiniteClassLoader ic2Items = new IndefiniteClassLoader("ic2.api.item.IC2Items");
				Method getItem = ic2Items.getMethod("getItem", String.class);
				// 合金
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "mixedMetalIngot"), 2016);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "advancedAlloy"), 2016);

				// プレート
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "platecopper"), 128);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "platetin"),  256);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "platebronze"), 160);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "plategold"), 2048);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "plateiron"), 256);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "platelead"), 512);

				// ケーシング
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "casingcopper"), 64);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "casingtin"), 128);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "casingbronze"), 80);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "casinggold"), 1024);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "casingiron"), 128);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "casinglead"), 256);

				// ケーブル
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "copperCableItem"), 64);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "tinCableItem"), 85);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "goldCableItem"), 512);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "ironCableItem"), 64);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "insulatedCopperCableItem"), 96);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "insulatedTinCableItem"), 117);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "insulatedGoldCableItem"), 576);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "insulatedIronCableItem"), 160);

				// 強化ガラス・石材
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "reinforcedGlass"), 4039);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "reinforcedStone"), 4039);

				// 電池
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "reBattery"), 757);

				// セル
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "cell"), 85);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "lavaCell"), 149);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "waterCell"), 85);

				// フェンス
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "ironFence"), 128);

				// 足場
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "scaffold"), 9);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "ironScaffold"), 120);

				// ツリータップ
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "treetap"), 40);

				// ラバーシート
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "rubberSapling"), 64);

				// コーヒー
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "coffeeBeans"), 64);
				ProjectEAPI.registerCustomEMC((ItemStack) getItem.invoke(null, "coffeePowder"), 64);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// ExtraUtilitiesの登録処理 (素材のEmc合計そのままで設定)
		if (EmcHandler.isLoadEUtil) {
			try {
				IndefiniteClassLoader eUtil = new IndefiniteClassLoader("com.rwtema.extrautils.ExtraUtils");
				Block transferNodeRemote = eUtil.getBlock("transferNodeRemote");
				Block transferNode		 = eUtil.getBlock("transferNode");
				Item  nodeUpgrade		 = eUtil.getItem("nodeUpgrade");
				Block enderQuarryUpgrade = eUtil.getBlock("enderQuarryUpgrade");
				Block enderMarker		 = eUtil.getBlock("enderMarker");
				Block magnumTorch		 = eUtil.getBlock("magnumTorch");

				ProjectEAPI.registerCustomEMC(new ItemStack(transferNodeRemote, 1, 0), 19990);	// (Items)
				ProjectEAPI.registerCustomEMC(new ItemStack(transferNodeRemote, 1, 6), 17426);	// (Liquids)
				ProjectEAPI.registerCustomEMC(new ItemStack(transferNode, 1, 12), 14941);		// (Energy)
				ProjectEAPI.registerCustomEMC(new ItemStack(transferNode, 1, 13), 162652);		// (Hyper Energy)
				ProjectEAPI.registerCustomEMC(new ItemStack(nodeUpgrade, 1, 5), 5768);			// (Transmitter)
				ProjectEAPI.registerCustomEMC(new ItemStack(nodeUpgrade, 1, 6), 5952);			// (Receiver)
				ProjectEAPI.registerCustomEMC(new ItemStack(enderQuarryUpgrade, 1, 0), 2322);	// (Base)
				ProjectEAPI.registerCustomEMC(new ItemStack(enderMarker), 1664);
				ProjectEAPI.registerCustomEMC(new ItemStack(magnumTorch), 68430);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void checkLoad()
	{
		for (ModContainer mod : Loader.instance().getModList()) {
			if (mod.getModId().equals("BuildCraft|Transport"))
				EmcHandler.isLoadBCTp = true;
			if (mod.getModId().equals("BuildCraft|Silicon"))
				EmcHandler.isLoadBCSl = true;
			if (mod.getModId().equals("IC2"))
				EmcHandler.isLoadIC2 = true;
			// ExtraUtilitiesを追加
			if (mod.getModId().equals("ExtraUtilities"))
				EmcHandler.isLoadEUtil = true;
		}

	}

}
