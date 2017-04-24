package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public abstract class HAPOperandImp  extends HAPSerializableImp implements HAPOperand{

	private String m_type;
	
	private List<HAPOperand> m_children;
	
	private String m_status;
	
	private HAPDataTypeCriteria m_outputCriteria;
	
	private Set<HAPRelationship> m_convertors;
	
	public HAPOperandImp(String type){
		this.m_type = type;
		this.m_children = new ArrayList<HAPOperand>();
		this.m_convertors = new HashSet<HAPRelationship>();
	}
	
	@Override
	public Set<HAPRelationship> getConverters(){	return this.m_convertors;	}
	protected void addConvertor(HAPRelationship convertor){  this.m_convertors.add(convertor);  }
	protected void addConvertors(Collection<HAPRelationship> convertors){  this.m_convertors.addAll(convertors);  }
	
	@Override
	public String getType(){ return this.m_type;  }
	
	@Override
	public HAPDataTypeCriteria getDataTypeCriteria(){  return this.m_outputCriteria; }
	protected void setDataTypeCriteria(HAPDataTypeCriteria dataTypeCriteria){  this.m_outputCriteria = dataTypeCriteria; }
	
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
	protected HAPDataTypeCriteria validate(HAPDataTypeCriteria criteria, HAPDataTypeCriteria expectCriteria, HAPProcessVariablesContext context, HAPDataTypeHelper dataTypeHelper){
		HAPDataTypeCriteria out = null;
		if(criteria==null){
			//if var is not defined in context, use expect one
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
			if(!targetCriteria.validate(this.getDataTypeCriteria(), dataTypeHelper))
			{
				this.setStatusInvalid();
				context.setFailure("Error!!!!");
			}
		}
	}
}
