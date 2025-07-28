package com.nosliw.entity.definition;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.entity.utils.HAPAttributeConstant;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.entity.utils.HAPEntityUtility;

/*
 * this class is used to define entity structure
 * it contains two parts:
 * 		base segment
 * 		critical value segment
 * when define a entity, one attribute can be critical attribute
 * with different value for critical attribute, we can define different entity segment for this value
 * so that when create a entity instance, the entity definition for the new instance will be 
 * 		attributes : base segment + critical segment for critical value of critical attribute
 * 		base class: base class from critical segment for critical value of critical attribute
 */

public class HAPEntityDefinitionCritical extends HAPEntityDefinitionSegment{
	@HAPAttribute
	public static final String CRITICALSEGMENTS = "criticalSegments";
	@HAPAttribute
	public static final String OTHERCRITICALSEGMENTS = "otherCriticalSegment";
	
	//critical attribute name/definition
	protected String m_criticalAttribute;
	private HAPAttributeDefinitionAtom m_criticalAttrDefinition;

	public HAPEntityDefinitionCritical(){}
	
	public HAPEntityDefinitionCritical(String name, String baseClassName, Set<String> groups, HAPEntityDefinitionManager entityDefinitionMan){
		super(name, baseClassName, groups, entityDefinitionMan);
	}	

	public HAPEntityDefinitionCritical(HAPEntityDefinitionSegment entityDefBasic){
		super(entityDefBasic);
	}	
	
	@Override
	public void afterLoad(){
		super.afterLoad();
		this.findCriticalAttribute();
	}

	//************************  methods about critical attribute
	/*
	 * critical attribute
	 * null if no critical attribute
	 */
	public String getCriticalAttribute() {return this.m_criticalAttribute;}

	/*
	 * get class type attribute definition
	 * null if no class type attribute
	 */
	public HAPAttributeDefinitionAtom getCriticalAttributeDefinition(){ 	return m_criticalAttrDefinition;	}

	public HAPEntityDefinitionSegment getCriticalEntitySegmentOther(){return (HAPEntityDefinitionSegment)this.getChild(OTHERCRITICALSEGMENTS);}
	public void setCriticalEntitySegmentOther(HAPEntityDefinitionSegment other){
		this.setupCriticalEntitySegment(other);
		this.updateChild(OTHERCRITICALSEGMENTS, other);
	}

	public void addCriticalEntitySegmentValue(String value, HAPEntityDefinitionSegment criticalEntity){
		this.setupCriticalEntitySegment(criticalEntity);
		this.getCriticalEntitySegments().updateChild(value, criticalEntity);
	}
	public HAPEntityDefinitionSegment getCriticalEntitySegment(String value){return (HAPEntityDefinitionSegment)this.getCriticalEntitySegments().getChild(value);}
	
	private HAPStringableValueMap getCriticalEntitySegments(){	return (HAPStringableValueMap)this.getChild(CRITICALSEGMENTS);	}
	
	/*
	 * set up child entity segment according to parent entity segment
	 */
	protected void setupCriticalEntitySegment(HAPEntityDefinitionSegment entityDef){
		//segment has the same group information as parent entity
		entityDef.addGroups(this.getGroups());
		//base name
		if(HAPBasicUtility.isStringEmpty(entityDef.getBaseClassName()))  entityDef.setBaseClassName(this.getBaseClassName());
	}
	
	/*
	 * get class name based on the critical attribute value
	 * return base class name if value is not critical value
	 */
	public String getCriticalClassName(String value) {
		String out = this.getBaseClassName();
		if(this.hasCriticalAttribute()){
			HAPEntityDefinitionSegment entityDef = this.getCriticalEntitySegmentByCriticalValue(value);
			if(entityDef!=null)			out = entityDef.getBaseClassName();
		}
		return out;
	}

	public Set<String> getAllCriticalValues(){
		Set<String> out = new HashSet<String>();
		if(this.hasCriticalAttribute()){
			out.addAll(this.getCriticalEntitySegments().getKeys());
			if(this.getCriticalEntitySegmentOther()!=null)  out.add(HAPConstant.ENTITY_CRITICALVALUE_OTHER);
		}
		return out;
	}
	
