package com.nosliw.entity.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceDataException;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.expression.utils.HAPExpressionUtility;
import com.nosliw.data.expression1.HAPExpression;
import com.nosliw.data.expression1.HAPExpressionInfo;
import com.nosliw.data1.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.sort.HAPSortingInfo;
import com.nosliw.entity.sort.HAPSortingQueryAttribute;
import com.nosliw.entity.utils.HAPAttributeConstant;

/*
 * class store all the information about defining a query
 * 		query name 
 * 			unique name in application
 * 			it is also used for finding physical implemenation of query
 * 		query expression
 * 		a set of named entity
 * 		projected attribute
 * 		sorted by attribute / expression /
 * 
 * attribute for sorting purpose are added to projection attribute automatically 
 *  
 * therefore, each changes on one entity related with either major entity type or secondary entity type may affect the result of query 
 */
public class HAPQueryDefinition implements HAPSerializable{
	//name for this query definition
	private String m_name;
	
	//all entitys query on   (parm name  ---  entity type) 
	private Map<String, String> m_entitys;
	//all entity groups to query on  (parm name --- group name)
	private Map<String, String> m_groups;

	//expression for this query
	private HAPExpression m_expression;
	//all the entity attributes returns from query (entity name & entity path)
	private Set<HAPQueryProjectAttribute> m_entityAttributes;
	//sorting configures
	private List<HAPSortingInfo> m_sortingInfos;
	
	//the object to execute the query
	private HAPQueryExecutor m_executor;
	
	private HAPDataTypeManager m_dataTypeMan;
	private HAPEntityDefinitionManager m_entityDefMan;
	
	//calculated info: whether the validation of this query require multiple entitys
	private boolean m_mutipleEntityQuery = false;
	private boolean m_validMutipleEntityQuery = false;
	
	//calculated infor: store all valid entity types (parm name --- all entity types)
	private Map<String, Set<String>> m_allValideEntityTypesByParms;
	private Set<String> m_allValideEntityTypes;
	
	//calculated info: store all the criteria entity attribute
	private Set<HAPQueryProjectAttribute> m_queryAttributes;
	
	public HAPQueryDefinition(String name, HAPExpressionInfo expression, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		this.m_name = name;

		this.m_entitys = new LinkedHashMap<String, String>();
		this.m_groups = new LinkedHashMap<String, String>();
		
		this.m_sortingInfos = new ArrayList<HAPSortingInfo>();

		this.m_entityDefMan = entityDefMan;
		this.m_dataTypeMan = dataTypeMan;
		
		this.m_expression = new HAPExpression(expression, dataTypeMan);
		
		//set query attributes info from expression
		this.m_queryAttributes = new HashSet<HAPQueryProjectAttribute>();
		for(String attr : this.m_expression.getAllPathVariables()){
			this.m_queryAttributes.add(new HAPQueryProjectAttribute(attr));
		}
		
		this.m_allValideEntityTypesByParms = new LinkedHashMap<String, Set<String>>();
		this.m_allValideEntityTypes = new HashSet<String>();
	}

	/*
	 * whether the validation of this query require multiple entitys
	 */
	public boolean isMultipleEntityQuery(){
		if(!this.m_validMutipleEntityQuery){
			//if no valid data, get it
			int size = m_entitys.size() + this.m_groups.size();
			if(size>1)  this.m_mutipleEntityQuery = true;
			else{
				for(HAPQueryProjectAttribute attrInfo : this.m_queryAttributes){
					Set<String> entityTypes = m_allValideEntityTypesByParms.get(attrInfo.entityName);
					for(String entityType : entityTypes){
						HAPEntityDefinitionCritical entitDef = this.getEntityDefinitionManager().getEntityDefinition(entityType);
						
					}
				}
			}
			
			this.m_validMutipleEntityQuery = true;
		}
		return this.m_mutipleEntityQuery;
	}
	
	/*
	 * check if an entity is valid data for parm 
	 */
	public boolean isRelatedEntity(String parm, HAPEntityWraper entity){
		Set<String> entityTypes = m_allValideEntityTypesByParms.get(parm);
		if(entityTypes!=null && entityTypes.contains(entity.getEntityType()))   return true;
		else return false;
	}

