package com.nosliw.entity.path;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.utils.HAPAttributeConstant;

/*
 * describe entity path information : base entity + path
 * this path may be a complex path that cover multiple solid entity
 */
public class HAPEntityPath implements HAPSerializable{

	//entity ID
	private HAPEntityID m_entityID;
	//path
	private String m_path;
	//calculated attr: entity wraper
	private HAPEntityWraper m_entityWraper;
	
	public HAPEntityPath(HAPEntityID ID, String path){
		this.m_entityID = ID;
		this.m_path = path;
	}
	
	public HAPEntityID getEntityID(){	return this.m_entityID;	}
	public void setEntityID(HAPEntityID ID){this.m_entityID = ID;}
	
	public String getPath(){return this.m_path;}
	public void setPath(String path){this.m_path=path;}
	
	public HAPEntityWraper getEntityWraper(){return this.m_entityWraper;}
	public void setEntityWraper(HAPEntityWraper wraper){this.m_entityWraper=wraper;}

	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		this.setJsonMap(jsonMap);
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
	
	public HAPEntityPath clone(){
		return new HAPEntityPath(this.m_entityID, this.m_path);
	}
	
	protected void setJsonMap(Map<String, String> jsonMap){
		if(this.m_entityID!=null)		jsonMap.put(HAPAttributeConstant.ENTITYPATH_ENTITYID, this.m_entityID.toString());
		jsonMap.put(HAPAttributeConstant.ENTITYPATH_PATH, this.m_path);
	}
}
