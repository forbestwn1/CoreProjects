package com.nosliw.data.core.operand;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.script.context.HAPContainerVariableCriteriaInfo;

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
		this.m_data = constantData;
	}

	public String getName(){  return this.m_name;  }
	public void setName(String name) {  this.m_name = name;   }
	
	public HAPData getData(){  return this.m_data;  }
	
	public void setData(HAPData data){ 	this.m_data = data;	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, m_name);
		jsonMap.put(DATA, HAPSerializeManager.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPMatchers discover(
			HAPContainerVariableCriteriaInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		//set output criteria
		if(this.getOutputCriteria()==null){
			HAPDataTypeCriteria criteria = dataTypeHelper.getDataTypeCriteriaByData(m_data);
			this.setOutputCriteria(criteria);
		}
		return HAPCriteriaUtility.isMatchable(this.getOutputCriteria(), expectCriteria, dataTypeHelper);
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
