package com.nosliw.core.application;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;

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
					JSONObject brickJsonObj =  valueWrapperJsonObj.getJSONObject(HAPWrapperValueInAttribute.VALUE);
					HAPIdBrickType brickTypeId = HAPUtilityBrick.parseBrickTypeId(brickJsonObj.opt(HAPBrick.BRICKTYPE));
					HAPBrick attrBrick = brickMan.getBrickPlugin(brickTypeId).newInstance();
					attrBrick.buildBrick(brickJsonObj, HAPSerializationFormat.JSON, brickMan);
					attribute.setValueInfo(new HAPWrapperValueInAttributeBrick(attrBrick));
				}
				else if(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID.equals(valueType)) {
					//ref to resource
					HAPResourceId resourceId = HAPFactoryResourceId.newInstance(valueWrapperJsonObj.opt(HAPWrapperValueInAttribute.VALUE));
					attribute.setValueInfo(new HAPWrapperValueInAttributeReferenceResource(resourceId));
				}
				else {
					//value
					Object attrValue = buildAttributeValueFormatJson(attribute.getName(), valueWrapperJsonObj.opt(HAPWrapperValueInAttribute.VALUE));
					attribute.setValueInfo(new HAPWrapperValueInAttributeValue(attrValue));
				}
				this.setAttribute(attribute);
			}
		}
		return true;
	}
	
    protected boolean buildAttributeValueFormatJson(String attrName, Object obj) {return true;}
}
