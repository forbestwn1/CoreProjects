package com.nosliw.common.interfac;

import java.util.Set;

public interface HAPWithParm {

	Set<String> getParmNames();
	
	Object getParmValue(String name);
	
}
