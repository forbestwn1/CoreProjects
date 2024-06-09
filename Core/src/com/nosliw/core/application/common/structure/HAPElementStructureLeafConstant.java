package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data1.HAPParserDataExpression;

@HAPEntityWithAttribute
public class HAPElementStructureLeafConstant extends HAPElementStructure{

	@HAPAttribute
	public static final String VALUE = "value";

	private Object m_value;
	
	public HAPElementStructureLeafConstant() {	}

	public HAPElementStructureLeafConstant(Object value) {
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
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser) {	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafConstant out = new HAPElementStructureLeafConstant();
		this.toStructureElement(out);
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(VALUE, this.m_value.toString());
		typeJsonMap.put(VALUE, this.m_value.getClass());
	}

	@Override
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
		((HAPElementStructureLeafConstant)out).m_value = this.m_value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafConstant) {
			HAPElementStructureLeafConstant ele = (HAPElementStructureLeafConstant)obj;
			if(!HAPUtilityBasic.isEquals(this.m_value, ele.m_value))  return false;
			out = true;
		}
		return out;
	}

}
