package com.nosliw.core.application.brick.adapter.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;

@HAPEntityWithAttribute
public interface HAPAdapterDataAssociation extends HAPBrick{

	@HAPAttribute
	public static final String DATAASSOCIATION = "dataAssociation";

	HAPDataAssociation getDataAssociation();

}
