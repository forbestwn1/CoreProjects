package com.nosliw.entity.data;

import java.lang.reflect.Constructor;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.data1.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeInfoWithVersion;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPAttributeDefinitionAtom;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;
import com.nosliw.entity.definition.HAPEntityDefinitionSegment;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;

public class HAPEntity extends HAPDataTypeImp{

	private HAPDataTypeManager m_dataTypeMan;
	private HAPOptionsDefinitionManager m_optionsMan;
	private HAPEntityDefinitionManager m_entityDefinitionMan;
	
	public HAPEntity(String name, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan) {
		super(new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_ENTITY, name),  null, null, null, "", dataTypeMan);
		this.m_dataTypeMan = dataTypeMan;
		this.m_entityDefinitionMan = entityDefMan;
		this.m_optionsMan = optionsMan;
	}

	@Override
	public HAPData getDefaultData() {
		return this.newEntity(this.getDataTypeInfo().getType());
	}

	@Override
	public HAPData toData(Object value, String format) {
		HAPData out = null;
		return out;
	}
	
	
	@Override
	public HAPServiceData validate(HAPData data) {
		return HAPServiceData.createSuccessData();
	}

	/*
	 * create a brand new Complex Entity, based on 1. entity type,  2, critical value
	 * 1. get entity definition based on critical value
	 * 2. create entity object based on entity definition
	 * 3. set entity definition to entity object
	 * 4. create attributes in the entity (attribute wraper + listener)
	 * 5. add critical attribute to entity
	 * 6. initialzation work after all attribute is created
	 * type:  complex entity type
	 * criticalValue:  critical attribute value if apliable, null if not specified
	 * return null if type not exists 
	 */
	public HAPEntityData newEntity(){return this.newEntity(null);}

	public HAPEntityData newEntity(String criticalValue){
		String type = this.getDataTypeInfo().getType();
		HAPEntityData entity = this.newEntityInstance(type, criticalValue);
		
		//fill out the attributes of complex entity
		Map<String, HAPAttributeDefinition> attrDefs = entity.getAttributeDefinitions();
		for(String attrName: attrDefs.keySet()){
			HAPAttributeDefinition attrDef = attrDefs.get(attrName);
			this.newAttributeWraper(attrDef, entity);
		}
		//after all the attributes are created, do something ---- consistency check
		entity.initialize();
		return entity;
	}
	
	/*
	 * create empty entity instance
	 * all the attribute of the entity is empty
	 * this method is used when loading data from storage
	 */
	public HAPEntityData newEmptyEntity(){return this.newEmptyEntity(null);}

	public HAPEntityData newEmptyEntity(String criticalValue){
		String type = this.getDataTypeInfo().getType();
		HAPEntityData entity = this.newEntityInstance(type, criticalValue);

		Map<String, HAPAttributeDefinition> attrDefs = entity.getAttributeDefinitions();
		for(String attrName: attrDefs.keySet()){
			HAPAttributeDefinition attrDef = attrDefs.get(attrName);
			this.newEmptyAttribute(attrDef, entity);
		}
		return entity;
	}

	public HAPDataWraper newAttributeWraper(HAPAttributeDefinition attrDef, HAPEntityData parent){
		HAPDataWraper out = newEmptyAttribute(attrDef, parent);
		out.initAttributeData();
		return out;
	}
	
	private HAPDataWraper newEmptyAttribute(HAPAttributeDefinition attrDef, HAPEntityData parent){
		String attribute = attrDef.getName();
		HAPDataTypeDefInfo type = attrDef.getDataTypeDefinitionInfo();
		
		HAPDataWraper wraper = this.createAttributeDataWraper(attrDef); 
		if(parent!=null){
			setAttribute(wraper, parent, attribute);
		}
		return wraper;
	}

	private HAPDataWraper createAttributeDataWraper(HAPAttributeDefinition attrDef){
		HAPDataWraper out = null;
		HAPDataTypeDefInfo dataTypeDefInfo = attrDef.getDataTypeDefinitionInfo();
		String categary = dataTypeDefInfo.getCategary();
		switch(categary){
		case HAPConstant.DATATYPE_CATEGARY_BLOCK:
		case HAPConstant.DATATYPE_CATEGARY_SIMPLE:
		{
			out = new HAPAtomWraper(dataTypeDefInfo, this.getDataTypeManager(), this.getEntityDefinitionManager());
			break;
		}
		case HAPConstant.DATATYPE_CATEGARY_CONTAINER:
		{
			
			break;
		}
		case HAPConstant.DATATYPE_CATEGARY_REFERENCE:
		{
			
			break;
		}
		case HAPConstant.DATATYPE_CATEGARY_ENTITY:
		{
			
			break;
		}
		
		}
		
		return out;
	}
	
	/*
	 * build parent, child relationship
	 */
	private HAPDataWraper setAttribute(HAPDataWraper attWraper, HAPEntityData parent, String attribute){
		if(attWraper != null){
			parent.addAttributeValue(attribute, attWraper);
		}
		return attWraper;
	}
	
	private HAPEntityData newEntityInstance(String type, String criticalValue){
		HAPEntityDefinitionCritical entityDef1 = this.getEntityDefinitionManager().getEntityDefinition(type);
		if(entityDef1==null)  return null;

		//get entityDef based on critical value
		HAPEntityDefinition entityDef = null;
		HAPAttributeDefinitionAtom criticalAttrDef = entityDef1.getCriticalAttributeDefinition();
		if(criticalAttrDef!=null){
			entityDef = entityDef1.getEntityDefinitionByCriticalValue(criticalValue);
		}
		else{
			entityDef = new HAPEntityDefinition(entityDef1, null, this.getEntityDefinitionManager());
		}

		//create complex entity object
		String className = entityDef.getBaseClassName();
		if(className==null)  className = this.getEntityDefinitionManager().getDefaultClassName();
		HAPEntityData entity = null;
		try {
			Class[] conInputs = {HAPDataType.class, HAPEntityDefinitionSegment.class}; 
			Constructor con = Class.forName(className).getConstructor(conInputs);
			entity = (HAPEntityData) con.newInstance(this.getEntityDefinitionManager().getEntityDataTypeByName(type), entityDef1);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		
		//set entity definition for this entity
		//this entity def may not from entity definition from entity defintion manager
		entity.setEntityInfo(entityDef);
		return entity;
	}
	
	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefinitionMan;}
	protected HAPOptionsDefinitionManager getOptionsManager(){return this.m_optionsMan;}
}
