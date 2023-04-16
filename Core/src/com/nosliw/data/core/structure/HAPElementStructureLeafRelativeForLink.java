package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;

public class HAPElementStructureLeafRelativeForLink extends HAPElementStructureLeafRelative{

	@HAPAttribute
	public static final String LINK = "link";
	
	@HAPAttribute
	public static final String MATCHERS = "matchers";

	//context node full name --- matchers
	//used to convert data from parent to data within uiTag
	private HAPMatchers m_matchers;

	public HAPElementStructureLeafRelativeForLink() {
	}
	
	public HAPElementStructureLeafRelativeForLink(String path) {
		super(path);
	}
	
	@Override
	public String getType() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_LINK;	}

	public void setMatchers(HAPMatchers matchers){	this.m_matchers = matchers;	}

	public void toStructureElement(HAPElementStructureLeafRelativeForLink out) {
		super.toStructureElement(out);
		out.m_matchers = this.m_matchers.cloneMatchers();
	}
	
	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafRelativeForLink out = new HAPElementStructureLeafRelativeForLink();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public HAPElementStructure solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructureLeafRelativeForLink out = (HAPElementStructureLeafRelativeForLink)this.cloneStructureElement();
		this.solidateConstantScript(this, constants, runtimeEnv);
		out.getReference().setElementPath(HAPUtilityScriptExpression.solidateLiterate(this.getReference().getElementPath(), constants, runtimeEnv));
		out.getReference().setParentValueContextName(this.getReference().getParentValueContextName());
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(LINK, this.getReference().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(MATCHERS, HAPUtilityJson.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafRelativeForLink) {
			HAPElementStructureLeafRelativeForLink ele = (HAPElementStructureLeafRelativeForLink)obj;
			if(!HAPUtilityBasic.isEquals(this.getReference(), ele.getReference()))  return false;
			if(!HAPUtilityBasic.isEquals(ele.m_matchers, this.m_matchers)) 	return false;
			out = true;
		}
		return out;
	}	
}
