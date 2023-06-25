package com.nosliw.data.core.operand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataTypeOperation;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.data.HAPOperationId;
import com.nosliw.data.core.data.HAPOperationOutInfo;
import com.nosliw.data.core.data.HAPOperationParmInfo;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceHelper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPOperandOperation extends HAPOperandImp{

	@HAPAttribute
	public static final String DATATYPEID = "dataTypeId";
	
	@HAPAttribute
	public static final String OPERATION = "operation";
	
	@HAPAttribute
	public static final String PARMS = "parms";

	@HAPAttribute
	public static final String BASE = "base";

	@HAPAttribute
	public static final String MATCHERSPARMS = "matchersParms";

	//the data type operation defined on
	protected HAPDataTypeId m_dataTypeId;
	
	//base data
	protected HAPWrapperOperand m_base;

	//operation name
	protected String m_operation;
	
	//operation parms
	protected Map<String, HAPWrapperOperand> m_parms = new LinkedHashMap<String, HAPWrapperOperand>();

	private Map<String, HAPMatchers> m_parmsMatchers;
	
	private HAPOperandOperation(){this.resetMatchers();}
	
	public HAPOperandOperation(HAPOperand base, String operation, List<HAPParmInOperationOperand> parms){
		super(HAPConstantShared.EXPRESSION_OPERAND_OPERATION);
		if(base!=null)		this.m_base = this.createOperandWrapper(base);
		this.m_operation = operation;

		for(HAPParmInOperationOperand opParm : parms)		this.m_parms.put(opParm.getName(), this.createOperandWrapper(opParm.getOperand()));

		this.resetMatchers();
	}
	
	public HAPOperandOperation(String dataTypeIdLiterate, String operation, List<HAPParmInOperationOperand> parms){
		super(HAPConstantShared.EXPRESSION_OPERAND_OPERATION);
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);
		this.m_operation = operation;
		
		for(HAPParmInOperationOperand opParm : parms){
			if(HAPUtilityBasic.isStringEmpty(opParm.getName()))			this.m_base = this.createOperandWrapper(opParm.getOperand());
			else			this.m_parms.put(opParm.getName(), this.createOperandWrapper(opParm.getOperand()));
		}
		
		this.resetMatchers();
	}

	public void setBase(HAPOperand base){
		this.m_base = null;
		if(base!=null){
			this.m_base = this.createOperandWrapper(base);
		}
	}
	
	public HAPWrapperOperand getBase(){  return this.m_base;  }
	
	public Map<String, HAPWrapperOperand> getParms(){   return this.m_parms;   }
	
	public HAPWrapperOperand addParm(String name, HAPOperand parmOperand){
		HAPWrapperOperand out = this.createOperandWrapper(parmOperand);
		this.m_parms.put(name, out);
		return out;
	}
	
	public void setParms(Map<String, HAPOperand> parms){
		for(String name : parms.keySet()){
			HAPWrapperOperand parmWrapper = this.addParm(name, parms.get(name));
		}
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
	public List<HAPWrapperOperand> getChildren(){
		List<HAPWrapperOperand> out = new ArrayList<HAPWrapperOperand>();
		if(this.m_base!=null)	out.add(this.m_base);
		out.addAll(this.m_parms.values());
		return out;
	}
	
	
	@Override
	public Set<HAPDataTypeConverter> getConverters(){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
		for(String parm : this.m_parmsMatchers.keySet()){
			out.addAll(HAPMatcherUtility.getConverterResourceIdFromRelationship(this.m_parmsMatchers.get(parm).discoverRelationships()));
		}
		return out;	
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERATION, this.m_operation);
		jsonMap.put(DATATYPEID, HAPSerializeManager.getInstance().toStringValue(this.m_dataTypeId, HAPSerializationFormat.LITERATE));
		if(this.m_base!=null)	jsonMap.put(BASE, HAPSerializeManager.getInstance().toStringValue(this.m_base, HAPSerializationFormat.JSON));

		jsonMap.put(PARMS, HAPUtilityJson.buildJson(this.m_parms, HAPSerializationFormat.JSON));
		jsonMap.put(MATCHERSPARMS, HAPUtilityJson.buildJson(this.m_parmsMatchers, HAPSerializationFormat.JSON));
	}

	private void resetMatchers(){
		this.m_parmsMatchers = new LinkedHashMap<String,HAPMatchers>();
	}
	
	@Override
	public HAPMatchers discover(
			HAPContainerVariableCriteriaInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker context,
			HAPDataTypeHelper dataTypeHelper) {
		//clear old matchers
		this.resetMatchers();
		
		//process base first
		if(this.m_base!=null) {
			HAPDataTypeCriteria baseCriteria = null;
			if(this.m_dataTypeId!=null)  baseCriteria = new HAPDataTypeCriteriaId(this.m_dataTypeId, null);
			this.m_base.getOperand().discover(variablesInfo, baseCriteria, context, dataTypeHelper);
		}
		
		//define seperate one, do not work on original one
		HAPDataTypeId dataTypeId = this.m_dataTypeId;
		
		//try to get operation data type according to base 
		if(dataTypeId==null && this.m_base!=null){
			//if data type is not determined, then use trunk data type of base data type if it has any
			HAPDataTypeCriteria baseDataTypeCriteria = this.m_base.getOperand().getOutputCriteria();
			if(baseDataTypeCriteria!=null)			dataTypeId = dataTypeHelper.getTrunkDataType(baseDataTypeCriteria);
		}

		if(dataTypeId !=null){
			//discover parms by operation definition
			HAPDataTypeOperation dataTypeOperation = dataTypeHelper.getOperationInfoByName(dataTypeId, m_operation);
			this.m_dataTypeId = dataTypeOperation.getTargetDataType().getTarget();
			
			List<HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
			for(HAPOperationParmInfo parmInfo : parmsInfo){
				HAPWrapperOperand parmOperandWrapper = this.m_parms.get(parmInfo.getName());
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
				HAPOperand parmDataType = this.m_parms.get(parm).getOperand();
				parmDataType.discover(variablesInfo, null, context, dataTypeHelper);
			}
			this.setOutputCriteria(null);
			return null;
		}
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandOperation out = new HAPOperandOperation();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandOperation operand){
		super.cloneTo(operand);
		operand.m_dataTypeId = this.m_dataTypeId;
		
		if(this.m_base!=null)		operand.m_base = this.m_base.cloneWrapper();

		operand.m_operation = this.m_operation;
		
		operand.m_parms = new LinkedHashMap<String, HAPWrapperOperand>();
		for(String name : this.m_parms.keySet()){
			operand.m_parms.put(name, this.m_parms.get(name).cloneWrapper());
		}
	}

	@Override
	public List<HAPResourceIdSimple> getResources(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceIdSimple> out = super.getResources(runtimeInfo, resourceManager);
		
		HAPOperationId operationId = this.getOperationId();
		//operation as resource
		if(operationId!=null)	out.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(operationId));
		
		return out;
	}
}
