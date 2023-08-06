package com.nosliw.data.core.domain.entity.expression.script;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
abstract public class HAPExecutableSegmentExpression extends HAPExecutableImp{
	
	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ID = "id";

	private String m_id;
	
	public HAPExecutableSegmentExpression(String id) { 
		this.m_id = id;
	}
	
	public abstract String getType();

	public String getId() {    return this.m_id;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(ID, m_id);
	}
}
