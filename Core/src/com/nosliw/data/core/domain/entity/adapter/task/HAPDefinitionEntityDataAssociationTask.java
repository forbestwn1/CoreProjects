package com.nosliw.data.core.domain.entity.adapter.task;

import com.nosliw.core.application.division.manual.HAPManualBrickSimple;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityDataAssociationTask extends HAPManualBrickSimple{

	public static final String ATTR_DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDefinitionGroupDataAssociationForTask dataAssciation) {    this.setAttributeObject(ATTR_DATAASSOCIATION, new HAPEmbededDefinition(dataAssciation));    }
	public HAPDefinitionGroupDataAssociationForTask getDataAssociation() {   return (HAPDefinitionGroupDataAssociationForTask)this.getAttributeValueOfValue(ATTR_DATAASSOCIATION);     }

	
	@Override
	public HAPDefinitionEntityDataAssociationTask cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDataAssociationTask out = new HAPDefinitionEntityDataAssociationTask();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
