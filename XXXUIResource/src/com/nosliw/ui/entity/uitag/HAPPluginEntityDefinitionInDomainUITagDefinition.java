package com.nosliw.ui.entity.uitag;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainUITagDefinition extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainUITagDefinition(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGDEFINITION, HAPDefinitionEntityUITagDefinition.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		HAPDefinitionEntityUITagDefinition uiTagDefinition = (HAPDefinitionEntityUITagDefinition)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
		
		JSONObject jsonObj = (JSONObject)jsonValue;
		
		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
		
		HAPEntityInfoImp info = new HAPEntityInfoImp();
		info.buildObject(jsonObj.optJSONObject(HAPDefinitionEntityUITagDefinition.INFO), HAPSerializationFormat.JSON);
		uiTagDefinition.setInfo(info);
		
		//attribute
		Map<String, HAPUITagAttributeDefinition> attributes = new LinkedHashMap<String, HAPUITagAttributeDefinition>();
		JSONArray attrArray = jsonObj.optJSONArray(HAPDefinitionEntityUITagDefinition.ATTRIBUTEDEFINITION);
		if(attrArray!=null) {
			for(int i=0; i<attrArray.length(); i++) {
				HAPUITagAttributeDefinition attr = new HAPUITagAttributeDefinition();
				attr.buildObject(attrArray.getJSONObject(i), HAPSerializationFormat.JSON);
				attributes.put(attr.getName(), attr);
			}
		}
		uiTagDefinition.setAttributeDefinition(attributes);
		
		//event
		Map<String, HAPUITagEventDefinition> events = new LinkedHashMap<String, HAPUITagEventDefinition>();
		JSONArray eventArray = jsonObj.optJSONArray(HAPDefinitionEntityUITagDefinition.EVENTDEFINITION);
		if(eventArray!=null) {
			for(int i=0; i<eventArray.length(); i++) {
				HAPUITagEventDefinition event = new HAPUITagEventDefinition();
				event.buildObject(eventArray.getJSONObject(i), HAPSerializationFormat.JSON);
				events.put(event.getName(), event);
			}
		}
		uiTagDefinition.setEventDefinition(events);
		
		String baseName = (String)jsonObj.opt(HAPDefinitionEntityUITagDefinition.BASE);
		if(baseName!=null)   uiTagDefinition.setBaseName(baseName);
		
		Object scriptResourceObj = jsonObj.opt(HAPDefinitionEntityUITagDefinition.SCRIPTRESOURCEID);
		if(scriptResourceObj==null) {
		}
		else {
			HAPResourceId scriptResourceId = null;
			if(scriptResourceObj instanceof String) {
				scriptResourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, scriptResourceObj, false);
			}
			else if(scriptResourceObj instanceof JSONObject){
				scriptResourceId = new HAPResourceIdSimple();
				scriptResourceId.buildObject(scriptResourceObj, HAPSerializationFormat.JSON);
			}
			uiTagDefinition.setScriptResourceId(scriptResourceId);
		}
		
		HAPConfigureParentRelationComplex parentRelationConfigure = new HAPConfigureParentRelationComplex(); 
		JSONObject parentRelationConfigureJson = jsonObj.optJSONObject(HAPDefinitionEntityUITagDefinition.PARENTRELATIONCONFIGURE);
		if(parentRelationConfigureJson!=null) {
			parentRelationConfigure.buildObject(parentRelationConfigureJson, HAPSerializationFormat.JSON);
		}
		uiTagDefinition.setParentRelationConfigure(parentRelationConfigure);
		
		HAPConfigureParentRelationComplex childRelationConfigure = new HAPConfigureParentRelationComplex(); 
		JSONObject childRelationConfigureJson = jsonObj.optJSONObject(HAPDefinitionEntityUITagDefinition.CHILDRELATIONCONFIGURE);
		if(childRelationConfigureJson!=null) {
			childRelationConfigure.buildObject(childRelationConfigureJson, HAPSerializationFormat.JSON);
		}
		uiTagDefinition.setChildRelationConfigure(childRelationConfigure);
	}

}
