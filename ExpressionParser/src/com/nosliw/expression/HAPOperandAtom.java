package com.nosliw.expression;

import java.util.Map;

import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfo;

public abstract class HAPOperandAtom extends HAPOperandImp{

	public HAPOperandAtom(HAPDataTypeManager dataTypeMan) {
		super(dataTypeMan);
	}

	public HAPOperandAtom(HAPDataTypeInfo outDataTypeInfo, HAPDataTypeManager dataTypeMan) {
		super(outDataTypeInfo, dataTypeMan);
	}

	@Override
	public boolean isScriptRunnable(String script){return true;}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonDataTypeMap){}
}
