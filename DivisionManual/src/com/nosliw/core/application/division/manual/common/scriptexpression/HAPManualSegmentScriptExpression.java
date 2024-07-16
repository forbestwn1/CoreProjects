package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
abstract public class HAPManualSegmentScriptExpression extends HAPExecutableImp{
	
	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ID = "id";

	private String m_id;
	
	public HAPManualSegmentScriptExpression(String id) { 
		this.m_id = id;
	}
	
	public abstract String getType();

	public String getId() {    return this.m_id;   }

	public List<HAPManualSegmentScriptExpression> getChildren(){     return new ArrayList<HAPManualSegmentScriptExpression>();      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(ID, m_id);
	}
}
