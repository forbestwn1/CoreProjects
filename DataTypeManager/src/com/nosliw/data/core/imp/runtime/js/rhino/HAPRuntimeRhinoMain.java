package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeManager;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.criteria.HAPDataTypeCriteriaManagerImp;
import com.nosliw.data.core.imp.expression.HAPExpressionManagerImp;
import com.nosliw.data.core.imp.init.HAPModuleInit;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSConverter;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSHelper;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSLibrary;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSOperation;
import com.nosliw.data.core.runtime.js.HAPResourceManagerJS;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpJSRhino;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPRuntimeRhinoMain {

	public static void main(String[] args){
		
		HAPModuleInit.init();
		
		HAPResourceManagerJS resourceMan = new HAPResourceManagerJS();
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_DATATYPEOPERATION, new HAPResourceManagerJSOperation());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, new HAPResourceManagerJSConverter());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, new HAPResourceManagerJSLibrary());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, new HAPResourceManagerJSHelper());
		
		HAPDataTypeManager dataTypeMan = new HAPDataTypeManagerImp();
		
		HAPDataTypeCriteriaManager criteriaMan = new HAPDataTypeCriteriaManagerImp();
		
		
		HAPExpressionManagerImp expressionMan = new HAPExpressionManagerImp(new HAPExpressionParserImp(criteriaMan, dataTypeMan), criteriaMan);
		expressionMan.importExpressionFromFolder(HAPFileUtility.getClassFolderPath(HAPRuntimeRhinoMain.class));
		
		HAPExpression expression = expressionMan.getExpression("expression1");
		
		HAPRuntimeImpJSRhino runtime = new HAPRuntimeImpJSRhino(new HAPResourceDiscoveryJSImp(), resourceMan);
		HAPData out = runtime.executeExpression(expression);
		System.out.println(out.toString());
	}
}
