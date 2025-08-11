package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.xxx.application1.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPManualConstantInScript extends HAPExecutableImp{

	@HAPAttribute
	public static String CONSTANTNAME = "constantName";

	@HAPAttribute
	public static String VALUE = "value";

	private String m_constantName; 
	
	private Object m_value;

	public HAPManualConstantInScript(String name){
		this.m_constantName = name;
	}
	
	public HAPManualConstantInScript(String name, Object value){
		this.m_constantName = name;
		this.m_value = value;
	}
	
	public String getConstantName(){	return this.m_constantName;	}

	public Object getValue() {    return this.m_value;    }
	public void setValue(Object value) {  this.m_value = value;      }
	
	public HAPManualConstantInScript cloneConstantInScript() {
		HAPManualConstantInScript out = new HAPManualConstantInScript(this.m_constantName, this.m_value);
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONSTANTNAME, this.m_constantName);
		jsonMap.put(VALUE, HAPManagerSerialize.getInstance().toStringValue(m_value, HAPSerializationFormat.JSON));
	}
}
