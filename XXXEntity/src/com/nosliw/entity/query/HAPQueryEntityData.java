package com.nosliw.entity.query;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.imp.HAPDataImp;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.path.HAPEntityPath;
import com.nosliw.entity.path.HAPEntityPathInfo;

/*
 * data for query result -- query entity
 */
public class HAPQueryEntityData extends HAPDataImp{
	//store all attributes
	private Set<HAPQueryProjectAttribute> m_projectAttributes;

	//store all the attribute data
	private Map<String, HAPQueryEntityAttributeWraper> m_attributeDatas;

	private Map<String, HAPEntityID> m_basicEntityIDs;
	
	private Set<HAPEntityID> m_IDs;
	
	public HAPQueryEntityData(Set<HAPQueryProjectAttribute> attributes) {
		super(HAPQueryEntity.dataType);
		this.m_projectAttributes = attributes;
		this.m_attributeDatas = new LinkedHashMap<String, HAPQueryEntityAttributeWraper>();
	}

	public Set<HAPEntityID> getEntityIDs(){return this.m_IDs;}
	public Map<String, HAPEntityID> getParmedEntityIDs(){ return this.m_basicEntityIDs; }
	
	public void process(Map<String, HAPEntityWraper> entityWrapers, HAPEntityDataAccess dataAccess){
		for(String parm : entityWrapers.keySet()){
			HAPEntityID ID = entityWrapers.get(parm).getID();
			this.m_basicEntityIDs.put(parm, ID);
			this.m_IDs.add(ID);
		}
		
		for(HAPQueryProjectAttribute proAttr : this.m_projectAttributes){
			HAPEntityWraper wraper = entityWrapers.get(proAttr.entityName);
			
			HAPEntityPathInfo entityPathInfo = new HAPEntityPathInfo(new HAPEntityPath(wraper.getID(), proAttr.attribute), dataAccess);
			m_attributeDatas.put(proAttr.toString(), new HAPQueryEntityAttributeWraper(entityPathInfo.getData(), this.getDataTypeManager()));
			
			m_IDs.addAll(entityPathInfo.getRelatedIDs());
		}
	}
	
	public Set<HAPQueryProjectAttribute> getAttributes(){	return this.m_projectAttributes;}

	public HAPQueryEntityAttributeWraper getAttributeWraper(HAPQueryProjectAttribute attrInfo){
		return this.m_attributeDatas.get(attrInfo.toString());
	}
	
	@Override
	public HAPData cloneData() {
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		if(format.equals(HAPSerializationFormat.JSON)){
			return HAPJsonUtility.getMapObjectJson(m_attributeDatas);
		}
		return null;
	}
}
