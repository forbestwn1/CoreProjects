package com.nosliw.data.core.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPPluginResourceDefinitionImp1 implements HAPPluginResourceDefinition{

	private HAPParserResourceEntity m_parser;
	
	private String m_resourceType;
	
	public HAPPluginResourceDefinitionImp1(String resourceType, HAPParserResourceEntity parser) {
		this.m_resourceType = resourceType;
		this.m_parser = parser;
	}
	
	@Override
	public String getResourceType() {		return this.m_resourceType;	}

	@Override
	public HAPEntityResourceDefinition getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId) {
		//get location information
		HAPInfoResourceLocation resourceLocInfo = HAPUtilityResourceId.getResourceLocationInfo(resourceId);
		//parse file
		HAPResourceDefinition1 moduleDef = this.parseResourceEntity(resourceLocInfo.getFiile()); 
		//set local base path
		moduleDef.setLocalReferenceBase(new HAPPathLocationBase(resourceLocInfo.getBasePath()));
		return moduleDef;
	}

	@Override
	public HAPEntityResourceDefinition getResourceEntityByLocalResourceId(HAPResourceIdLocal localResourceId, HAPResourceDefinition1 relatedResource) {
		HAPResourceDefinition1 out = null;
		String path = relatedResource.getLocalReferenceBase().getPath() + localResourceId.getResourceType() + "/" + localResourceId.getName() + ".res";
		out = this.parseResourceEntity(HAPUtilityFile.readFile(path));
		out.setLocalReferenceBase(relatedResource.getLocalReferenceBase());
//		if(out instanceof HAPWithAttachment) {
//			((HAPWithAttachment)out).setLocalReferenceBase(localResourceId.getBasePath());
//		}
		return out;
	}

	@Override
	public HAPEntityResourceDefinition parseResourceEntity(Object obj) {
		HAPResourceDefinition1 out = null;
		if(obj instanceof JSONObject) {
			out = this.m_parser.parseJson((JSONObject)obj);
		}
		else if(obj instanceof File) {
			out = m_parser.parseFile((File)obj);
		}
		else if(obj instanceof String) {
			out = m_parser.parseContent((String)obj);
		}
		return out;
	}

}
