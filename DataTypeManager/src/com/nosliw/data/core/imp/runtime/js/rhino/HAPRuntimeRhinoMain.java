package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeManager;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.expression.HAPExpressionImp;
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
		
		HAPDataTypeHelper dataTypeHelper = new HAPDataTypeHelperImp();
		
		
		HAPExpressionManagerImp expressionMan = new HAPExpressionManagerImp(new HAPExpressionParserImp(), dataTypeHelper);
		expressionMan.importExpressionFromFolder(HAPFileUtility.getClassFolderPath(HAPRuntimeRhinoMain.class));
		
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.getExpression("expression1");
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.getExpression("expression2");
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.getExpression("expression3");
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.getExpression("expression4");
		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.getExpression("expression5");
		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
		
		HAPRuntimeImpJSRhino runtime = new HAPRuntimeImpJSRhino(new HAPResourceDiscoveryJSImp(), resourceMan);
		runtime.start();
		HAPData out = runtime.executeExpression(expression);
		runtime.close();
	}
}
