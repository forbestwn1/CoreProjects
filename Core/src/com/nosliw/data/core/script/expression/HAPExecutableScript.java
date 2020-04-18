package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;

public interface HAPExecutableScript {

	@HAPAttribute
	public static String SCRIPTTYPE = "scriptType";

	@HAPAttribute
	public static String ID = "id";

	String getScriptType();

	String getId();

	Set<HAPVariableInfo> getVariablesInfo();
	
	Set<HAPDefinitionConstant> getConstantsDefinition();
	void updateConstant(Map<String, Object> value);
}
