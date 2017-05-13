package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * This is variable info for expression 
 */
public class HAPVariableInfo {

	//use stack to store all the change applied for criteria
	private List<HAPDataTypeCriteria> m_criteriaStack = new ArrayList<HAPDataTypeCriteria>();
	
	//status of variable, now there are two status
	//open: the criteria is open to change
	//close : the criteria is close to change
	private String m_status;

	public HAPVariableInfo(){
		this(null);
	}	
	
	public HAPVariableInfo(HAPDataTypeCriteria criteria, String status){
		this.m_status = status;
		this.m_criteriaStack.add(criteria);
	}

	public HAPVariableInfo(HAPDataTypeCriteria criteria){
		this.m_criteriaStack.add(criteria);
		if(criteria==null)   this.m_status = HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN;
		else   this.m_status = HAPConstant.EXPRESSION_VARIABLE_STATUS_CLOSE;
	}
	
	public String getStatus(){		return this.m_status;	}
	
	public HAPDataTypeCriteria getCriteria(){
		return m_criteriaStack.get(this.m_criteriaStack.size()-1);
	}
	
	public void setCriteria(HAPDataTypeCriteria criteria){
		this.m_criteriaStack.add(criteria);
	}
	
}
