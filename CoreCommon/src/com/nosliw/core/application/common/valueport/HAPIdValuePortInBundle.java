package com.nosliw.core.application.common.valueport;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.HAPIdBrickInBundle;

@HAPEntityWithAttribute
public class HAPIdValuePortInBundle extends HAPSerializableImp{

	@HAPAttribute
	public static final String BRICKID = "brickId";

	@HAPAttribute
	public static final String VALUEPORTSIDE = "valuePortSide";

	@HAPAttribute
	public static final String VALUEPORTID = "valuePortId";

	@HAPAttribute
	public static final String KEY = "key";

	private HAPIdBrickInBundle m_brickId;
	
	private String m_valuePortSide;
	
	private HAPIdValuePortInBrick m_valuePortId;
	
	public HAPIdValuePortInBundle() {}
	
	public HAPIdValuePortInBundle(HAPIdBrickInBundle brickRef, String valuePortSide, HAPIdValuePortInBrick valuePortId) {
		this.m_brickId = brickRef;
		this.m_valuePortSide = valuePortSide;
		this.m_valuePortId = valuePortId;
	}
	
	//which entity this value port belong
	public HAPIdBrickInBundle getBrickId() {    return this.m_brickId;     }
	public void setBlockId(HAPIdBrickInBundle blockId) {     this.m_brickId = blockId;      }
	
	public String getValuePortSide() {    return this.m_valuePortSide;    }
	public void setValuePortSide(String valuePortSide) {     this.m_valuePortSide = valuePortSide;       }
	
	public HAPIdValuePortInBrick getValuePortId() {   return this.m_valuePortId;     }
	public void setValuePortId(HAPIdValuePortInBrick valuePortId) {     this.m_valuePortId = valuePortId;      }

	public String getKey() {    
		String[] segs =  {this.m_brickId!=null?this.m_brickId.getIdPath():"", this.m_valuePortSide, this.m_valuePortId!=null?this.m_valuePortId.getKey():""};
		return HAPUtilityNamingConversion.cascadeComponents(segs, HAPConstantShared.SEPERATOR_PREFIX);       
	}
	
	@Override
	public HAPIdValuePortInBundle cloneValue() {
		return new HAPIdValuePortInBundle(this.m_brickId.cloneValue(), this.m_valuePortSide, this.m_valuePortId.cloneValue());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;

		Object brickRefObj = jsonObj.opt(BRICKID);
		if(brickRefObj!=null) {
			this.m_brickId = new HAPIdBrickInBundle();
			this.m_brickId.buildObject(brickRefObj, HAPSerializationFormat.JSON);
		}
		
		this.m_valuePortSide = (String)jsonObj.opt(VALUEPORTSIDE);
		
		Object valuePortIdObj = jsonObj.opt(VALUEPORTID);
		if(valuePortIdObj!=null) {
			this.m_valuePortId = new HAPIdValuePortInBrick();
			this.m_valuePortId.buildObject(valuePortIdObj, HAPSerializationFormat.JSON);
		}
		
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		if(this.m_brickId!=null) {
			jsonMap.put(BRICKID, this.m_brickId.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUEPORTSIDE, this.m_valuePortSide);
		if(this.m_valuePortId!=null) {
			jsonMap.put(VALUEPORTID, this.m_valuePortId.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(KEY, this.getKey());
	}
}
