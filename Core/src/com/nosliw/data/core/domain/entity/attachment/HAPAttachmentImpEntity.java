package com.nosliw.data.core.domain.entity.attachment;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//attachment that have real entity
public class HAPAttachmentImpEntity extends HAPAttachmentImp{

	@HAPAttribute
	public static String ENTITY = "entity";

	private HAPManualBrick m_entity;
	
	private HAPAttachmentImpEntity() {}
	
	public HAPAttachmentImpEntity(String valueType, Object rawValue, HAPIdEntityInDomain entityId, HAPEntityInfo entityInfo, HAPManualBrick entity) {
		super(valueType, rawValue, entityId, entityInfo);
		this.setEntity(entity);
	}
	
	public HAPManualBrick getEntity() {    return this.m_entity;    }
	public void setEntity(HAPManualBrick entityObj) {    this.m_entity = entityObj;    }

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
		if(obj instanceof HAPAttachmentImpEntity) {
			HAPAttachmentImpEntity ele = (HAPAttachmentImpEntity)obj;
			if(super.equals(ele)) {
				if(HAPUtilityBasic.isEquals(ele.m_entity, this.m_entity)) {
					out = true;
				}
			}
		}
		return out;
	}
	
	@Override
	public HAPAttachmentImpEntity cloneAttachment() {
		HAPAttachmentImpEntity out = new HAPAttachmentImpEntity();
		this.cloneToAttachment(out);
		out.m_entity = this.m_entity;
		return out;
	}
}
