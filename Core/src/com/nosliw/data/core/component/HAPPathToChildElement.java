package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPPathToChildElement extends HAPSerializableImp{

	@HAPAttribute
	public static String ELEMENTTYPE = "elementType";

	@HAPAttribute
	public static String ELEMENTID = "elementId";

	private String m_elementType;
	
	private String m_elementId;
	
	public String getElementType() {    return this.m_elementType;   }
	public void setElementType(String elementType) {   this.m_elementType = elementType;     }
	
	public String getElementId() {    return this.m_elementId;     }
	public void setElementId(String elementId) {    this.m_elementId = elementId;    }
	
	public HAPPathToChildElement clonePathToChildElement() {
		HAPPathToChildElement out = new HAPPathToChildElement();
		out.m_elementId = this.m_elementId;
		out.m_elementType = this.m_elementType;
		return out;
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format){
		if(value instanceof String) {
			String[] segs = HAPUtilityNamingConversion.parseLevel2((String)value);
			this.m_elementId = segs[0];
			if(segs.length>=2)  this.m_elementType = segs[1];
		}
		else if(value instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)value;
			this.m_elementId = (String)jsonObj.opt(ELEMENTID);
			this.m_elementType = (String)jsonObj.opt(ELEMENTTYPE);
		}
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTTYPE, this.m_elementType);
		jsonMap.put(ELEMENTID, this.m_elementId);
	}

}
