package com.nosliw.data.core.story.element.node;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.design.HAPChangeResult;
import com.nosliw.data.core.story.design.HAPUtilityChange;

@HAPEntityWithAttribute
public class HAPStoryNodeUIData extends HAPStoryNodeImp{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_UIDATA; 
	
	@HAPAttribute
	public static final String TAGNAME = "tagName";
	
	@HAPAttribute
	public static final String DATATYPE = "dataType";
	
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	
	private String m_tagName;
	
	private HAPDataTypeCriteria m_dataTypeCriteria;
	
	private Map<String, String> m_attributes;
	
	public HAPStoryNodeUIData() {
		super(STORYNODE_TYPE);
		this.m_attributes = new LinkedHashMap<String, String>();
	}
	
	public HAPStoryNodeUIData(String tagName, HAPDataTypeCriteria dataTypeCriteria) {
		this();
		this.m_tagName = tagName;
		this.m_dataTypeCriteria = dataTypeCriteria;
	}
	
	public String getTagName() {     return this.m_tagName;     }
	
	public void addAttribute(String name, String value) {     this.m_attributes.put(name, value);      }
	
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
		this.m_dataTypeCriteria = HAPCriteriaUtility.parseCriteria((String)jsonObj.opt(DATATYPE));
		JSONObject attrMap = jsonObj.optJSONObject(ATTRIBUTES);
		for(Object key : attrMap.keySet()) {
			String name = (String)key;
			this.m_attributes.put(name, attrMap.getString(name));
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, m_tagName);
		jsonMap.put(DATATYPE, this.m_dataTypeCriteria.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildMapJson(m_attributes));
	}
}
