package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;

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
	public String getType() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE;	}

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

	@Override
	public HAPElementStructure solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructureLeafRelativeForDefinition out = (HAPElementStructureLeafRelativeForDefinition)this.cloneStructureElement();
		this.solidateConstantScript(this, constants, runtimeEnv);
		out.getReference().setElementPath(HAPUtilityScriptExpression.solidateLiterate(this.getReference().getElementPath(), constants, runtimeEnv));
		out.getReference().setParentValueContextName(this.getReference().getParentValueContextName());
		return out;
	}
}
