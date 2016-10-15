package com.nosliw.entity.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data.imp.HAPDataImp;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;
import com.nosliw.entity.definition.HAPEntityDefinitionSegment;
import com.nosliw.entity.event.HAPEvent;
import com.nosliw.entity.operation.HAPEntityOperationFactory;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.entity.utils.HAPEntityDataUtility;
import com.nosliw.entity.utils.HAPEntityUtility;

/*
 * entity has three states:
 * 		init:  just created, in the process of initiazation
 * 		transient : 
 * 				once entity is initialize, entity enter detached status
 * 				each attribute must have validate value according to rule
 * 		persistent : 
 * 				entity is saved to database
 * 				entity has id
 * 				entity must be validated
 * 		dirty:
 * 				once persistent entity is update, and have not save to database, then into dirty status
 * 
 * role of entity
 * 		have a collection of attribute 
 * 		is the event listener of all attribute 
 * 		
 * in term of event:
 * 		once entity get event from attribute, 
 * 		the onEvent in wraper is called. 
 * 		then decided if the event should be propogate or not
 * 
 * ID : each entity has unique ID in the range of App
 * 		the ID will not be changed once entity is persistened
 * 		in the type of String
 * 		entity loader take charge of assign ID to entity and get entity based on ID
 * relationship between entity:
 * 		owner: 
 * 			one entity own another entity, 
 * 			means that when parent entity is deleted, child will be deleted
 * 		reference : 
 * 			parent entity have referenece of another entity, 
 * 			when parent is deleted, child still alive
 * 			when the real entity is deleted, then parent entity will be informed
 * attribute : 
 * 		two type of attribute: normal, internal
 * 		normal attribute: 
 * 			the attribute defined in entity definition
 * 			the attribute will be persistened 
 * 		internal attribute: 
 * 			the attribute not defined in entity definition
 * 			the attribute is calculated out after the entity is created
 * 			for instance, data definition path
 * 			internal attribute is simple, just parm-value pair, not validation, no notification				
 */
public class HAPEntityData extends HAPDataImp
{
	private HAPEntityWraper m_wraper;						//wraper for entity
	private HAPEntityDefinitionSegment m_entityInfo;   		//entity information for this complex entity
	
	private Map<String, String> m_internalAttributes;			//attribute values which is work out after entity in inited
	private Map<String, HAPDataWraper> m_attributes = null;	//attribute -- ATTRIBUTE and value wraper
	
	protected HAPEntityData(){
		super(null);
	}
	
	public HAPEntityData(HAPDataType dataType, HAPEntityDefinitionSegment entityInfo){
		super(dataType);
		this.m_entityInfo = entityInfo;
		this.m_attributes = new LinkedHashMap<String, HAPDataWraper>();
		this.m_internalAttributes = new LinkedHashMap<String, String>();
	} 
	
	
	//add costomized initialization code after attributes are created / reset
	protected void initialize(){}

	public List<HAPEntityOperationInfo> init(Map<String, String> parms){
		List<HAPEntityOperationInfo> out = new ArrayList<HAPEntityOperationInfo>();
		//set parms value to attribute
		for(String attr : parms.keySet()){
			HAPEntityOperationInfo opInfo = null;
			String value = parms.get(attr);
			HAPAttributeDefinition attrDef = this.getAttributeDefinition(attr);
			HAPDataTypeDefInfo attrDataTypeInfo = attrDef.getDataTypeDefinitionInfo();
			String categary = attrDataTypeInfo.getCategary();
			if(HAPEntityDataTypeUtility.isAtomType(attrDataTypeInfo)){
				opInfo = HAPEntityOperationFactory.createAttributeAtomSetOperationByString(this.getWraper().getID(), attr, value);
			}
			else if(HAPEntityDataTypeUtility.isReferenceType(attrDataTypeInfo)){
				opInfo = HAPEntityOperationFactory.createAttributeReferenceSetOperation(this.getWraper().getID(), attr, new HAPEntityID(value));
			}
			out.add(opInfo);
		}
		return out;
	}
	
	
	
	/*
	 *  clear up entity itself
	 */
	@Override
	protected void doClearUp(Map<String, Object> parms){
		final int scope = HAPEntityUtility.getScopeFromClearupParms(parms);
		
		HAPEntityDataUtility.iterateEntityAttributes(this, null, new HAPDataWraperTask(){
			@Override
			public HAPServiceData process(HAPDataWraper wraper, Object data) {
				wraper.clearUp(scope);
				return HAPServiceData.createSuccessData(data);
			}

			@Override
			public Object afterProcess(HAPDataWraper wraper, Object data) {
				return data;
			}
		});
		
		this.m_attributes.clear();
		this.m_wraper = null;
	}
	
	
	//**************************  attribute value
	/*
	 * get value of a attribute
	 */
	public HAPData getAttributeValue(String attrName){
		return this.getAttributeValueWraper(attrName).getData();
	}

	public HAPAttributeDefinition getAttributeDefinition(String attrName){
		return this.getEntityInfo().getAttributeDefinitionByPath(attrName);
	}
	
	/*
	 * get value wraper of a attribute
	 */
	public HAPDataWraper getAttributeValueWraper(String attrName){
		if(HAPBasicUtility.isStringEmpty(attrName))  return this.getWraper();
		else 	return this.m_attributes.get(attrName);
	}
	
	public HAPEntityData getAttributeEntityValue(String attrName){
		HAPEntityWraper wraper = (HAPEntityWraper)this.m_attributes.get(attrName);
		return wraper.getEntityData();
	}
	
