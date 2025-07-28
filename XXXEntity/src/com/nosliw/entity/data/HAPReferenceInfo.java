package com.nosliw.entity.data;

import com.nosliw.common.serialization.HAPSerializable;

/*
 * class to store reference info
 * there are two types of reference
 * 		absolute:  reference through entityID + path
 * 		relative:  reference through relative path related to current position of wrapper
 */
public abstract class HAPReferenceInfo implements HAPSerializable{

	abstract public int getType();
	
}
