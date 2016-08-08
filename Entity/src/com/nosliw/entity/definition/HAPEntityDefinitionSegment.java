package com.nosliw.entity.definition;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPStringableJson;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntityBasic;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.entity.utils.HAPAttributeConstant;

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
public class HAPEntityDefinitionSegment extends HAPStringableJson{
	//store all the document related information, for instance, name, description
	private HAPStringableValueEntityBasic m_entityDescription;
	
	//groups this entity belong to
	protected Set<String> m_groups;
	//java class name for entity object
	protected String m_baseClassName;

	//entity attribute information
	protected Map<String, HAPAttributeDefinition> m_attributeDefs; 
	
	private HAPEntityDefinitionManager m_entityDefinitionMan;
	
	public HAPEntityDefinitionSegment(String name, String baseClassName, Set<String> groups, HAPEntityDefinitionManager entityDefinitionMan){
		this.m_attributeDefs = new LinkedHashMap<String, HAPAttributeDefinition>();
		this.m_entityDescription = new HAPStringableValueEntityBasic("", name);
		this.m_groups = new HashSet<String>();
		if(groups!=null)		this.m_groups.addAll(groups);

		if(!HAPBasicUtility.isStringEmpty(baseClassName))	this.m_baseClassName = baseClassName;
		else  this.m_baseClassName=this.getEntityDefinitionManager().getDefaultClassName(); 

		this.m_entityDefinitionMan = entityDefinitionMan;
	}	
	
	protected HAPEntityDefinitionSegment(HAPEntityDefinitionManager entityDefinitionMan){
		this(null, null, null, entityDefinitionMan);
	}

	public HAPEntityDefinitionSegment(HAPEntityDefinitionSegment def){
		this(def.getEntityDefinitionManager());
		this.cloneFrom(def);
	}
	
	
	/******************************************   Serialization  *********************************************/
	
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap){
		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		for(String attrName : this.getAttributeNames()){
			HAPAttributeDefinition attrDef = this.getAttributeDefinitionByPath(attrName);
			attrJsonMap.put(attrName, attrDef.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
		}
		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYDEFINITION_ATTRIBUTES, HAPJsonUtility.getMapJson(attrJsonMap));
		
		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYDEFINITION_ENTITYNAME, this.getEntityName());
		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYDEFINITION_GROUPS, HAPJsonUtility.getSetObjectJson(m_groups));
		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYDEFINITION_BASECLASS, this.getBaseClassName());
	}
	
	/******************************************   Attribute  *********************************************/
	/*
	 * get entity's base attributes names, not including those come with the subclass attribute
	 */
	public Set<String> getAttributeNames() {return this.m_attributeDefs.keySet();}

	/*
	 * get entity's base attribute definitions info, not including those come with the subclass attribute
	 */
	public Map<String, HAPAttributeDefinition> getAttributeDefinitions() {	return this.m_attributeDefs;}
	
	protected void copyAttributeDefinition(HAPAttributeDefinition defin){
		HAPAttributeDefinition def1 = defin.cloneDefinition(this);
		this.m_attributeDefs.put(def1.getName(), def1);
	}

	protected void copyAttributeDefinitions(Set<HAPAttributeDefinition> defins) {
		for(HAPAttributeDefinition def: defins)		this.copyAttributeDefinition(def);
	}

	public void addAttributeDefinition(HAPAttributeDefinition defin){	this.m_attributeDefs.put(defin.getName(), defin);	}
	
	public void removeAttributeDefinition(String attrName) {this.m_attributeDefs.remove(attrName);	}
	
	public HAPAttributeDefinition getAttributeDefinitionByName(String attrName){return this.m_attributeDefs.get(attrName);}
	
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
	public HAPStringableValueEntityBasic getEntityDescription(){ return this.m_entityDescription; }
	
	public String getEntityName(){return this.getEntityDescription().getName();}
	
	public Set<String> getGroups() {	return this.m_groups;	}

	/* get defined class name for this entity
	 * if not defined, then use method getDefaultClassName in EntityDefinitionManager instead 
	 */
	public String getBaseClassName() {	return this.m_baseClassName;	}

	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefinitionMan;}
	
	/******************************************   Clone  *********************************************/
	public HAPEntityDefinitionSegment cloneDefinition(){
		HAPEntityDefinitionSegment out = new HAPEntityDefinitionSegment(this.getEntityDefinitionManager());
		out.cloneFrom(this);
		return out;
	}

	protected void cloneFrom(HAPEntityDefinitionSegment entityDef){
		this.m_entityDefinitionMan = entityDef.m_entityDefinitionMan;
		this.m_entityDescription.cloneFrom(entityDef.getEntityDescription());
		this.getEntityDescription().setName(entityDef.getEntityDescription().getName());
		this.m_groups.addAll(entityDef.m_groups);
		this.m_baseClassName = entityDef.getBaseClassName();
		this.m_attributeDefs.putAll(entityDef.m_attributeDefs);
	}

	public HAPEntityDefinitionSegment hardMerge(HAPEntityDefinitionSegment entityDefSegment){
		HAPEntityDefinitionSegment entityDef = this.cloneDefinition(); 
		entityDef.m_attributeDefs.putAll(entityDefSegment.getAttributeDefinitions());
		return entityDef;
	}
	
	/******************************************   Loader  *********************************************/
	/*
	 * method to be called after entity definition is loaded
	 */
	public void afterLoad(){}
}
