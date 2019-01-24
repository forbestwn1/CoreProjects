package com.nosliw.test.uisource;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.processor.HAPProcessorUIConstant;

public class HAPUIResourceConstantTest {
/*
	public static void main(String[] agrs) throws JSONException{

		//module init
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		String file = HAPFileUtility.getFileNameOnClassPath(HAPUIResourceTest.class, "Constants.txt");
		String fileContent = HAPFileUtility.readFile(new File(file));
		JSONObject rootJson = new JSONObject(fileContent);

		//expected output
		Map<String, Object> constantsExprectOutput = new LinkedHashMap<String, Object>();
		JSONObject expectOutputJson = rootJson.getJSONObject("expectedOutput");
		Iterator<String> outputConstantNames = expectOutputJson.keys();
		while(outputConstantNames.hasNext()){
			String name = outputConstantNames.next();
			constantsExprectOutput.put(name, expectOutputJson.get(name));
		}
		
		Map<String, HAPConstantDef> constantsDef = new LinkedHashMap<String, HAPConstantDef>();
		JSONObject constantsJson = rootJson.getJSONObject("constants");
		Iterator<String> defNames = constantsJson.keys();
		while(defNames.hasNext()){
			String defName = defNames.next();
			constantsDef.put(defName, new HAPConstantDef(constantsJson.get(defName)));
		}
		
		//process constant defs
		HAPIdGenerator idGenerator = new HAPIdGenerator(1);
		HAPDefinitionUIUnitResource uiResource = new HAPDefinitionUIUnitResource(null, null);
		uiResource.setConstantDefs(constantsDef);
		HAPConstantProcessor.processConstantDefs(uiResource, null, runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getRuntime());
		//compare output
		for(String name : constantsDef.keySet()){
			HAPConstantDef constantDef = constantsDef.get(name);
			compareOutput(name, constantDef, constantsExprectOutput.get(name));
		}
	}

	private static void compareOutput(String name, HAPConstantDef constantDef, Object expected){
		boolean result = false;
		if(expected instanceof JSONObject || expected instanceof JSONArray){
			result = constantDef.getValue().toString().equals(expected.toString());
		}
		else{
			result = constantDef.getValue().equals(expected);
		}

		System.out.println();
		System.out.println();
		if(result)		System.out.println("------   " + name +  "   ------");
		else  		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXX   " + name +  "   XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println(constantDef.getValue().toString());
		System.out.println();
		System.out.println(expected.toString());
		if(result)		System.out.println("------   " + name +  "   ------");
		else  		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXX   " + name +  "   XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println();
		System.out.println();
	}
*/	
}
