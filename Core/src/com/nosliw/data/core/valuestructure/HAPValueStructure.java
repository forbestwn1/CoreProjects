package com.nosliw.data.core.valuestructure;

import com.nosliw.data.core.structure.HAPStructure;

//
public interface HAPValueStructure extends HAPStructure{

	//whether root is inherited by child
	boolean isInheriable(String rootId);

	boolean isExternalVisible(String rootId);
	
	HAPValueStructure getParent();
	void setParent(HAPValueStructure parent);
}
