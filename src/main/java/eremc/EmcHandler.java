package eremc;

import ic2.api.item.IC2Items;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import buildcraft.transport.gates.GateDefinition;
import buildcraft.transport.gates.GateDefinition.GateLogic;
import buildcraft.transport.gates.GateDefinition.GateMaterial;
import buildcraft.transport.gates.ItemGate;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class EmcHandler
{
	private static boolean isLoadBCTp = false;
	private static boolean isLoadBCSl = false;
	private static boolean isLoadIC2 = false;

	/**
	 * ic2のAPIを参考にしました。
	 *
	 * 雛形↓
	 * ProjectEAPI.registerCustomEMC(IC2Items.getItem(""), );
	 * ↑
	 */
	public static void registerEMC() {
		checkLoad();

		// バニラの板ガラスを追加(1EMC)
		ProjectEAPI.registerCustomEMC(new ItemStack(Blocks.glass_pane, 1), 1);

		if (EmcHandler.isLoadBCTp) {
			try {
				Class<?> bcTp = Class.forName("buildcraft.BuildCraftTransport");

				Item item = null;
				Object ret = bcTp.getField("pipeWire").get(null);
				if (ret instanceof Item)
					item = (Item)ret;

				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 0), 840);
				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 1), 2960);
				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 2), 840);
				ProjectEAPI.registerCustomEMC(new ItemStack(item, 1, 3), 840);
				// 一体型,タイマー型,コンパレータ型などはaddGateExpansionメソッドで設定できるのだと思う
				ItemStack rs = ItemGate.makeGateItem(GateMaterial.REDSTONE, GateLogic.AND);
				ItemStack iand = ItemGate.makeGateItem(GateDefinition.GateMaterial.IRON, GateDefinition.GateLogic.AND);
				ItemStack ior = ItemGate.makeGateItem(GateDefinition.GateMaterial.IRON, GateDefinition.GateLogic.OR);
				ItemStack gand = ItemGate.makeGateItem(GateDefinition.GateMaterial.GOLD, GateDefinition.GateLogic.AND);
				ItemStack gor = ItemGate.makeGateItem(GateDefinition.GateMaterial.GOLD, GateDefinition.GateLogic.OR);
				ItemStack dand = ItemGate.makeGateItem(GateDefinition.GateMaterial.DIAMOND, GateDefinition.GateLogic.AND);
				ItemStack dor = ItemGate.makeGateItem(GateDefinition.GateMaterial.DIAMOND, GateDefinition.GateLogic.OR);
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
				// TODO 自動生成された catch ブロック
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
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		if (EmcHandler.isLoadIC2) {
			// 合金
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("mixedMetalIngot"), 2016);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("advancedAlloy"), 2016);

			// プレート
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("platecopper"), 128);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("platetin"),  256);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("platebronze"), 160);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("plategold"), 2048);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("plateiron"), 256);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("platelead"), 512);

			// ケーシング
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("casingcopper"), 64);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("casingtin"), 128);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("casingbronze"), 80);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("casinggold"), 1024);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("casingiron"), 128);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("casinglead"), 256);

			// ケーブル
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("copperCableItem"), 64);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("tinCableItem"), 85);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("goldCableItem"), 512);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("ironCableItem"), 64);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("insulatedCopperCableItem"), 96);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("insulatedTinCableItem"), 117);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("insulatedGoldCableItem"), 576);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("insulatedIronCableItem"), 160);

			// 強化ガラス・石材
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("reinforcedGlass"), 4039);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("reinforcedStone"), 4039);

			// 電池
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("reBattery"), 757);

			// セル
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("cell"), 85);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("lavaCell"), 149);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("waterCell"), 85);

			// フェンス
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("ironFence"), 128);

			// 足場
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("scaffold"), 9);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("ironScaffold"), 120);

			// ツリータップ
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("treetap"), 40);

			// ラバーシート
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("rubberSapling"), 64);

			// コーヒー
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("coffeeBeans"), 64);
			ProjectEAPI.registerCustomEMC(IC2Items.getItem("coffeePowder"), 64);

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
		}

	}

}
