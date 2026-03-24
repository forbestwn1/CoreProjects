package com.nosliw.core.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPIdBrick extends HAPSerializableImp{

	@HAPAttribute
	public static final String BRICKTYPEID = "brickTypeId";

	@HAPAttribute
	public static final String DIVISION = "division";

	@HAPAttribute
	public static final String ID = "id";

	private HAPIdBrickType m_brickTypeId;
	
	//entity may store and process differently, 
	private String m_division;
	
	private String m_id;

	public HAPIdBrick() {}
	
	public HAPIdBrick(HAPIdBrickType entityTypeId, String division, String id) {
		this.m_brickTypeId = entityTypeId;
		this.m_division = division;
		this.m_id = id;
	}
	
	public HAPIdBrickType getBrickTypeId() {    return this.m_brickTypeId;     }
	public void setBrickTypeId(HAPIdBrickType entityTypeId) {    this.m_brickTypeId = entityTypeId;      }
	
	public String getDivision() {     return this.m_division;     }
	public void setDivision(String division) {     this.m_division = division;     }
	
	public String getId() {    return this.m_id;    }
	
	public String getKey() {
		return HAPUtilityNamingConversion.cascadeLevel2(new String[] {this.m_id, this.m_brickTypeId.getKey(), this.m_division});
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DIVISION, this.m_division);
		jsonMap.put(BRICKTYPEID, this.m_brickTypeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ID, this.m_id);
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] segs = HAPUtilityNamingConversion.parseLevel2(literateValue);
		this.m_id = segs[0];
		if(segs.length>=2) {
			this.m_brickTypeId = new HAPIdBrickType();
			this.m_brickTypeId.buildObject(segs[1], HAPSerializationFormat.LITERATE);
		}
		if(segs.length>=3) {
			this.m_division = segs[2];
		}
		return true;  
	}
	
	@Override
	protected boolean buildObjectByJson(Object obj){
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			this.m_id = jsonObj.getString(ID);
			this.m_division = (String)jsonObj.opt(DIVISION);
			this.m_brickTypeId = new HAPIdBrickType();
			this.m_brickTypeId.buildObject(jsonObj.opt(BRICKTYPEID), HAPSerializationFormat.JSON);
		}
		else if(obj instanceof String) {
			this.buildObjectByLiterate((String)obj);
		}
		return true;  
	}
}
