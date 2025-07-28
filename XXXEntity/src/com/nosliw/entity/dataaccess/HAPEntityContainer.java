package com.nosliw.entity.dataaccess;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;

/*
 * store a set of entitys
 * provide function:
 * 		add entity
 * 		remove entity by ID
 * 		get entity by ID
 * 		get entitys by type
 * 		get all typs
 * 		get all entitys
 * 		clear up
 */
public class HAPEntityContainer implements HAPSerializable{

	private Map<String, Map<String, HAPEntityWraper>> m_entitys;
	
	public HAPEntityContainer(){
		this.m_entitys = new LinkedHashMap<String, Map<String, HAPEntityWraper>>();
	}
	
	public void clearup(){
		this.m_entitys.clear();
	}
	
	public void addEntity(HAPEntityWraper entity){
		String type = entity.getEntityType();
		Map<String, HAPEntityWraper> typeEntitys = this.m_entitys.get(type);
		if(typeEntitys==null){
			typeEntitys = new LinkedHashMap<String, HAPEntityWraper>();
			this.m_entitys.put(type, typeEntitys);
		}
		typeEntitys.put(this.getEntityIDKey(entity.getID()), entity);
	}
	
	public void remove(HAPEntityID entityID){
		String type = entityID.getEntityType();
		Map<String, HAPEntityWraper> typeEntitys = this.m_entitys.get(type);
		if(typeEntitys!=null){
			typeEntitys.remove(this.getEntity(entityID));
			if(typeEntitys.size()==0) this.m_entitys.remove(type);
		}
	}
	
	public HAPEntityWraper getEntity(HAPEntityID entityID){
		HAPEntityWraper out = null;
		String type = entityID.getEntityType();
		Map<String, HAPEntityWraper> typeEntitys = this.m_entitys.get(type);
		
		if(typeEntitys!=null){
			HAPEntityID ID1 = entityID.getRootEntityID();
			HAPEntityWraper rEntity = typeEntitys.get(this.getEntityIDKey(ID1));
			if(rEntity!=null)		out = (HAPEntityWraper)rEntity.getChildWraperByPath(entityID.getAttributePath());
		}
		return out;
	}

	public Set<HAPEntityWraper> getAllEntitys(){
		Set<HAPEntityWraper> out = new HashSet<HAPEntityWraper>();
		for(String type : this.getAllTypes()){
			out.addAll(this.m_entitys.get(type).values());
		}
		return out;
	}
	
	
	public Set<HAPEntityWraper> getAllEntityByType(String type){
		Map<String, HAPEntityWraper> typeEntitys = this.m_entitys.get(type);
		Set<HAPEntityWraper> out = new HashSet<HAPEntityWraper>();
		if(typeEntitys!=null)		out.addAll(typeEntitys.values());
		return out;
	}
	
	public String[] getAllTypes(){
		return this.m_entitys.keySet().toArray(new String[0]);
	}
	
	private String getEntityIDKey(HAPEntityID ID){
		return ID.toString();
	}

	@Override
	public String toStringValue(String format) {
		Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
		for(String type : m_entitys.keySet()){
			Map<String, HAPEntityWraper> entitys = this.m_entitys.get(type);
			Map<String, String> jsonMap = new LinkedHashMap<String, String>();
			for(String key : entitys.keySet()){
				jsonMap.put(key, entitys.get(key).toStringValue(format));
			}
			outJsonMap.put(type, HAPJsonUtility.buildMapJson(jsonMap));
		}
		return HAPJsonUtility.buildMapJson(outJsonMap);
	}
}
