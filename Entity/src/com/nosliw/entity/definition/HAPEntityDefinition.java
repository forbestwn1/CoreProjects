package com.nosliw.entity.definition;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.entity.utils.HAPAttributeConstant;

/*
 * this entity definition is the entity definition used to create entity instance 
 * it is also stored in each entity
 * it has
 * 		all the information of entity definition segment plus
 * 	 	critical value when create entity instance
 */
public class HAPEntityDefinition extends HAPEntityDefinitionSegment{

	//null: no critical attribute
	//"" : critical attribute value is ""
	@HAPAttribute
	public final static String CRITICALATTRVALUE = "criticalAttrValue";

	public HAPEntityDefinition(HAPEntityDefinitionSegment def, String criticalValue, HAPEntityDefinitionManager entityDefinitionMan)
	{
		super(entityDefinitionMan);
		this.cloneFrom(def);
		this.m_criticalAttrValue = criticalValue;
	}
	
	public static HAPEntityDefinition buildEntityDefinition(){
		
	}
	
	public String getCriticalAttrValue() {		return this.getBasicAncestorValueString(CRITICALATTRVALUE);	}
	public void setCriticalAttrValue(String value){  this.updateBasicChild(CRITICALATTRVALUE, value); }

}
