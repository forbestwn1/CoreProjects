package com.nosliw.test.runtimerhino;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.imp.expression.HAPExpressionDefinitionSuiteImp;
import com.nosliw.data.core.imp.expression.HAPExpressionImp;
import com.nosliw.data.core.imp.expression.HAPExpressionImporter;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
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
			HAPExpressionImp expression = (HAPExpressionImp)runtimeEnvironment.getExpressionManager().processExpression(null, suiteName, "main", null);
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
	
	private static void processResult(HAPExpressionDefinitionSuiteImp suite, HAPServiceData resultServiceData){
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
			resultStr =  resultServiceData.getCode() + "";
			expecectResultStr = suite.getErrorCode() +"";
			if((resultServiceData.getCode()+"").equals(suite.getErrorCode()))  success = true;
			else success = false;
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
	
	private static void executeSuites(String[] suites, HAPRuntimeEnvironmentImpRhino runtimeEnvironment){
		for(String suiteName : suites){
			executeSuite(suiteName, runtimeEnvironment);
		}
		
	}
	
	public static void main(String[] args){
		
		//module init
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		//start runtime
		runtimeEnvironment.getRuntime().start();
		
		HAPExpressionImporter.importExpressionSuiteFromFolder(HAPFileUtility.getClassFolderName(HAPExpressionTest.class), runtimeEnvironment.getExpressionManager());
		
		executeSuites(new String[]{
				"expression0",
				"expression1",
				"expression10",
				"expression2",
				"expression3",
				"expression4",
				"expression5",
				"expression6",

//				"expression7",
				"expression71",
//				"expression8",
				
//				"expression9",
			}, runtimeEnvironment);
		
//		for(String suiteName : runtimeEnvironment.getExpressionManager().getExpressionDefinitionSuites()){
//			executeSuite(suiteName, runtimeEnvironment);
//		}
		
//		finally{
//			//shut down runtime
//			runtime.close();
//		}
		
	}
}
