package com.nosliw.core.application.common.constant;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;

public class HAPDefinitionConstant extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String VALUE = "value";

	private Object m_value;
	
	private HAPData m_data;
	
	public HAPDefinitionConstant() {}
	
	public HAPDefinitionConstant(String id, Object value) {
		this.setId(id);
		this.setValue(value);
	}
	
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
	
	public HAPDefinitionConstant cloneConstantDefinition() {
		HAPDefinitionConstant out = new HAPDefinitionConstant();
		this.cloneToEntityInfo(out);
		out.setValue(this.m_value);
		return out;
	}
}
