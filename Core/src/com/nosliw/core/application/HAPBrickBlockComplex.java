package com.nosliw.core.application;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPWithDataExpression;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPContainerVariableInfo;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWithVariable;
import com.nosliw.core.application.common.valueport.HAPWrapperValuePort;
import com.nosliw.core.application.valuecontext.HAPValueContext;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPValuePortValueContext;

@HAPEntityWithAttribute
public class HAPBrickBlockComplex extends HAPBrickBlock implements HAPWithVariable, HAPWithDataExpression{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";

	private HAPContainerVariableInfo m_variableContainer;
	
	private HAPContainerDataExpression m_dataExpressionContainer;
	
	private HAPValueContext m_valueContext;

	private HAPDomainValueStructure m_valueStructureDomain; 
	
	public HAPBrickBlockComplex() {
		this.m_variableContainer = new HAPContainerVariableInfo();
		this.m_dataExpressionContainer = new HAPContainerDataExpression(); 
	}
	
	public void setValueContext(HAPValueContext valueContext) {     this.m_valueContext = valueContext;      }
	public HAPValueContext getValueContext() {    return this.m_valueContext;    }
	
	public void setValueStructureDomain(HAPDomainValueStructure valueStructureDomain) {   this.m_valueStructureDomain = valueStructureDomain;     }

	@Override
	public HAPContainerVariableInfo getVariablesInfo() {   return this.m_variableContainer;  }
	
	@Override
	public HAPContainerDataExpression getDataExpressions() {    return this.m_dataExpressionContainer;  }
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPGroupValuePorts valePortGroup = new HAPGroupValuePorts();
		valePortGroup.setName(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT);
		valePortGroup.addValuePort(new HAPWrapperValuePort(new HAPValuePortValueContext(this, this.m_valueStructureDomain)), true);
		out.addValuePortGroup(valePortGroup, true);
		
		for(HAPGroupValuePorts group : this.getInternalOtherValuePortGroups()) {
			out.addValuePortGroup(group, false);
		}
		return out;
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPGroupValuePorts valePortGroup = new HAPGroupValuePorts();
		valePortGroup.setName(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT);
		valePortGroup.addValuePort(new HAPWrapperValuePort(new HAPValuePortValueContext(this, this.m_valueStructureDomain)), true);
		out.addValuePortGroup(valePortGroup, true);
		
		for(HAPGroupValuePorts group : this.getExternalOtherValuePortGroups()) {
			out.addValuePortGroup(group, false);
		}
		return out;
	}

	protected Set<HAPGroupValuePorts> getInternalOtherValuePortGroups() {   return new HashSet<HAPGroupValuePorts>();   }
	protected Set<HAPGroupValuePorts> getExternalOtherValuePortGroups() {   return new HashSet<HAPGroupValuePorts>();   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_valueContext!=null) {
			jsonMap.put(VALUECONTEXT, this.m_valueContext.toStringValue(HAPSerializationFormat.JSON));
		}
		
		if(!this.m_variableContainer.isEmpty()) {
			jsonMap.put(VARIABLEINFOS, this.m_variableContainer.toStringValue(HAPSerializationFormat.JSON));
		}
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		if(this.m_valueContext!=null) {
			jsonMap.put(VALUECONTEXT, this.m_valueContext.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
	}
	
	@Override
	protected boolean buildBrickFormatJson(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		super.buildBrickFormatJson(jsonObj, brickMan);
		JSONObject valueContextJsonObj = jsonObj.optJSONObject(VALUECONTEXT);
		if(valueContextJsonObj!=null) {
			this.m_valueContext = new HAPValueContext();
			this.m_valueContext.buildObject(valueContextJsonObj, HAPSerializationFormat.JSON);
			
		}
		return true;
	}
}
