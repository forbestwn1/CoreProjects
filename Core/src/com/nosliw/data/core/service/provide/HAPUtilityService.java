package com.nosliw.data.core.service.provide;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.component.HAPDefinitionExternalMapping;
import com.nosliw.data.core.component.HAPNameMapping;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceProvider;

public class HAPUtilityService {

	public static HAPResultService generateSuccessResult(Map<String, HAPData> output) {
		return new HAPResultService(HAPConstant.SERVICE_RESULT_SUCCESS, output);
	}

	public static Map<String, HAPResultService> readServiceResult(InputStream inputStream) {
		Map<String, HAPResultService> out = new LinkedHashMap<String, HAPResultService>();
		String content = HAPFileUtility.readFile(inputStream);
		JSONArray resultArray = new JSONArray(content);
		for(int i=0; i<resultArray.length(); i++) {
			HAPResultService result = new HAPResultService();
			result.buildObject(resultArray.getJSONObject(i), HAPSerializationFormat.JSON);
		}
		return out;
	}
	
	public static HAPResultService readServiceResult(InputStream inputStream, String resultName) {
		return readServiceResult(inputStream).get(resultName);
	}

	public static void process() {
		
	}
	
	public static void solveServiceProvider(HAPWithServiceProvider child, HAPWithServiceProvider parent, HAPDefinitionExternalMapping externalMapping, HAPNameMapping nameMapping, HAPManagerServiceDefinition serviceDefinitionMan) {
		Map<String, HAPDefinitionServiceProvider> parentProviders = parent!=null?parent.getServiceProviderDefinitions() : new LinkedHashMap<String, HAPDefinitionServiceProvider>();
		Map<String, HAPDefinitionServiceProvider> mappedParentProviders = null;
		if(nameMapping!=null)   mappedParentProviders = (Map<String, HAPDefinitionServiceProvider>)nameMapping.mapEntity(parentProviders, HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE);
		else mappedParentProviders = parentProviders;
		
		Set<HAPDefinitionServiceProvider> providers = HAPUtilityServiceUse.buildServiceProvider(externalMapping, mappedParentProviders, serviceDefinitionMan);
		for(HAPDefinitionServiceProvider provider : providers)	child.addServiceProviderDefinition(provider);
	}

}
