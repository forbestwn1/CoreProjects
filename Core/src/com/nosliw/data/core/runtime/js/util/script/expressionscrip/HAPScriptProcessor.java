package com.nosliw.data.core.runtime.js.util.script.expressionscrip;

import com.nosliw.data.core.script.expression.HAPExecutableScript;

public interface HAPScriptProcessor {

	HAPOutputScriptProcessor processor(
		HAPExecutableScript scriptExe,
		String funciontParmName,
		String expressionsDataParmName,
		String constantsDataParmName,
		String variablesDataParmName
	);

}
