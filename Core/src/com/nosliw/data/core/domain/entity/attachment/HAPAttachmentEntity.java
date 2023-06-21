package com.nosliw.data.core.domain.entity.attachment;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//attachment that have real entity
public class HAPAttachmentEntity extends HAPAttachmentImp{

	@HAPAttribute
	public static String ENTITY = "entity";

	private Object m_entity;
	
	private HAPAttachmentEntity() {}
	
	public HAPAttachmentEntity(String valueType, Object rawValue, HAPIdEntityInDomain entityId, HAPEntityInfo entityInfo) {
		super(valueType, rawValue, entityId, entityInfo);
	}
	
	public Object getEntity() {    return this.m_entity;    }
	public void setEntity(Object entityObj) {    this.m_entity = entityObj;    }

	public String getEntityString() {   return (String)this.getEntity();   }
	public Integer getEntityInteger() {   return (Integer)this.getEntity();   }
	public Boolean getEntityBoolean() {   return (Boolean)this.getEntity();   }
	public JSONObject getEntityJsonObj() {   return (JSONObject)this.getEntity();   }
	public JSONArray getEntityJsonArray() {   return (JSONArray)this.getEntity();   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_entity!=null)		jsonMap.put(ENTITY, HAPUtilityJson.buildJson(this.m_entity, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_entity = jsonObj.opt(ENTITY);
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentEntity) {
			HAPAttachmentEntity ele = (HAPAttachmentEntity)obj;
			if(super.equals(ele)) {
				if(HAPUtilityBasic.isEquals(ele.m_entity, this.m_entity)) {
					out = true;
				}
			}
		}
		return out;
	}
	
	@Override
	public HAPAttachmentEntity cloneAttachment() {
		HAPAttachmentEntity out = new HAPAttachmentEntity();
		this.cloneToAttachment(out);
		out.m_entity = this.m_entity;
		return out;
	}
}
