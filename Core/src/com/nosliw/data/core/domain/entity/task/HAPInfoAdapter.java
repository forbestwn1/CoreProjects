package com.nosliw.data.core.domain.entity.task;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPInfoAdapter extends HAPSerializableImp{

	@HAPAttribute
	public static final String NAME = "name";

	private String m_name;
	
	public HAPInfoAdapter(String name) {
		this.m_name = name;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
	}
}
