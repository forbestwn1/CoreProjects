package com.nosliw.core.application.uitag;

import java.util.List;
import java.util.Map;

import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.structure.HAPValueContextDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPUITagValueContextDefinition implements HAPValueContextDefinition{

	private List<HAPWrapperValueStructure> m_valueStructures;
	
	@Override
	public List<HAPWrapperValueStructure> getValueStructures() {   return this.m_valueStructures;  }

	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,
			HAPParserDataExpression expressionParser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {
		// TODO Auto-generated method stub
		
	}

}