	public HAPStringableValueMap getCriticalValueAttributeDefinitions(String criticalValue){
		return this.getEntityDefinitionByCriticalValue(criticalValue).getAttributeDefinitions();
	}
	
	/*
	 * get entity whole definition according to the value of the critical attribute
	 * if has not critical attribute, then return basic entity part
	 * if criticalValue is not valid one, then return basic entity part
	 */
	public HAPEntityDefinition getEntityDefinitionByCriticalValue(String criticalValue) {
		HAPEntityDefinition out = null;
		
		//not critical attribute OR invalid criticalValue, then use base segment 
		if(!this.hasCriticalAttribute() || this.isValidCriticalValue(criticalValue)){
			out = this.clone(HAPEntityDefinition.class);
		}
		else{
			HAPEntityDefinitionSegment criticalEntityDefSegment = this.getCriticalEntitySegmentByCriticalValue(criticalValue);
			out = criticalEntityDefSegment.clone(HAPEntityDefinition.class);
			out.setCriticalAttrValue(criticalValue);
			
			
		}
		
		
		
		//not critical attribute OR invalid criticalValue, then use base segment 
		if(!this.hasCriticalAttribute() || this.isValidCriticalValue(criticalValue))  return new HAPEntityDefinition(this, null, this.getEntityDefinitionManager());

		HAPEntityDefinitionSegment criticalEntityDefSegment = this.getCriticalEntitySegmentByCriticalValue(criticalValue);
		
		HAPEntityDefinitionSegment entityDef = null;
		if(criticalEntityDefSegment!=null)			entityDef = (HAPEntityDefinitionSegment)this.hardMergeSegment(criticalEntityDefSegment);
		else	entityDef = this;

		return new HAPEntityDefinition(entityDef, criticalValue, this.getEntityDefinitionManager());
	}
	
	/*
	 * check if this entity def has critical attribute defined
	 */
	public boolean hasCriticalAttribute(){return !HAPBasicUtility.isStringNotEmpty(this.getCriticalAttribute());}

	/*
	 * get critical part of entity def according to critical value
	 */
	protected HAPEntityDefinitionSegment getCriticalEntitySegmentByCriticalValue(String value){
		//if value is invalid, return null
		if(!this.isValidCriticalValue(value))  return null;
		HAPEntityDefinitionSegment entityDef = this.getCriticalEntitySegment(value);
		if(entityDef==null)			entityDef = this.getCriticalEntitySegmentOther();
		return entityDef;
	}
	
	/*
	 * search in all attribute to find critical attribute 
	 */
	private void findCriticalAttribute(){
		for(String name : this.getAttributeDefinitions().getKeys()){
			HAPAttributeDefinition attr = this.getAttributeDefinitionByName(name); 
			if(HAPEntityDataTypeUtility.isAtomType(attr.getDataTypeDefinitionInfo())){
				if(((HAPAttributeDefinitionAtom)attr).getIsCritical()){
					this.m_criticalAttribute = attr.getName();
					this.m_criticalAttrDefinition = (HAPAttributeDefinitionAtom)attr;
					break;
				}
			}
		}
	}
	
	@Override
	public HAPAttributeDefinition getAttributeDefinitionByName(String attrName){
		HAPAttributeDefinition attrDef = null;
		
		HAPSegmentParser segments = HAPNamingConversionUtility.isKeywordPhrase(attrName);
		if(segments!=null){
			//contain keyword
			String keyword = segments.next();
			if(keyword.equals(HAPConstant.ATTRIBUTE_PATH_CRITICAL)){
				//critical 
				String optionValue = segments.next();
				String name = segments.next();
				
				HAPEntityDefinitionSegment optionEntity = getCriticalEntitySegmentByCriticalValue(optionValue); 
				if(optionEntity==null)	return null;
				
				attrDef = optionEntity.getAttributeDefinitionByPath(name);
			}
		}
		else{
			//attribute name not contains keyword, then it is normal attribute
			attrDef = this.getAttributeDefinitionByName(attrName);
		}
		return attrDef;
	}
	
	/*
	 * check if value is a valid value for critical attribute value
	 */
	private boolean isValidCriticalValue(String value){		return HAPEntityUtility.getEmptyCriticalValue()==value;	}
}
