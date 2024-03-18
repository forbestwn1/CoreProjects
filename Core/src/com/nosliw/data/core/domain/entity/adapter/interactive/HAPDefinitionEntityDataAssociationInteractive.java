package com.nosliw.data.core.domain.entity.adapter.interactive;

import com.nosliw.core.application.division.manual.HAPManualBrickSimple;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityDataAssociationInteractive extends HAPManualBrickSimple{

	public static final String ATTR_DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDefinitionGroupDataAssociationForTask dataAssciation) {    this.setAttributeObject(ATTR_DATAASSOCIATION, new HAPEmbededDefinition(dataAssciation));    }
	public HAPDefinitionGroupDataAssociationForTask getDataAssociation() {   return (HAPDefinitionGroupDataAssociationForTask)this.getAttributeValue(ATTR_DATAASSOCIATION);     }

	
	@Override
	public HAPDefinitionEntityDataAssociationInteractive cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDataAssociationInteractive out = new HAPDefinitionEntityDataAssociationInteractive();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
