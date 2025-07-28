package com.nosliw.entity.options;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.query.HAPQueryDefinition;
import com.nosliw.entity.query.HAPQueryDefinitionManager;
import com.nosliw.entity.query.HAPQueryEntityWraper;
import com.nosliw.entity.query.HAPQueryManager;

public abstract class HAPOptionsDefinitionQuery extends HAPOptionsDefinition{

	//query definition that this options depends on
	private String m_queryDefName;

	//parms used for query
	private Map<String, HAPData> m_queryParms; 

	//calculated
	private HAPQueryDefinition m_queryDef;

	private HAPQueryDefinitionManager m_queryDefMan;
	
	public HAPOptionsDefinitionQuery(String name, String queryDefName, Map<String, HAPData> queryParms, HAPAttributeDefinition attrDef, HAPQueryDefinitionManager queryDefMan, HAPDataTypeManager dataTypeMan) {
		super(name, attrDef.getDataTypeDefinitionInfo(), null, dataTypeMan);
		this.m_queryDefMan = queryDefMan;
		this.m_queryDefName = queryDefName;
	}

	public List<HAPWraper> getOptions(Map<String, HAPData> parms, HAPQueryManager queryManager){
		Map<String, HAPData> queryParms = new LinkedHashMap<String, HAPData>();
		queryParms.putAll(parms);
		queryParms.putAll(this.m_queryParms);
		List<HAPQueryEntityWraper> queryEntitys = queryManager.query(getQueryDefinition(), parms, null, false).getEntitys();
		List<HAPWraper> out = new ArrayList<HAPWraper>();
		for(HAPQueryEntityWraper queryEntity : queryEntitys){
			out.add(queryEntity);
		}
		return out;
	}

	public HAPQueryDefinition getQueryDefinition(){
		if(this.m_queryDef==null){
			this.m_queryDef = this.getQueryDefinitionManager().getQueryDefinition(m_queryDefName);
		}
		return this.m_queryDef;
	}

	public Map<String, HAPData> getQueryParms(){	return this.m_queryParms;	}
	
	public void addQueryParms(String name, HAPData parm){ this.m_queryParms.put(name, parm);}
	
	public String getQueryDefinitionName(){return this.m_queryDefName;}
	
	protected HAPQueryDefinitionManager getQueryDefinitionManager(){	return this.m_queryDefMan;	}
	
	@Override
	public String getType() {	return HAPConstant.OPTIONS_TYPE_QUERY;	}
}
