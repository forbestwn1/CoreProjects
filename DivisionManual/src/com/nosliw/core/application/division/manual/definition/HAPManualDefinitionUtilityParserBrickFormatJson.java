package com.nosliw.core.application.division.manual.definition;

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
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualWithBrick;
import com.nosliw.core.application.division.manual.common.attachment.HAPReferenceAttachment;
import com.nosliw.core.application.dynamic.HAPInputDynamicTask;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualDefinitionUtilityParserBrickFormatJson {

	public static HAPManualDefinitionWrapperBrickRoot parseRootBrickWrapper(JSONObject jsonObj, HAPIdBrickType brickTypeIfNotProvided, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionBrickMan, HAPManagerApplicationBrick brickManager) {
		HAPManualDefinitionWrapperBrickRoot out = new HAPManualDefinitionWrapperBrickRoot();
		
		HAPManualDefinitionBrick brickDef = parseBrick(jsonObj, brickTypeIfNotProvided, parseContext, manualDivisionBrickMan, brickManager);
		out.setBrick(brickDef);
		
		Object infoObj = jsonObj.opt(HAPManualDefinitionWrapperBrickRoot.INFO);
		out.buildEntityInfoByJson(infoObj);
		
		return out;
	}
	
	public static List<HAPManualDefinitionAdapter> parseAdapters(Object adaptersObj, HAPIdBrickType adapterTypeId, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager){
		List<HAPManualDefinitionAdapter> out = new ArrayList<HAPManualDefinitionAdapter>();
		if(adaptersObj instanceof JSONArray) {
			JSONArray adaptersArray = (JSONArray)adaptersObj;
			for(int i=0; i<adaptersArray.length(); i++) {
				HAPManualDefinitionAdapter adapterInfo = parseAdapter(adaptersArray.getJSONObject(i), adapterTypeId, parseContext, manualDivisionEntityMan, entityManager);
				if(adapterInfo!=null) {
					out.add(adapterInfo);
				}
			}
		}
		else if(adaptersObj instanceof JSONObject) {
			HAPManualDefinitionAdapter adapterInfo = parseAdapter((JSONObject)adaptersObj, adapterTypeId, parseContext, manualDivisionEntityMan, entityManager);
			if(adapterInfo!=null) {
				out.add(adapterInfo);
			}
		}
		return out;
	}
	
	public static HAPManualDefinitionAttributeInBrick parseAttribute(String attrName, JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPEntityInfo info = HAPUtilityEntityInfo.buildEntityInfoFromJson(jsonObj, HAPManualDefinitionAttributeInBrick.INFO);
		
		if(HAPUtilityEntityInfo.isEnabled(info)) {
			HAPManualDefinitionAttributeInBrick out = new HAPManualDefinitionAttributeInBrick();
			
			//parse attribute value
			HAPManualDefinitionWrapperValue attrValueInfo = parseWrapperValue(jsonObj, entityTypeIfNotProvided, parseContext, manualDivisionEntityMan, entityManager);
			out.setValueWrapper(attrValueInfo);
			
			//parse info
			info.cloneToEntityInfo(out);
			if(attrName!=null) {
				out.setName(attrName);
			}
			
			//parse adapter
			Object adaptersObj = jsonObj.opt(HAPManualDefinitionAttributeInBrick.ADAPTER);
			if(adaptersObj!=null) {
				List<HAPManualDefinitionAdapter> adapters = parseAdapters(adaptersObj, adapterTypeId, parseContext, manualDivisionEntityMan, entityManager);
				adapters.forEach(adapter->out.addAdapter(adapter));
				
				if(adaptersObj instanceof JSONArray) {
					JSONArray adaptersArray = (JSONArray)adaptersObj;
					for(int i=0; i<adaptersArray.length(); i++) {
						HAPManualDefinitionAdapter adapterInfo = parseAdapter(adaptersArray.getJSONObject(i), adapterTypeId, parseContext, manualDivisionEntityMan, entityManager);
						if(adapterInfo!=null) {
							out.addAdapter(adapterInfo);
						}
					}
				}
				else if(adaptersObj instanceof JSONObject) {
					HAPManualDefinitionAdapter adapterInfo = parseAdapter((JSONObject)adaptersObj, adapterTypeId, parseContext, manualDivisionEntityMan, entityManager);
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

	public static HAPManualDefinitionBrick parseBrick(JSONObject jsonObj, HAPIdBrickType brickTypeIfNotProvided, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionBrickMan, HAPManagerApplicationBrick brickManager) {
		Object brickTypeObj = jsonObj.opt(HAPManualWithBrick.BRICKTYPEID);   //if entity type is defined in entity, then override provided
		HAPIdBrickType brickTypeId = HAPUtilityBrickId.parseBrickTypeId(brickTypeObj, brickTypeIfNotProvided, brickManager);
		
		Object brickObj = jsonObj.opt(HAPManualWithBrick.BRICK);
		if(brickObj==null)
		{
			brickObj = jsonObj;    //if no entity node, then using root
		}
		return manualDivisionBrickMan.parseBrickDefinition(brickObj, brickTypeId, HAPSerializationFormat.JSON, parseContext);
	}	
		
	//parse entity as attribute value (value may be entity or reference(resource, attachment, local))
	public static HAPManualDefinitionWrapperValue parseWrapperValue(JSONObject jsonObj, HAPIdBrickType brickTypeIfNotProvided, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualDefinitionWrapperValue out = null;

		//try with definition
		Object entityTypeObj = jsonObj.opt(HAPManualWithBrick.BRICKTYPEID);   //if entity type is defined in entity, then override provided
		HAPIdBrickType brickTypeId = HAPUtilityBrickId.parseBrickTypeId(entityTypeObj, brickTypeIfNotProvided, entityManager);
		
		//local entity reference
		if(out==null) {
			Object entityRefObj = jsonObj.opt(HAPManualDefinitionWrapperValueReferenceBrick.BRICKREFERENCE);
			if(entityRefObj!=null) {
				HAPIdBrick entityId = HAPUtilityBrickId.parseBrickIdAgressive(entityRefObj, brickTypeIfNotProvided, parseContext.getBrickDivision(), entityManager); 
				HAPManualDefinitionBrick refBrick = parseLocalValue(entityId, parseContext, manualDivisionEntityMan);
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
				HAPReferenceAttachment reference = HAPReferenceAttachment.newInstance(referenceObj, brickTypeId.getBrickType());
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
			HAPManualDefinitionBrick brickDef = manualDivisionEntityMan.parseBrickDefinition(brickObj, brickTypeId, HAPSerializationFormat.JSON, parseContext);

			out = new HAPManualDefinitionWrapperValueBrick(brickDef);
		}
		
		return out;
	}

	private static HAPManualDefinitionBrick parseLocalValue(HAPIdBrick entityId, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan) {
		HAPManualDefinitionInfoBrickLocation entityLocationInfo = HAPManualDefinitionUtilityBrickLocation.getLocalBrickLocationInfo(parseContext.getBasePath(), entityId);
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());
		return manualDivisionEntityMan.parseBrickDefinitionWrapper(content, entityId.getBrickTypeId(), entityLocationInfo.getFormat(), parseContext).getBrick();
	}
	
	private static HAPManualDefinitionAdapter parseAdapter(JSONObject adapterObj, HAPIdBrickType adatperTypeId, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick textDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualDefinitionAdapter adapterInfo = null;
		if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
			HAPManualDefinitionWrapperValue adpaterEntityDefInfo = parseWrapperValue(adapterObj, adatperTypeId, parseContext, textDivisionEntityMan, entityManager);
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


	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*	
	
	public static HAPEmbededDefinition parseEmbededEntity(Object obj, HAPIdBrickType entityType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededEntity(obj, entityType, null, parserContext, domainEntityManager, resourceDefinitionManager);
	}

	public static HAPEmbededDefinition parseEmbededEntity(Object obj, HAPIdBrickType entityType, HAPIdBrickType adapterType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPEmbededDefinition out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			
			//embeded entity
			HAPIdEntityInDomain entityId = parseWrapperValue(getEntityObject(jsonObj), entityType, parserContext, domainEntityManager, resourceDefinitionManager); 

			if(entityId!=null) {
				out = new HAPEmbededDefinition(entityId); 
				//adapter
				Object adapterObjs = jsonObj.opt(HAPEmbeded.ADAPTER);
				if(adapterObjs!=null) {
					List<HAPInfoAdapterDefinition> adapters = parseAdapter(adapterObjs, figureoutAdatperType(entityId.getEntityType(), adapterType,domainEntityManager), parserContext, domainEntityManager, resourceDefinitionManager);
					for(HAPInfoAdapterDefinition adapter : adapters) {
						out.addAdapter(adapter);
					}
				}
			}
		}
		return out;
	}

	public static HAPEmbededDefinition parseEmbededComplexEntity(JSONObject jsonObj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededComplexEntity(jsonObj, entityType, null, parentEntityId, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);		
	}
	
	public static HAPEmbededDefinition parseEmbededComplexEntity(JSONObject jsonObj, String entityType, String adapterType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededComplexEntity(jsonObj, entityType, adapterType, parentEntityId, null, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);		
	}
	
	public static HAPEmbededDefinition parseEmbededComplexEntity(JSONObject jsonObj, String entityType, String adapterType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPEmbededDefinition out = null;
		//embeded entity
		HAPIdEntityInDomain entityId = parseComplexEntity(getEntityObject(jsonObj), entityType, parentEntityId, parentRelationConfigureExternal, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager); 
		
		if(entityId!=null) {
			out = new HAPEmbededDefinition(entityId);
			//adapter
			Object adapterObjs = jsonObj.opt(HAPEmbeded.ADAPTER);
			if(adapterObjs!=null) {
				List<HAPInfoAdapterDefinition> adapters = parseAdapter(adapterObjs, figureoutAdatperType(entityId.getEntityType(), adapterType, domainEntityManager), parserContext, domainEntityManager, resourceDefinitionManager);
				for(HAPInfoAdapterDefinition adapter : adapters) {
					out.addAdapter(adapter);
				}
			}
		}
		return out;
	}


	private static HAPIdEntityInDomain parseComplexEntity(JSONObject jsonObj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		//entity itself
		HAPIdEntityInDomain out = parseWrapperValue(jsonObj, entityType, parserContext, domainEntityManager, resourceDefinitionManager);

		if(out!=null && parentEntityId!=null) {
			//parent relation
			HAPInfoParentComplex parentInfo = new HAPInfoParentComplex();

			//parent id
			parentInfo.setParentId(parentEntityId);

			//parent relation configure
			HAPConfigureParentRelationComplex parentRelationConfigure = parentInfo.getParentRelationConfigure();
			
			//default configure
			parentRelationConfigure.mergeHard(parentRelationConfigureDefault);

			//external configure
			parentRelationConfigure.mergeHard(parentRelationConfigureExternal);

			//customer configure
			JSONObject parentRelationConfigureObjCustomer = jsonObj.optJSONObject(HAPInfoEntityInDomainDefinition.PARENT);
			HAPConfigureParentRelationComplex customerConfigure = null;
			if(parentRelationConfigureObjCustomer!=null) {
				customerConfigure = new HAPConfigureParentRelationComplex();
				customerConfigure.buildObject(parentRelationConfigureObjCustomer, HAPSerializationFormat.JSON);
			}
			parentRelationConfigure.mergeHard(customerConfigure);
			
			((HAPDomainEntityDefinitionLocalComplex)parserContext.getCurrentDomain()).buildComplexParentRelation(out, parentInfo);
		}
		
		return out;
	}
*/
}
