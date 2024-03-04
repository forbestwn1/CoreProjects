package com.nosliw.data.core.entity.division.manual.valuestructure;

import java.util.List;
import java.util.Map;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.division.manual.HAPManualEntitySimple;
import com.nosliw.data.core.entity.division.manual.container.HAPDefinitionEntityContainerSimpleList;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

public class HAPDefinitionEntityValueContext extends HAPManualEntitySimple implements HAPWithConstantScriptExpression{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	private List<HAPDefinitionEntityWrapperValueStructure> m_valueStructures;
	
	public HAPDefinitionEntityValueContext() {
		super(HAPEnumEntityType.VALUECONTEXT_100);
		this.setAttributeEntity(VALUESTRUCTURE, new HAPDefinitionEntityContainerSimpleList<HAPDefinitionEntityWrapperValueStructure>(HAPEnumEntityType.VALUESTRUCTURE_100));
	}
	
	public List<HAPDefinitionEntityWrapperValueStructure> getValueStructures(){   return this.m_valueStructures;  }
	public void addValueStructure(HAPDefinitionEntityWrapperValueStructure valueStructure) {    this.getValueStructureContainer().addElement(valueStructure);    }
	
	private HAPDefinitionEntityContainerSimpleList<HAPDefinitionEntityWrapperValueStructure> getValueStructureContainer() {
		return (HAPDefinitionEntityContainerSimpleList<HAPDefinitionEntityWrapperValueStructure>)this.getAttributeEntity(VALUESTRUCTURE);
	}
	
	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,	HAPParserDataExpression expressionParser) {
		for(HAPDefinitionEntityWrapperValueStructure valueStructure : this.getValueStructures()) {
			valueStructure.discoverConstantScript(complexEntityId, parserContext, expressionParser);
		}
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

}
