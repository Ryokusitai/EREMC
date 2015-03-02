package eremc.asm;

import java.util.HashMap;
import java.util.Map;

public class MethodNameList
{
	private static MethodNameList instance = new MethodNameList();
	public static MethodNameList getInstance() {
		return instance;
	}

	// デバッグ時と実際の環境での動作時ではメソッド名が変わるためその切り替えを行う
	private final boolean DEBUG = false;

	private Map<String, String> methodNameList = new HashMap<String,String>();
	private MethodNameList() {// areItemStacksEqual = func_77970_a
		methodNameList.put("areItemStacksEqual", "func_77970_a");
		methodNameList.put("stackTagCompound","field_77990_d");
		methodNameList.put("getCommandSenderName","func_70005_c_");
	}

	public static String getName(String methodName) {
		if (instance.methodNameList.containsKey(methodName)) {
			if (instance.DEBUG) {
				return methodName;
			} else {
				return instance.methodNameList.get(methodName);
			}
		}
		throw new IllegalArgumentException(methodName + "はリストに登録されていません");
	}

}
