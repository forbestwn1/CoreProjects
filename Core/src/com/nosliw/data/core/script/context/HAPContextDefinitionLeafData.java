package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPContextDefinitionLeafData extends HAPContextDefinitionLeafVariable{

	@HAPAttribute
	public static final String CRITERIA  = "criteria";

	//context definition of that node (criteria)
	private HAPVariableInfo m_criteria;
	
	public HAPContextDefinitionLeafData(){
	}
	
	@Override
	public String getType() {	return HAPConstant.CONTEXT_ELEMENTTYPE_DATA;	}

	public void setCriteria(HAPVariableInfo criteria){	this.m_criteria = criteria;	}
	
	public HAPVariableInfo getCriteria(){   return this.m_criteria;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_criteria!=null)  	jsonMap.put(CRITERIA, this.m_criteria.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public HAPContextDefinitionElement cloneContextDefinitionElement() {
		HAPContextDefinitionLeafData out = new HAPContextDefinitionLeafData();
		this.toContextDefinitionElement(out);
		return out;
	}

	@Override
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		super.toContextDefinitionElement(out);
		((HAPContextDefinitionLeafData)out).m_criteria = this.m_criteria.cloneVariableInfo();
	}

	@Override
	public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants,
			HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextDefinitionElement out = this.cloneContextDefinitionElement();
		return out;
	}
}
