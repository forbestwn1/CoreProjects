package com.nosliw.common.boolop;

import com.nosliw.common.serialization.HAPSerializable;

public interface HAPBooleanTask extends HAPSerializable{

	boolean execute(Object data);
	
}
