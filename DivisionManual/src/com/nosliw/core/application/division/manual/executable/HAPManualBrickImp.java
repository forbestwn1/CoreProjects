package com.nosliw.core.application.division.manual.executable;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.HAPWrapperValueOfValue;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualBrickImp extends HAPManualBrick{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public boolean buildBrick(Object value, HAPSerializationFormat format, HAPManagerApplicationBrick brickMan) {
		switch(format) {
		case JSON:
			buildBrickFormatJson((JSONObject)value, brickMan);
			break;
		case JSON_FULL:
			break;
		case HTML:
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return true;
	}

	protected boolean buildBrickFormatJson(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		JSONArray attrsJsonArray = jsonObj.optJSONArray(HAPManualBrick.ATTRIBUTE);
		if(attrsJsonArray!=null) {
			return buildBrickFromJson(jsonObj, brickMan);
		}
		else {
			return buildBrickFromJsonSlim(jsonObj, brickMan);
		}
	}
	
	private boolean buildBrickFromJson(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		JSONArray attrsJsonArray = jsonObj.optJSONArray(HAPManualBrick.ATTRIBUTE);
		for(int i=0; i<attrsJsonArray.length(); i++) {
			HAPManualAttributeInBrick attribute = new HAPManualAttributeInBrick();
			
			JSONObject attrJsonObj = attrsJsonArray.getJSONObject(i);
			//parse attribute entity info
			attribute.buildObject(attrJsonObj, HAPSerializationFormat.JSON);
			
			//parse attribute value wrapper
			JSONObject valueWrapperJsonObj = attrJsonObj.getJSONObject(HAPManualAttributeInBrick.VALUEWRAPPER);
			String valueType =(String)valueWrapperJsonObj.opt(HAPWrapperValue.VALUETYPE);
			if(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_VALUE.equals(valueType)) {
				//value
				attribute.setValueOfValue(buildAttributeValueFormatJson(attribute.getName(), valueWrapperJsonObj.opt(HAPWrapperValueOfValue.VALUE)));
			}
			else {
				HAPIdBrickType blockType = null;
				if(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK.equals(valueType)) {
					//brick
					JSONObject brickJsonObj =  valueWrapperJsonObj.getJSONObject(HAPWrapperValueOfBlock.BLOCK);
					blockType = HAPUtilityBrickId.parseBrickTypeId(brickJsonObj.opt(HAPManualBrick.BRICKTYPE));
					HAPManualBrick attrBrick = brickMan.newBrickInstance(blockType);
					attrBrick.buildBrick(brickJsonObj, HAPSerializationFormat.JSON, brickMan);
					attribute.setValueOfBrick(attrBrick);
				}
				else if(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID.equals(valueType)) {
					//ref to resource
					HAPResourceId resourceId = HAPFactoryResourceId.newInstance(valueWrapperJsonObj.opt(HAPWrapperValueOfReferenceResource.RESOURCEID));
					attribute.setValueOfResourceId(resourceId);
					blockType = HAPUtilityBrickId.getBrickTypeIdFromResourceId(resourceId);
				}

				//parse adapter
				JSONArray adapterJsonArray = attrJsonObj.optJSONArray(HAPManualAttributeInBrick.ADAPTER);
				if(adapterJsonArray!=null) {
					List<HAPWrapperAdapter> adapters = parseAdapter(adapterJsonArray, figureoutAdatperType(blockType, null, brickMan), brickMan);
					for(HAPWrapperAdapter adapter : adapters) {
						attribute.addAdapter(adapter);
					}
				}
			}
			
			this.setAttribute(attribute);
		}
		return true;
	}
	
	private static List<HAPWrapperAdapter> parseAdapter(JSONArray adaptersArray, HAPIdBrickType adatperTypeId, HAPManagerApplicationBrick brickManager){
		List<HAPWrapperAdapter> out = new ArrayList<HAPWrapperAdapter>();

		for(int i=0; i<adaptersArray.length(); i++) {
			JSONObject adapterObj = adaptersArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
				HAPWrapperAdapter adapterWrapper = null;
				
				JSONObject brickJson = adapterObj.optJSONObject(HAPWrapperAdapterWithBrick.BRICK);
				Object resourceIdObj = adapterObj.opt(HAPWrapperAdapterWithReferenceResource.RESOURCEID);
				if(brickJson!=null) {
					//brick
					HAPManualBrick adapterBrick = HAPUtilitySerializeJson.buildBrick(brickJson, adatperTypeId, brickManager);
					adapterWrapper = new HAPWrapperAdapterWithBrick(adapterBrick);
				}
				else if(resourceIdObj!=null) {
					//resourceId
					adapterWrapper = new HAPWrapperAdapterWithReferenceResource(HAPFactoryResourceId.newInstance(resourceIdObj));
				}
				
				adapterWrapper.buildEntityInfoByJson(adapterObj);
				if(adapterWrapper.getName()==null) {
					adapterWrapper.setName(HAPConstantShared.NAME_DEFAULT);
				}
				out.add(adapterWrapper);
			}
		}
		return out;
	}
	
	private static HAPIdBrickType figureoutAdatperType(HAPIdBrickType blockType, HAPIdBrickType adapterType, HAPManagerApplicationBrick brickMan) {
		if(adapterType!=null) {
			return adapterType;
		} else {
			return brickMan.getDefaultAdapterTypeByBlockType(blockType);
		}
	}


	
	private boolean buildBrickFromJsonSlim(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		for(Object key : jsonObj.keySet()) {
			HAPManualAttributeInBrick attribute = new HAPManualAttributeInBrick();
			String attrName = (String)key;
			attribute.setName(attrName);
			HAPIdBrickType attrBrickType = this.getAttributeBrickType(attrName);
			Object attrObj = jsonObj.opt(attrName);
			if(attrBrickType==null) {
				//value
				attribute.setValueOfValue(buildAttributeValueFormatJson(attrName, jsonObj.opt(attrName)));
			}
			else {
				//
				if(attrObj instanceof String) {
					//resource id
					attribute.setValueOfResourceId(HAPFactoryResourceId.newInstance(attrObj));
				}
				else if(attrObj instanceof JSONObject){
					JSONObject attrObjJson = (JSONObject)attrObj;
					Object resourceIdObj = attrObjJson.opt(HAPWrapperValueOfReferenceResource.RESOURCEID);
					if(resourceIdObj!=null) {
						//resource id
						attribute.setValueOfResourceId(HAPFactoryResourceId.newInstance(attrObj));
					}
					else {
						//brick
						attribute.setValueOfBrick(HAPUtilitySerializeJson.buildBrick(attrObjJson, attrBrickType, brickMan));
					}
				}
			}
			this.setAttribute(attribute);
		}
		return true;
	}
	
    protected Object buildAttributeValueFormatJson(String attrName, Object obj) {return true;}
    
    protected HAPIdBrickType getAttributeBrickType(String attrName) {   return null;     }

}
