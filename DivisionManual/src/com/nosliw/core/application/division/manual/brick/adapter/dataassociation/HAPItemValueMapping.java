package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.core.application.common.structure.HAPElementStructure;

@HAPEntityWithAttribute
public class HAPItemValueMapping<T> extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String DEFINITION = "definition";

	@HAPAttribute
	public static final String TARGET = "target";

	//context element definition
	private HAPElementStructure m_definition;

	private T m_target;
	
	public HAPItemValueMapping() {}
	
	public HAPItemValueMapping(HAPElementStructure definition, T target) {
		this.m_definition = definition;
		this.m_target = target;
	}
	
	public T getTarget() {   return this.m_target;   }
	
	public void setTarget(T target) {    this.m_target = target;    }
	
	public HAPElementStructure getDefinition() {	return this.m_definition;	}
	
	public void setDefinition(HAPElementStructure definition) {   this.m_definition = definition;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TARGET, HAPSerializeManager.getInstance().toStringValue(this.m_target, HAPSerializationFormat.JSON));
	}
	
	public HAPItemValueMapping<T> cloneValueMappingItem() {
		HAPItemValueMapping<T> out = new HAPItemValueMapping<T>();
		this.cloneToEntityInfo(out);
		if(this.m_definition!=null)  out.m_definition = this.m_definition.cloneStructureElement();
		if(this.m_target!=null)  out.m_target = this.m_target;
		return out;
	}
}
