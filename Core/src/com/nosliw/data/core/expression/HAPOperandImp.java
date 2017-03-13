package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public abstract class HAPOperandImp  extends HAPSerializableImp implements HAPOperand{

	private String m_type;
	
	private List<HAPOperand> m_children;
	
	private String m_status;
	
	private HAPDataTypeCriteriaManager m_criteriaMan;
	
	public HAPOperandImp(String type, HAPDataTypeCriteriaManager criteriaMan){
		this.m_type = type;
		this.m_children = new ArrayList<HAPOperand>();
		this.m_criteriaMan = criteriaMan;
	}
	
	protected HAPDataTypeCriteriaManager getDataTypeCriteriaManager(){  return this.m_criteriaMan;  }
	
	@Override
	public String getType(){ return this.m_type;  }
	
	@Override
	public String getStatus(){		return this.m_status;	}
	public void setStatus(String status){  this.m_status = status; }
	public void setStatusInvalid(){  this.setStatus(HAPConstant.EXPRESSION_OPERAND_STATUS_INVALID); }
	
	@Override
	public List<HAPOperand> getChildren(){		return this.m_children;	}
	
	protected void addChildOperand(HAPOperand child){  this.m_children.add(child); }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}
	
	protected HAPDataTypeCriteria validate(HAPDataTypeCriteria criteria, HAPDataTypeCriteria expectCriteria, HAPProcessVariablesContext context){
		HAPDataTypeCriteria out = null;
		if(criteria==null){
			//if var is not defined in context, use expect one
			out = expectCriteria;
		}
		else{
			//if var is defined in context, use and between var in context and expect
			out = this.getDataTypeCriteriaManager().and(criteria, expectCriteria);
			if(expectCriteria!=null && out==null){
				//error
				context.setFailure("Error");
			}
		}
		return out;
	}
	
	protected void outputCompatible(HAPDataTypeCriteria targetCriteria, HAPProcessVariablesContext context){
		if(targetCriteria != null)
		{
			if(!targetCriteria.validate(this.getDataTypeCriteria()))
			{
				this.setStatusInvalid();
				context.setFailure("Error!!!!");
			}
		}
	}
}
