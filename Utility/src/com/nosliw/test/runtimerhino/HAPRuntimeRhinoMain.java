package com.nosliw.test.runtimerhino;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.imp.expression.HAPExpressionDefinitionSuiteImp;
import com.nosliw.data.core.imp.expression.HAPExpressionImp;
import com.nosliw.data.core.imp.expression.HAPExpressionManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPRuntimeEnvironmentImpJS;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.expression.test.HAPExpressionTest;

public class HAPRuntimeRhinoMain {

	private static void executeSuite(String suiteName, HAPRuntimeEnvironment runtimeEnvironment){
		//new runtime
		HAPRuntimeImpRhino runtime = (HAPRuntimeImpRhino)runtimeEnvironment.getRuntime();

		try{
			//parse to build expression object
			final HAPExpressionDefinitionSuiteImp suite = (HAPExpressionDefinitionSuiteImp)runtimeEnvironment.getExpressionManager().getExpressionDefinitionSuite(suiteName);
			HAPExpressionImp expression = (HAPExpressionImp)runtimeEnvironment.getExpressionManager().processExpression(suiteName, "main", null);
			Map<String, HAPData> varData = suite.getVariableData();
			
			//execute expression
			HAPRuntimeTask task1 = new HAPRuntimeTaskExecuteExpressionRhino(expression, varData);
			task1.registerListener(new HAPRunTaskEventListener(){
				@Override
				public void finish(HAPRuntimeTask task){
					try{
						HAPServiceData serviceData = task.getResult();
						if(serviceData.isSuccess()){
							Object result = serviceData.getData();
							HAPDataWrapper exprectResult = suite.getResult();
							System.out.println("Expression Result : " + result);
							if(result.equals(exprectResult)){
							}
							else{
								throw new Exception();
							}
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

			runtime.executeTask(task1);
			
//			HAPRuntimeTask task2 = new HAPRuntimeTaskExecuteExpressionRhino(expression, varData);
//			Object result = runtime.executeTaskSync(task2).getData();
//			System.out.println("Expression Result : " + result);
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
		HAPRuntimeEnvironmentImpJS runtimeEnvironment = new HAPRuntimeEnvironmentImpJS();
		
		//start runtime
		runtimeEnvironment.getRuntime().start();
		
		HAPExpressionManagerImp expressionMan = (HAPExpressionManagerImp)runtimeEnvironment.getExpressionManager();
		expressionMan.importExpressionFromFolder(HAPFileUtility.getClassFolderName(HAPExpressionTest.class));
		
		executeSuite("expression7", runtimeEnvironment);
		
//		for(String suiteName : expressionMan.getExpressionDefinitionSuites()){
//			executeSuite(suiteName, runtimeEnvironment);
//		}
		
		
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
