package com.nosliw.data.core.script.context;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoUtility;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPContextEntity  extends HAPContext implements HAPEntityInfo{

	private HAPEntityInfoWritable m_entityInfo;
	
	public HAPContextEntity() {
		this.m_entityInfo = new HAPEntityInfoWritableImp();
	}

	@Override
	public String getId() {  return this.m_entityInfo.getId();  }

	@Override
	public String getName() {  return this.m_entityInfo.getName();  }

	@Override
	public String getDescription() {  return this.m_entityInfo.getDescription();  }

	@Override
	public HAPInfo getInfo() {  return this.m_entityInfo.getInfo();  }

	@Override
	public void setId(String id) {   this.m_entityInfo.setId(id); }

	@Override
	public void setName(String name) {  this.m_entityInfo.setName(name);  }

	@Override
	public void setDescription(String description) {  this.m_entityInfo.setDescription(description);  }

	@Override
	public void setInfo(HAPInfo info) {  this.m_entityInfo.setInfo(info);  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		HAPEntityInfoUtility.buildJsonMap(jsonMap, this);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_entityInfo.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	public HAPEntityInfo cloneEntityInfo() {	return this.cloneContextEntity();	}
	
	public HAPContextEntity cloneBaseInfo() {
		HAPContextEntity out = new HAPContextEntity();
		this.toContextEntityBaseInfo(out);
		return out;
	}

	public HAPContextEntity cloneContextEntity() {
		HAPContextEntity out = new HAPContextEntity();
		this.toContextEntityBaseInfo(out);
		this.toContext(out);
		return out;
	}

	public void toContextEntity(HAPContextEntity contextEntity) {
		this.toContextEntityBaseInfo(contextEntity);
		this.toContext(contextEntity);
	}

	public void toContextEntityBaseInfo(HAPContextEntity contextEntity) {
		this.m_entityInfo.cloneToEntityInfo(contextEntity.m_entityInfo); 
	}
	
	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {  this.m_entityInfo.cloneToEntityInfo(entityInfo); }

	@Override
	public void buildEntityInfoByJson(Object json) {	this.m_entityInfo.buildObject(json, HAPSerializationFormat.JSON);	}

}
