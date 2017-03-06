package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPOperandImp  extends HAPSerializableImp implements HAPOperand{

	private List<HAPOperand> m_children;
	
	public HAPOperandImp(){
		this.m_children = new ArrayList<HAPOperand>();
	}
	
	@Override
	public List<HAPOperand> getChildren(){		return this.m_children;	}
	
	protected void addChildOperand(HAPOperand child){  this.m_children.add(child); }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}
}
