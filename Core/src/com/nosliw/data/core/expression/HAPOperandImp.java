package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public abstract class HAPOperandImp  extends HAPSerializableImp implements HAPOperand{

	private List<HAPOperand> m_children;
	
	private HAPDataTypeCriteriaManager m_criteriaMan;
	
	public HAPOperandImp(HAPDataTypeCriteriaManager criteriaMan){
		this.m_children = new ArrayList<HAPOperand>();
		this.m_criteriaMan = criteriaMan;
	}
	
	protected HAPDataTypeCriteriaManager getDataTypeCriteriaManager(){  return this.m_criteriaMan;  }
	
	@Override
	public List<HAPOperand> getChildren(){		return this.m_children;	}
	
	protected void addChildOperand(HAPOperand child){  this.m_children.add(child); }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}
}
