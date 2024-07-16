package com.nosliw.data.core.script.expression1.imp.literate;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptWithSegmentImp;


public class HAPExecutableScriptSegExpressionScript extends HAPExecutableScriptWithSegmentImp{

	public HAPExecutableScriptSegExpressionScript(String id) {
		super(id);
	}
	
	@Override
	public String getScriptType() {   return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTCOMPLEX;  }

}
