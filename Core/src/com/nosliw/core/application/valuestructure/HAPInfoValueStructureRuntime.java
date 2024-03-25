package com.nosliw.core.application.valuestructure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

@HAPEntityWithAttribute
public class HAPInfoValueStructureRuntime extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String INITVALUE = "initValue";

	private Object m_initValue;
	
	public HAPInfoValueStructureRuntime(String id, HAPInfo info, String name) {
		this.setId(id);
		this.setName(name);
		this.setInfo(info);
	}

	public void setInitValue(Object initValue){     this.m_initValue = initValue;      }
	public Object getInitValue() {     return this.m_initValue;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INITVALUE, HAPSerializeManager.getInstance().toStringValue(m_initValue, HAPSerializationFormat.JSON));
	}
}
