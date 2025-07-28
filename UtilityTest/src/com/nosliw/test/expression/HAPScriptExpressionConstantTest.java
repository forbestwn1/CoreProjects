package com.nosliw.test.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionScriptExpression;
import com.nosliw.core.application.division.manual.HAPManualUtilityScriptExpressionConstant;
import com.nosliw.core.runtimeenv.js.rhino.HAPRuntimeEnvironmentImpRhino;

public class HAPScriptExpressionConstantTest {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		String scriptExpression1 = "HaHaHa  <%=(&(fromConstant)&.value+&(constantValueInteger)&)+&(constantValueString)&%>  End!!!!";
		String scriptExpression = "<%=&(value1)&%>";
				
		Map<String, Object> constants = new LinkedHashMap<String, Object>();
		constants.put("constantValueString", "aaaaa");
		constants.put("constantValueInteger", Integer.valueOf(10000));
		constants.put("fromConstant", new JSONObject("{'value':'kkkkkkkkk'}"));
		constants.put("value1", "value1111111");
		
		HAPServiceData out = HAPManualUtilityScriptExpressionConstant.executeScriptExpressionConstant(new HAPDefinitionScriptExpression(scriptExpression, null), constants, runtimeEnvironment);

		System.out.println("--------------------   -------------------------");
		System.out.println(out);
		System.out.println();
	}
}
