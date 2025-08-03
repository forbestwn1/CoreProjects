package com.nosliw.core.application.common.dataexpression1;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.xxx.application.valueport.HAPIdElement;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPOperandVariable extends HAPOperandImp{
 
	@HAPAttribute
	public final static String VARIABLENAME = "variableName";
	
	@HAPAttribute
	public final static String VARIABLEID = "variableId";
	
	@HAPAttribute
	public final static String VARIABLEKEY = "variableKey";
	
	protected String m_variableName;
	
	protected HAPIdElement m_variableId;
	
	protected String m_variableKey;
	
	private HAPOperandVariable(){}
	
	public HAPOperandVariable(String name){
		super(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE);
		this.m_variableName = name;
	}
	
	public String getVariableName(){  return this.m_variableName;  }
	public void setVariableName(String name){   this.m_variableName = name;  }
	
	public HAPIdElement getVariableId(){  return this.m_variableId;  }
	public void setVariableId(HAPIdElement id){   this.m_variableId = id;  }
	
	public String getVariableKey() {    return this.m_variableKey;    }
	public void setVariableKey(String key) {     this.m_variableKey = key;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, m_variableName);
		if(m_variableId!=null)   jsonMap.put(VARIABLEID, m_variableId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEKEY, m_variableKey);
	}

	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		
		HAPInfoCriteria variableInfo = variablesInfo.getVaraibleCriteriaInfo(this.getVariableKey());
		
		HAPMatchers matchers = HAPUtilityCriteria.mergeVariableInfo(variableInfo, expectCriteria, dataTypeHelper);
		
		//set output criteria
		this.setOutputCriteria(variableInfo.getCriteria());

		//cal converter
		return matchers;
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandVariable out = new HAPOperandVariable();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandVariable operand){
		super.cloneTo(operand);
		operand.m_variableName = this.m_variableName;
	}
	
}
