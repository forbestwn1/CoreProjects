package com.nosliw.core.xxx.application.entity.service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.common.interactive.HAPResultInteractiveTask;
import com.nosliw.core.data.HAPData;

public class HAPUtilityService {

	public static HAPResultInteractiveTask generateSuccessResult(Map<String, HAPData> output) {
		return new HAPResultInteractiveTask(HAPConstantShared.SERVICE_RESULT_SUCCESS, output);
	}

	public static Map<String, HAPResultInteractiveTask> readServiceResult(InputStream inputStream) {
		Map<String, HAPResultInteractiveTask> out = new LinkedHashMap<String, HAPResultInteractiveTask>();
		String content = HAPUtilityFile.readFile(inputStream);
		JSONArray resultArray = new JSONArray(content);
		for(int i=0; i<resultArray.length(); i++) {
			JSONObject resultJsonObj = resultArray.getJSONObject(i);
			HAPResultInteractiveTask result = new HAPResultInteractiveTask();
			result.buildObject(resultJsonObj, HAPSerializationFormat.JSON);
			out.put(resultJsonObj.getString("name"), result);
		}
		return out;
	}
	
	public static HAPResultInteractiveTask readServiceResult(InputStream inputStream, String name) {
		return readServiceResult(inputStream).get(name);
	}

	public static void solveServiceProvider(HAPWithServiceUse child, HAPWithServiceUse parent, HAPDefinitionEntityContainerAttachment attachment, HAPNameMapping nameMapping, HAPManagerServiceDefinition serviceDefinitionMan) {
		Map<String, HAPDefinitionServiceProvider> parentProviders = parent!=null?parent.getServiceProviderDefinitions() : new LinkedHashMap<String, HAPDefinitionServiceProvider>();
		Map<String, HAPDefinitionServiceProvider> mappedParentProviders = null;
		if(nameMapping!=null) {
			mappedParentProviders = (Map<String, HAPDefinitionServiceProvider>)nameMapping.mapEntity(parentProviders, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE);
		} else {
			mappedParentProviders = parentProviders;
		}
		
		Map<String, HAPDefinitionServiceProvider> providers = HAPUtilityServiceUse.buildServiceProvider(attachment, mappedParentProviders, serviceDefinitionMan);
		for(HAPDefinitionServiceProvider provider : providers.values()) {
			child.addServiceProviderDefinition(provider);
		}
	}

}
