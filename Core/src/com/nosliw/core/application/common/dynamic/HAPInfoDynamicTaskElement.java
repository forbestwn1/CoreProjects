package com.nosliw.core.application.common.dynamic;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public abstract class HAPInfoDynamicTaskElement extends HAPEntityInfoImp{

	@HAPAttribute
	public final static String TYPE = "type"; 

	public abstract String getType();

	public abstract HAPInfoDynamicTaskElement getChild(String childName);
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		return true;
	}
	
	public static HAPInfoDynamicTaskElement parse(Object obj) {
		HAPInfoDynamicTaskElement out = null;
		
		JSONObject jsonObj = (JSONObject)obj;
		String type = jsonObj.getString(TYPE);
		
		switch(type) {
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_SET:
			out = new HAPInfoDynamicTaskElementLeafSet();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
			break;
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_SIMPLE:
			out = new HAPInfoDynamicTaskElementLeafSimple();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
			break;
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_NODE:
			out = new HAPInfoDynamicTaskElementNode();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
			break;
		}
		
		if(out.getName()==null) {
			out.setName(HAPConstantShared.NAME_DEFAULT);
		}
		
		return out;
	}
	
}
