package com.nosliw.application.runtimerhino;

import java.util.Map;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.imp.expression.HAPExpressionDefinitionSuiteImp;
import com.nosliw.data.core.imp.expression.HAPExpressionImp;
import com.nosliw.data.core.imp.expression.HAPExpressionManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPRuntimeEnvironmentImpJS;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.rhino.HAPExpressionTaskRhino;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;

public class HAPRuntimeRhinoMain {

	private static void executeSuite(String suiteName, HAPRuntimeEnvironment runtimeEnvironment){
		//new runtime
		HAPRuntimeImpRhino runtime = new HAPRuntimeImpRhino(runtimeEnvironment);

		//runtim init
		runtime.start();

		try{
			//parse to build expression object
			final HAPExpressionDefinitionSuiteImp suite = (HAPExpressionDefinitionSuiteImp)runtimeEnvironment.getExpressionManager().getExpressionDefinitionSuite(suiteName);
			HAPExpressionImp expression = (HAPExpressionImp)runtimeEnvironment.getExpressionManager().processExpression(suiteName, "main", null);
			Map<String, HAPData> varData = suite.getVariableData();
			
			//execute expression
			runtime.executeExpressionTask(new HAPExpressionTaskRhino(expression, varData){
				@Override
				public void doSuccess() {
					try{
						Object result = getResult();
						HAPDataWrapper exprectResult = suite.getResult();
						System.out.println("Expression Result : " + result);
						if(result.equals(exprectResult)){
						}
						else{
							throw new Exception();
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			//shut down runtime
			runtime.close();
		}
		
	}
	
	public static void main(String[] args){
		
		//module init
		HAPModuleRuntimeJS runtimeJSModule = new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance());;
		
		HAPRuntimeEnvironmentImpJS runtimeEnvironment = new HAPRuntimeEnvironmentImpJS(runtimeJSModule);
		
		HAPExpressionManagerImp expressionMan = (HAPExpressionManagerImp)runtimeEnvironment.getExpressionManager();
		expressionMan.importExpressionFromFolder(HAPFileUtility.getClassFolderName(HAPRuntimeRhinoMain.class));
		
		executeSuite("expression6", runtimeEnvironment);
		
		for(String suiteName : expressionMan.getExpressionDefinitionSuites()){
//			executeSuite(suiteName, expressionMan, dataTypeMan, resourceMan);
		}
		
		
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression1", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression2", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression3", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));

//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression5", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
		
		
//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression4", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));

//		HAPExpressionImp expression = (HAPExpressionImp)expressionMan.processExpression("expression7", null);
//		System.out.println(HAPJsonUtility.formatJson(expression.toStringValue(HAPSerializationFormat.JSON)));
		
//		HAPRuntimeImpJSRhino runtime = new HAPRuntimeImpJSRhino(new HAPResourceDiscoveryJSImp(), resourceMan);
//		try{
//			runtime.start();
//			
//			runtime.loadScriptFromFile("loadResource1.js", HAPRuntimeRhinoMain.class, null, null);
//			
//			HAPDataWrapper baseData = new HAPDataWrapper("{dataTypeId:\"test.string;1.0.0\", value:\"This is my world!\"}");
//			HAPDataWrapper baseDataUrl = new HAPDataWrapper("{dataTypeId:\"test.url;1.0.0\", value:\"This is my world!\"}");
//			HAPDataWrapper fromData = new HAPDataWrapper("{dataTypeId:\"test.integer;1.0.0\", value:2}");
//			HAPDataWrapper toData = new HAPDataWrapper("{dataTypeId:\"test.integer;1.0.0\", value:7}");
//			Map<String, HAPData> varData = new LinkedHashMap<String, HAPData>();
//			varData.put("fromVar", fromData);
//			varData.put("toVar", toData);
//			varData.put("baseVar", baseData);
//			varData.put("baseVarUrl", baseDataUrl);
//			
//			runtime.executeExpressionTask(new HAPExpressionTaskRhino(expression, varData){
//				@Override
//				public void doSuccess() {
//					Object result = getResult();
//					System.out.println("Expression Result : " + result);
//				}
//			});
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		finally{
//			runtime.close();
//		}
	}
}
