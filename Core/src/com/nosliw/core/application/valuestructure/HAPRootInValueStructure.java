package com.nosliw.core.application.valuestructure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPElementStructure;

public class HAPRootInValueStructure extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	@HAPAttribute
	public static final String DEFAULT = "defaultValue";

	//default value for the element, used in runtime when no value is set
	private Object m_defaultValue;

	//context element definition
	private HAPElementStructure m_definition;
	
	public HAPRootInValueStructure() {}
	
	public HAPRootInValueStructure(HAPElementStructure definition, HAPEntityInfo entityInfo) {
		this.m_definition = definition;
		entityInfo.cloneToEntityInfo(this);
	}

	public Object getDefaultValue(){	return this.m_defaultValue;  	}
	
	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}


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
		
		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPRootStructure) {
			HAPRootStructure root = (HAPRootStructure)obj;
			if(!super.equals(obj)) {
				return false;
			}
			out = true;
		}
		return out;
	}

}
