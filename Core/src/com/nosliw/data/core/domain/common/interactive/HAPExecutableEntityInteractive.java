package com.nosliw.data.core.domain.common.interactive;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntitySimple;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;

@HAPEntityWithAttribute
public class HAPExecutableEntityInteractive extends HAPExecutableEntitySimple{

	@HAPAttribute
	public static final String ATTR_INTERACTIVE = "interactive";

	public HAPExecutableEntityInteractive() {}
	
	public HAPDefinitionInteractive getInteractive() {  return (HAPDefinitionInteractive)this.getNormalAttributeValue(ATTR_INTERACTIVE);  }

	public void setInteractive(HAPDefinitionInteractive interactive) {    this.setNormalAttributeObject(ATTR_INTERACTIVE, new HAPEmbededExecutable(interactive));      }
	
}
