package com.nosliw.data.core.interactive;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityInteractive extends HAPManualBrick{

	public static final String ATTR_INTERACTIVE = "interactive";

	public HAPDefinitionEntityInteractive() {
	}
	
	public HAPDefinitionInteractive getInteractive() {  return (HAPDefinitionInteractive)this.getAttributeValue(ATTR_INTERACTIVE);  }

	public void setInteractive(HAPDefinitionInteractive interactive) {    this.setAttributeObject(ATTR_INTERACTIVE, new HAPEmbededDefinition(interactive));      }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInteractive out = new HAPDefinitionEntityInteractive();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
