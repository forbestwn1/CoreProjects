package com.nosliw.core.application.division.manual.core.definition;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.dynamic.HAPInputDynamicTask;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application.division.manual.core.definition1.HAPManualDefinitionWrapperValueDynamic;
import com.nosliw.core.xxx.application.division.manual.core.definition1.HAPManualDefinitionWrapperValueReferenceAttachment;
import com.nosliw.core.xxx.application.division.manual.core.definition1.HAPManualDefinitionWrapperValueReferenceBrick;

public class HAPManualDefinitionUtilityParserBrickFormatJson {

	public static HAPManualDefinitionWrapperBrickRoot parseRootBrickWrapper(JSONObject jsonObj, HAPIdBrickType brickTypeIfNotProvided, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionWrapperBrickRoot out = new HAPManualDefinitionWrapperBrickRoot();
		
		HAPManualDefinitionBrick brickDef = parseBrick(jsonObj, brickTypeIfNotProvided, parseContext);
		out.setBrick(brickDef);
		
		Object infoObj = jsonObj.opt(HAPManualDefinitionWrapperBrickRoot.INFO);
		out.buildEntityInfoByJson(infoObj);
		
		return out;
	}
	
	public static List<HAPManualDefinitionAdapter> parseAdapters(Object adaptersObj, HAPIdBrickType adapterTypeId, HAPManualDefinitionContextParse parseContext){
		List<HAPManualDefinitionAdapter> out = new ArrayList<HAPManualDefinitionAdapter>();
		if(adaptersObj instanceof JSONArray) {
			JSONArray adaptersArray = (JSONArray)adaptersObj;
			for(int i=0; i<adaptersArray.length(); i++) {
				HAPManualDefinitionAdapter adapterInfo = parseAdapter(adaptersArray.getJSONObject(i), adapterTypeId, parseContext);
				if(adapterInfo!=null) {
					out.add(adapterInfo);
				}
			}
		}
		else if(adaptersObj instanceof JSONObject) {
			HAPManualDefinitionAdapter adapterInfo = parseAdapter((JSONObject)adaptersObj, adapterTypeId, parseContext);
			if(adapterInfo!=null) {
				out.add(adapterInfo);
			}
		}
		return out;
	}
	
	public static HAPManualDefinitionAttributeInBrick parseAttribute(String attrName, JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualDefinitionContextParse parseContext) {
		HAPEntityInfo info = HAPUtilityEntityInfo.buildEntityInfoFromJson(jsonObj, HAPManualDefinitionAttributeInBrick.INFO);
		
		if(HAPUtilityEntityInfo.isEnabled(info)) {
			HAPManualDefinitionAttributeInBrick out = new HAPManualDefinitionAttributeInBrick();
			
			//parse attribute value
			HAPManualDefinitionWrapperValue attrValueInfo = parseWrapperValue(jsonObj, entityTypeIfNotProvided, parseContext);
			out.setValueWrapper(attrValueInfo);
			
			//parse info
			info.cloneToEntityInfo(out);
			if(attrName!=null) {
				out.setName(attrName);
			}
			
			//parse adapter
			Object adaptersObj = jsonObj.opt(HAPManualDefinitionAttributeInBrick.ADAPTER);
			if(adaptersObj!=null) {
				List<HAPManualDefinitionAdapter> adapters = parseAdapters(adaptersObj, adapterTypeId, parseContext);
				adapters.forEach(adapter->out.addAdapter(adapter));
				
				if(adaptersObj instanceof JSONArray) {
					JSONArray adaptersArray = (JSONArray)adaptersObj;
					for(int i=0; i<adaptersArray.length(); i++) {
						HAPManualDefinitionAdapter adapterInfo = parseAdapter(adaptersArray.getJSONObject(i), adapterTypeId, parseContext);
						if(adapterInfo!=null) {
							out.addAdapter(adapterInfo);
						}
					}
				}
				else if(adaptersObj instanceof JSONObject) {
					HAPManualDefinitionAdapter adapterInfo = parseAdapter((JSONObject)adaptersObj, adapterTypeId, parseContext);
					if(adapterInfo!=null) {
						out.addAdapter(adapterInfo);
					}
				}
			}
			
			//parse relation
			Object relationObjs = jsonObj.opt(HAPManualDefinitionAttributeInBrick.RELATION);
			if(relationObjs!=null) {
				JSONArray relationArray = (JSONArray)relationObjs;
				for(int i=0; i<relationArray.length(); i++) {
					out.addRelation(HAPManualDefinitionBrickRelation.parseRelation(relationArray.getJSONObject(i)));
				}
			}
			return out;
		} else {
			return null;
		}
	}

	public static HAPManualDefinitionBrick parseBrick(JSONObject jsonObj, HAPIdBrickType brickTypeIfNotProvided, HAPManualDefinitionContextParse parseContext) {
		Object brickTypeObj = jsonObj.opt(HAPManualDefinitionWithBrick.BRICKTYPEID);   //if entity type is defined in entity, then override provided
		HAPIdBrickType brickTypeId = HAPUtilityBrickId.parseBrickTypeId(brickTypeObj, brickTypeIfNotProvided, parseContext.getBrickManager());
		
		Object brickObj = jsonObj.opt(HAPManualDefinitionWithBrick.BRICK);
		if(brickObj==null)
		{
			brickObj = jsonObj;    //if no entity node, then using root
		}
		return HAPManualDefinitionUtilityParserBrick.parseBrickDefinition(brickObj, brickTypeId, HAPSerializationFormat.JSON, parseContext);
	}	
		
