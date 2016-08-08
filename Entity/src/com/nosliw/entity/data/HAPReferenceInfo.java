package com.nosliw.entity.data;

import com.nosliw.common.serialization.HAPStringable;

/*
 * class to store reference info
 * there are two types of reference
 * 		absolute:  reference through entityID + path
 * 		relative:  reference through relative path related to current position of wrapper
 */
public abstract class HAPReferenceInfo implements HAPStringable{

	abstract public int getType();
	
}
