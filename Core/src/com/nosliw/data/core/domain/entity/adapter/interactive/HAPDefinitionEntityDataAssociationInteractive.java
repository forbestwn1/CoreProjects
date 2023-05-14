package com.nosliw.data.core.domain.entity.adapter.interactive;

import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityDataAssociationInteractive extends HAPDefinitionEntityInDomainSimple{

	public static final String ATTR_DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDefinitionGroupDataAssociationForTask dataAssciation) {    this.setNormalAttributeObject(ATTR_DATAASSOCIATION, new HAPEmbededDefinition(dataAssciation));    }
	public HAPDefinitionGroupDataAssociationForTask getDataAssociation() {   return (HAPDefinitionGroupDataAssociationForTask)this.getNormalAttributeValue(ATTR_DATAASSOCIATION);     }

	
	@Override
	public HAPDefinitionEntityDataAssociationInteractive cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDataAssociationInteractive out = new HAPDefinitionEntityDataAssociationInteractive();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
