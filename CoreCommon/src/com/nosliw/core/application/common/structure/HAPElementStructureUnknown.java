package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.structure22.HAPElementStructure;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPElementStructureUnknown extends HAPElementStructure{

	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,	HAPParserDataExpression expressionParser) {}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

	@Override
	public String getType() {  return HAPConstantShared.CONTEXT_ELEMENTTYPE_UNKNOW;  }

	@Override
	public HAPElementStructure cloneStructureElement() {  return new  HAPElementStructureUnknown();  }  

}
