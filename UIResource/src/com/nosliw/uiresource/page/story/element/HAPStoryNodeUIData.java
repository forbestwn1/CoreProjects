package com.nosliw.uiresource.page.story.element;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;
import com.nosliw.uiresource.page.story.model.HAPUIDataInfo;

@HAPEntityWithAttribute
public class HAPStoryNodeUIData extends HAPStoryNodeUI{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_UIDATA; 
	
	@HAPAttribute
	public static final String TAGNAME = "tagName";
	
	@HAPAttribute
	public static final String DATAINFO = "dataInfo";
	
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	
	private String m_tagName;
	
	private Map<String, String> m_attributes;
	
	private HAPUIDataInfo m_uiDataInfo;
	
	public HAPStoryNodeUIData() {
		super(STORYNODE_TYPE);
		this.m_attributes = new LinkedHashMap<String, String>();
	}

	public HAPStoryNodeUIData(String tagName, HAPUIDataInfo uiDataInfo) {
		this();
		this.m_tagName = tagName;
		this.m_uiDataInfo = uiDataInfo;
	}

	public String getTagName() {     return this.m_tagName;     }
	
	public void addAttribute(String name, String value) {     this.m_attributes.put(name, value);      }
	public Map<String, String> getAttributes(){   return this.m_attributes;     }
	
	@Override
	public HAPChangeResult patch(String path, Object value) {
		HAPChangeResult out = super.patch(path, value);
		if(out!=null)  return out; 
		else {
			out = new HAPChangeResult();
			if(TAGNAME.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, TAGNAME, this.m_tagName));
				this.m_tagName = (String)value;
				return out;
			}
		}
		return null;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_tagName = (String)jsonObj.opt(TAGNAME);
		
		JSONObject dataInfoObj = jsonObj.optJSONObject(DATAINFO);
		if(dataInfoObj!=null) {
			this.m_uiDataInfo = new HAPUIDataInfo();
			this.m_uiDataInfo.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		
		JSONObject attrMap = jsonObj.optJSONObject(ATTRIBUTES);
		if(attrMap!=null) {
			for(Object key : attrMap.keySet()) {
				String name = (String)key;
				this.m_attributes.put(name, attrMap.getString(name));
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, m_tagName);
		jsonMap.put(DATAINFO, this.m_uiDataInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildMapJson(m_attributes));
	}
}
