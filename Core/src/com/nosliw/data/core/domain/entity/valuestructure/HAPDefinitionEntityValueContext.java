package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

public class HAPDefinitionEntityValueContext extends HAPDefinitionEntityInDomainSimple implements HAPWithConstantScriptExpression{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	public HAPDefinitionEntityValueContext() {
		this.setAttributeValueObject(VALUESTRUCTURE, new ArrayList<HAPDefinitionWrapperValueStructure>());
	}
	
	public List<HAPDefinitionWrapperValueStructure> getValueStructures(){   return (List<HAPDefinitionWrapperValueStructure>)this.getAttributeValue(VALUESTRUCTURE);  }
	public void addValueStructure(HAPDefinitionWrapperValueStructure valueStructure) {    this.getValueStructures().add(valueStructure);    }
	
	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,	HAPParserDataExpression expressionParser) {
		for(HAPDefinitionWrapperValueStructure valueStructure : this.getValueStructures()) {
			valueStructure.discoverConstantScript(complexEntityId, parserContext, expressionParser);
		}
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityValueContext out = new HAPDefinitionEntityValueContext();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
