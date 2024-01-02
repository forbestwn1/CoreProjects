package com.nosliw.data.core.domain.entity.adapter.task;

import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityDataAssociationTask extends HAPDefinitionEntityInDomainSimple{

	public static final String ATTR_DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDefinitionGroupDataAssociationForTask dataAssciation) {    this.setAttributeObject(ATTR_DATAASSOCIATION, new HAPEmbededDefinition(dataAssciation));    }
	public HAPDefinitionGroupDataAssociationForTask getDataAssociation() {   return (HAPDefinitionGroupDataAssociationForTask)this.getAttributeValue(ATTR_DATAASSOCIATION);     }

	
	@Override
	public HAPDefinitionEntityDataAssociationTask cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDataAssociationTask out = new HAPDefinitionEntityDataAssociationTask();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
