package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeManager;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementRange;
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
	private HAPOperandConverter m_baseConvertors;

	//operation name
	protected String m_operation;
	
	//operation parms
	protected Map<String, HAPOperand> m_parms;

	private Map<String, HAPOperandConverter> m_convertors;
	
	//out put criteria
	protected HAPDataTypeCriteria m_output;
	
	protected HAPDataTypeManager m_dataTypeMan;
	
	public HAPOperandOperation(HAPOperand base, String operation, Map<String, HAPOperand> parms, HAPDataTypeCriteriaManager criteriaMan){
		super(HAPConstant.EXPRESSION_OPERAND_OPERATION, criteriaMan);
		this.m_base = base;
		this.m_operation = operation;
		this.m_parms = parms;
	}
	
	public HAPOperandOperation(String dataTypeIdLiterate, String operation, Map<String, HAPOperand> parms, HAPDataTypeCriteriaManager criteriaMan){
		super(HAPConstant.EXPRESSION_OPERAND_OPERATION, criteriaMan);
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
	public HAPDataTypeCriteria discoverVariables(
			Map<String, HAPDataTypeCriteria> variables, 
			HAPDataTypeCriteria expectCriteria,
			HAPProcessVariablesContext context) {
		//process base first
		if(this.m_base!=null)			this.m_base.discoverVariables(variables, null, context);
		
		//define seperate one, do not work on original one
		HAPDataTypeId dataTypeId = this.m_dataTypeId;
		
		//try to get base data type
		if(dataTypeId==null && this.m_base!=null){
			HAPDataTypeCriteria baseDataTypeCriteria = this.m_base.getDataTypeCriteria();
			if(baseDataTypeCriteria.getType().equals(HAPConstant.DATATYPECRITERIA_TYPE_DATATYPEID)){
				dataTypeId = ((HAPDataTypeCriteriaElementId)baseDataTypeCriteria).getDataTypeId();
			}
		}

		if(dataTypeId !=null){
			this.m_dataTypeId = dataTypeId;
			//discover parms by operation definition
			HAPDataTypeOperation dataTypeOperation = this.m_dataTypeMan.getOperationInfoByName(dataTypeId, m_operation);
			Map<String, HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
			for(String parm: this.m_parms.keySet()){
				HAPOperand parmDataType = this.m_parms.get(parm);
				HAPDataTypeCriteria parmCriteria = parmsInfo.get(parm).getCriteria();
				//loose input criteria
				HAPDataTypeCriteria loosedParmCriteria = this.getDataTypeCriteriaManager().looseCriteria(parmCriteria);
				loosedParmCriteria = parmDataType.discoverVariables(variables, loosedParmCriteria, context);
			}
			
			//discover base
			if(this.m_base!=null){
				this.m_base.discoverVariables(variables, new HAPDataTypeCriteriaElementRange(dataTypeId, null, this.getDataTypeCriteriaManager()), context);
			}
			this.setDataTypeCriteria(dataTypeOperation.getOperationInfo().getOutputInfo().getCriteria());
			//check if output compatible with expect
			if(!this.getDataTypeCriteriaManager().compatibleWith(this.m_output, expectCriteria)){
				context.addMessage("Error");
			}
		}
		else{
			for(String parm: this.m_parms.keySet()){
				HAPOperand parmDataType = this.m_parms.get(parm);
				parmDataType.discoverVariables(variables, null, context);
			}
			this.setDataTypeCriteria(null);
		}
		return this.getDataTypeCriteria();
	}

	@Override
	public HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo){

		if(this.m_dataTypeId !=null){
			//normalize parms
			HAPDataTypeOperation dataTypeOperation = this.m_dataTypeMan.getOperationInfoByName(this.m_dataTypeId, m_operation);
			Map<String, HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
			for(String parm: this.m_parms.keySet()){
				HAPDataTypeCriteria parmDefCriteria = parmsInfo.get(parm).getCriteria();
				HAPOperand parmDataType = this.m_parms.get(parm);
				HAPDataTypeCriteria normalizedParmCriteria = parmDataType.normalize(variablesInfo);
				
				//figure out the convertor: from normalized to parmDataType
				HAPRelationship relationship = this.getDataTypeCriteriaManager().buildConvertor(normalizedParmCriteria, parmDefCriteria);
				if(relationship!=null){
					this.m_convertors.put(parm, new HAPOperandConverter(relationship, this.getDataTypeCriteriaManager()));
				}
			}

			//process base
			if(this.m_base!=null){
				HAPDataTypeCriteria normalizedParmCriteria = this.m_base.normalize(variablesInfo);
				//convertor : from normalized to this.m_dataTypeId
				HAPRelationship relationship = this.getDataTypeCriteriaManager().buildConvertor(normalizedParmCriteria, new HAPDataTypeCriteriaElementId(this.m_dataTypeId, this.getDataTypeCriteriaManager()));
				this.m_baseConvertors = new HAPOperandConverter(relationship, this.getDataTypeCriteriaManager());
			}
		}
		return this.getDataTypeCriteria();
	}
}
