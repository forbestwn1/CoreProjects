package com.nosliw.data.core.domain.common.interactive;

import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntitySimple;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;

public class HAPExecutableEntityInteractive extends HAPExecutableEntitySimple{

	public static final String ATTR_INTERACTIVE = "interactive";

	public HAPExecutableEntityInteractive() {}
	
	public HAPDefinitionInteractive getInteractive() {  return (HAPDefinitionInteractive)this.getNormalAttributeValue(ATTR_INTERACTIVE);  }

	public void setInteractive(HAPDefinitionInteractive interactive) {    this.setNormalAttributeObject(ATTR_INTERACTIVE, new HAPEmbededExecutable(interactive));      }
	
}
