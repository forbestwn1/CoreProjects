package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.structure.HAPValueContextDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainerList;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPManualDefinitionBrickValueContext extends HAPManualDefinitionBrickBlockSimple implements HAPValueContextDefinition{

	
	public HAPManualDefinitionBrickValueContext() {
		super(HAPManualEnumBrickType.VALUECONTEXT_100);
	}
	
	@Override
	protected void init() {
		this.setAttributeWithValueBrick(VALUESTRUCTURE, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINERLIST_100));
	}
	
	@Override
	public List<HAPWrapperValueStructure> getValueStructures() {    return (List)getManualValueStructures();  }
	public List<HAPManualDefinitionBrickWrapperValueStructure> getManualValueStructures(){
		List<HAPManualDefinitionBrickWrapperValueStructure> out = new ArrayList<HAPManualDefinitionBrickWrapperValueStructure>();
		for(HAPManualDefinitionAttributeInBrick attr: this.getValueStructureContainer().getPublicAttributes()) {
			out.add((HAPManualDefinitionBrickWrapperValueStructure)((HAPManualDefinitionWrapperValueBrick)attr.getValueWrapper()).getBrick());
		}
		return out;
	}
	
	public void addValueStructure(HAPManualDefinitionBrickWrapperValueStructure valueStructure) {    this.getValueStructureContainer().addElement(valueStructure);    }
	
	private HAPManualDefinitionBrickContainerList getValueStructureContainer() {
		return (HAPManualDefinitionBrickContainerList)this.getAttributeValueWithBrick(VALUESTRUCTURE);
	}
	
	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,	HAPParserDataExpression expressionParser) {
		for(HAPManualDefinitionBrickWrapperValueStructure valueStructure : this.getValueStructures()) {
			valueStructure.discoverConstantScript(complexEntityId, parserContext, expressionParser);
		}
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

}
