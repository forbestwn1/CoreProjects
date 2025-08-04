package com.nosliw.core.application.division.manual.common.task;

import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.interactive.interfacee.task.HAPDefinitionInteractive;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPManualBrickInteractive extends HAPManualDefinitionBrick{

	public static final String ATTR_INTERACTIVE = "interactive";

	public HAPManualBrickInteractive() {
	}
	
	public HAPDefinitionInteractive getInteractive() {  return (HAPDefinitionInteractive)this.getAttributeValueOfValue(ATTR_INTERACTIVE);  }

	public void setInteractive(HAPDefinitionInteractive interactive) {    this.setAttributeObject(ATTR_INTERACTIVE, new HAPEmbededDefinition(interactive));      }
	
	@Override
	public HAPManualDefinitionBrick cloneEntityDefinitionInDomain() {
		HAPManualBrickInteractive out = new HAPManualBrickInteractive();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
