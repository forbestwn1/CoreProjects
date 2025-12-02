package com.nosliw.core.application.division.story;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializable;

public interface HAPStoryReferenceElement extends HAPEntityOrReference, HAPSerializable{

	HAPStoryReferenceElement cloneElementReference();
	
}
