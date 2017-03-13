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
	
	protected String outputCompatible(HAPDataTypeCriteria targetCriteria){
		if(targetCriteria == null)   return null;
		else{
			if(targetCriteria.validate(this.getDataTypeCriteria()))
			{
				return null;
			}
			else{
				this.setStatusInvalid();
				return "error";
			}
		}
	}
}
