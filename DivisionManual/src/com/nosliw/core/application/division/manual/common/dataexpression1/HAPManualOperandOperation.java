package com.nosliw.core.application.division.manual.common.dataexpression1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandOperation;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandOperation;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPDataTypeOperation;
import com.nosliw.core.data.HAPDataWrapper;
import com.nosliw.core.data.HAPOperationOutInfo;
import com.nosliw.core.data.HAPOperationParmInfo;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPManualOperandOperation extends HAPManualOperand implements HAPOperandOperation{

	private HAPDataTypeId m_dataTypeId;
	
	private String m_operation; 
	
	private HAPManualWrapperOperand m_base;
	
	private Map<String, HAPManualWrapperOperand> m_parms;
	
	private Map<String, HAPMatchers> m_parmsMatchers;
	
	public HAPManualOperandOperation(HAPDefinitionOperandOperation operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_OPERATION, operandDefinition);
		this.m_parms = new LinkedHashMap<String, HAPManualWrapperOperand>(); 
		this.m_parmsMatchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_dataTypeId = operandDefinition.getDataTypeId();
		this.m_operation = operandDefinition.getOperaion();
	}

	@Override
	public HAPOperand getBase() {   return this.m_base.getOperand();  }
	public void setBase(HAPManualOperand base) {   this.m_base = new HAPManualWrapperOperand(base);      }

	@Override
	public Map<String, HAPOperand> getParms() {
		Map<String, HAPOperand> out = new LinkedHashMap<String, HAPOperand>();
		for(String name : this.m_parms.keySet()) {
			out.put(name, this.m_parms.get(name).getOperand());
		}
		return out;
	}
	public void setParm(String name, HAPManualOperand parm) {
		this.m_parms.put(name, new HAPManualWrapperOperand(parm));
	}

	@Override
	public HAPDataTypeId getDataTypeId() {   return this.m_dataTypeId;   }

	@Override
	public String getOperaion() {   return this.m_operation;   }

	@Override
	public Map<String, HAPMatchers> getParmMatchers() {   return this.m_parmsMatchers;   }

	@Override
	public List<HAPManualWrapperOperand> getChildren(){   
		List<HAPManualWrapperOperand> out = new ArrayList<HAPManualWrapperOperand>();
		if(this.m_base!=null) {
			out.add(this.m_base);
		}
		for(String name : this.m_parms.keySet()) {
			out.add(this.m_parms.get(name));
		}
		return out;
	}
	
	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker context,
			HAPDataTypeHelper dataTypeHelper) {
		//clear old matchers
		this.resetMatchers();
		
		//process base first
		if(this.m_base!=null) {
			HAPDataTypeCriteria baseCriteria = null;
			if(this.m_dataTypeId!=null) {
				baseCriteria = new HAPDataTypeCriteriaId(this.m_dataTypeId, null);
			}
			this.m_base.getOperand().discover(variablesInfo, baseCriteria, context, dataTypeHelper);
		}
		
		//define seperate one, do not work on original one
		HAPDataTypeId dataTypeId = this.m_dataTypeId;
		
		//try to get operation data type according to base 
		if(dataTypeId==null && this.m_base!=null){
			//if data type is not determined, then use trunk data type of base data type if it has any
			HAPDataTypeCriteria baseDataTypeCriteria = this.m_base.getOperand().getOutputCriteria();
			if(baseDataTypeCriteria!=null) {
				dataTypeId = dataTypeHelper.getTrunkDataType(baseDataTypeCriteria);
			}
		}

		if(dataTypeId !=null){
			//discover parms by operation definition
			HAPDataTypeOperation dataTypeOperation = dataTypeHelper.getOperationInfoByName(dataTypeId, m_operation);
			this.m_dataTypeId = dataTypeOperation.getTargetDataType().getTarget();
			
			List<HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
			for(HAPOperationParmInfo parmInfo : parmsInfo){
				HAPManualWrapperOperand parmOperandWrapper = this.m_parms.get(parmInfo.getName());
				if(parmOperandWrapper==null && this.m_base!=null && parmInfo.getIsBase()){
					//if parm does not exist, then try to use base
					parmOperandWrapper = this.createOperandWrapper(this.m_base.getOperand());
					this.m_parms.put(parmInfo.getName(), parmOperandWrapper);
					this.setBase(null);
				}
				
				HAPMatchers matchers = parmOperandWrapper.getOperand().discover(variablesInfo, parmInfo.getCriteria(), context, dataTypeHelper);
				if(matchers!=null){
					this.m_parmsMatchers.put(parmInfo.getName(), matchers);
				}
			}
			
			HAPOperationOutInfo outputInfo = dataTypeOperation.getOperationInfo().getOutputInfo();
			if(outputInfo!=null){
				//for criteria containing expression, execute expression here, using parmName : parmOperand for each parm for expression
				Map<String, HAPData> expressionParms = new LinkedHashMap<String, HAPData>();
				for(HAPOperationParmInfo parmInfo : parmsInfo){
					String parmName = parmInfo.getName();
					HAPOperand parmOperand = this.m_parms.get(parmName).getOperand();
					HAPData expressionParmData = new HAPDataWrapper(new HAPDataTypeId("test.parm"), parmOperand); 
					expressionParms.put(parmName, expressionParmData);
				}
				
				dataTypeHelper.processExpressionCriteria(outputInfo.getCriteria(), expressionParms);
				this.setOutputCriteria(outputInfo.getCriteria());
			}
			//check if output compatible with expect
			if(dataTypeHelper.convertable(this.getOutputCriteria(), expectCriteria)==null){
				context.addMessage("Error");
			}
			return HAPUtilityCriteria.isMatchable(outputInfo.getCriteria(), expectCriteria, dataTypeHelper);
		}
		else{
			//if we don't have operation data type 
			for(String parm: this.m_parms.keySet()){
				HAPManualOperand parmDataType = this.m_parms.get(parm).getOperand();
				parmDataType.discover(variablesInfo, null, context, dataTypeHelper);
			}
			this.setOutputCriteria(null);
			return null;
		}
	}

	private void resetMatchers(){
		this.m_parmsMatchers = new LinkedHashMap<String,HAPMatchers>();
	}

	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERATION, this.m_operation);
		jsonMap.put(DATATYPEID, HAPManagerSerialize.getInstance().toStringValue(this.m_dataTypeId, HAPSerializationFormat.LITERATE));
		if(this.m_base!=null) {
			jsonMap.put(BASE, HAPManagerSerialize.getInstance().toStringValue(this.getBase(), HAPSerializationFormat.JAVASCRIPT));
		}

		jsonMap.put(PARMS, HAPUtilityJson.buildJson(this.getParms(), HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(MATCHERSPARMS, HAPUtilityJson.buildJson(this.getParmMatchers(), HAPSerializationFormat.JAVASCRIPT));
	}
}