	/*
	 * check if an entity is valid data for any parm 
	 */
	public boolean isRelatedEntity(HAPEntityWraper entity){
		return this.m_allValideEntityTypes.contains(entity.getEntityType());
	}
	
	/*
	 * check if an entity parms is valid for query 
	 */
	public boolean isValidEntity(Map<String, HAPEntityWraper> entityWrapers, Map<String, HAPData> parms) {
		//check if parms are valid & prepare wrapper parms 
		Map<String, HAPWraper> wraperParms = new LinkedHashMap<String, HAPWraper>();
		for(String parm : entityWrapers.keySet()){
			HAPEntityWraper entityWraper = entityWrapers.get(parm);
			if(!this.isRelatedEntity(parm, entityWraper))  return false;
			wraperParms.put(parm, entityWraper);
		}
		
		//if no expression provided, then valid all entity under valid entity type
		if(this.m_expression==null)  return true;
		
		try {
			//run expression 
			return HAPExpressionUtility.executeValidationExpression(this.m_expression, parms, wraperParms);
		} catch (HAPServiceDataException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * in addition to add sorting info, also add sorting attribute to query project attribute
	 */
	public void addSortingInfo(HAPSortingInfo sortingInfo){
		this.m_sortingInfos.add(sortingInfo);
		if(HAPConstant.SORTING_TYPE_ATTRIBUTE==sortingInfo.getType()){
			HAPSortingQueryAttribute qSortingInfo = (HAPSortingQueryAttribute)sortingInfo;
			this.m_entityAttributes.add(new HAPQueryProjectAttribute(qSortingInfo.getEntityName(), qSortingInfo.getAttribute()));
		}
	}
	public List<HAPSortingInfo> getSortingInfos(){return this.m_sortingInfos;}
	
	public String getName(){return this.m_name;}
	
	public void addDataTypeDefInfo(String name, HAPDataTypeDefInfo info){
		if(info.getEntityGroups().size()>0){
			for(String group : info.getEntityGroups()){
				this.m_groups.put(name, group);
				Set<String> entitys = this.getEntityDefinitionManager().getEntityNamesByGroup(group);
				for(String entity : entitys){
					this.addEntity(name, entity);
				}
			}
		}
		else{
			this.m_entitys.put(name, info.getType());
		}
	}
	
	private void addEntity(String name, String entity){
		this.m_allValideEntityTypes.add(entity);
		Set<String> entitys = this.m_allValideEntityTypesByParms.get(name);
		if(entitys==null){
			entitys = new HashSet<String>();
			this.m_allValideEntityTypesByParms.put(name, entitys);
		}
		entitys.add(entity);
	}
	
	public HAPExpression getExpression(){return this.m_expression;}
	
	public Set<HAPQueryProjectAttribute> getEntityAttributes(){return this.m_entityAttributes;}
	public void addEntityAttribute(String entityName, String attr){this.m_entityAttributes.add(new HAPQueryProjectAttribute(entityName, attr));}
	
	public HAPQueryExecutor getExecutor(){return this.m_executor; }
	public void setExecutor(HAPQueryExecutor executor){ this.m_executor = executor;}
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(HAPAttributeConstant.QUERYDEFINITION_NAME, this.m_name);
		jsonMap.put(HAPAttributeConstant.QUERYDEFINITION_EXPRESSIONINFO, this.getExpression().toStringValue(format));

		jsonMap.put(HAPAttributeConstant.QUERYDEFINITION_PROJECTATTRIBUTES, HAPJsonUtility.getSetObjectJson(this.m_queryAttributes));

		return HAPJsonUtility.buildMapJson(jsonMap);
	}
	
	static public HAPQueryDefinition parse(JSONObject jsonObject, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		String name = jsonObject.optString(HAPAttributeConstant.QUERYDEFINITION_NAME);

		return null;
	}
	
	protected HAPDataTypeManager getDataTypeMan(){return this.m_dataTypeMan;}
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefMan;}
	
}
