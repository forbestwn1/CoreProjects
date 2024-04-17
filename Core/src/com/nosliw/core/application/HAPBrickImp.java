package com.nosliw.core.application;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPFactoryResourceId;

public class HAPBrickImp extends HAPBrick{

	@Override
	public boolean buildBrick(Object value, HAPSerializationFormat format, HAPManagerApplicationBrick brickMan) {
		
		switch(format) {
		case JSON:
			buildBrickFormatJson((JSONObject)value, brickMan);
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
		JSONArray attrsJsonArray = jsonObj.optJSONArray(HAPBrick.ATTRIBUTE);
		if(attrsJsonArray!=null) {
			return buildBrickFromJson(jsonObj, brickMan);
		}
		else {
			return buildBrickFromJsonSlim(jsonObj, brickMan);
		}
	}
	
	private boolean buildBrickFromJson(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		JSONArray attrsJsonArray = jsonObj.optJSONArray(HAPBrick.ATTRIBUTE);
		for(int i=0; i<attrsJsonArray.length(); i++) {
			HAPAttributeInBrick attribute = new HAPAttributeInBrick();
			
			JSONObject attrJsonObj = attrsJsonArray.getJSONObject(i);
			//parse attribute entity info
			attribute.buildObject(attrJsonObj, HAPSerializationFormat.JSON);
			
			//parse attribute value wrapper
			JSONObject valueWrapperJsonObj = attrJsonObj.getJSONObject(HAPAttributeInBrick.VALUEWRAPPER);
			String valueType =(String)valueWrapperJsonObj.opt(HAPWrapperValueInAttribute.VALUETYPE);
			if(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK.equals(valueType)) {
				//brick
				JSONObject brickJsonObj =  valueWrapperJsonObj.getJSONObject(HAPWrapperValueInAttributeBrick.BRICK);
				HAPIdBrickType brickTypeId = HAPUtilityBrick.parseBrickTypeId(brickJsonObj.opt(HAPBrick.BRICKTYPE));
				HAPBrick attrBrick = brickMan.getBrickPlugin(brickTypeId).newInstance();
				attrBrick.buildBrick(brickJsonObj, HAPSerializationFormat.JSON, brickMan);
				attribute.setValueOfBrick(attrBrick);
			}
			else if(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID.equals(valueType)) {
				//ref to resource
				attribute.setValueOfResourceId(HAPFactoryResourceId.newInstance(valueWrapperJsonObj.opt(HAPWrapperValueInAttributeReferenceResource.RESOURCEID)));
			}
			else {
				//value
				attribute.setValueOfValue(buildAttributeValueFormatJson(attribute.getName(), valueWrapperJsonObj.opt(HAPWrapperValueInAttributeValue.VALUE)));
			}
			this.setAttribute(attribute);
		}
		return true;
	}
	
	private boolean buildBrickFromJsonSlim(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		for(Object key : jsonObj.keySet()) {
			HAPAttributeInBrick attribute = new HAPAttributeInBrick();
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
					Object resourceIdObj = attrObjJson.opt(HAPWrapperValueInAttributeReferenceResource.RESOURCEID);
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
	
    protected boolean buildAttributeValueFormatJson(String attrName, Object obj) {return true;}
    
    protected HAPIdBrickType getAttributeBrickType(String attrName) {   return null;     }
}
