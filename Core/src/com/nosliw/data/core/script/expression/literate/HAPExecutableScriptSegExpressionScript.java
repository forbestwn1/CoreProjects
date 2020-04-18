package com.nosliw.data.core.script.expression.literate;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.expression.HAPExecutableScriptWithSegmentImp;

public class HAPExecutableScriptSegExpressionScript extends HAPExecutableScriptWithSegmentImp{

	public HAPExecutableScriptSegExpressionScript(String id) {
		super(id);
	}
	
	@Override
	public String getScriptType() {   return HAPConstant.SCRIPT_TYPE_SEG_EXPRESSIONSCRIPT;  }

}
