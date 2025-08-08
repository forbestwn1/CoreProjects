package com.nosliw.core.application.brick.adapter.dataassociationforexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.xxx.application.common.dataassociation.HAPDataAssociationForExpression;

@HAPEntityWithAttribute
public interface HAPAdapterDataAssociationForExpression extends HAPBrick{

	@HAPAttribute
	public static final String DATAASSOCIATION = "dataAssociation";
	
	HAPDataAssociationForExpression getDataAssociation();

}
