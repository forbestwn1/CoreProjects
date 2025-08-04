package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.common.structure.HAPValueContextDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainerList;
import com.nosliw.core.application.division.manual.core.a.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;

public class HAPManualDefinitionBrickValueContext extends HAPManualDefinitionBrick implements HAPValueContextDefinition{

	
	public HAPManualDefinitionBrickValueContext() {
		super(HAPManualEnumBrickType.VALUECONTEXT_100);
	}
	
	@Override
	protected void init() {
		this.setAttributeValueWithBrick(VALUESTRUCTURE, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINERLIST_100));
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
	
	public void addValueStructure(HAPManualDefinitionBrickWrapperValueStructure valueStructure) {    this.getValueStructureContainer().addElementWithBrickOrReference(valueStructure);    }
	
	private HAPManualDefinitionBrickContainerList getValueStructureContainer() {
		return (HAPManualDefinitionBrickContainerList)this.getAttributeValueOfBrick(VALUESTRUCTURE);
	}
	
}
