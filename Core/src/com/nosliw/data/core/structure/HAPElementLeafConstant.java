package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPElementLeafConstant extends HAPElement{

	@HAPAttribute
	public static final String VALUE = "value";

	private Object m_value;
	
	public HAPElementLeafConstant() {	}

	public HAPElementLeafConstant(Object value) {
		this.m_value = value;
	}
	
	@Override
	public String getType() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT;	}

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
	public HAPElement cloneStructureElement() {
		HAPElementLeafConstant out = new HAPElementLeafConstant();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public HAPElement solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {  return this; }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(VALUE, this.m_value.toString());
		typeJsonMap.put(VALUE, this.m_value.getClass());
	}

	@Override
	public void toStructureElement(HAPElement out) {
		super.toStructureElement(out);
		((HAPElementLeafConstant)out).m_value = this.m_value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementLeafConstant) {
			HAPElementLeafConstant ele = (HAPElementLeafConstant)obj;
			if(!HAPBasicUtility.isEquals(this.m_value, ele.m_value))  return false;
			out = true;
		}
		return out;
	}
}
