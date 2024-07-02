package com.nosliw.core.application.common.scriptexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
abstract public class HAPSegmentScriptExpression extends HAPExecutableImp{
	
	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ID = "id";

	private String m_id;
	
	public HAPSegmentScriptExpression(String id) { 
		this.m_id = id;
	}
	
	public abstract String getType();

	public String getId() {    return this.m_id;   }

	public List<HAPSegmentScriptExpression> getChildren(){     return new ArrayList<HAPSegmentScriptExpression>();      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(ID, m_id);
	}
}
