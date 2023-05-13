package com.nosliw.data.core.domain.common.interactive;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;

public class HAPDefinitionEntityInteractive extends HAPDefinitionEntityInDomain{

	public static final String ATTR_INTERACTIVE = "interactive";

	public HAPDefinitionEntityInteractive() {
	}
	
	public HAPDefinitionInteractive getInteractive() {  return (HAPDefinitionInteractive)this.getNormalAttributeValue(ATTR_INTERACTIVE);  }

	public void setInteractive(HAPDefinitionInteractive interactive) {    this.setNormalAttributeObject(ATTR_INTERACTIVE, new HAPEmbededDefinition(interactive));      }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInteractive out = new HAPDefinitionEntityInteractive();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
