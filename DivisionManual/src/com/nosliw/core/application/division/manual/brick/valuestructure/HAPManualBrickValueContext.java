package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainerList;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

public class HAPManualBrickValueContext extends HAPManualDefinitionBrickBlockSimple implements HAPWithConstantScriptExpression{

	public static final String VALUESTRUCTURE = "valueStructure";
	
//	private List<HAPManualBrickWrapperValueStructure> m_valueStructures;
	
	public HAPManualBrickValueContext() {
		super(HAPManualEnumBrickType.VALUECONTEXT_100);
	}
	
	@Override
	protected void init() {
		this.setAttributeWithValueBrick(VALUESTRUCTURE, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINERLIST_100));
	}
	
	public List<HAPManualBrickWrapperValueStructure> getValueStructures(){
		List<HAPManualBrickWrapperValueStructure> out = new ArrayList<HAPManualBrickWrapperValueStructure>();
		for(HAPManualDefinitionAttributeInBrick attr: this.getValueStructureContainer().getPublicAttributes()) {
			out.add((HAPManualBrickWrapperValueStructure)((HAPManualDefinitionWrapperValueBrick)attr.getValueWrapper()).getBrick());
		}
		return out;
	}
	
	public void addValueStructure(HAPManualBrickWrapperValueStructure valueStructure) {    this.getValueStructureContainer().addElement(valueStructure);    }
	
	private HAPManualBrickContainerList getValueStructureContainer() {
		return (HAPManualBrickContainerList)this.getAttributeValueWithBrick(VALUESTRUCTURE);
	}
	
	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,	HAPParserDataExpression expressionParser) {
		for(HAPManualBrickWrapperValueStructure valueStructure : this.getValueStructures()) {
			valueStructure.discoverConstantScript(complexEntityId, parserContext, expressionParser);
		}
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

}