	//parse entity as attribute value (value may be entity or reference(resource, attachment, local))
	public static HAPManualDefinitionWrapperValue parseWrapperValue(JSONObject jsonObj, HAPIdBrickType brickTypeIfNotProvided, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionWrapperValue out = null;

		//try with definition
		Object entityTypeObj = jsonObj.opt(HAPManualDefinitionWithBrick.BRICKTYPEID);   //if entity type is defined in entity, then override provided
		HAPIdBrickType brickTypeId = HAPUtilityBrickId.parseBrickTypeId(entityTypeObj, brickTypeIfNotProvided, parseContext.getBrickManager());
		
		//local entity reference
		if(out==null) {
			Object entityRefObj = jsonObj.opt(HAPManualDefinitionWrapperValueReferenceBrick.BRICKREFERENCE);
			if(entityRefObj!=null) {
				HAPIdBrick entityId = HAPUtilityBrickId.parseBrickIdAgressive(entityRefObj, brickTypeIfNotProvided, parseContext.getBrickDivision(), parseContext.getBrickManager()); 
				HAPManualDefinitionBrick refBrick = parseLocalValue(entityId, parseContext);
				out = new HAPManualDefinitionWrapperValueBrick(refBrick);
			}
		}
		
		//resource id
		if(out==null) {
			Object resourceObj = jsonObj.opt(HAPManualDefinitionWrapperValueReferenceResource.RESOURCEID);
			if(resourceObj!=null) {
				HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(brickTypeId!=null?brickTypeId.getBrickType():null, brickTypeId!=null?brickTypeId.getVersion():null, resourceObj);
				out = new HAPManualDefinitionWrapperValueReferenceResource(resourceId);
				
				JSONObject dynamicInputsObj = jsonObj.optJSONObject(HAPManualDefinitionWrapperValueReferenceResource.DYNAMICINPUT);
				if(dynamicInputsObj!=null) {
					HAPInputDynamicTask dynamicTaskInput = new HAPInputDynamicTask();
					dynamicTaskInput.buildObject(dynamicInputsObj, HAPSerializationFormat.JSON);
					((HAPManualDefinitionWrapperValueReferenceResource)out).setDynamicInput(dynamicTaskInput);
				}
			}
		}
		
		//reference
		if(out==null) {
			Object referenceObj = jsonObj.opt(HAPManualDefinitionWrapperValueReferenceAttachment.REFERENCE);
			if(referenceObj!=null) {
				HAPManualDefinitionReferenceAttachment reference = HAPManualDefinitionReferenceAttachment.newInstance(referenceObj, brickTypeId.getBrickType());
				out = new HAPManualDefinitionWrapperValueReferenceAttachment(reference);
			}
		}
		
		//dynamic
		if(out==null) {
			Object dynamicObj = jsonObj.opt(HAPManualDefinitionWrapperValueDynamic.DYNAMIC);
			if(dynamicObj!=null) {
				HAPValueOfDynamic dynamicValue = new HAPValueOfDynamic();
				dynamicValue.buildObject(dynamicObj, HAPSerializationFormat.JSON);
				out = new HAPManualDefinitionWrapperValueDynamic(dynamicValue);
			}
		}

		//value
		if(out==null) {
			Object valueObj = jsonObj.opt(HAPManualDefinitionWrapperValueValue.VALUE);
			if(valueObj!=null) {
				out = new HAPManualDefinitionWrapperValueValue(valueObj);
			}
		}
		
		//brick
		if(out==null) {
			Object brickObj = jsonObj.opt(HAPManualDefinitionWrapperValueBrick.BRICK);
			if(brickObj==null)
			{
				brickObj = jsonObj;    //if no entity node, then using root
			}
			HAPManualDefinitionBrick brickDef = HAPManualDefinitionUtilityParserBrick.parseBrickDefinition(brickObj, brickTypeId, HAPSerializationFormat.JSON, parseContext);

			out = new HAPManualDefinitionWrapperValueBrick(brickDef);
		}
		
		return out;
	}

	private static HAPManualDefinitionBrick parseLocalValue(HAPIdBrick entityId, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionInfoBrickLocation entityLocationInfo = HAPManualDefinitionUtilityBrickLocation.getLocalBrickLocationInfo(parseContext.getBasePath(), entityId);
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());
		return HAPManualDefinitionUtilityParserBrick.parseBrickDefinitionWrapper(content, entityId.getBrickTypeId(), entityLocationInfo.getFormat(), parseContext).getBrick();
	}
	
	private static HAPManualDefinitionAdapter parseAdapter(JSONObject adapterObj, HAPIdBrickType adatperTypeId, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionAdapter adapterInfo = null;
		if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
			HAPManualDefinitionWrapperValue adpaterEntityDefInfo = parseWrapperValue(adapterObj, adatperTypeId, parseContext);
			adapterInfo = new HAPManualDefinitionAdapter(adpaterEntityDefInfo);
			adapterInfo.buildEntityInfoByJson(adapterObj);
			if(adapterInfo.getName()==null) {
				adapterInfo.setName(HAPConstantShared.NAME_DEFAULT);
			}
		}
		return adapterInfo;
	}
	
	private static String figureoutAdatperType(String entityType, String adapterType, HAPManagerDomainEntityDefinition domainEntityManager) {
		if(adapterType!=null) {
			return adapterType;
		} else {
			return domainEntityManager.getDefaultAdapterByEntity(entityType);
		}
	}
}
