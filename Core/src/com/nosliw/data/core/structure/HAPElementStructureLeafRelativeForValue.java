package com.nosliw.data.core.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression1.HAPUtilityScriptExpression;

//a element refer to another element (on another tree or same tree)
//it can be use in define value structure by reference to another element
//or data association between two element
public class HAPElementStructureLeafRelativeForValue extends HAPElementStructureLeafRelative{

	@HAPAttribute
	public static final String LINK = "link";
	
	@HAPAttribute
	public static final String DEFINITION = "definition";

	@HAPAttribute
	public static final String MATCHERS = "matchers";

	@HAPAttribute
	public static final String REVERSEMATCHERS = "reverseMatchers";

	private HAPElementStructure m_definition;
	
	//context node full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;

	private Map<String, HAPMatchers> m_reverseMatchers;
	
	public HAPElementStructureLeafRelativeForValue() {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_reverseMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	public HAPElementStructureLeafRelativeForValue(String path) {
		super(path);
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_reverseMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	@Override
	public String getType() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE;	}

	@Override
	public HAPElementStructure getSolidStructureElement() {	return this.m_definition;	}

	public HAPElementStructure getDefinition() {   return this.m_definition;   }
	public void setDefinition(HAPElementStructure definition) {   this.m_definition = definition.getSolidStructureElement();   }
	
	public void setMatchers(Map<String, HAPMatchers> matchers){
		this.m_matchers.clear();
		this.m_matchers.putAll(matchers);
		this.m_reverseMatchers.clear();
		for(String name : matchers.keySet()) {
			this.m_reverseMatchers.put(name, HAPMatcherUtility.reversMatchers(matchers.get(name)));
		}
	}

	@Override
	public HAPElementStructure getChild(String childName) {
		if(this.m_definition!=null) 		return this.m_definition.getChild(childName);
		return null;   
	}
	
	@Override
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
		HAPElementStructureLeafRelativeForValue that = (HAPElementStructureLeafRelativeForValue)out;
		if(this.m_definition!=null)  that.m_definition = this.m_definition.cloneStructureElement();
		
		for(String name : this.m_matchers.keySet()) 	that.m_matchers.put(name, this.m_matchers.get(name).cloneMatchers());
		for(String name : this.m_reverseMatchers.keySet())   that.m_reverseMatchers.put(name, this.m_reverseMatchers.get(name).cloneMatchers());
	}
	
	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafRelativeForValue out = new HAPElementStructureLeafRelativeForValue();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public HAPElementStructure solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructureLeafRelativeForValue out = (HAPElementStructureLeafRelativeForValue)this.cloneStructureElement();
		this.solidateConstantScript(this, constants, runtimeEnv);
		out.getReference().setPath(HAPUtilityScriptExpression.solidateLiterate(this.getReference().getPath(), constants, runtimeEnv));
		out.getReference().setParentValueContextName(this.getReference().getParentValueContextName());
		if(this.m_definition!=null) 	out.m_definition = (HAPElementStructure)this.m_definition.solidateConstantScript(constants, runtimeEnv);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(LINK, this.getReference().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(DEFINITION, HAPUtilityJson.buildJson(this.m_definition, HAPSerializationFormat.JSON));
		if(this.m_matchers!=null && !this.m_matchers.isEmpty()){
			jsonMap.put(MATCHERS, HAPUtilityJson.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
			jsonMap.put(REVERSEMATCHERS, HAPUtilityJson.buildJson(this.m_reverseMatchers, HAPSerializationFormat.JSON));
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafRelativeForValue) {
			HAPElementStructureLeafRelativeForValue ele = (HAPElementStructureLeafRelativeForValue)obj;
			if(!HAPUtilityBasic.isEquals(this.getReference(), ele.getReference()))  return false;
			if(!HAPUtilityBasic.isEqualMaps(ele.m_matchers, this.m_matchers)) 	return false;
			if(!HAPUtilityBasic.isEqualMaps(ele.m_reverseMatchers, this.m_matchers))  return false;
			if(!ele.m_definition.equals(this.m_definition))  return false;
			out = true;
		}
		return out;
	}	
}
