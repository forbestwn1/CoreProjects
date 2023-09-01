package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

//a element refer to another element (on another tree or same tree)
//it can be use in define value structure by reference to another element
//or data association between two element
public class HAPElementStructureLeafRelativeForDefinition extends HAPElementStructureLeafRelative{

	@HAPAttribute
	public static final String REFERENCE = "reference";
	
	public HAPElementStructureLeafRelativeForDefinition() {
		super();
	}
	
	public HAPElementStructureLeafRelativeForDefinition(String reference) {
		super(reference);
	}
	
	@Override
	public String getType() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION;	}

	@Override
	public HAPElementStructure getSolidStructureElement() {	return this.getResolveInfo().getSolidElement();	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCE, this.getReference().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafRelativeForDefinition out = new HAPElementStructureLeafRelativeForDefinition();
		this.toStructureElement(out);
		return out;
	}

}
