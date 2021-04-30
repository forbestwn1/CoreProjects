package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public abstract class HAPElement extends HAPSerializableImp{
	
	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String PROCESSED = "processed";
	
	private boolean m_processed = false;
	
	public HAPElement(){}
	
	abstract public String getType(); 
	
	abstract public HAPElement cloneStructureElement();

	abstract public HAPElement toSolidStructureElement(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv);

	public HAPElement getSolidStructureElement() {  return this;  }
	
	public void toStructureElement(HAPElement out) {
		out.m_processed = this.m_processed;
	}

	public HAPElement getChild(String childName) {   return null;  }
	
	public void processed() {   this.m_processed = true;   }
	public void unProcessed() {    this.m_processed = false;    }
	public boolean isProcessed() {  return this.m_processed;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(PROCESSED, this.isProcessed()+"");
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPElement) {
			HAPElement ele = (HAPElement)obj;
			if(!ele.getType().equals(this.getType()))  return false;
			if(!(ele.m_processed==this.m_processed))  return false;
			out = true;
		}
		return out;
	}
}
