package com.nosliw.data.core;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;

public class HAPRelationshipImp extends HAPSerializableImp implements HAPRelationship{

	private HAPDataTypeId m_source;
	private HAPDataTypeId m_taget;
	private HAPRelationshipPath m_path;
	
	public HAPRelationshipImp() {}
	
	public HAPRelationshipImp(HAPDataTypeId source, HAPDataTypeId target, HAPRelationshipPath path) {
		this.m_source = source;
		this.m_taget = target;
		this.m_path = path;
	}
	
	@Override
	public HAPDataTypeId getTarget() {  return this.m_taget;  }

	@Override
	public HAPDataTypeId getSource() {  return this.m_source;  }

	@Override
	public HAPRelationshipPath getPath() {  return this.m_path;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_source = new HAPDataTypeId();
		this.m_source.buildObject(jsonObj.getString(SOURCE), HAPSerializationFormat.LITERATE);
		this.m_taget = new HAPDataTypeId();
		this.m_taget.buildObject(jsonObj.getString(TARGET), HAPSerializationFormat.LITERATE);
		this.m_path = new HAPRelationshipPath();
		this.m_path.buildObject(jsonObj.getString(PATH), HAPSerializationFormat.LITERATE);
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SOURCE, HAPSerializeManager.getInstance().toStringValue(this.getSource(), HAPSerializationFormat.LITERATE));
		jsonMap.put(TARGET, HAPSerializeManager.getInstance().toStringValue(this.getTarget(), HAPSerializationFormat.LITERATE));
		jsonMap.put(PATH, HAPSerializeManager.getInstance().toStringValue(this.getPath(), HAPSerializationFormat.LITERATE));
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPRelationshipImp) {
			HAPRelationshipImp relationship = (HAPRelationshipImp)obj;
			if(!HAPBasicUtility.isEquals(this.m_source, relationship.m_source))  return false;
			if(!HAPBasicUtility.isEquals(this.m_taget, relationship.m_taget))  return false;
			if(!HAPBasicUtility.isEquals(this.m_path, relationship.m_path))  return false;
			out = true;
		}
		return out;
	}
}
