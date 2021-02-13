package com.nosliw.data.core.component;

//component contains two parts
//    	static info : complex resource definition (data context, attachment, )
//		dynamic info: lifecycle action, event handler, use service
public interface HAPComponent extends HAPDefinitionResourceComplex, HAPWithLifecycleAction, HAPWithEventHanlder, HAPWithService{

	HAPComponent cloneComponent();
}
