package com.nosliw.data.core.operand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;

public abstract class HAPOperandImp  extends HAPSerializableImp implements HAPOperand{

	private String m_type;
	
	private List<HAPOperandWrapper> m_children;
	
	private String m_status;
	
	private HAPDataTypeCriteria m_outputCriteria;
	
	protected HAPOperandImp(){}
	
	public HAPOperandImp(String type){
		this.m_type = type;
		this.m_children = new ArrayList<HAPOperandWrapper>();
	}
	
	@Override
	public Set<HAPDataTypeConverter> getConverters(){	return new HashSet<HAPDataTypeConverter>();	}
	
	@Override
	public List<HAPResourceId> getResources() {
		List<HAPResourceId> out = new ArrayList<HAPResourceId>();
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
	protected void setOutputCriteria(HAPDataTypeCriteria dataTypeCriteria){  this.m_outputCriteria = dataTypeCriteria; }
	
	@Override
	public String getStatus(){		return this.m_status;	}
	public void setStatus(String status){  this.m_status = status; }
	public void setStatusInvalid(){  this.setStatus(HAPConstant.EXPRESSION_OPERAND_STATUS_INVALID); }
	
	@Override
	public List<HAPOperandWrapper> getChildren(){		return this.m_children;	}
	
	protected void addChildOperand(HAPOperandWrapper child){  this.m_children.add(child); }
	
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
	protected HAPMatchers isMatchable(HAPDataTypeCriteria criteria, HAPDataTypeCriteria expectCriteria, HAPProcessExpressionDefinitionContext context, HAPDataTypeHelper dataTypeHelper){
		if(expectCriteria==null)   return null;
		
		HAPMatchers out = dataTypeHelper.buildMatchers(criteria, expectCriteria);
		if(out==null){
			//not able to match, then error
			context.setFailure("Error");
		}
		return out;
	}
	
	
	/**
	 * "And" two criteria and create output. If the "And" result is empty, then set error  
	 * @param criteria
	 * @param expectCriteria
	 * @param context
	 * @return
	 */
	protected HAPDataTypeCriteria validate(HAPDataTypeCriteria criteria, HAPDataTypeCriteria expectCriteria, HAPProcessExpressionDefinitionContext context, HAPDataTypeHelper dataTypeHelper){
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
				context.setFailure("Error");
			}
		}
		return out;
	}
	
	protected void outputCompatible(HAPDataTypeCriteria targetCriteria, HAPProcessExpressionDefinitionContext context, HAPDataTypeHelper dataTypeHelper){
		if(targetCriteria != null)
		{
			if(!targetCriteria.validate(this.getOutputCriteria(), dataTypeHelper))
			{
				this.setStatusInvalid();
				context.setFailure("Error!!!!");
			}
		}
	}
	
	protected void cloneTo(HAPOperandImp operand){
		
	}
	
	protected HAPOperandWrapper createOperandWrapper(HAPOperand operand){
		return new HAPOperandWrapper(operand);
	}
}
