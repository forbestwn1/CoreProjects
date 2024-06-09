package com.nosliw.core.application.common.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data1.HAPParserDataExpression;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPElementStructureLeafProvide extends HAPElementStructureLeafVariable{

	public static final String PROVIDE = "provide";

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String DEFINITION = "definition";

	@HAPAttribute
	public static final String MATCHERS = "matchers";

	@HAPAttribute
	public static final String REVERSEMATCHERS = "reverseMatchers";

	private String m_name;
	
	private HAPElementStructure m_definition;

	//context node full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;

	private Map<String, HAPMatchers> m_reverseMatchers;

	public HAPElementStructureLeafProvide() {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_reverseMatchers = new LinkedHashMap<String, HAPMatchers>();
	}

	public String getName() {   return this.m_name;    }
	public void setName(String name) {    this.m_name = name;     }
	
	public HAPElementStructure getDefinition() {    return this.m_definition;     }
	public void setDefinition(HAPElementStructure definition) {     this.m_definition = definition;       }
	
	public void setMatchers(Map<String, HAPMatchers> matchers){
		this.m_matchers.clear();
		this.m_matchers.putAll(matchers);
		this.m_reverseMatchers.clear();
		for(String name : matchers.keySet()) {
			this.m_reverseMatchers.put(name, HAPMatcherUtility.reversMatchers(matchers.get(name)));
		}
	}
	
	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,
			HAPParserDataExpression expressionParser) {   
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_PROVIDE;	}

	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafProvide out = new HAPElementStructureLeafProvide();
		out.m_name = this.m_name;
		if(this.m_definition!=null)  out.m_definition = this.m_definition.cloneStructureElement();
		return out;
	}

}
