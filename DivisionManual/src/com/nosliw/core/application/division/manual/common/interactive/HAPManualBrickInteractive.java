package com.nosliw.core.application.division.manual.common.interactive;

import com.nosliw.core.application.brick.interactive.interfacee.HAPDefinitionInteractive;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPManualBrickInteractive extends HAPManualBrick{

	public static final String ATTR_INTERACTIVE = "interactive";

	public HAPManualBrickInteractive() {
	}
	
	public HAPDefinitionInteractive getInteractive() {  return (HAPDefinitionInteractive)this.getAttributeValue(ATTR_INTERACTIVE);  }

	public void setInteractive(HAPDefinitionInteractive interactive) {    this.setAttributeObject(ATTR_INTERACTIVE, new HAPEmbededDefinition(interactive));      }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPManualBrickInteractive out = new HAPManualBrickInteractive();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
