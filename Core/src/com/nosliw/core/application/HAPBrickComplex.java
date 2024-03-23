package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.valuecontext.HAPValueContext;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPValuePortValueContext;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPBrickComplex extends HAPBrick{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";
	
	private HAPValueContext m_valueContext;

	private HAPDomainValueStructure m_valueStructureDomain; 
	
	public void setValueContext(HAPValueContext valueContext) {     this.m_valueContext = valueContext;      }
	public HAPValueContext getValueContext() {    return this.m_valueContext;    }
	
	public void setValueStructureDomain(HAPDomainValueStructure valueStructureDomain) {   this.m_valueStructureDomain = valueStructureDomain;     }

	
	@Override
	public HAPContainerValuePorts getValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		out.addValuePort(new HAPValuePortValueContext(this, this.m_valueStructureDomain, true));
		out.addValuePorts(this.getOtherValuePorts());
		return out;
	}
	
	protected HAPContainerValuePorts getOtherValuePorts() {   return null;   }
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUECONTEXT, this.m_valueContext.toStringValue(HAPSerializationFormat.JSON));
	}
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(VALUECONTEXT, this.m_valueContext.toResourceData(runtimeInfo).toString());
	}
	
}
