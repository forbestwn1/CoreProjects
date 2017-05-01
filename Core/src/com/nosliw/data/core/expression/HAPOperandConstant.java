package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;

public class HAPOperandConstant extends HAPOperandImp{

	public final static String NAME = "name"; 

	public final static String DATA = "data"; 
	
	protected HAPData m_data;

	protected String m_name;
	
	public HAPOperandConstant(String constantStr){
		super(HAPConstant.EXPRESSION_OPERAND_CONSTANT);
		HAPData data = HAPDataUtility.buildDataWrapper(constantStr);
		if(data==null){
			//not a valid data literate, then it is a constant name
			this.m_name = constantStr;
		}
		else{
			this.setData(data);
		}
	}
	
	public String getName(){  return this.m_name;  }
	
	public void setData(HAPData data){ 
		this.m_data = data;
		
		if(this.m_data==null){
			int kkkk = 5555;
			kkkk++;
		}
		
		this.setDataTypeCriteria(new HAPDataTypeCriteriaElementId(this.m_data.getDataTypeId()));
	}
	
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
	public HAPDataTypeCriteria discover(
			Map<String, HAPDataTypeCriteria> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context,
			HAPDataTypeHelper dataTypeHelper) {
		this.validate(this.getDataTypeCriteria(), expectCriteria, context, dataTypeHelper);
		return this.getDataTypeCriteria();
	}

	@Override
	public HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo, HAPDataTypeHelper dataTypeHelper){
		return this.getDataTypeCriteria();
	}
	
}
