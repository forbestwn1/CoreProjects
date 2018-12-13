package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPContextDefinitionRoot extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	//context element definition
	private HAPContextDefinitionElement m_definition;
	
	//calculated, discover all the relative element with path to it: path --- element
	private Map<String, HAPContextDefinitionLeafRelative> m_relativeEleInfo;
	
	public HAPContextDefinitionRoot() {}
	
	public HAPContextDefinitionRoot(HAPContextDefinitionElement definition) {  this.m_definition = definition;  }
	
	public boolean isConstant() {		return HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT.equals(this.m_definition.getType());	}
	
	public Map<String, HAPContextDefinitionLeafRelative> getRelativeInfo() {
		if(this.isConstant())  return null;
		if(this.m_relativeEleInfo==null) {
			this.m_relativeEleInfo = HAPUtilityContext.isContextDefinitionElementRelative(m_definition);
		}
		if(this.m_relativeEleInfo==null || this.m_relativeEleInfo.isEmpty())  return null;
		return this.m_relativeEleInfo;
	}
	
	public boolean isAbsolute() {  return !(this.isConstant() || this.getRelativeInfo()!=null);      }

	public HAPContextDefinitionElement getDefinition() {   return this.m_definition;   }

	public void setDefinition(HAPContextDefinitionElement definition) {   this.m_definition = definition;  }
	
	public HAPContextDefinitionRoot cloneContextDefinitionRootBase() {
		HAPContextDefinitionRoot out = new HAPContextDefinitionRoot();
		this.cloneToEntityInfo(out);
		return out;
	}

	public HAPContextDefinitionRoot cloneContextDefinitionRoot() {
		HAPContextDefinitionRoot out = this.cloneContextDefinitionRootBase(); 
		out.m_definition = this.m_definition.cloneContextDefinitionElement();
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
	}
}
