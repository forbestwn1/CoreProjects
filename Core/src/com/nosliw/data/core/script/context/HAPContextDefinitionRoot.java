package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPContextDefinitionRoot extends HAPEntityInfoWritableImp{

	public static final String INHERIT_MODE = "inherit";
	public static final String INHERIT_MODE_FINAL = "final";
	
	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	@HAPAttribute
	public static final String DEFAULT = "defaultValue";

	//default value for the element, used in runtime when no value is set
	private Object m_defaultValue;

	//context element definition
	private HAPContextDefinitionElement m_definition;
	
	//calculated, discover all the relative element with path to it: path --- element
	private Map<String, HAPContextDefinitionLeafRelative> m_relativeEleInfo;
	
	public HAPContextDefinitionRoot() {}
	
	public HAPContextDefinitionRoot(HAPContextDefinitionElement definition) {  this.m_definition = definition;  }

	public Object getDefaultValue(){   
		Object value;
		if(this.isConstant()) {
			//for constant, default value is constant value
			HAPContextDefinitionLeafConstant constantEle = (HAPContextDefinitionLeafConstant)this.getDefinition();
			value = constantEle.getValue();
		}
		else {
			value = this.m_defaultValue;
		}
		return value;  
	}

	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

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
		out.m_defaultValue = this.m_defaultValue;
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
		
		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPContextDefinitionRoot) {
			HAPContextDefinitionRoot root = (HAPContextDefinitionRoot)obj;
			if(!super.equals(obj))  return false;
			if(!HAPBasicUtility.isEquals(this.m_defaultValue, root.m_defaultValue))  return false;
			if(!HAPBasicUtility.isEquals(this.m_definition, root.m_definition)) return false;
			out = true;
		}
		return out;
	}

}
