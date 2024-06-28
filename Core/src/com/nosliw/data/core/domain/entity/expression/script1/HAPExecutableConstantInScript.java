package com.nosliw.data.core.domain.entity.expression.script1;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPExecutableConstantInScript extends HAPExecutableImp{

	@HAPAttribute
	public static String CONSTANTNAME = "constantName";

	@HAPAttribute
	public static String VALUE = "value";

	private String m_constantName; 
	
	private Object m_value;

	public HAPExecutableConstantInScript(String name, Object value){
		this.m_constantName = name;
		this.m_value = value;
	}
	
	public String getConstantName(){	return this.m_constantName;	}

	public Object getValue() {    return this.m_value;    }
	
	public HAPExecutableConstantInScript cloneConstantInScript() {
		HAPExecutableConstantInScript out = new HAPExecutableConstantInScript(this.m_constantName, this.m_value);
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONSTANTNAME, this.m_constantName);
		jsonMap.put(VALUE, HAPManagerSerialize.getInstance().toStringValue(m_value, HAPSerializationFormat.JSON));
	}
}
