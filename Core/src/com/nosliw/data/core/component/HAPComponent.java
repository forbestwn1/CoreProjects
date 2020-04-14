package com.nosliw.data.core.component;

//entity info : name, description, info
//id
//context
//attachment mapping
//
public interface HAPComponent extends HAPResourceDefinitionComplex, HAPWithLifecycleAction, HAPWithEventHanlder{

	HAPComponent cloneComponent();
}