	public HAPDataWraper getChildPathWraper(String attributeList){
		int index = attributeList.indexOf(HAPConstant.SEPERATOR_PATH);
		if(index == -1){
			return this.getAttributeValueWraper(attributeList);
		}
		else{
			String attr = attributeList.substring(0, index);
			String rest = attributeList.substring(index+1, attributeList.length());
			HAPDataWraper wraper = this.getAttributeValueWraper(attr);
			return wraper.getChildWraperByPath(rest);
		}
	}
	
	/*
	 * add an attribute to entity
	 * 		set up parent-attribute relationship
	 * 		set listener 
	 */
	void addAttributeValue(String attrName, HAPDataWraper value)
	{
		value.setParentEntity(this, attrName, null);
		this.m_attributes.put(attrName, value);
	}

	/*
	 * clear up attribute from entity
	 * clear up attribute wraper
	 */
	public void removeAttribute(String attr, int scope){
		HAPDataWraper wraper = this.m_attributes.remove(attr);
		wraper.clearUp(scope);
	}

	//***************************   Validation
	/*
	 * validate if the entity as a whole is valid
	 */
	public HAPServiceData validate(){
		return HAPServiceData.createSuccessData();
	}

	
	//***************************   Clone
	
	@Override
	//not clone wraper info
	//not clone listener info
	public HAPEntityData cloneData()
	{
		HAPEntityData entity = new HAPEntityData(this.getDataType(), this.getEntityInfo()); 
		
		Iterator<String> attrs = this.m_attributes.keySet().iterator();
		while(attrs.hasNext()){
			String attr = attrs.next();
			HAPDataWraper subEn = this.m_attributes.get(attr);
			entity.addAttributeValue(attr, (HAPDataWraper)subEn.cloneWraper());
		}
		
		for(String attr : this.m_internalAttributes.keySet()){
			entity.setInternalAttribute(attr, this.m_internalAttributes.get(attr));
		}
		return entity;
	}
	
	
	//***************************   Event
	/*
	 * let wraper handle the different envent
	 * 
	 */
	public HAPServiceData onEvent(HAPEvent event) {
		return this.getWraper().onEvent(event);
	}
	
	//handle event from attribute
	//return true: forward to parent
	//       false: not forward
	protected HAPServiceData handleEvent(HAPEvent event){
		return HAPServiceData.createSuccessData();
	}
	
	//**************************  Basic Info
	public HAPEntityWraper getWraper(){return this.m_wraper;}
	void setWraper(HAPEntityWraper wraper){this.m_wraper=wraper;}

	void setEntityInfo(HAPEntityDefinitionSegment info){this.m_entityInfo=(HAPEntityDefinitionCritical)info.cloneDefinition();}
	public HAPEntityDefinitionSegment getEntityInfo(){return this.m_entityInfo;}

	public String[] getAttributes(){return this.m_attributes.keySet().toArray(new String[0]);}
	public Map<String, HAPAttributeDefinition> getAttributeDefinitions(){return this.getEntityInfo().getAttributeDefinitions();} 
	
	public String getEntityName(){return this.getEntityInfo().getEntityName();}
	public Set<String> getGroups(){return this.getEntityInfo().getGroups();}
	
	public String getInternalAttribute(String attr){return (String)this.m_internalAttributes.get(attr);}
	public void setInternalAttribute(String attr, String value){this.m_internalAttributes.put(attr, value);}
	
	//***************************   Parse
	@Override
	public String toDataStringValue(String format) {
		if(format.equals(HAPSerializationFormat.JSON)){
			Map<String, String> jsonMap = new LinkedHashMap<String, String>();
			for(String attr : this.getAttributes()){
				HAPDataWraper attrValue = this.getAttributeValueWraper(attr);
				jsonMap.put(attr, attrValue.toStringValue(HAPSerializationFormat.JSON));
			}

			for(String attr : this.m_internalAttributes.keySet()){
				jsonMap.put(attr, this.m_internalAttributes.get(attr));
			}
			return HAPJsonUtility.buildMapJson(jsonMap);
		}
		return null;
	}

	//***************************   Environment
	
	
	
	//	public void setOptions(String attrName, String[] options)
//	{
//		HAPAtomAttributeDefinition attrDef = (HAPAtomAttributeDefinition)this.getEntityInfo().getAttributeDefinition(attrName);
//		attrDef.setOptions(options);
//	}
//	public void setRuleConfigure(String attrName, HAPRuleConfigure ruleConfig)
//	{
//		HAPAttributeDefinition attrDef = this.getEntityInfo().getAttributeDefinition(attrName);
//		attrDef.setRuleConfigure(ruleConfig);
//	}
	
//	public String[] getAttributeOptions(String attr)
//	{
//		String[] out = null;
//		
//		HAPAttributeDefinition attrDef = this.getEntityInfo().getAttributeDefinition(attr);
//		if(HAPUtility.isAtomType(attrDef.getType()))
//		{
//			HAPAtomAttributeDefinition def = (HAPAtomAttributeDefinition)attrDef;
//			String[] ops = def.getOptions();
//			String optionClass = def.getOptionClass();
//			
//			if(ops!=null && ops.length>0)  return ops;
//			if(optionClass==null || optionClass.isEmpty())  return null;
//			
//			try {
//				HAPAttributeOptions optionObj = (HAPAttributeOptions) Class.forName(optionClass).newInstance();
//				return optionObj.getOptions(this, attr);
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//				return null;
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//				return null;
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//				return null;
//			}
//		}
//		return out;
//	}
	
	
}
