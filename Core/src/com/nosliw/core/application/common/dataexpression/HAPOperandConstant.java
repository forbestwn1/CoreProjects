package com.nosliw.core.application.common.dataexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.valueport.HAPContainerVariableInfo;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPOperandConstant extends HAPOperandImp{

	@HAPAttribute
	public final static String NAME = "name"; 

	@HAPAttribute
	public final static String DATA = "data"; 
	
	protected HAPData m_data;

	protected String m_name;
	
	private HAPOperandConstant(){}
	
	public HAPOperandConstant(String constantStr){
		super(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT);
		HAPData data = HAPUtilityData.buildDataWrapper(constantStr);
		if(data==null){
			//not a valid data literate, then it is a constant name
			this.m_name = constantStr;
		}
		else{
			this.setData(data);
		}
	}

	public HAPOperandConstant(HAPData constantData){
		super(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT);
		this.setData(constantData);
	}

	public String getName(){  return this.m_name;  }
	public void setName(String name) {  this.m_name = name;   }
	
	public HAPData getData(){  return this.m_data;  }
	
	public void setData(HAPData data){ 	this.m_data = data;	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, m_name);
		jsonMap.put(DATA, HAPManagerSerialize.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		//set output criteria
		if(this.getOutputCriteria()==null){
			HAPDataTypeCriteria criteria = dataTypeHelper.getDataTypeCriteriaByData(m_data);
			this.setOutputCriteria(criteria);
		}
		return HAPUtilityCriteria.isMatchable(this.getOutputCriteria(), expectCriteria, dataTypeHelper);
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandConstant out = new HAPOperandConstant();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandConstant operand){
		super.cloneTo(operand);
		operand.m_name = this.m_name;
		operand.m_data = this.m_data;
		
	}
}
