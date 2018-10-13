package com.nosliw.test.task11;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.task111.HAPDefinitionTaskSuiteForTest;
import com.nosliw.data.core.task111.HAPExporterLog;
import com.nosliw.data.core.task111.HAPLogTask;
import com.nosliw.data.core.task111.HAPTaskDefinitionSuiteImporter;

public class HAPTaskMain {
/*
	public static void main(String[] args) {
		//module init
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPTaskDefinitionSuiteImporter.importTaskDefinitionSuiteFromFolder(HAPFileUtility.getClassFolderName(HAPTaskMain.class), runtimeEnvironment.getTaskManager());
		
		executeSuites(new String[]{
//				"expression0",
//				"expression1",
//				"expression10",
//				"expression2",
//				"expression3",
//				"expression4",
//				"expression5",
//				"expression51",
//				"expression6",

//				"expression7",
//				"expression71",

//				"expression72",
//				"expression73",

//				"expression8",
				
//				"expression9",
//				"school",
				"myrealtor",
			}, runtimeEnvironment);
		
//		finally{
//			//shut down runtime
//			runtime.close();
//		}
	}

	private static void processResult(HAPDefinitionTaskSuiteForTest suite, HAPServiceData resultServiceData){
		boolean success = true;
		String resultStr = "";
		String expecectResultStr = "";
		String suiteName = suite.getName();
		if(resultServiceData.isSuccess()){
			HAPDataWrapper result = (HAPDataWrapper)resultServiceData.getData();
			HAPDataWrapper exprectResult = suite.getResult();
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
	
	private static void executeSuites(String[] suites, HAPRuntimeEnvironmentImpRhino runtimeEnvironment){
		for(String suiteName : suites){
			HAPDefinitionTaskSuiteForTest suite = (HAPDefinitionTaskSuiteForTest)runtimeEnvironment.getTaskManager().getTaskDefinitionSuite(suiteName);
			HAPLogTask taskLog = new HAPLogTask();
			HAPData out = runtimeEnvironment.getTaskManager().executeTask("main", suite, suite.getVariableData(), taskLog);
			
			HAPExporterLog.exportLog(suiteName, taskLog);
			
			processResult(suite, HAPServiceData.createSuccessData(out));
		}
	}
*/
}
