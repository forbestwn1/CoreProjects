package com.nosliw.data.core.domain.container;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.domain.HAPEmbeded;

public interface HAPInfoContainerElement<T extends HAPEmbeded> extends HAPSerializable{

	public static final String ELEMENTNAME = "eleName";
	
	public static final String ENTITYID = "entityId";

	public static final String ENTITY = "entity";

	String getInfoType();

	String getElementId(); 

	T getEmbededElementEntity();

	String getElementName();
	
	HAPInfoContainerElementImp cloneContainerElementInfo();

	
}
