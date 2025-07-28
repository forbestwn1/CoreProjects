package com.nosliw.core.application.brick.adapter.dataassociationfortask;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForTask;

@HAPEntityWithAttribute
public interface HAPAdapterDataAssociationForTask extends HAPBrick{

	@HAPAttribute
	public static final String DATAASSOCIATION = "dataAssociation";
	
	HAPDataAssociationForTask getDataAssociation();
}
