package com.nosliw.uiresource.page.execute;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.script.expression.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

public class HAPUtilityExecutable {

	public static List<HAPScriptExpression> getScriptExpressionFromExeUnit(HAPExecutableUIUnit exeUnit){
		List<HAPScriptExpression> out = new ArrayList<HAPScriptExpression>();
		for(HAPUIEmbededScriptExpressionInContent scriptExpressionInConent : exeUnit.getScriptExpressionsInContent())  out.addAll(scriptExpressionInConent.getScriptExpressionsList());
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : exeUnit.getScriptExpressionsInAttribute())  out.addAll(scriptExpressionInAttribute.getScriptExpressionsList()); 
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : exeUnit.getScriptExpressionsInTagAttribute())  out.addAll(scriptExpressionInAttribute.getScriptExpressionsList()); 
		return out;
	}

	public static List<HAPEmbededScriptExpression> getEmbededScriptExpressionFromExeUnit(HAPExecutableUIUnit exeUnit){
		List<HAPEmbededScriptExpression> out = new ArrayList<HAPEmbededScriptExpression>();
		out.addAll(exeUnit.getScriptExpressionsInContent());
		out.addAll(exeUnit.getScriptExpressionsInAttribute());
		out.addAll(exeUnit.getScriptExpressionsInTagAttribute());
		return out;
	}
	
}
