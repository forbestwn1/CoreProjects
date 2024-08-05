package com.nosliw.core.application.uitag;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.HAPWithValueContext;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.common.structure.HAPUtilityValueStructureParser;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinitionImp;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityUITagDefinitionParser {

	static private void parseValueContext(HAPUITagValueContextDefinition valueContext, JSONArray valueStructuresArray) {
		
		for(int i=0; i<valueStructuresArray.length(); i++) {
			JSONObject valueStructureWrapperObj = valueStructuresArray.getJSONObject(i);
			
			HAPUITagWrapperValueStructure valueStructureWrapper = new HAPUITagWrapperValueStructure();
			
			HAPUtilityValueStructureParser.parseValueStructureWrapper(valueStructureWrapper, valueStructureWrapperObj);
			
			HAPValueStructureDefinition valueStructure = new HAPValueStructureDefinitionImp();
			HAPUtilityValueStructureParser.parseValueStructureJson(valueStructureWrapperObj.getJSONObject(HAPWrapperValueStructure.VALUESTRUCTURE), valueStructure);
			valueStructureWrapper.setValueStructure(valueStructure);
			
			valueContext.getValueStructures().add(valueStructureWrapper);
		}
	}
	
	static public HAPUITagDefinition parseUITagDefinition(JSONObject jsonObj) {

		HAPUITagDefinition out = new HAPUITagDefinition();

		//parse value context
		HAPUITagValueContextDefinition valueContext = new HAPUITagValueContextDefinition();
		parseValueContext(valueContext, jsonObj.getJSONArray(HAPWithValueContext.VALUECONTEXT));
		out.setValueContext(valueContext);

		//base
		String baseName = (String)jsonObj.opt(HAPUITagDefinition.BASE);
		if(baseName!=null) {
			out.setBase(baseName);
		}
		
		//script
		Object scriptResourceObj = jsonObj.opt(HAPUITagDefinition.SCRIPTRESOURCEID);
		if(scriptResourceObj==null) {
			throw new RuntimeException();
		}
		else {
			HAPResourceId scriptResourceId = HAPFactoryResourceId.newInstance(scriptResourceObj);
//			if(scriptResourceObj instanceof String) {
//				scriptResourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, null, scriptResourceObj);
//			}
//			else if(scriptResourceObj instanceof JSONObject){
//				scriptResourceId = new HAPResourceIdSimple();
//				scriptResourceId.buildObject(scriptResourceObj, HAPSerializationFormat.JSON);
//			}
			out.setScriptResourceId(scriptResourceId);
		}
		
		//parent relation
		JSONArray parentRelationJsonArray = jsonObj.optJSONArray(HAPUITagDefinition.PARENTRELATION);
		if(parentRelationJsonArray!=null) {
			for(int i=0; i<parentRelationJsonArray.length(); i++) {
				out.addParentRelation(HAPManualDefinitionBrickRelation.parseRelation(parentRelationJsonArray.getJSONObject(i)));
			}
		}
		
		
/*		
		HAPEntityInfoImp info = new HAPEntityInfoImp();
		info.buildObject(jsonObj.optJSONObject(HAPDefinitionEntityUITagDefinition.INFO), HAPSerializationFormat.JSON);
		out.setInfo(info);
		
		
		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
		
		
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
		if(baseName!=null) {
			uiTagDefinition.setBaseName(baseName);
		}
		
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
*/		
		
		return out;
	}
	
	
}
