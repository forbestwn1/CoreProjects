package com.nosliw.entity.data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.entity.utils.HAPAttributeConstant;

/*
 * store all the component of entity ID
 * it is the ID information for root entity
 *    type: 	entity type of root entity
 *    id: 		entity id of root entity which is unique within same entity type
 */
public class HAPEntityID implements HAPStringable{
	//entity type name
	private String m_entityType;
	//root entity id
	private String m_id;
	
	//calculated attr, key for id, unique string for id
	private String m_key;

	public HAPEntityID(String type, String id){
		this.m_entityType = type;
		this.m_id = id;
	}
	
	public String getEntityType(){return this.m_entityType;}
	public String getId(){return this.m_id;}
	public String getKey(){
		if(this.m_key==null){
			if(HAPBasicUtility.isStringEmpty(this.getEntityType()))  return null;
			if(HAPBasicUtility.isStringEmpty(this.getId()))  return null;
			
			StringBuffer out = new StringBuffer();
			out.append(HAPConstant.SEPERATOR_PART);
			out.append(this.getEntityType());
			out.append(HAPConstant.SEPERATOR_PART);
			out.append(this.getId());
			this.m_key = out.toString();
		}
		return this.m_key;
	}

	public static HAPEntityID parseKey(String key){
		HAPSegmentParser idSegs = new HAPSegmentParser(key, HAPConstant.SEPERATOR_PART);
		String entityType = idSegs.next();
		String id = idSegs.next();
		return new HAPEntityID(entityType, id);
	}
	
	@Override
	public String toStringValue(String format){
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(HAPAttributeConstant.ENTITYID_ID, this.m_id);
		jsonMap.put(HAPAttributeConstant.ENTITYID_ENTITYTYPE, this.m_entityType);
		jsonMap.put(HAPAttributeConstant.ENTITYID_KEY, this.getKey());
		return HAPJsonUtility.getMapJson(jsonMap);
	}
	
	public static HAPEntityID parseJson(JSONObject jsonEntityID){
		String type = jsonEntityID.optString(HAPAttributeConstant.ENTITYID_ENTITYTYPE);
		String id = jsonEntityID.optString(HAPAttributeConstant.ENTITYID_ID);
		return new HAPEntityID(type, id);
	}
	
	@Override
	public HAPEntityID clone(){
		return new HAPEntityID(this.m_entityType, this.m_id);
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof HAPEntityID){
			HAPEntityID ID = (HAPEntityID)o;
			if(	HAPBasicUtility.isEquals(ID.getEntityType(), this.getEntityType()) &&
					HAPBasicUtility.isEquals(ID.getId(), this.getId())){
				return true;
			}
			else  return false;
		}
		else if(o instanceof String){
			//for key
			HAPEntityID ID = HAPEntityID.parseKey((String)o);
			return this.equals(ID);
		}
		else  return false;
	}

	public String toString(){	return this.getKey();	}
}
