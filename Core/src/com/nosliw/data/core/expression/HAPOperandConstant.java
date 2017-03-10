package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapperLiterate;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;

public class HAPOperandConstant extends HAPOperandImp{

	public final static String NAME = "name"; 

	public final static String DATA = "data"; 
	
	protected HAPData m_data;

	protected String m_name;
	
	public HAPOperandConstant(String constantStr){
		this.m_data = (HAPData)HAPSerializeManager.getInstance().buildObject(HAPDataWrapperLiterate.class.getName(), constantStr, HAPSerializationFormat.LITERATE);
		if(this.m_data==null){
			//not a valid data literate, then it is a constant name
			this.m_name = constantStr;
		}
	}
	
	@Override
	public String getType(){	return HAPConstant.EXPRESSION_OPERAND_CONSTANT;}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, m_name);
		jsonMap.put(DATA, HAPSerializeManager.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON_FULL));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, m_name);
		jsonMap.put(DATA, HAPSerializeManager.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDataTypeCriteria getDataTypeInfo() {
		if(this.m_data==null)  return null;
		else return new HAPDataTypeCriteriaElementId(this.m_data.getDataTypeId());
	}
	
}
