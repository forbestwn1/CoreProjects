package com.nosliw.data.core.operand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPOperandImp  extends HAPSerializableImp implements HAPOperand{

	private String m_type;
	 
	private String m_status;
	
	private HAPDataTypeCriteria m_outputCriteria;
	
	protected HAPOperandImp(){}
	
	public HAPOperandImp(String type){
		this.m_type = type;
	}
	
	@Override
	public Set<HAPDataTypeConverter> getConverters(){	return new HashSet<HAPDataTypeConverter>();	}
	
	@Override
	public List<HAPResourceIdSimple> getResources(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceIdSimple> out = new ArrayList<HAPResourceIdSimple>();
		//converter as resource
		for(HAPDataTypeConverter converter : this.getConverters()){
			out.add(new HAPResourceIdConverter(converter));
		}
		return out;
	}
	
	@Override
	public String getType(){ return this.m_type;  }
	
	@Override
	public HAPDataTypeCriteria getOutputCriteria(){  return this.m_outputCriteria; }
	public void setOutputCriteria(HAPDataTypeCriteria dataTypeCriteria){  this.m_outputCriteria = dataTypeCriteria; }
	
	@Override
	public String getStatus(){		return this.m_status;	}
	public void setStatus(String status){  this.m_status = status; }
	public void setStatusInvalid(){  this.setStatus(HAPConstantShared.EXPRESSION_OPERAND_STATUS_INVALID); }
	
	@Override
	public List<HAPWrapperOperand> getChildren(){		return new ArrayList<HAPWrapperOperand>();	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(STATUS, this.getStatus());
		jsonMap.put(OUTPUTCRITERIA, HAPSerializeManager.getInstance().toStringValue(this.getOutputCriteria(), HAPSerializationFormat.LITERATE));
	}

	
	/**
	 * "And" two criteria and create output. If the "And" result is empty, then set error  
	 * @param criteria
	 * @param expectCriteria
	 * @param context
	 * @return
	 */
	protected HAPDataTypeCriteria validate(HAPDataTypeCriteria criteria, HAPDataTypeCriteria expectCriteria, HAPProcessTracker processTracker, HAPDataTypeHelper dataTypeHelper){
		HAPDataTypeCriteria out = null;
		if(criteria==HAPDataTypeCriteriaAny.getCriteria()){
			//if var is any (not defined)
			out = expectCriteria;
		}
		else{
			//if var is defined in context, use and between var in context and expect
			out = dataTypeHelper.and(criteria, expectCriteria);
			if(expectCriteria!=null && out==null){
				//error
				processTracker.setFailure("Error");
			}
		}
		return out;
	}
	
	protected void outputCompatible(HAPDataTypeCriteria targetCriteria, HAPProcessTracker processTracker, HAPDataTypeHelper dataTypeHelper){
		if(targetCriteria != null)
		{
			if(!targetCriteria.validate(this.getOutputCriteria(), dataTypeHelper))
			{
				this.setStatusInvalid();
				processTracker.setFailure("Error!!!!");
			}
		}
	}
	
	protected void cloneTo(HAPOperandImp operand){
		operand.m_type = this.m_type;
		operand.m_status = this.m_status;
		operand.m_outputCriteria = this.m_outputCriteria;
		
	}
	
	protected HAPWrapperOperand createOperandWrapper(HAPOperand operand){
		return new HAPWrapperOperand(operand);
	}
}
