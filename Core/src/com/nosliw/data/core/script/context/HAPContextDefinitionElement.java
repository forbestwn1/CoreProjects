package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public abstract class HAPContextDefinitionElement extends HAPSerializableImp{
	
	@HAPAttribute
	public static final String TYPE = "type";

	private boolean m_processed = false;
	
	public HAPContextDefinitionElement(){}
	
	abstract public String getType(); 
	
	abstract public HAPContextDefinitionElement cloneContextDefinitionElement();

	abstract public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv);

	public HAPContextDefinitionElement getSolidContextDefinitionElement() {  return this.cloneContextDefinitionElement();  }
	
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		out.m_processed = this.m_processed;
	}

	public HAPContextDefinitionElement getChild(String childName) {   return null;  }
	
	public void processed() {   this.m_processed = true;   }
	public boolean isProcessed() {  return this.m_processed;   }
	

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}
	
}
