package com.nosliw.data.core.interactive;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityInteractive extends HAPDefinitionEntityInDomain{

	public static final String ATTR_INTERACTIVE = "interactive";

	public HAPDefinitionEntityInteractive() {
	}
	
	public HAPDefinitionInteractive getInteractive() {  return (HAPDefinitionInteractive)this.getAttributeValue(ATTR_INTERACTIVE);  }

	public void setInteractive(HAPDefinitionInteractive interactive) {    this.setAttributeObject(ATTR_INTERACTIVE, new HAPEmbededDefinition(interactive));      }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInteractive out = new HAPDefinitionEntityInteractive();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
