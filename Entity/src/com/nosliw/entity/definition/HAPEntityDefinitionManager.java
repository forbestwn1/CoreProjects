package com.nosliw.entity.definition;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.data.HAPEntity;
import com.nosliw.entity.definition.xmlimp.HAPEntityDefinitionImporter;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.utils.HAPEntityNamingConversion;

/*
 * this is manager class that manage : 
 * 		entity definition
 * 		attribute definition
 * the actual data loading job is done by loader object 
 * in order to add new entity definition to EntityDefintionManager
 * 		create entity loader object
 * 		add entty loader object to manager
 */

public class HAPEntityDefinitionManager extends HAPConfigurableImp implements HAPSerializable{

	//loader name  --- entity definition loader
	private Map<String, HAPEntityDefinitionLoader> m_entityLoaders = null;    
	//entity name  ----  entity definition
	private Map<String, HAPEntityDefinitionCritical> m_entityDefinitions = null;  

	//group --- entitys
	private Map<String, Set<HAPEntityDefinitionCritical>> m_groupTypes = null;  
	//entity name ---- data type
	//this attribute is for convinience and performance
	//the same information is stored in DataTypeManager
	private Map<String, HAPDataType> m_entityDataTypes;  
	
	private HAPDataTypeManager m_dataTypeMan;
	private HAPOptionsDefinitionManager m_optionsMan;
	private HAPEntityDefinitionImporter m_entityDefImporter;
	
	
	public HAPEntityDefinitionManager(HAPConfigureImp configuration, HAPDataTypeManager dataTypeMan, HAPOptionsDefinitionManager optionsMan){
		super("entitydefintion.properties", configuration);
		this.m_dataTypeMan = dataTypeMan;
		this.m_entityDefImporter = new HAPEntityDefinitionImporter();
		this.init();
	}

	public void init(){
		this.loadValueInfos();
		
		this.m_entityDefinitions = new LinkedHashMap<String, HAPEntityDefinitionCritical>();
		this.m_entityLoaders = new LinkedHashMap<String, HAPEntityDefinitionLoader>();
		this.m_attributeDefinitions = new LinkedHashMap<String, HAPAttributeDefinition>();
		this.m_attributeLoaders = new LinkedHashMap<String, HAPAttributeDefinitionLoader>();
		this.m_groupTypes = new LinkedHashMap<String, Set<HAPEntityDefinitionCritical>>();
		this.m_entityDataTypes = new LinkedHashMap<String, HAPDataType>();
	}
	
	private void loadValueInfos(){
		
	}
	
	public void load(){
		for(String name : this.m_entityLoaders.keySet()){
			HAPEntityDefinitionLoader loader = this.m_entityLoaders.get(name);
			Set<HAPEntityDefinitionCritical> defs = loader.loadEntityDefinition();
		}
		this.afterLoad();
	}
	
	protected void afterLoad(){}

	
	//*************************** group  ****************************
	/*
	 * get all Groups according to entity type name
	 */
	public Set<String> getEntityGroupsByEntityName(String name){return this.getEntityDefinition(name).getGroups();}
	
	/*
	 * get all entity definition under group
	 */
	public HAPEntityDefinitionCritical[] getEntityDefinitionsByGroup(String group){
		return this.m_groupTypes.get(group).toArray(new HAPEntityDefinitionCritical[0]);
	}

	/*
	 * get all entity names under group
	 */
	public Set<String> getEntityNamesByGroup(String group){
		Set<String> out = new HashSet<String>();
		for(HAPEntityDefinitionCritical def : this.getEntityDefinitionsByGroup(group)){
			out.add(def.getEntityName());
		}
		return out;
	}

	/*
	 * get all possible Entity name by description
	 * input: either entity name or group name (@XXXX)
	 */
	public Set<String> getCandidateEntityNames(String nameDesp){
		Set<String> out = new HashSet<String>();
		String group = HAPEntityNamingConversion.getGroupName(nameDesp);
		if(group==null){
			//entity
			out.add(nameDesp);
		}
		else{
			//group
			return this.getEntityNamesByGroup(group);
		}
		return out;
	}
	
	/*
	 * get all possible entity names according to data type definition information
	 */
	public Set<String> getCandidateEntityNames(HAPDataTypeDefInfo dataTypeInfo){
		String type = dataTypeInfo.getType();
		Set<String> groups = dataTypeInfo.getEntityGroups();
		
		Set<String> out = new HashSet<String>();
		if(groups!=null && groups.size()>0){
			for(String group : groups){
				out.addAll(this.getEntityNamesByGroup(group));
			}
		}
		else{
			out.add(type);
		}
		return out;
	}
	
	
	//*************************** Loader  ****************************
	/*
	 * method to add entity definition loader, 
	 * it also load the entity definition into manager class
	 * return : all the entity definition loaded 
	 */
	public Set<HAPEntityDefinitionCritical> addEntityDefinitionLoader(HAPEntityDefinitionLoader loader)
	{
		this.m_entityLoaders.put(loader.getName(), loader);
		Set<HAPEntityDefinitionCritical> defs = loader.loadEntityDefinition();
		for(HAPEntityDefinitionCritical def : defs){
			this.addEntityDefinition(def);
		}
		return defs;
	}

	
	//*************************** method for entity / attribute management  ****************************
	/*
	 * get entity definition information by entity name
	 */
	public HAPEntityDefinitionCritical getEntityDefinition(String name){	return this.m_entityDefinitions.get(name);	}

