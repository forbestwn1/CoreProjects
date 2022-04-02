package com.nosliw.data.core.component;

import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.component.command.HAPWithCommand;
import com.nosliw.data.core.component.event.HAPWithEvent;

//component contains two parts
//    	static info : complex resource definition (data context, attachment, )
//		dynamic info: lifecycle action, event handler, use service
public interface HAPDefinitionEntityComponent extends HAPDefinitionEntityInDomainComplex, HAPWithTask, HAPWithService, HAPWithCommand, HAPWithEvent{

	HAPDefinitionEntityComponent cloneComponent();
}
