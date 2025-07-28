package com.nosliw.core.application.common.dataexpression.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPDefinitionOperand extends HAPSerializableImp{

	public static String TYPE = "type";
	
	private String m_type;
	
	public HAPDefinitionOperand(String type) {
		this.m_type = type;
	}
	
	public String getType(){ return this.m_type;  }

	public List<HAPDefinitionOperand> getChildren(){   return new ArrayList<HAPDefinitionOperand>();    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}
}
