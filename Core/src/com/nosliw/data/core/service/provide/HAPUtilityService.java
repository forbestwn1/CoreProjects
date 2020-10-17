package com.nosliw.data.core.service.provide;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPNameMapping;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;

public class HAPUtilityService {

	public static HAPResultService generateSuccessResult(Map<String, HAPData> output) {
		return new HAPResultService(HAPConstant.SERVICE_RESULT_SUCCESS, output);
	}

	public static Map<String, HAPResultService> readServiceResult(InputStream inputStream) {
		Map<String, HAPResultService> out = new LinkedHashMap<String, HAPResultService>();
		String content = HAPFileUtility.readFile(inputStream);
		JSONArray resultArray = new JSONArray(content);
		for(int i=0; i<resultArray.length(); i++) {
			JSONObject resultJsonObj = resultArray.getJSONObject(i);
			HAPResultService result = new HAPResultService();
			result.buildObject(resultJsonObj, HAPSerializationFormat.JSON);
			out.put(resultJsonObj.getString("name"), result);
		}
		return out;
	}
	
	public static HAPResultService readServiceResult(InputStream inputStream, String name) {
		return readServiceResult(inputStream).get(name);
	}

	public static void process() {
		
	}
	
	public static void solveServiceProvider(HAPWithServiceUse child, HAPWithServiceUse parent, HAPAttachmentContainer attachment, HAPNameMapping nameMapping, HAPManagerServiceDefinition serviceDefinitionMan) {
		Map<String, HAPDefinitionServiceProvider> parentProviders = parent!=null?parent.getServiceProviderDefinitions() : new LinkedHashMap<String, HAPDefinitionServiceProvider>();
		Map<String, HAPDefinitionServiceProvider> mappedParentProviders = null;
		if(nameMapping!=null)   mappedParentProviders = (Map<String, HAPDefinitionServiceProvider>)nameMapping.mapEntity(parentProviders, HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE);
		else mappedParentProviders = parentProviders;
		
		Map<String, HAPDefinitionServiceProvider> providers = HAPUtilityServiceUse.buildServiceProvider(attachment, mappedParentProviders, serviceDefinitionMan);
		for(HAPDefinitionServiceProvider provider : providers.values())	child.addServiceProviderDefinition(provider);
	}

}
