package com.nosliw.data.core.imp.runtime.js.rhino;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeManager;
import com.nosliw.data.core.HAPDataWrapper;
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
import com.nosliw.data.core.runtime.js.rhino.HAPExpressionTaskRhino;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpJSRhino;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPRuntimeRhinoMain {

	public static void main(String[] args){
		
		HAPModuleInit.init();
		
		HAPResourceManagerJS resourceMan = new HAPResourceManagerJS();
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION, new HAPResourceManagerJSOperation());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, new HAPResourceManagerJSConverter());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, new HAPResourceManagerJSLibrary());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, new HAPResourceManagerJSHelper());
		
		HAPDataTypeManager dataTypeMan = new HAPDataTypeManagerImp();
		
		HAPDataTypeHelper dataTypeHelper = new HAPDataTypeHelperImp();
		
		
		HAPExpressionManagerImp expressionMan = new HAPExpressionManagerImp(new HAPExpressionParserImp(), dataTypeHelper);
		expressionMan.importExpressionFromFolder(HAPFileUtility.getClassFolderPath(HAPRuntimeRhinoMain.class));
		
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression1", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression2", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression3", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));

		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression5", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
		
		
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression4", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
		
		HAPRuntimeImpJSRhino runtime = new HAPRuntimeImpJSRhino(new HAPResourceDiscoveryJSImp(), resourceMan);
		try{
			runtime.start();
			
			runtime.loadScriptFromFile("loadResource1.js", HAPRuntimeRhinoMain.class, null, null);
			
			HAPDataWrapper baseData = new HAPDataWrapper("{dataTypeId:\"base.string;1.0.0\", value:\"This is my world!\"}");
			HAPDataWrapper fromData = new HAPDataWrapper("{dataTypeId:\"base.integer;1.0.0\", value:2}");
			HAPDataWrapper toData = new HAPDataWrapper("{dataTypeId:\"base.integer;1.0.0\", value:7}");
			Map<String, HAPData> varData = new LinkedHashMap<String, HAPData>();
			varData.put("fromVar", fromData);
			varData.put("toVar", toData);
			varData.put("baseVar", baseData);
			
			runtime.executeExpressionTask(new HAPExpressionTaskRhino(expression, varData){
				@Override
				public void doSuccess() {
					Object result = getResult();
					System.out.println("Expression Result : " + result);
				}
			});
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			runtime.close();
		}
	}
}
