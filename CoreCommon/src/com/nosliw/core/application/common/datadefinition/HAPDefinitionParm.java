package com.nosliw.core.application.common.datadefinition;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.xxx.application.common.interactive1.HAPRequestParmInInteractive;

public class HAPDefinitionParm extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATADEFINITION = "dataDefinition";
	
	private HAPDataDefinitionWritableWithInit m_dataDefinition;
	
	public static HAPDefinitionParm buildUndefinedVariableInfo() {
		return buildParm(null);
	}
	
	public static HAPDefinitionParm buildParmFromObject(Object def) {
		HAPDefinitionParm out = new HAPDefinitionParm();
		out.buildObject(def, null);
		return out;
	}

	public static HAPDefinitionParm buildParm(HAPDataTypeCriteria criteria) {
		return buildParm(null, criteria);
	}

	public static HAPDefinitionParm buildParm(String name, HAPDataTypeCriteria criteria) {
		HAPDefinitionParm out = new HAPDefinitionParm();
		if(criteria!=null) {
			out.m_dataDefinition.setCriteria(criteria);
		}
		else {
			
		}
		
		if(name!=null) {
			out.setName(name);
		}
		return out;
	}
	
	protected HAPDefinitionParm() {
		this.m_dataDefinition = new HAPDataDefinitionWritableWithInit();
	}
	
	public HAPDataDefinitionWritableWithInit getDataDefinition() {
		return this.m_dataDefinition;
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPRequestParmInInteractive){
			HAPRequestParmInInteractive varInfo = (HAPRequestParmInInteractive)obj;
			return super.equals(varInfo);
		}
		return out;
	}
	
	@Override
	public HAPDefinitionParm cloneVariableInfo() {
		HAPDefinitionParm out = new HAPDefinitionParm();
		this.cloneToVariableInfo(out);
		return out;
	}
	
}
