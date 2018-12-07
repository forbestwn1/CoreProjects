package com.nosliw.data.core.script.context;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

/**
 * Node in context definition
 * One node either 
 * 		has data type  -- a leaf
 *   or has children -- a branch
 * cannot be both
 */
@HAPEntityWithAttribute
public abstract class HAPContextDefinitionElement extends HAPSerializableImp{

	public HAPContextDefinitionElement(){
	}
	
	abstract public String getType(); 
}
