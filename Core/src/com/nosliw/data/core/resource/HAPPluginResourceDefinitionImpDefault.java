package com.nosliw.data.core.resource;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionResource;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpDefault implements HAPPluginResourceDefinition{

	private String m_resourceType;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPPluginResourceDefinitionImpDefault(String resourceType, HAPRuntimeEnvironment runtimeEnv) {
		this.m_resourceType = resourceType;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public String getResourceType() {		return this.m_resourceType;	}

	@Override
	public HAPIdEntityInDomain getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId, HAPDomainEntityDefinitionGlobal globalDomain) {
		HAPDomainEntityDefinitionResource resourceDomain = globalDomain.newResourceDomain(resourceId);
		
		//get location information
		HAPInfoResourceLocation resourceLocInfo = HAPUtilityResourceId.getResourceLocationInfo(resourceId);
		resourceDomain.setLocationBase(resourceLocInfo.getBasePath());
		//read content
		JSONObject entityJsonObj = new JSONObject(HAPFileUtility.readFile(resourceLocInfo.getFiile()));
		//parse json object
		return parseEntity(entityJsonObj, new HAPContextParser(globalDomain, resourceDomain.getDomainId()));
	}

	@Override
	public HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal localResourceId, HAPDomainEntityDefinitionGlobal globalDomain, String currentDomainId) {
		HAPPathLocationBase localRefBase = globalDomain.getResourceDomainById(currentDomainId).getLocationBase();
		String path = localRefBase.getPath() + localResourceId.getResourceType() + "/" + localResourceId.getName() + ".res";
		HAPIdEntityInDomain out = this.parseEntity(HAPFileUtility.readFile(path), new HAPContextParser(globalDomain, currentDomainId));
		return out;
	}

	private HAPIdEntityInDomain parseEntity(Object content, HAPContextParser parserContext) {
		JSONObject jsonObj = null;
		if(content instanceof JSONObject) jsonObj = (JSONObject)content;
		else if(content instanceof String)  jsonObj = new JSONObject(HAPJsonUtility.formatJson((String)content));
		HAPIdEntityInDomain entityId = HAPUtilityParserEntity.parseEntity(jsonObj, this.getResourceType(), parserContext, this.m_runtimeEnv.getDomainEntityManager(), this.m_runtimeEnv.getResourceDefinitionManager()); 
		return entityId;
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }
	
//	@Override
//	public HAPIdEntityInDomain parseResourceEntity(Object content, HAPDomainDefinitionEntity entityDomain, HAPLocalReferenceBase localRefBase) {
//		JSONObject jsonObj = null;
//		if(content instanceof JSONObject) jsonObj = (JSONObject)content;
//		else if(content instanceof String)  jsonObj = new JSONObject(content);
//		
//		return this.parseJson(jsonObj, entityDomain, localRefBase);
//	}
//
//	protected HAPIdEntityInDomain addComplexEntity(HAPInfoComplexEntityDefinition complexEntityInfo, JSONObject entityJsonObj, HAPDomainDefinitionEntity definitionDomain) {
//	
//		HAPDefinitionEntityComplex complexResourceDef = complexEntityInfo.getComplexEntity();
//		
//		//entity info
//		complexResourceDef.buildEntityInfoByJson(entityJsonObj);
//		
//		//parse attachment
//		JSONObject pageInfoObj = entityJsonObj.optJSONObject(HAPWithAttachment.ATTACHMENT);
//		if(pageInfoObj!=null) {
//			HAPUtilityAttachment.parseDefinition(pageInfoObj, complexResourceDef.getAttachmentContainer());
//		}
//		
//		//value structure
//		HAPComplexValueStructure valueStructureComplex = new HAPComplexValueStructure();
//		JSONObject valueStructureJsonObj = entityJsonObj.optJSONObject(HAPWithValueStructure.VALUESTRUCTURE);
//		HAPUtilityComplexValueStructure.setValueStructureDefault(valueStructureComplex, HAParserComponentValueStructure.parseComponentValueStructure(valueStructureJsonObj, complexResourceDef.getValueStructureTypeIfNotDefined()));
//		
//		return definitionDomain.addComplexEntity(complexEntityInfo, valueStructureComplex);
//	}
	
	
}
