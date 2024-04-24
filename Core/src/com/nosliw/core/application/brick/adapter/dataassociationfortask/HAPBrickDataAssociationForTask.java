package com.nosliw.core.application.brick.adapter.dataassociationfortask;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickAdapter;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPBrickDataAssciation;
import com.nosliw.core.application.brick.container.HAPBrickContainer;

@HAPEntityWithAttribute
public class HAPBrickDataAssociationForTask extends HAPBrickAdapter{

	@HAPAttribute
	public static final String DATAASSOCIATIONREQUEST = "dataAssociationRequest";
	
	@HAPAttribute
	public static final String DATAASSOCIATIONRESPONSE = "dataAssociationResponse";
	
	@Override
	public void init() {
		this.getBrickManager().newBrickInstance(HAPEnumBrickType.CONTAINER_100);
	}

	public void setRequestDataAssociation(HAPBrickDataAssciation dataAssociation) {		this.setAttributeValueWithBrick(DATAASSOCIATIONREQUEST, dataAssociation);	}
	public void setResponseDataAssociation(String responseName, HAPBrickDataAssciation dataAssociation) {
		HAPBrickContainer responses = (HAPBrickContainer)this.getAttributeValueOfBrick(DATAASSOCIATIONRESPONSE);
		responses.addElement(responseName, dataAssociation);
	}
}
