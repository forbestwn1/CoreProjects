package com.nosliw.data.core.common;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPUtilityData;

public class HAPDefinitionConstant extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String VALUE = "value";

	private Object m_value;
	
	private HAPData m_data;
	
	public Object getValue() {   return this.m_value;   }
	public void setValue(Object value) {   
		this.m_value = value;    
		this.m_data = HAPUtilityData.buildDataWrapperFromObject(value);
	}

	public boolean isData() {   return this.m_data!=null;    }
	public HAPData getData() {    return this.m_data;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.setValue(jsonObj.get(VALUE));
		return true;  
	}
}
