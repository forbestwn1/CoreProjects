package com.nosliw.data.core.domain.entity.container;

import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoValueType;

public class HAPDefinitionEntityComplexContainer extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_INDEX_ID = "idIndex";

	public static final String ATTR_ELEMENT_TYPEINFO = "eleTypeInfo";

	public String addElementAttribute(HAPIdEntityInDomain entityId, Boolean autoProcess) {
		int idIndex = this.getIdIndex();
		String attrName = "Element_"+idIndex;
		idIndex++;
		this.setIdIndex(idIndex);
		
		this.setAttribute(attrName, new HAPEmbededDefinition(entityId), this.getElmentValueTypeInfo(), autoProcess);
		return attrName;
	}
	
	public HAPInfoValueType getElmentValueTypeInfo() {     return (HAPInfoValueType)this.getAttributeValue(ATTR_ELEMENT_TYPEINFO);     }
	public void setElementValueTypeInfo(HAPInfoValueType eleValueTypeInfo) {    this.setAttributeValueObject(ATTR_ELEMENT_TYPEINFO, eleValueTypeInfo);      }
	
	private int getIdIndex() {    return (Integer)this.getAttributeValue(ATTR_INDEX_ID, Integer.valueOf(0));     }
	private void setIdIndex(int idIndex) {    this.setAttributeValueObject(ATTR_INDEX_ID, Integer.valueOf(idIndex));      }

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexContainer out = new HAPDefinitionEntityComplexContainer();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
