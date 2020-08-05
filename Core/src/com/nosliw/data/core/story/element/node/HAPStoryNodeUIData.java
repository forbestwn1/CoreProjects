package com.nosliw.data.core.story.element.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.design.HAPChangeItem;

@HAPEntityWithAttribute
public class HAPStoryNodeUIData extends HAPStoryNodeImp{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_UIDATA; 
	
	@HAPAttribute
	public static final String TAGNAME = "tagName";
	
	@HAPAttribute
	public static final String DATATYPE = "dataType";
	
	private String m_tagName;
	
	private HAPDataTypeCriteria m_dataTypeCriteria;
	
	public HAPStoryNodeUIData() {
		super(STORYNODE_TYPE);
	}
	
	public HAPStoryNodeUIData(String tagName, HAPDataTypeCriteria dataTypeCriteria) {
		super(STORYNODE_TYPE);
		this.m_tagName = tagName;
		this.m_dataTypeCriteria = dataTypeCriteria;
	}
	
	@Override
	public List<HAPChangeItem> patch(String path, Object value) {
		List<HAPChangeItem> out = super.patch(path, value); 
		if(out!=null)  return out; 
		else {
			if(TAGNAME.equals(path)) {
				this.m_tagName = (String)value;
				return new ArrayList<HAPChangeItem>();
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
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, m_tagName);
		jsonMap.put(STORYNODE_TYPE, this.m_dataTypeCriteria.toStringValue(HAPSerializationFormat.LITERATE));
	}

}
