package com.nosliw.core.application.common.dataexpression.definition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPOperationId;

public class HAPDefinitionOperandOperation extends HAPDefinitionOperand{

	//the data type operation defined on
	protected HAPDataTypeId m_dataTypeId;
	
	//operation name
	protected String m_operation;
	
	//base dataHAPDefinitionOperand
	protected HAPDefinitionOperand m_base;

	//operation parms
	protected Map<String, HAPDefinitionOperand> m_parms = new LinkedHashMap<String, HAPDefinitionOperand>();


	public HAPDefinitionOperandOperation(HAPDefinitionOperand base, String operation, List<HAPDefinitionParmInOperationOperand> parms){
		super(HAPConstantShared.EXPRESSION_OPERAND_OPERATION);
		if(base!=null) {
			this.m_base = base;
		}
		this.m_operation = operation;

		for(HAPDefinitionParmInOperationOperand opParm : parms) {
			this.m_parms.put(opParm.getName(), opParm.getOperand());
		}
	}
	
	public HAPDefinitionOperandOperation(String dataTypeIdLiterate, String operation, List<HAPDefinitionParmInOperationOperand> parms){
		super(HAPConstantShared.EXPRESSION_OPERAND_OPERATION);
		this.m_dataTypeId = (HAPDataTypeId)HAPManagerSerialize.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);
		this.m_operation = operation;
		
		for(HAPDefinitionParmInOperationOperand opParm : parms){
			if(HAPUtilityBasic.isStringEmpty(opParm.getName())) {
				this.m_base = opParm.getOperand();
			} else {
				this.m_parms.put(opParm.getName(), opParm.getOperand());
			}
		}
	}
	
	public HAPDefinitionOperand getBase(){  return this.m_base;  }
	
	public Map<String, HAPDefinitionOperand> getParms(){   return this.m_parms;   }
	
	public void addParm(String name, HAPDefinitionOperand parmOperand){
		this.m_parms.put(name, parmOperand);
	}
	
	public HAPDataTypeId getDataTypeId(){   return this.m_dataTypeId; }

	public String getOperaion(){  return this.m_operation;  }
	
	public HAPOperationId getOperationId(){
		HAPOperationId out = null;
		if(this.m_dataTypeId!=null){
			out = new HAPOperationId(this.m_dataTypeId, this.m_operation);
		}
		return out;  
	}
	
	@Override
	public List<HAPDefinitionOperand> getChildren(){
		List<HAPDefinitionOperand> out = new ArrayList<HAPDefinitionOperand>();
		if(this.m_base!=null) {
			out.add(this.m_base);
		}
		out.addAll(this.m_parms.values());
		return out;
	}
}
