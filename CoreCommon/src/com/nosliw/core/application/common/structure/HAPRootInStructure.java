package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPRootInStructure extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	//context element definition
	private HAPElementStructure m_definition;
	
	public HAPRootInStructure() {}
	
	public HAPRootInStructure(HAPElementStructure definition, HAPEntityInfo entityInfo) {
		this.m_definition = definition;
		if(entityInfo!=null) {
			entityInfo.cloneToEntityInfo(this);
		}
	}

	public HAPElementStructure getDefinition() {   return this.m_definition;   }

	public void setDefinition(HAPElementStructure definition) {   this.m_definition = definition;  }

	public HAPRootStructure cloneRootBase() {
		HAPRootStructure out = new HAPRootStructure();
		this.cloneToEntityInfo(out);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPRootInStructure) {
			HAPRootInStructure root = (HAPRootInStructure)obj;
			if(!super.equals(obj)) {
				return false;
			}
			out = true;
		}
		return out;
	}

}
