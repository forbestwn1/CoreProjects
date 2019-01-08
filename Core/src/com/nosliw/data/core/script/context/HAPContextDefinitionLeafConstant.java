package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;

@HAPEntityWithAttribute
public class HAPContextDefinitionLeafConstant extends HAPContextDefinitionElement{

	@HAPAttribute
	public static final String VALUE = "value";

	private Object m_value;
	
	public HAPContextDefinitionLeafConstant() {	}

	public HAPContextDefinitionLeafConstant(Object value) {
		this.m_value = value;
	}
	
	@Override
	public String getType() {		return HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT;	}

	public void setValue(Object value){		this.m_value = value;	}

	public Object getValue(){   return this.m_value;  }
	
	/**
	 * Get data value of value
	 * if not data, then return null
	 * if is data, then return data object
	 */
	public HAPData getDataValue(){
		HAPDataWrapper out = new HAPDataWrapper();
		boolean isData = out.buildObjectByLiterate(this.m_value.toString());
		if(isData)  return out;
		else return null;
	}

	@Override
	public HAPContextDefinitionElement cloneContextDefinitionElement() {
		HAPContextDefinitionLeafConstant out = new HAPContextDefinitionLeafConstant();
		this.toContextDefinitionElement(out);
		return out;
	}

	@Override
	public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants, HAPRequirementContextProcessor contextProcessRequirement) {  return this; }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(VALUE, this.m_value.toString());
		typeJsonMap.put(VALUE, this.m_value.getClass());
	}

	@Override
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		super.toContextDefinitionElement(out);
		((HAPContextDefinitionLeafConstant)out).m_value = this.m_value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPContextDefinitionLeafConstant) {
			HAPContextDefinitionLeafConstant ele = (HAPContextDefinitionLeafConstant)obj;
			if(!HAPBasicUtility.isEquals(this.m_value, ele.m_value))  return false;
			out = true;
		}
		return out;
	}
}
