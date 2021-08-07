package com.nosliw.data.core.component;

import com.nosliw.data.core.component.command.HAPWithCommand;
import com.nosliw.data.core.component.event.HAPWithEvent;

//component contains two parts
//    	static info : complex resource definition (data context, attachment, )
//		dynamic info: lifecycle action, event handler, use service
public interface HAPComponent extends HAPDefinitionResourceComplex, HAPWithLifecycleAction, HAPWithEventHanlder, HAPWithService, HAPWithCommand, HAPWithEvent{

	HAPComponent cloneComponent();
}
