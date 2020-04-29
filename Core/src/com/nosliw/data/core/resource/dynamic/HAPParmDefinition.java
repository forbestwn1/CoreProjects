package com.nosliw.data.core.resource.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPUtilityData;

public class HAPParmDefinition extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATA = "data";

	private HAPData m_data;
	
	public HAPData getData() {    return this.m_data;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_data!=null)  	jsonMap.put(DATA, this.m_data.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		Object dataObj = jsonObj.opt(DATA);
		if(dataObj!=null) this.m_data = HAPUtilityData.buildDataWrapperFromObject(dataObj);
		return true;  
	}
}
