package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPConverters;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.HAPOperationOutInfo;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementRange;

public class HAPOperandOperation extends HAPOperandImp{

	public static final String DATATYPE = "dataType";
	
	public static final String BASEDATA = "baseData";
	
	public static final String OPERATION = "operation";
	
	public static final String PARMS = "parms";
	
	//the data type operation defined on
	protected HAPDataTypeId m_dataTypeId;
	
	//base data
	protected HAPOperand m_base;
	private HAPConverters m_baseConvertors;

	//operation name
	protected String m_operation;
	
	//operation parms
	protected Map<String, HAPOperand> m_parms;

	private Map<String, HAPConverters> m_parmConvertors;
	
	public HAPOperandOperation(HAPOperand base, String operation, Map<String, HAPOperand> parms){
		super(HAPConstant.EXPRESSION_OPERAND_OPERATION);
		this.m_base = base;
		this.m_operation = operation;
		this.m_parms = parms;
		this.m_parmConvertors = new LinkedHashMap<String, Map<HAPDataTypeId, HAPRelationship>>();
		this.processChildenOperand();
	}
	
	public HAPOperandOperation(String dataTypeIdLiterate, String operation, Map<String, HAPOperand> parms){
		super(HAPConstant.EXPRESSION_OPERAND_OPERATION);
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);
		this.m_operation = operation;
		this.m_parms = parms;
		this.m_parmConvertors = new LinkedHashMap<String, Map<HAPDataTypeId, HAPRelationship>>();
		this.processChildenOperand();
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

	public HAPOperationId getOperationId(){
		HAPOperationId out = null;
		if(this.m_dataTypeId!=null){
			out = new HAPOperationId(this.m_dataTypeId, this.m_operation);
		}
		return out;  
	}
	
	private void processChildenOperand(){
		if(this.m_base!=null)  this.addChildOperand(m_base);
		for(String parm : this.m_parms.keySet()){
			this.addChildOperand(this.m_parms.get(parm));
		}
	}
	
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
	public HAPConverters discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context,
			HAPDataTypeHelper dataTypeHelper) {
		//process base first
		if(this.m_base!=null)			this.m_base.discover(variablesInfo, null, context, dataTypeHelper);
		
		//define seperate one, do not work on original one
		HAPDataTypeId dataTypeId = this.m_dataTypeId;
		
		//try to get operation data type
		if(dataTypeId==null && this.m_base!=null){
			//if data type is not determined, then use trunk data type of base data type if it has any
			HAPDataTypeCriteria baseDataTypeCriteria = this.m_base.getDataTypeCriteria();
			if(baseDataTypeCriteria!=null)			dataTypeId = dataTypeHelper.getTrunkDataType(baseDataTypeCriteria);
		}

		if(dataTypeId !=null){
			this.m_dataTypeId = dataTypeId;
			//discover parms by operation definition
			HAPDataTypeOperation dataTypeOperation = dataTypeHelper.getOperationInfoByName(dataTypeId, m_operation);
			Map<String, HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
			for(String parm: this.m_parms.keySet()){
				HAPOperand parmDataType = this.m_parms.get(parm);
				if(parmDataType!=null){
					HAPDataTypeCriteria parmCriteria = parmsInfo.get(parm).getCriteria();
					//loose input criteria
					HAPConverters converter = parmDataType.discover(variablesInfo, parmCriteria, context, dataTypeHelper);
					if(converter!=null){
						this.m_parmConvertors.put(parm, converter);
					}
				}
			}
			
			//discover base
			if(this.m_base!=null){
				this.m_baseConvertors = this.m_base.discover(variablesInfo, new HAPDataTypeCriteriaElementId(dataTypeId), context, dataTypeHelper);
			}
			
			HAPOperationOutInfo outputInfo = dataTypeOperation.getOperationInfo().getOutputInfo();
			if(outputInfo!=null){
				this.setDataTypeCriteria(outputInfo.getCriteria());
			}
			//check if output compatible with expect
			if(!dataTypeHelper.compatibleWith(this.getDataTypeCriteria(), expectCriteria)){
				context.addMessage("Error");
			}
			return this.isConvertable(outputInfo.getCriteria(), expectCriteria, context, dataTypeHelper);
		}
		else{
			//if we don't have operation data type 
			for(String parm: this.m_parms.keySet()){
				HAPOperand parmDataType = this.m_parms.get(parm);
				parmDataType.discover(variablesInfo, null, context, dataTypeHelper);
			}
			this.setDataTypeCriteria(null);
			return null;
		}
	}

	@Override
	public HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo, HAPDataTypeHelper dataTypeHelper){

		if(this.m_dataTypeId !=null){
			//normalize parms
			HAPDataTypeOperation dataTypeOperation = dataTypeHelper.getOperationInfoByName(this.m_dataTypeId, m_operation);
			Map<String, HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
			for(String parm: this.m_parms.keySet()){
				HAPDataTypeCriteria parmDefCriteria = parmsInfo.get(parm).getCriteria();
				HAPOperand parmDataType = this.m_parms.get(parm);
				if(parmDataType!=null){
					HAPDataTypeCriteria normalizedParmCriteria = parmDataType.normalize(variablesInfo, dataTypeHelper);
					
					//figure out the convertor: from normalized to parmDataType
					Map<HAPDataTypeId, HAPRelationship> relationships = dataTypeHelper.buildConvertor(normalizedParmCriteria, parmDefCriteria);
					this.addConvertors(relationships.values());
					if(relationships!=null){
						this.m_convertors.put(parm, relationships);
					}
				}
			}

			//process base
			if(this.m_base!=null){
				HAPDataTypeCriteria normalizedParmCriteria = this.m_base.normalize(variablesInfo, dataTypeHelper);
				//convertor : from normalized to this.m_dataTypeId
				this.m_baseConvertors = dataTypeHelper.buildConvertor(normalizedParmCriteria, new HAPDataTypeCriteriaElementId(this.m_dataTypeId));
				this.addConvertors(this.m_baseConvertors.values());
			}
		}
		return this.getDataTypeCriteria();
	}
}
