package com.nosliw.entity.definition.xmlimp;

import org.w3c.dom.Element;

import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPAttributeDefinitionEntity;
import com.nosliw.entity.definition.HAPEntityDefinitionSegment;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;

public class HAPEntityAttributeDefinitionXML extends HAPAttributeDefinitionEntity{

	public HAPEntityAttributeDefinitionXML(Element ele, HAPEntityDefinitionSegment entityDefinition, HAPEntityDefinitionMeta metadata, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan) {
		super(HAPEntityDefinitionLoaderXmlUtility.readAttributeName(ele), entityDefinition, dataTypeMan, entityDefMan, optionsMan);

		HAPEntityDefinitionLoaderXmlUtility.readCommonAttributeDefinition(ele, this, metadata, this.getDataTypeManager());

	}

}
