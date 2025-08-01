package com.nosliw.core.application.entity.service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.data.HAPData;

public class HAPUtilityService {

	public static HAPResultInteractive generateSuccessResult(Map<String, HAPData> output) {
		return new HAPResultInteractive(HAPConstantShared.SERVICE_RESULT_SUCCESS, output);
	}

	public static Map<String, HAPResultInteractive> readServiceResult(InputStream inputStream) {
		Map<String, HAPResultInteractive> out = new LinkedHashMap<String, HAPResultInteractive>();
		String content = HAPUtilityFile.readFile(inputStream);
		JSONArray resultArray = new JSONArray(content);
		for(int i=0; i<resultArray.length(); i++) {
			JSONObject resultJsonObj = resultArray.getJSONObject(i);
			HAPResultInteractive result = new HAPResultInteractive();
			result.buildObject(resultJsonObj, HAPSerializationFormat.JSON);
			out.put(resultJsonObj.getString("name"), result);
		}
		return out;
	}
	
	public static HAPResultInteractive readServiceResult(InputStream inputStream, String name) {
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
