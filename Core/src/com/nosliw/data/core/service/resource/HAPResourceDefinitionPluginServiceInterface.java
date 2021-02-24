package com.nosliw.data.core.service.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.service.interfacee.HAPInfoServiceInterface;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceDefinitionPluginServiceInterface implements HAPPluginResourceDefinition{

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE;  }

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		String fileName = HAPSystemFolderUtility.getServiceInterfaceFolder() + new HAPResourceIdServiceInterface(resourceId).getServiceInterfaceId().getId() + ".res";
		String content = HAPFileUtility.readFile(new File(fileName));
		return this.parseResourceDefinition(content);
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		HAPInfoServiceInterface out = new HAPInfoServiceInterface();
		if(content instanceof String) {
			out.buildObject(new JSONObject(content.toString()), HAPSerializationFormat.JSON);
		}
		else if(content instanceof JSONObject) {
			out.buildObject(content, HAPSerializationFormat.JSON);
		}
		return out;
	}

}
