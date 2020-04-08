package com.nosliw.data.core.script.expression;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPExecutableScript {

	@HAPAttribute
	public static String SCRIPTTYPE = "scriptType";

	@HAPAttribute
	public static String ID = "id";

	String getScriptType();

	String getId();
}
