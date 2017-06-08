package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaAny;

public abstract class HAPOperandImp  extends HAPSerializableImp implements HAPOperand{

	private String m_type;
	
	private List<HAPOperand> m_children;
	
	private String m_status;
	
	private HAPDataTypeCriteria m_outputCriteria;
	
	public HAPOperandImp(String type){
		this.m_type = type;
		this.m_children = new ArrayList<HAPOperand>();
	}
	
	@Override
	public Set<HAPRelationship> getConverters(){	return null;	}
	
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
	public List<HAPOperand> getChildren(){		return this.m_children;	}
	
	protected void addChildOperand(HAPOperand child){  this.m_children.add(child); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(STATUS, this.getStatus());
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	/**
	 * "And" two criteria and create output. If the "And" result is empty, then set error  
	 * @param criteria
	 * @param expectCriteria
	 * @param context
	 * @return
	 */
	protected HAPMatchers isMatchable(HAPDataTypeCriteria criteria, HAPDataTypeCriteria expectCriteria, HAPProcessVariablesContext context, HAPDataTypeHelper dataTypeHelper){
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
	protected HAPDataTypeCriteria validate(HAPDataTypeCriteria criteria, HAPDataTypeCriteria expectCriteria, HAPProcessVariablesContext context, HAPDataTypeHelper dataTypeHelper){
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
	
	protected void outputCompatible(HAPDataTypeCriteria targetCriteria, HAPProcessVariablesContext context, HAPDataTypeHelper dataTypeHelper){
		if(targetCriteria != null)
		{
			if(!targetCriteria.validate(this.getOutputCriteria(), dataTypeHelper))
			{
				this.setStatusInvalid();
				context.setFailure("Error!!!!");
			}
		}
	}
}
