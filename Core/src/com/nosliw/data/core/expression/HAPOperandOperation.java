package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeManager;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public class HAPOperandOperation extends HAPOperandImp{

	public static final String DATATYPE = "dataType";
	
	public static final String BASEDATA = "baseData";
	
	public static final String OPERATION = "operation";
	
	public static final String PARMS = "parms";
	
	//the data type operation defined on
	protected HAPDataTypeId m_dataTypeId;
	
	//base data
	protected HAPOperand m_base;

	//operation name
	protected String m_operation;
	
	//operation parms
	protected Map<String, HAPOperand> m_parms;

	protected HAPDataTypeManager m_dataTypeMan;
	
	public HAPOperandOperation(HAPOperand base, String operation, Map<String, HAPOperand> parms, HAPDataTypeCriteriaManager criteriaMan){
		super(criteriaMan);
		this.m_base = base;
		this.m_operation = operation;
		this.m_parms = parms;
	}
	
	public HAPOperandOperation(String dataTypeIdLiterate, String operation, Map<String, HAPOperand> parms, HAPDataTypeCriteriaManager criteriaMan){
		super(criteriaMan);
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);
		this.m_operation = operation;
		this.m_parms = parms;
	}

	public void setBase(HAPOperand base){
		this.m_base = base;
		if(this.m_base!=null)		this.addChildOperand(m_base);
	}
	
	public void setParms(Map<String, HAPOperand> parms){
		this.m_parms.putAll(parms);
		for(HAPOperand parm : parms.values()){
			this.addChildOperand(parm);
		}
	}
	
	public HAPDataTypeId getDataTypeId(){   return this.m_dataTypeId; }

	@Override
	public String getType() {		return HAPConstant.EXPRESSION_OPERAND_OPERATION;	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERATION, this.m_operation);
		jsonMap.put(DATATYPE, HAPSerializeManager.getInstance().toStringValue(this.m_dataTypeId, HAPSerializationFormat.LITERATE));
		jsonMap.put(BASEDATA, HAPSerializeManager.getInstance().toStringValue(this.m_base, HAPSerializationFormat.JSON_FULL));
		
		Map<String, String> parmsJsonMap = new LinkedHashMap<String, String>();
		for(String parmName : this.m_parms.keySet()){
			parmsJsonMap.put(parmName, HAPSerializeManager.getInstance().toStringValue(this.m_parms.get(parmName), HAPSerializationFormat.JSON_FULL));
		}
		jsonMap.put(PARMS, HAPJsonUtility.buildMapJson(parmsJsonMap));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERATION, this.m_operation);
		jsonMap.put(DATATYPE, HAPSerializeManager.getInstance().toStringValue(this.m_dataTypeId, HAPSerializationFormat.LITERATE));
		jsonMap.put(BASEDATA, HAPSerializeManager.getInstance().toStringValue(this.m_base, HAPSerializationFormat.JSON));
		
		Map<String, String> parmsJsonMap = new LinkedHashMap<String, String>();
		for(String parmName : this.m_parms.keySet()){
			parmsJsonMap.put(parmName, HAPSerializeManager.getInstance().toStringValue(this.m_parms.get(parmName), HAPSerializationFormat.JSON));
		}
		jsonMap.put(PARMS, HAPJsonUtility.buildMapJson(parmsJsonMap));
	}

	@Override
	public HAPDataTypeCriteria processVariable(Map<String, HAPDataTypeCriteria> variables, HAPDataTypeCriteria expectCriteria) {
		//process base first
		if(this.m_base!=null){
			this.m_base.processVariable(variables, null);
		}
		
		//try to get base data type
		if(this.m_dataTypeId==null && this.m_base!=null){
			HAPDataTypeCriteria baseDataTypeCriteria = this.m_base.getDataTypeCriteria();
			if(baseDataTypeCriteria.getType().equals(HAPConstant.DATATYPECRITERIA_TYPE_DATATYPEID)){
				this.m_dataTypeId = ((HAPDataTypeCriteriaElementId)baseDataTypeCriteria).getDataTypeId();
			}
		}

		if(this.m_dataTypeId !=null){
			HAPDataTypeOperation dataTypeOperation = this.m_dataTypeMan.getOperationInfoByName(m_dataTypeId, m_operation);
			
			Map<String, HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
			for(String parm: this.m_parms.keySet()){
				HAPOperand parmDataType = this.m_parms.get(parm);
				parmDataType.processVariable(variables, parmsInfo.get(parm).getCriteria());
			}
			
			return dataTypeOperation.getOperationInfo().getOutputInfo().getCriteria();
		}
		
		return null;
	}
}
