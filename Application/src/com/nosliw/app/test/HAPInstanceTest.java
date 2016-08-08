package com.nosliw.app.test;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.app.instance.HAPApplicationInstance;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperand;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;
import com.nosliw.expression.HAPExpressionParser;
import com.nosliw.uiresource.HAPUIResource;

public class HAPInstanceTest {

	public static void main(String[] args) {

		HAPApplicationInstance instance = HAPApplicationInstance.getApplicationInstantce();
//		HAPDataTypeManager dataTypeMan = instance.getDataTypeManager();
		
//		System.out.println(instance.getEntityDefinitionManager().toString());

		System.out.println(instance.getDataTypeManager().toString());
		
		
//		String str = "!(integer:simple)!.largerThan(?(bb)?, :(cc):)";
		String str = ":(test1):.cascade(!(string:simple)!.toString(?(bb)?).cascade(?(aa)?))";
//		HAPOperand operand = HAPExpressionParser.parseExpression(str, instance.getDataTypeManager());
//		System.out.println(operand.toString());
	
//		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>();
//		constants.put("aa", dataTypeMan.STRING.createDataByValue(" constantValue "));
//		constants.put("bb", dataTypeMan.BOOLEAN.createDataByValue(false));
//		
//		HAPExpression expression = new HAPExpression(new HAPExpressionInfo(str, constants, null), dataTypeMan);
//		HAPData out = expression.execute(null, null);
//		System.out.println(out.toString());
		
//		HAPUIResource resource = instance.getUIResourceManager().getUIResource("business");
//		System.out.println(resource.toString());
	}

}

