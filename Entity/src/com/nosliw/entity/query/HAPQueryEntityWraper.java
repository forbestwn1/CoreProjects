package com.nosliw.entity.query;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.path.HAPEntityPath;

public class HAPQueryEntityWraper extends HAPWraper{

	//all attribute defined with this query entity
	private Set<HAPQueryProjectAttribute> m_attributes;
	
	private String m_id;
	
	public HAPQueryEntityWraper(Set<HAPQueryProjectAttribute> attributes, HAPDataTypeManager dataTypeMan){
		super(new HAPDataTypeDefInfo(HAPConstant.DATATYPE_CATEGARY_QUERYENTITY, HAPConstant.DATATYPE_TYPE_QUERYENTITY_NORMAL), dataTypeMan);
		this.m_attributes = attributes;
		HAPQueryEntityData queryEntityData = new HAPQueryEntityData(attributes); 
		this.setData(queryEntityData);
	}

	public Set<HAPEntityID> getEntityIDs(){		return this.getQueryEntityData().getEntityIDs();	}
	
	public String getId(){return this.m_id;}
	
	public void process(Map<String, HAPEntityWraper> entityWrapers, HAPEntityDataAccess dataAccess){
		this.getQueryEntityData().process(entityWrapers, dataAccess);
	}

	public HAPQueryEntityData getQueryEntityData(){	return (HAPQueryEntityData)this.getData();}
	
	public Set<HAPQueryProjectAttribute> getAttributes(){	return this.m_attributes;	}
	
	
	public HAPQueryEntityAttributeWraper getAttributeWraper(String entityName, String attribute){	return this.getQueryEntityData().getAttributeWraper(new HAPQueryProjectAttribute(entityName, attribute));	}
	
	
	public HAPQueryEntityWraper clone(HAPEntityDataAccess dataAccess){
		HAPQueryEntityWraper out = new HAPQueryEntityWraper(this.m_attributes, this.getDataTypeManager());
//		this.cloneTo(out);
//		
//		HAPQueryEntityData queryEntityData = this.getQueryEntityData();
//		for(HAPQueryProjectAttribute attr : queryEntityData.getAttributes()){
//			out.getQueryEntityData().addAttributeWraper(queryEntityData.getAttributeWraper(attr).cloneWraper());
//		}
		return out;
	}
	
	@Override
	protected void cloneTo(HAPWraper wraper){
		super.cloneTo(wraper);
		
		HAPQueryEntityWraper out = (HAPQueryEntityWraper)wraper;
	}

	@Override
	protected void buildOjbectJsonMap(Map<String, String> map, Map<String, Class> dataTypeMap){
		super.buildOjbectJsonMap(map, dataTypeMap);
	}
}