	/*
	 * get all entity names
	 */
	public Set<String> getAllEntityDefinitionName(){	return this.m_entityDefinitions.keySet();}

	public Map<String, HAPEntityDefinitionCritical> getAllEntityDefinitions(){	return this.m_entityDefinitions;}
	
	
	/*
	 * add entity definition
	 */
	public void addEntityDefinition(HAPEntityDefinitionCritical def){
		//add name---entity def
		this.m_entityDefinitions.put(def.getEntityName(), def);
		
		//add group --- entitys
		Set<String> entityGroups = def.getGroups();
		for(String entityGroup : entityGroups){
			Set<HAPEntityDefinitionCritical> types = this.m_groupTypes.get(entityGroup);
			if(types==null){
				types = new HashSet<HAPEntityDefinitionCritical>();
				this.m_groupTypes.put(entityGroup, types);
			}
			types.add(def);
		}
		
		//add entity data type
		HAPEntity entityDataType = new HAPEntity(def.getEntityName(), this.getDataTypeManager(), this, this.getOptonsManager());
		//register new entity data type
		this.getDataTypeManager().registerDataType(entityDataType);
		//save the new entity data type
		this.m_entityDataTypes.put(def.getEntityName(), entityDataType);

	}
	
	@Override
	public String toStringValue(String format){
		if(format.equals(HAPSerializationFormat.JSON)){
			Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
			for(String name : this.getAllEntityDefinitionName()){
				outJsonMap.put(name, this.getEntityDefinition(name).toStringValue(format));
			}
			return HAPJsonUtility.buildMapJson(outJsonMap);
		}
		return null;
	}
	
	@Override
	public String toString(){
		StringBuffer out = new StringBuffer();
		
		out.append("\n\n\n**************************     EntityDefinitionManager  Start   *****************************\n");
		out.append(HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON)));
		out.append("\n**************************     EntityDefinitionManager  End   *****************************\n\n\n");
		
		return out.toString();
	}
	
	/*
	 * return the default class name
	 * this value is used when no class name is defined for entity
	 */
	public String getDefaultClassName(){	return "com.nosliw.entity.data.HAPEntityData";	}
	
	/*
	 * get entity data type object according to entity name
	 */
	public HAPEntity getEntityDataTypeByName(String name){
		return (HAPEntity)this.m_entityDataTypes.get(name);
	}
	
	
	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
	protected HAPOptionsDefinitionManager getOptonsManager(){return this.m_optionsMan;}
	
	
	//*************************** Attribute  ****************************
	//entity attribute definition loader
	private Map<String, HAPAttributeDefinitionLoader> m_attributeLoaders = null;
	//entity attribute definition
	private Map<String, HAPAttributeDefinition> m_attributeDefinitions = null;	
	
	/*
	 * method to add attribute definition loader, 
	 * it also load the attribute definition into manager class
	 * return : all the attribute definitions loaded 
	 */
	public Set<HAPAttributeDefinition> addEntityAttributeDefinitionLoader(HAPAttributeDefinitionLoader loader){
		this.m_attributeLoaders.put(loader.getName(), loader);
		Set<HAPAttributeDefinition> defs = loader.loadEntityAttributeDefinition();
		for(HAPAttributeDefinition def : defs){
			this.addAttributeDefinition(def);
		}
		return defs;
	}

	/*
	 * add entity attribute definition
	 */
	public void addAttributeDefinition(HAPAttributeDefinition def){
		this.m_attributeDefinitions.put(def.getName(), def);
	}
	
	/*
	 * get entity attribute definition information by attribute name
	 */
	public HAPAttributeDefinition getAttributeDefinition(String name){
		return this.m_attributeDefinitions.get(name);
	}
	
	/*
	 * get all attribute names
	 */
	public String[] getAllAttributeDefinitionName(){
		return this.m_attributeDefinitions.keySet().toArray(new String[0]);
	}
	
	public HAPAttributeDefinition parseAttributeDefinitionJson(JSONObject jsonObj){
		return null;
	}
	
	public HAPEntityDefinitionCritical parseEntityDefinitionJson(JSONObject jsonObj){
		return null;
	}
	
}
