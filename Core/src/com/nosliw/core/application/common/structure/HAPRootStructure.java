package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPWithConstantScriptExpression;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

@HAPEntityWithAttribute
public class HAPRootStructure extends HAPEntityInfoWritableImp implements HAPWithConstantScriptExpression{

	public static final String INHERIT_MODE = "inherit";
	public static final String INHERIT_MODE_FINAL = "final";

	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	@HAPAttribute
	public static final String DEFAULT = "defaultValue";

	//default value for the element, used in runtime when no value is set
	private Object m_defaultValue;

	//context element definition
	private HAPElementStructure m_definition;
	
	//calculated, discover all the relative element with path to it: path --- element
	private Map<String, HAPElementStructureLeafRelative> m_relativeEleInfo;
	
	public HAPRootStructure() {}
	
	public HAPRootStructure(HAPElementStructure definition, HAPEntityInfo entityInfo) {
		this.m_definition = definition;
		entityInfo.cloneToEntityInfo(this);
	}

	public Object getDefaultValue(){   
		Object value;
		if(this.isConstant()) {
			//for constant, default value is constant value
			HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)this.getDefinition();
			value = constantEle.getValue();
		}
		else {
			value = this.m_defaultValue;
		}
		return value;  
	}
	
	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

	public boolean isConstant() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(this.m_definition.getType());	}
	
	public Map<String, HAPElementStructureLeafRelative> getRelativeInfo() {
		if(this.isConstant())  return null;
		if(this.m_relativeEleInfo==null) {
			this.m_relativeEleInfo = HAPUtilityStructure.discoverRelativeElement(this);
		}
		if(this.m_relativeEleInfo==null || this.m_relativeEleInfo.isEmpty())  return null;
		return this.m_relativeEleInfo;
	}
	
	public boolean isAbsolute() {  return !(this.isConstant() || this.getRelativeInfo()!=null);      }

	public HAPElementStructure getDefinition() {   return this.m_definition;   }

	public void setDefinition(HAPElementStructure definition) {   this.m_definition = definition;  }

	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,	HAPParserDataExpression expressionParser) {
		this.m_definition.discoverConstantScript(complexEntityId, parserContext, expressionParser);
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {
		this.m_definition.solidateConstantScript(values);
	}

	public HAPRootStructure cloneRootBase() {
		HAPRootStructure out = new HAPRootStructure();
		this.cloneToEntityInfo(out);
		return out;
	}
	
	public HAPRootStructure cloneExceptElement() {
		HAPRootStructure out = new HAPRootStructure();
		this.cloneToEntityInfo(out);
		if(this.m_definition!=null)  out.m_definition = this.m_definition.cloneStructureElement();
		out.m_defaultValue = this.m_defaultValue;
		return out;
	}

	public HAPRootStructure cloneRoot() {
		HAPRootStructure out = this.cloneExceptElement();
		return out;
	}

	@Override
	public Object cloneValue() {
		return this.cloneRoot();     
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
		if(obj instanceof HAPRootStructure) {
			HAPRootStructure root = (HAPRootStructure)obj;
			if(!super.equals(obj))  return false;
			if(!HAPUtilityBasic.isEquals(this.m_defaultValue, root.m_defaultValue))  return false;
			if(!HAPUtilityBasic.isEquals(this.m_definition, root.m_definition)) return false;
			out = true;
		}
		return out;
	}

}
