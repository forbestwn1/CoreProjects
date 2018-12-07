package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPConstant;

public class HAPContextDefinitionRoot extends HAPEntityInfoImp{

	//context element definition
	private HAPContextDefinitionElement m_definition;
	
	//calculated, relative element information: path --- element
	private Map<String, HAPContextDefinitionLeafRelative> m_relativeEleInfo;
	
	public boolean isConstant() {		return HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT.equals(this.m_definition.getType());	}
	
	public Map<String, HAPContextDefinitionLeafRelative> getRelativeInfo() {
		if(this.isConstant())  return null;
		if(this.m_relativeEleInfo!=null) {
			this.m_relativeEleInfo = HAPUtilityContext.isContextDefinitionElementRelative(m_definition);
		}
		if(this.m_relativeEleInfo==null || this.m_relativeEleInfo.isEmpty())  return null;
		return this.m_relativeEleInfo;
	}
	
	public boolean isAbsolute() {  return !(this.isConstant() || this.getRelativeInfo()!=null);      }

	public HAPContextDefinitionElement getDefinition() {   return this.m_definition;   }

	public void setDefinition(HAPContextDefinitionElement definition) {   this.m_definition = definition;  }
	
	
	public void toContextDefinitionRoot(HAPContextDefinitionRoot root) {
		this.cloneToEntityInfo(root);
		
	}
	
	public HAPContextDefinitionRoot cloneContextDefinitionRoot() {
		HAPContextDefinitionRoot out = new HAPContextDefinitionRoot();
		this.cloneToEntityInfo(out);
		out.m_definition = this.m_definition.cloneContextDefinitionElement();
		return out;
	}
}
