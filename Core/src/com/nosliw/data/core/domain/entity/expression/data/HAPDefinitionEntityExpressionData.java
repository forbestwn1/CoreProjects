package com.nosliw.data.core.domain.entity.expression.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.container.HAPDefinitionEntityComplexContainer;

public abstract class HAPDefinitionEntityExpressionData extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_INDEX_ID = "idIndex";
	
	public static final String ATTR_ATTRIBUTES_REFERENCE = "referenceAttribute";

	public static final String ATTR_REFERENCES = "references";

	public HAPDefinitionEntityExpressionData() {
		this.setAttributeValueObject(ATTR_ATTRIBUTES_REFERENCE, new HashSet<String>());
	}

	public abstract List<HAPDefinitionExpressionData> getAllExpressionItems();

	public HAPDefinitionEntityComplexContainer getReferences(HAPDomainEntityDefinitionGlobal globalDomain){    
		return (HAPDefinitionEntityComplexContainer)globalDomain.getEntityInfoDefinition((HAPIdEntityInDomain)this.getAttributeValue(ATTR_REFERENCES)).getEntity();    
	}
	
	//add referenced expression as attribute so that it can be processed under complex entity framework
	//return attribute name
	public String addReferencedExpressionAttribute(HAPIdEntityInDomain entityId) {
		int idIndex = this.getIdIndex();
		Set<String> refAttrs = this.getReferenceAttributes();
		
		String attrName = "Reference_"+idIndex;
		this.setAttributeValueComplex(attrName, entityId);
		
		refAttrs.add(attrName);
		idIndex++;
		this.setIdIndex(idIndex);
		
		return attrName;
	}
	
	private int getIdIndex() {    return (Integer)this.getAttributeValue(ATTR_INDEX_ID, Integer.valueOf(0));     }
	private void setIdIndex(int idIndex) {    this.setAttributeValueObject(ATTR_INDEX_ID, Integer.valueOf(idIndex));      }

	public Set<String> getReferenceAttributes(){    return (Set<String>)this.getAttributeValue(ATTR_ATTRIBUTES_REFERENCE);     }
	
}
