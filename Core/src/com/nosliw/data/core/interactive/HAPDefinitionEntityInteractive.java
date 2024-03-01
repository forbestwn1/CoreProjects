package com.nosliw.data.core.interactive;

import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;

public class HAPDefinitionEntityInteractive extends HAPManualEntity{

	public static final String ATTR_INTERACTIVE = "interactive";

	public HAPDefinitionEntityInteractive() {
	}
	
	public HAPDefinitionInteractive getInteractive() {  return (HAPDefinitionInteractive)this.getAttributeValue(ATTR_INTERACTIVE);  }

	public void setInteractive(HAPDefinitionInteractive interactive) {    this.setAttributeObject(ATTR_INTERACTIVE, new HAPEmbededDefinition(interactive));      }
	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInteractive out = new HAPDefinitionEntityInteractive();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
