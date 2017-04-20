package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.imp.criteria.HAPDataTypeCriteriaManagerImp;
import com.nosliw.data.core.imp.expression.HAPExpressionManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSConverter;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSHelper;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSLibrary;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSOperation;
import com.nosliw.data.core.runtime.js.HAPResourceManagerJS;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImp;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPRuntimeRhinoMain {

	public void main(String[] args){
		
		HAPResourceManagerJS resourceMan = new HAPResourceManagerJS();
		
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_DATATYPEOPERATION, new HAPResourceManagerJSOperation());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, new HAPResourceManagerJSConverter());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_LIBRARY, new HAPResourceManagerJSLibrary());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_HELPER, new HAPResourceManagerJSHelper());
		
		HAPExpressionManagerImp expressionMan = new HAPExpressionManagerImp(new HAPExpressionParserImp(), new HAPDataTypeCriteriaManagerImp());

		expressionMan.registerExpressionInfo("main", null);
		HAPExpression expression = expressionMan.processExpressionInfo("main");
		
		HAPRuntimeImp runtime = new HAPRuntimeImp(new HAPResourceDiscoveryJSImp(), resourceMan);
		HAPData out = runtime.executeExpression(expression);
		System.out.println(out.toString());
	}
}
