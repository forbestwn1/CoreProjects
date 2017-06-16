package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.HAPOperationOutInfo;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;
import com.nosliw.data.core.runtime.HAPResourceUtility;

public class HAPOperandOperation extends HAPOperandImp{

	@HAPAttribute
	public static final String DATATYPEID = "dataTypeId";
	
	@HAPAttribute
	public static final String OPERATION = "operation";
	
	@HAPAttribute
	public static final String BASE = "base";

	@HAPAttribute
	public static final String MATCHERSBASE = "matchersBase";

	@HAPAttribute
	public static final String PARMS = "parms";
	
	@HAPAttribute
	public static final String MATCHERSPARMS = "matchersParms";

	//the data type operation defined on
	protected HAPDataTypeId m_dataTypeId;
	
	//base data
	protected HAPOperand m_base;
	private HAPMatchers m_baseMatchers;

	//operation name
	protected String m_operation;
	
	//operation parms
	protected Map<String, HAPOperand> m_parms;

	private Map<String, HAPMatchers> m_parmsMatchers;
	
	public HAPOperandOperation(HAPOperand base, String operation, Map<String, HAPOperand> parms){
		super(HAPConstant.EXPRESSION_OPERAND_OPERATION);
		this.m_base = base;
		this.m_operation = operation;
		this.m_parms = parms;
		this.resetMatchers();
		this.processChildenOperand();
	}
	
	public HAPOperandOperation(String dataTypeIdLiterate, String operation, Map<String, HAPOperand> parms){
		super(HAPConstant.EXPRESSION_OPERAND_OPERATION);
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);
		this.m_operation = operation;
		this.m_parms = parms;
		this.resetMatchers();
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
	public Set<HAPDataTypeConverter> getConverters(){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
		for(String parm : this.m_parmsMatchers.keySet()){
			out.addAll(HAPResourceUtility.getConverterResourceIdFromRelationship(this.m_parmsMatchers.get(parm).getRelationships()));
		}
		
		if(this.m_baseMatchers!=null){
			out.addAll(HAPResourceUtility.getConverterResourceIdFromRelationship(this.m_baseMatchers.getRelationships()));
		}
		return out;	
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERATION, this.m_operation);
		jsonMap.put(DATATYPEID, HAPSerializeManager.getInstance().toStringValue(this.m_dataTypeId, HAPSerializationFormat.LITERATE));
		jsonMap.put(BASE, HAPSerializeManager.getInstance().toStringValue(this.m_base, HAPSerializationFormat.JSON));
		jsonMap.put(MATCHERSBASE, HAPSerializeManager.getInstance().toStringValue(this.m_baseMatchers, HAPSerializationFormat.JSON));
		
		jsonMap.put(PARMS, HAPJsonUtility.buildJson(this.m_parms, HAPSerializationFormat.JSON));
		jsonMap.put(MATCHERSPARMS, HAPJsonUtility.buildJson(this.m_parmsMatchers, HAPSerializationFormat.JSON));
	}

	private void resetMatchers(){
		this.m_parmsMatchers = new LinkedHashMap<String,HAPMatchers>();
		this.m_baseMatchers = null;
	}
	
	@Override
	public HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context,
			HAPDataTypeHelper dataTypeHelper) {
		//clear old matchers
		this.resetMatchers();
		
		//process base first
		if(this.m_base!=null)			this.m_base.discover(variablesInfo, null, context, dataTypeHelper);
		
		//define seperate one, do not work on original one
		HAPDataTypeId dataTypeId = this.m_dataTypeId;
		
		//try to get operation data type according to base 
		if(dataTypeId==null && this.m_base!=null){
			//if data type is not determined, then use trunk data type of base data type if it has any
			HAPDataTypeCriteria baseDataTypeCriteria = this.m_base.getOutputCriteria();
			if(baseDataTypeCriteria!=null)			dataTypeId = dataTypeHelper.getTrunkDataType(baseDataTypeCriteria);
		}

		if(dataTypeId !=null){
			this.m_dataTypeId = dataTypeId;
			//discover parms by operation definition
			HAPDataTypeOperation dataTypeOperation = dataTypeHelper.getOperationInfoByName(dataTypeId, m_operation);
			List<HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
			for(HAPOperationParmInfo parmInfo : parmsInfo){
				
				
				
			}
			
			for(String parm: this.m_parms.keySet()){
				HAPOperand parmDataType = this.m_parms.get(parm);
				if(parmDataType!=null){
					HAPDataTypeCriteria parmCriteria = parmsInfo.get(parm).getCriteria();
					//loose input criteria
					HAPMatchers converter = parmDataType.discover(variablesInfo, parmCriteria, context, dataTypeHelper);
					if(converter!=null){
						this.m_parmsMatchers.put(parm, converter);
					}
				}
			}
			
			//discover base
			if(this.m_base!=null){
				this.m_baseMatchers = this.m_base.discover(variablesInfo, new HAPDataTypeCriteriaElementId(dataTypeId), context, dataTypeHelper);
			}
			
			HAPOperationOutInfo outputInfo = dataTypeOperation.getOperationInfo().getOutputInfo();
			if(outputInfo!=null){
				this.setOutputCriteria(outputInfo.getCriteria());
			}
			//check if output compatible with expect
			if(!dataTypeHelper.compatibleWith(this.getOutputCriteria(), expectCriteria)){
				context.addMessage("Error");
			}
			return this.isMatchable(outputInfo.getCriteria(), expectCriteria, context, dataTypeHelper);
		}
		else{
			//if we don't have operation data type 
			for(String parm: this.m_parms.keySet()){
				HAPOperand parmDataType = this.m_parms.get(parm);
				parmDataType.discover(variablesInfo, null, context, dataTypeHelper);
			}
			this.setOutputCriteria(null);
			return null;
		}
	}
}
