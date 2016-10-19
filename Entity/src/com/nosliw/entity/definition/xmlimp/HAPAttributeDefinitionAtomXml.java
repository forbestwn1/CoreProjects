package com.nosliw.entity.definition.xmlimp;

import org.w3c.dom.Element;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPAttributeDefinitionAtom;
import com.nosliw.entity.definition.HAPEntityDefinitionSegment;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;

public class HAPAttributeDefinitionAtomXml extends HAPAttributeDefinitionAtom{

	public HAPAttributeDefinitionAtomXml(Element ele, HAPEntityDefinitionSegment entityDefinition, HAPEntityDefinitionMeta metadata, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan) {
		super(HAPEntityDefinitionLoaderXmlUtility.readAttributeName(ele), entityDefinition, dataTypeMan, entityDefMan, optionsMan);
	
		HAPEntityDefinitionLoaderXmlUtility.readCommonAttributeDefinition(ele, this, metadata, this.getDataTypeManager());

		//iscritical
		String critical = ele.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_ATTR_CRITICAL);
		
		if(HAPBasicUtility.isStringNotEmpty(critical)){	this.setIsCritical(HAPBasicUtility.toBoolean(critical));	}

		String defaultValue = ele.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_ATTR_DEFAULT);
		if(HAPBasicUtility.isStringNotEmpty(defaultValue)){
			this.setDefaultValue(this.getDataTypeManager().parseString(defaultValue, this.getDataTypeDefinitionInfo().getCategary(), this.getDataTypeDefinitionInfo().getType()));
		}
	}
}
