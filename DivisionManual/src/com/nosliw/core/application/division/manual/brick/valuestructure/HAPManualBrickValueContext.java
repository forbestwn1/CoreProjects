package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualWrapperValueBrick;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainerList;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data1.HAPParserDataExpression;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

public class HAPManualBrickValueContext extends HAPManualBrickBlockSimple implements HAPWithConstantScriptExpression{

	public static final String VALUESTRUCTURE = "valueStructure";
	
//	private List<HAPManualBrickWrapperValueStructure> m_valueStructures;
	
	public HAPManualBrickValueContext() {
		super(HAPManualEnumBrickType.VALUECONTEXT_100);
		this.setAttributeWithValueBrick(VALUESTRUCTURE, new HAPManualBrickContainerList(HAPManualEnumBrickType.VALUESTRUCTURE_100));
	}
	
	public List<HAPManualBrickWrapperValueStructure> getValueStructures(){
		List<HAPManualBrickWrapperValueStructure> out = new ArrayList<HAPManualBrickWrapperValueStructure>();
		for(HAPManualAttribute attr: this.getValueStructureContainer().getPublicAttributes()) {
			out.add((HAPManualBrickWrapperValueStructure)((HAPManualWrapperValueBrick)attr.getValueWrapper()).getBrick());
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
