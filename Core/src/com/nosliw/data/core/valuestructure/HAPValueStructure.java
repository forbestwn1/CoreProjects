package com.nosliw.data.core.valuestructure;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.structure.HAPStructure;

//
public interface HAPValueStructure extends HAPStructure{

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String INFO = "info";
	

	//whether root is inherited by child
	boolean isInheriable(String rootId);

	boolean isExternalVisible(String rootId);
	
	HAPValueStructure getParent();
	void setParent(HAPValueStructure parent);
}
