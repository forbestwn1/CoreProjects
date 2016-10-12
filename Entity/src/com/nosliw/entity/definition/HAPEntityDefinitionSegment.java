package com.nosliw.entity.definition;

import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.strvalue.HAPStringableValueBasic;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.strvalue.HAPStringableValueUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPSegmentParser;

/*
 * this class is the definition of an entity segment
 * this segment can be used in 
 * 		1.  base segment when define entity
 *      2.  critical value segment when define entity
 *      3.  entity definition for entity instance
 * every entity type has 
 * 	entity name, for instance : common.Parm
 * 	group:
 * 		group is introduced when dealing with datasource.
 * 		because there are different type of datasoruces and system allow other type datasources are introduced into system as plugin
 * 		for some entity, for instance Face, it has attribute typed datasource. 
 * 		in that case, we use group to group different type of datasource under one group name
 * 		therefore, face entity just define its attribute by group name
 * 		one entity can belong to mutiple group
 *  attributes, there are two type of attributes : normal and critical one
 *  	critical attribute: when critical attribute is set to different value, the entity's attribute definition may changes. 
 *  	The class name may change as well
 *  	critical is in text type
 *  class
 *  	base class to create complex entity object
 *		for entity with critical attribute, each critical value may has its own base class,   	
 *  critical value:
 *  	"" : valid critical value
 *      null : invalid or unknown
 *  provide method to create another entity definition based on critical attribute value
 */
@HAPEntityWithAttribute(baseName="ENTITYDEFINITION")
public class HAPEntityDefinitionSegment extends HAPStringableValueEntity{
	@HAPAttribute
	public final static String NAME = "name";
	@HAPAttribute
	public final static String DESCRIPTION = "description";
	@HAPAttribute
	public final static String GROUPS = "groups";
	@HAPAttribute
	public final static String BASECLASS = "baseClass";
	@HAPAttribute
	public final static String ATTRIBUTES = "attributes";
	
	private HAPEntityDefinitionManager m_entityDefinitionMan;

	public HAPEntityDefinitionSegment(){}
	
	public HAPEntityDefinitionSegment(String name, String baseClassName, Set<String> groups, HAPEntityDefinitionManager entityDefinitionMan){
		this.m_entityDefinitionMan = entityDefinitionMan;
	}	
	
	protected HAPEntityDefinitionSegment(HAPEntityDefinitionManager entityDefinitionMan){
		this(null, null, null, entityDefinitionMan);
	}

	public HAPEntityDefinitionSegment(HAPEntityDefinitionSegment def){
		this(def.getEntityDefinitionManager());
		this.cloneFrom(def);
	}
	
	
	/******************************************   Attribute  *********************************************/
	/*
	 * get entity's base attributes names, not including those come with the subclass attribute
	 */
	public Set<String> getAttributeNames() {
		HAPStringableValueMap attributes = this.getAttributeDefinitions();
		return attributes.getKeys();
	}

	/*
	 * get entity's base attribute definitions info, not including those come with the subclass attribute
	 */
	public HAPStringableValueMap getAttributeDefinitions() {	return (HAPStringableValueMap)this.getChild(ATTRIBUTES);}
	
	protected void copyAttributeDefinition(HAPAttributeDefinition defin){
		HAPAttributeDefinition def1 = defin.cloneDefinition(this);
		this.addAttributeDefinition(def1);
	}

	protected void copyAttributeDefinitions(Set<HAPAttributeDefinition> defins) {
		for(HAPAttributeDefinition def: defins)		this.copyAttributeDefinition(def);
	}

	public void addAttributeDefinition(HAPAttributeDefinition defin){	this.getAttributeDefinitions().updateChild(defin.getName(), defin);	}
	
	public void removeAttributeDefinition(String attrName) {	this.getAttributeDefinitions().updateChild(attrName, null);	}
	
	public HAPAttributeDefinition getAttributeDefinitionByName(String attrName){	return (HAPAttributeDefinition)this.getAttributeDefinitions().getChild(attrName);	}
	
	/*
	 * get attribute definitions by attribute path
	 * return null if no attribute exists
	 * 
	 * the attribute path is a list of jointed attribute  for example : 
	 * 			children.name:..
	 * 			#critical|value|attribute
	 * 			#element.address
	 */
	public HAPAttributeDefinition getAttributeDefinitionByPath(String attrPath) {
		HAPSegmentParser pathParser = new HAPSegmentParser(attrPath);
		String attrName = pathParser.next();
		String attrRest = pathParser.getRestPath();
		
		HAPAttributeDefinition attrDef = this.getAttributeDefinitionByName(attrName);
		if(attrDef==null)  return null;
		if(HAPBasicUtility.isStringEmpty(attrRest))  return attrDef;
		else return attrDef.getChildAttrByPath(attrRest);
	}

	/******************************************   Basic Information  *********************************************/
	public String getName(){  return this.getBasicAncestorValueString(NAME); }
	public String getDescription(){  return this.getBasicAncestorValueString(DESCRIPTION); }

	public String getEntityName(){return this.getName();}
	
	public List<String> getGroups() {	return this.getBasicAncestorValueArray(GROUPS);	}
	public void addGroups(List<String> groups){
		List<String> gs = this.getGroups();
		gs.addAll(groups);
		HAPStringableValueBasic groupsValue = HAPStringableValueBasic.buildFromObject(gs);
		this.updateChild(GROUPS, groupsValue);
	}
	
	/* get defined class name for this entity
	 * if not defined, then use method getDefaultClassName in EntityDefinitionManager instead 
	 */
	public String getBaseClassName() {	return this.getBasicAncestorValueString(BASECLASS);	}
	public void setBaseClassName(String baseClassName){  this.updateBasicChild(BASECLASS, baseClassName); }

	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefinitionMan;}
	
	/******************************************   Clone  *********************************************/
	protected void cloneFrom(HAPEntityDefinitionSegment entityDef){
		super.cloneFrom(entityDef);
		this.m_entityDefinitionMan = entityDef.m_entityDefinitionMan;
	}

	public HAPEntityDefinitionSegment hardMergeSegment(HAPEntityDefinitionSegment entityDefSegment){
		HAPEntityDefinitionSegment out = this.clone(HAPEntityDefinitionSegment.class);
		
		Set<String> attrs = HAPStringableValueUtility.getExpectedAttributesInEntity(HAPEntityDefinitionSegment.class);
		attrs.remove(HAPEntityDefinitionSegment.ATTRIBUTES);
		
		out.hardMergeWith(entityDefSegment, attrs);
		
		Set<String> attrNames = entityDefSegment.getAttributeNames();
		for(String attrName : attrNames){
			out.addAttributeDefinition(entityDefSegment.getAttributeDefinitionByName(attrName).cloneDefinition(out));
		}
		return out;
	}
	
	/******************************************   Loader  *********************************************/
	/*
	 * method to be called after entity definition is loaded
	 */
	public void afterLoad(){}
}
