package com.nosliw.entity.definition;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
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

	//critical attribute name/definition
	protected String m_criticalAttribute;
	private HAPAttributeDefinitionAtom m_criticalAttrDefinition;
	//entity attributes parts determined by critical value
	protected Map<String, HAPEntityDefinitionSegment> m_criticalEntitySegmentsValue = null;
	//other critical value: when have no value match within m_criticalEntitys with critical attribute value, then use other part  
	protected HAPEntityDefinitionSegment m_criticalEntitySegmentOther = null;
	
	public HAPEntityDefinitionCritical(String name, String baseClassName, Set<String> groups, HAPEntityDefinitionManager entityDefinitionMan){
		super(name, baseClassName, groups, entityDefinitionMan);
		m_criticalEntitySegmentsValue = new LinkedHashMap<String, HAPEntityDefinitionSegment>();
	}	

	public HAPEntityDefinitionCritical(HAPEntityDefinitionSegment entityDefBasic){
		super(entityDefBasic);
		m_criticalEntitySegmentsValue = new LinkedHashMap<String, HAPEntityDefinitionSegment>();
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

	public HAPEntityDefinitionSegment getCriticalEntitySegmentOther(){return this.m_criticalEntitySegmentOther;}
	public void setCriticalEntitySegmentOther(HAPEntityDefinitionSegment other){
		this.setupCriticalEntitySegment(other);
		this.m_criticalEntitySegmentOther=other;
	}

	public void addCriticalEntitySegmentValue(String value, HAPEntityDefinitionSegment criticalEntity){
		this.setupCriticalEntitySegment(criticalEntity);
		this.m_criticalEntitySegmentsValue.put(value, criticalEntity);
	}
	public HAPEntityDefinitionSegment getCriticalEntity(String value){return this.m_criticalEntitySegmentsValue.get(value);}
	
	/*
	 * set up child entity segment according to parent entity segment
	 */
	protected void setupCriticalEntitySegment(HAPEntityDefinitionSegment entityDef){
		//segment has the same group information as parent entity
		entityDef.m_groups.addAll(this.m_groups);
		//base name
		if(HAPBasicUtility.isStringEmpty(entityDef.m_baseClassName))  entityDef.m_baseClassName = this.m_baseClassName;
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
			out.addAll(this.m_criticalEntitySegmentsValue.keySet());
			if(this.m_criticalEntitySegmentOther!=null)  out.add(HAPConstant.CONS_ENTITY_CRITICALVALUE_OTHER);
		}
		return out;
	}
	
	public Map<String, HAPAttributeDefinition> getCriticalValueAttributeDefinitions(String criticalValue){
		return this.getEntityDefinitionByCriticalValue(criticalValue).getAttributeDefinitions();
	}
	
	/*
	 * get entity whole definition according to the value of the critical attribute
	 * if has not critical attribute, then return basic entity part
	 * if criticalValue is not valid one, then return basic entity part
	 */
	public HAPEntityDefinition getEntityDefinitionByCriticalValue(String criticalValue) {
		//not critical attribute OR invalid criticalValue, then use base segment 
		if(!this.hasCriticalAttribute() || this.isValidCriticalValue(criticalValue))  return new HAPEntityDefinition(this, null, this.getEntityDefinitionManager());

		HAPEntityDefinitionSegment criticalEntityDefSegment = this.getCriticalEntitySegmentByCriticalValue(criticalValue);
		
		HAPEntityDefinitionSegment entityDef = null;
		if(criticalEntityDefSegment!=null)			entityDef = criticalEntityDefSegment.hardMerge(this);
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
		HAPEntityDefinitionSegment entityDef = this.m_criticalEntitySegmentsValue.get(value);
		if(entityDef==null)			entityDef = this.m_criticalEntitySegmentOther;
		return entityDef;
	}
	
	/*
	 * search in all attribute to find critical attribute 
	 */
	private void findCriticalAttribute(){
		for(String name : this.getAttributeDefinitions().keySet()){
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
			if(keyword.equals(HAPConstant.CONS_ATTRIBUTE_PATH_CRITICAL)){
				//critical 
				String optionValue = segments.next();
				String name = segments.next();
				
				HAPEntityDefinitionSegment optionEntity = this.m_criticalEntitySegmentsValue.get(optionValue);
				if(optionEntity==null)  optionEntity = this.m_criticalEntitySegmentOther;
				if(optionEntity==null)	return null;
				
				attrDef = optionEntity.getAttributeDefinitionByPath(name);
			}
		}
		else{
			//attribute name not contains keyword, then it is normal attribute
			attrDef = this.m_attributeDefs.get(attrName);
		}
		return attrDef;
	}
	
	/*
	 * check if value is a valid value for critical attribute value
	 */
	private boolean isValidCriticalValue(String value){		return HAPEntityUtility.getEmptyCriticalValue()==value;	}
	
	@Override
	public HAPEntityDefinitionCritical cloneDefinition()
	{
		HAPEntityDefinitionCritical entityDef = new HAPEntityDefinitionCritical(this.getEntityName(), this.getBaseClassName(), this.getGroups(), this.getEntityDefinitionManager());
		entityDef.cloneFrom(this);
		return entityDef;
	}
	
	protected void cloneFrom(HAPEntityDefinitionCritical entityDef){
		super.cloneFrom(entityDef);
		if(entityDef.m_criticalEntitySegmentsValue!=null){
			this.m_criticalEntitySegmentsValue = new LinkedHashMap<String, HAPEntityDefinitionSegment>(); 
			for(String vv :entityDef.m_criticalEntitySegmentsValue.keySet()){
				this.m_criticalEntitySegmentsValue.put(vv, entityDef.m_criticalEntitySegmentsValue.get(vv).cloneDefinition());
			}
		}
		if(entityDef.m_criticalEntitySegmentOther!=null)	this.m_criticalEntitySegmentOther = entityDef.m_criticalEntitySegmentOther.cloneDefinition();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap){
		//critical attribute
		Map<String, String> criAttrJsonMap = new LinkedHashMap<String, String>();
		for(String criVal : this.m_criticalEntitySegmentsValue.keySet()){
			HAPEntityDefinitionSegment criEntityDef = this.m_criticalEntitySegmentsValue.get(criVal);
			criAttrJsonMap.put(criVal, criEntityDef.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
		}
		
		//other critical attribute
		if(this.m_criticalEntitySegmentOther!=null){
			criAttrJsonMap.put(HAPConstant.CONS_ENTITY_CRITICALVALUE_OTHER, m_criticalEntitySegmentOther.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
		}
		String criAttrJson = HAPJsonUtility.getMapJson(criAttrJsonMap);
		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYDEFINITION_CRITICALENTITYS, criAttrJson);
		
	}	
}
