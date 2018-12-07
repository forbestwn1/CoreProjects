package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPContextFlat extends HAPSerializableImp{

	@HAPAttribute
	public static final String CONTEXT = "context";

	@HAPAttribute
	public static final String NAMEMAPPING = "nameMapping";
	
	private HAPContext m_context;
	
	private Map<String, String> m_nameMapping;
	
	
	public HAPContextFlat() {
		this.m_context = new HAPContext();
		this.m_nameMapping = new LinkedHashMap<String, String>();
	}
	
	public HAPContext getContext() {  return this.m_context;  }
	
	public void addElement(String name, HAPContextNodeRoot rootEle){		this.m_context.addElement(name, rootEle);	}
	
	public void addNameMapping(String name, String nameMapping) {		this.m_nameMapping.put(name, nameMapping);	}
	
	public Map<String, Object> getConstantValue(){		return this.m_context.getConstantValue();	}
	
	public HAPContextFlat getVariableContext() {   
		HAPContextFlat out = new HAPContextFlat();
		out.m_context = this.m_context.getVariableContext();
		out.m_nameMapping.putAll(this.m_nameMapping);
		return out;
	}

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAMEMAPPING, HAPJsonUtility.buildMapJson(this.m_nameMapping));
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
	}
	
}
