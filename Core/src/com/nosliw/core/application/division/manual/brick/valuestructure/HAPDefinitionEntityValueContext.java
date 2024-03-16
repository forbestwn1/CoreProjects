package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualEntitySimple;
import com.nosliw.core.application.division.manual.HAPManualInfoAttributeValueEntity;
import com.nosliw.core.application.division.manual.brick.container.HAPDefinitionEntityContainerSimpleList;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

public class HAPDefinitionEntityValueContext extends HAPManualEntitySimple implements HAPWithConstantScriptExpression{

	public static final String VALUESTRUCTURE = "valueStructure";
	
//	private List<HAPDefinitionEntityWrapperValueStructure> m_valueStructures;
	
	public HAPDefinitionEntityValueContext() {
		super(HAPEnumBrickType.VALUECONTEXT_100);
		this.setAttributeEntity(VALUESTRUCTURE, new HAPDefinitionEntityContainerSimpleList<HAPDefinitionEntityWrapperValueStructure>(HAPEnumBrickType.VALUESTRUCTURE_100));
	}
	
	public List<HAPDefinitionEntityWrapperValueStructure> getValueStructures(){
		List<HAPDefinitionEntityWrapperValueStructure> out = new ArrayList<HAPDefinitionEntityWrapperValueStructure>();
		for(HAPManualAttribute attr: this.getValueStructureContainer().getPublicAttributes()) {
			out.add((HAPDefinitionEntityWrapperValueStructure)((HAPManualInfoAttributeValueEntity)attr.getValueInfo()).getEntity());
		}
		return out;
	}
	
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
