package com.nosliw.data.core.domain.entity.attachment1;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPSupplementResourceId;
import com.nosliw.data.core.structure.data.HAPContextDataFactory;
import com.nosliw.data.core.structure.data.HAPContextDataFlat;

public class HAPUtilityAttachment {

	public static final String ATTRIBUTE_OVERRIDE_MODE = "overide"; 
	public static final String OVERRIDE_MODE_CONFIGURABLE = "configurable"; 
	public static final String OVERRIDE_MODE_NONE = "none"; 

	public static final String ATTRIBUTE_FLAG_OVERRIDE = "flagOveride"; 

	public static void mergeContainerToResourceIdSupplement(HAPResourceId resourceId, HAPDefinitionEntityContainerAttachment sourceContainer) {
		//resource Id to attachment container
		HAPDefinitionEntityContainerAttachment merged = new HAPDefinitionEntityContainerAttachment();
		{
			Map<String, Map<String, HAPResourceId>> resourceIds = resourceId.getSupplement().getAllSupplymentResourceId();
			for(String type : resourceIds.keySet()) {
				Map<String, HAPResourceId> byId = resourceIds.get(type);
				for(String id : byId.keySet()) {
					HAPAttachmentReference ele = new HAPAttachmentReferenceExternal(byId.get(id));
					merged.addAttachment(type, ele);
				}
			}
		}

		//container merged to container from resource suppliment
		merged.merge(sourceContainer, HAPConstant.INHERITMODE_CHILD);
		
		//container from resource supplment to supplement
		Map<String, Map<String, HAPResourceId>> resourceIds = new LinkedHashMap<String, Map<String, HAPResourceId>>();
		Map<String, Map<String, HAPAttachment>> mergedAtts = merged.getAllAttachment();
		for(String type : mergedAtts.keySet()) {
			Map<String, HAPAttachment> byId = mergedAtts.get(type);
			Map<String, HAPResourceId> byIdOut = new LinkedHashMap<>();
			for(String id : byId.keySet()) {
				HAPAttachment attachment = byId.get(id);
				if(attachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL)) {
					byIdOut.put(id, ((HAPAttachmentReference)attachment).getReferenceId());
				}
			}
			resourceIds.put(type, byIdOut);
		}
		HAPSupplementResourceId mergedSupplement = HAPSupplementResourceId.newInstance(resourceIds);
		resourceId.setSupplement(mergedSupplement);
	}
	
	public static void mergeAttachmentInResourceIdSupplementToContainer(HAPResourceId resourceId, HAPDefinitionEntityContainerAttachment targetContainer, String mode) {
		if(resourceId==null)  return;
		HAPSupplementResourceId resourceIdSupplement = resourceId.getSupplement();
		if(resourceIdSupplement!=null) {
			HAPDefinitionEntityContainerAttachment tempContainer = new HAPDefinitionEntityContainerAttachment();
			Map<String, Map<String, HAPResourceId>> resourceIds = resourceIdSupplement.getAllSupplymentResourceId();
			for(String type : resourceIds.keySet()) {
				Map<String, HAPResourceId> byId = resourceIds.get(type);
				for(String id : byId.keySet()) {
					HAPAttachmentReference ele = new HAPAttachmentReferenceExternal(byId.get(id));
					tempContainer.addAttachment(type, ele);
				}
			}
			targetContainer.merge(tempContainer, mode);
		}
	}
	
	public static HAPContextDataFlat getTestDataFromAttachment(HAPWithAttachment withAttachment, String name) {
		return HAPUtilityAttachment.getContextDataFromAttachment(withAttachment.getAttachmentContainer(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_TESTDATA, name);
	}
	
	public static HAPContextDataFlat getContextDataFromAttachment(HAPDefinitionEntityContainerAttachment attContainer, String type, String name) {
		HAPContextDataFlat out = new HAPContextDataFlat();
		HAPAttachmentEntity attachment = (HAPAttachmentEntity)attContainer.getElement(type, name);
		if(attachment!=null) {
			JSONObject dataObjJson = attachment.getEntityJsonObj();
			out = HAPContextDataFactory.newContextDataFlat(dataObjJson);
		}
		return out;
	}
	
	public static Map<String, Object> getTestValueFromAttachment(HAPWithAttachment withAttachment, String name) {
		return HAPUtilityAttachment.getContextValueFromAttachment(withAttachment.getAttachmentContainer(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_TESTDATA, name);
	}

	public static Map<String, Object> getTestValueFromAttachment(HAPDefinitionEntityContainerAttachment attachment, String name) {
		return HAPUtilityAttachment.getContextValueFromAttachment(attachment, HAPConstantShared.RUNTIME_RESOURCE_TYPE_TESTDATA, name);
	}

	public static Map<String, Object> getContextValueFromAttachment(HAPDefinitionEntityContainerAttachment attContainer, String type, String name) {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		HAPAttachmentEntity attachment = (HAPAttachmentEntity)attContainer.getElement(type, name);
		if(attachment!=null) {
			JSONObject dataObjJson = attachment.getEntityJsonObj();
			for(Object key : dataObjJson.keySet()) {
				String n  = (String)key;
				out.put(n, dataObjJson.get(n));
			}
		}
		return out;
	}
	
	public static HAPResourceDefinition1 getResourceDefinition(HAPDefinitionEntityContainerAttachment attContainer, String type, String name, HAPManagerResourceDefinition resourceDefMan) {
		HAPAttachment attachment = attContainer.getElement(type, name);
		return getResourceDefinition(attachment, resourceDefMan);
	}
	
	public static HAPResourceDefinition1 getResourceDefinition(HAPAttachment attachment, HAPManagerResourceDefinition resourceDefMan) {
		HAPResourceDefinition1 out = null;
		if(attachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL)||attachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL)) {
			HAPResourceId resourceId = ((HAPAttachmentReference)attachment).getReferenceId();
			out = resourceDefMan.getLocalResourceDefinition(resourceId);
		}
		else if(attachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_ENTITY)){
			out = resourceDefMan.parseResourceDefinition(attachment.getValueType(), ((HAPAttachmentEntity)attachment).getEntityJsonObj());
		}
		return out;
	}
	
	public static void parseDefinition(JSONObject attachmentDefJson, HAPDefinitionEntityContainerAttachment attachmentContainer) {
		for(Object key : attachmentDefJson.keySet()) {
			String type = (String)key;
			JSONArray byNameArray = attachmentDefJson.getJSONArray(type);
			for(int i=0; i<byNameArray.length(); i++) {
				JSONObject attachmentJson = byNameArray.getJSONObject(i);
				HAPAttachment attachment = null;
				Object referenceIdObj = attachmentJson.opt(HAPAttachmentReference.REFERENCEID);
				if(referenceIdObj!=null) {
					HAPResourceId resourceId = HAPFactoryResourceId.newInstance(type, referenceIdObj);
					if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
						attachment = new HAPAttachmentReferenceLocal(type);
					}
					else {
						attachment = new HAPAttachmentReferenceExternal(type);
					}
				}
				else if(attachmentJson.opt(HAPAttachmentEntity.ENTITY)!=null){
					attachment = new HAPAttachmentEntity(type);
				}
				else {
					attachment = new HAPAttachmentPlaceHolder(type);
				}
				attachment.buildObject(attachmentJson, HAPSerializationFormat.JSON);
				attachmentContainer.addAttachment(type, attachment);
			}
		}
	}

	public static void setOverridenByParent(HAPAttachment ele) {
		ele.getInfo().setValue(ATTRIBUTE_FLAG_OVERRIDE, Boolean.TRUE);
	}
	
	public static boolean isOverridenByParent(HAPAttachment ele) {
		return Boolean.TRUE.equals(ele.getInfo().getValue(ATTRIBUTE_FLAG_OVERRIDE));
	}
	
	public static boolean isOverridenByParentMode(HAPAttachment ele) {
		String mode = (String)ele.getInfo().getValue(ATTRIBUTE_OVERRIDE_MODE);
		if(mode==null)   mode =  OVERRIDE_MODE_NONE;
		return OVERRIDE_MODE_CONFIGURABLE.equals(mode);
	}
	
}
