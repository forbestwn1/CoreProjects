package com.nosliw.entity.data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPReferenceInfoAbsolute extends HAPReferenceInfo{

	private HAPEntityID m_entityID;
	
	private String m_path;
	
	public HAPReferenceInfoAbsolute(HAPEntityID entityId, String path){
		this.m_path = path;
		this.m_entityID = entityId;
	}
	
	@Override
	public int getType() {	return HAPConstant.REFERENCE_TYPE_ABSOLUTE;	}

	public HAPEntityID getEntityID(){	return this.m_entityID;	}
	
	public String getPath(){	return this.m_path;	}
	
	@Override
	public boolean equals(Object o){
		if(o==null)  return false;
		
		if(o instanceof HAPReferenceInfoAbsolute){
			HAPReferenceInfoAbsolute path = (HAPReferenceInfoAbsolute)o;
			if(HAPBasicUtility.isEquals(path.m_path, this.m_path) && HAPBasicUtility.isEquals(path.m_entityID, this.m_entityID)){
				return true;
			}
			else{
				return false;
			}
		}
		else return false;
	}

	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		
		String out = HAPJsonUtility.buildMapJson(jsonMap);
		return out;
	}

	public static HAPReferenceInfoAbsolute parseJson(JSONObject jsonEntityID){
		return null;
	}
}
