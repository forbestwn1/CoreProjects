package com.nosliw.data.core.domain.entity.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public abstract class HAPDefinitionEntityExpression extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_INDEX_ID = "idIndex";
	
	public static final String ATTR_ATTRIBUTES_REFERENCE = "referenceAttribute";
	
	public HAPDefinitionEntityExpression() {
		this.setNormalAttributeValueObject(ATTR_ATTRIBUTES_REFERENCE, new HashSet<String>());
	}

	public abstract List<HAPDefinitionExpression> getAllExpressions();
	
	//add referenced expression as attribute so that it can be processed under complex entity framework
	//return attribute name
	public String addReferencedExpressionAttribute(HAPIdEntityInDomain entityId) {
		int idIndex = this.getIdIndex();
		Set<String> refAttrs = this.getReferenceAttributes();
		
		String attrName = "Reference_"+idIndex;
		this.setNormalAttributeValueComplex(attrName, entityId);
		
		refAttrs.add(attrName);
		idIndex++;
		this.setIdIndex(idIndex);
		
		return attrName;
	}
	
	private int getIdIndex() {    return (Integer)this.getNormalAttributeValue(ATTR_INDEX_ID, Integer.valueOf(0));     }
	private void setIdIndex(int idIndex) {    this.setNormalAttributeValueObject(ATTR_INDEX_ID, Integer.valueOf(idIndex));      }

	private Set<String> getReferenceAttributes(){    return (Set<String>)this.getNormalAttributeValue(ATTR_ATTRIBUTES_REFERENCE);     }
	
}
