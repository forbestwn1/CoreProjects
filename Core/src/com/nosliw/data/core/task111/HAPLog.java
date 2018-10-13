package com.nosliw.data.core.task111;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPLog extends HAPSerializableImp{

	private List<HAPLog> m_children;
	
	public HAPLog() {
		this.m_children = new ArrayList<HAPLog>();
	}

	public List<HAPLog> getChildren(){   return this.m_children;   }
	
	public void addChild(HAPLog child) {	this.m_children.add(child);	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}	
	
}
