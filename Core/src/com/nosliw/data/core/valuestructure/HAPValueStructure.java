package com.nosliw.data.core.valuestructure;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.structure.HAPStructure;

//
public interface HAPValueStructure extends HAPSerializable, HAPStructure{

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String INFO = "info";
	

	//whether root is inherited by child
	boolean isInheriable(String rootId);

	boolean isExternalVisible(String rootId);

	boolean isFlat();

	boolean isEmpty();
	
	void hardMergeWith(HAPValueStructure context);

}
