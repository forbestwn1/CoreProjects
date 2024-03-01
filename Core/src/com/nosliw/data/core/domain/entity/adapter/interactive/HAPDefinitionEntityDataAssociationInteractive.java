package com.nosliw.data.core.domain.entity.adapter.interactive;

import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.entity.division.manual.HAPManualEntitySimple;

public class HAPDefinitionEntityDataAssociationInteractive extends HAPManualEntitySimple{

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
