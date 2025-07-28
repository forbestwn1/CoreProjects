package com.nosliw.test.runtimerhino;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.data.HAPDataWrapper;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;

public class HAPRuntimeRhinoMain {

/*	
	private static void executeSuite(String suiteName, HAPRuntimeEnvironment runtimeEnvironment){
		//new runtime
		HAPRuntimeImpRhino runtime = (HAPRuntimeImpRhino)runtimeEnvironment.getRuntime();

		try{
			//parse to build expression object
			final HAPExpressionDefinitionSuiteImp suite = (HAPExpressionDefinitionSuiteImp)runtimeEnvironment.getTaskManager().getTaskDefinitionSuite(suiteName);
			HAPExpressionImp expression = (HAPExpressionImp)runtimeEnvironment.getExpressionManager().processExpression(null, "main", suiteName, null);
			Map<String, HAPData> varData = suite.getVariableData();
			
			//execute expression
			HAPRuntimeTask task1 = new HAPRuntimeTaskExecuteExpressionRhino(expression, varData);
			task1.registerListener(new HAPRunTaskEventListener(){
				@Override
				public void finish(HAPRuntimeTask task){
					try{
						HAPServiceData serviceData = task.getResult();
						processResult(suite, serviceData);
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
		
	}
	
	private static void processResult(HAPDefinitionTaskSuiteForTest suite, HAPServiceData resultServiceData){
		boolean success = true;
		String resultStr = "";
		String expecectResultStr = "";
		String suiteName = suite.getName();
		if(resultServiceData.isSuccess()){
			Object resultObj = resultServiceData.getData();
			HAPDataWrapper exprectResult = suite.getResult();
			HAPDataWrapper result = new HAPDataWrapper();
			result.buildObject(resultObj, HAPSerializationFormat.JSON);
			resultStr =  result + "";
			expecectResultStr = exprectResult +"";
			if(result.equals(exprectResult))	success = true;
			else		success = false;
		}
		else{
//			resultStr =  resultServiceData.getCode() + "";
//			expecectResultStr = suite.getErrorCode() +"";
//			if((resultServiceData.getCode()+"").equals(suite.getErrorCode()))  success = true;
//			else success = false;
		}
		
		System.out.println();
		if(success){
			System.out.println("***************************** Test "+ suite.getName() +"*****************************");
			System.out.println("******Result : ");
			System.out.println(resultStr);
			System.out.println("*****************************"+ "" +"*****************************");
			System.out.println();
		}
		else{
			System.err.println("***************************** Test "+ suite.getName() +"*****************************");
			System.err.println("******Result : ");
			System.err.println(resultStr);
			System.err.println("******Expect Result : ");
			System.err.println(expecectResultStr);
			System.err.println("*****************************"+ "" +"*****************************");
		}
		System.out.println();
	}
*/	
	
	private static void executeSuites(String[] suites, HAPRuntimeEnvironmentImpRhino runtimeEnvironment){
//		for(String suiteName : suites){
//			HAPDefinitionTaskSuiteForTest suite = (HAPDefinitionTaskSuiteForTest)runtimeEnvironment.getTaskManager().getTaskDefinitionSuite(suiteName);
//			HAPLogTask taskLog = new HAPLogTask();
//			HAPData out = runtimeEnvironment.getTaskManager().executeTask("name", suite, suite.getVariableData(), taskLog);
//			processResult(suite, HAPServiceData.createSuccessData(out));
//		}
	}
	
	public static void main(String[] args){
		
		//module init
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
//		HAPTaskDefinitionSuiteImporter.importTaskDefinitionSuiteFromFolder(HAPFileUtility.getClassFolderName(HAPExpressionTest.class), runtimeEnvironment.getTaskManager());
		
		executeSuites(new String[]{
//				"expression0",
//				"expression1",
//				"expression10",
//				"expression2",
//				"expression3",
//				"expression4",
//				"expression5",
//				"expression6",

//				"expression7",
//				"expression71",

//				"expression72",
//				"expression73",

				"expression8",
				
//				"expression9",
			}, runtimeEnvironment);
		
//		finally{
//			//shut down runtime
//			runtime.close();
//		}
		
	}
}
