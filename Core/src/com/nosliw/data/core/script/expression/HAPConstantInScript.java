package com.nosliw.data.core.script.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;

public class HAPConstantInScript extends HAPSerializableImp{

	@HAPAttribute
	public static String CONSTANTNAME = "constantName";

	@HAPAttribute
	public static String VALUE = "value";
	
	private String m_constantName; 

	private Object m_value;
	
	public HAPConstantInScript(String name){
		this.m_constantName = name;
	}
	
	public String getConstantName(){	return this.m_constantName;	}
	
	public Object getValue() {  return this.m_value;   }
	public void setValue(Object value) {    this.m_value = value;    }
	
	public String updateName(HAPUpdateName nameUpdate) {
		this.m_constantName = nameUpdate.getUpdatedName(m_constantName);
		return this.m_constantName;
	}

	public HAPConstantInScript cloneConstantInScript() {
		HAPConstantInScript out = new HAPConstantInScript(this.m_constantName);
		out.m_value = this.m_value;
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONSTANTNAME, this.m_constantName);
		jsonMap.put(VALUE, HAPJsonUtility.buildJson(this.m_value, HAPSerializationFormat.JSON));
	}
}
