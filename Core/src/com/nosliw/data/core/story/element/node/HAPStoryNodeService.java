package com.nosliw.data.core.story.element.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.design.HAPChangeItem;

@HAPEntityWithAttribute
public class HAPStoryNodeService extends HAPStoryNodeImp{
	
	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_SERVICE; 
	
	@HAPAttribute
	public static final String REFERENCEID = "referenceId";
	
	private String m_referenceId;
	
	public HAPStoryNodeService() {
		super(STORYNODE_TYPE);
	}
	
	public String getReferenceId() {   return this.m_referenceId;   }
	public void setReferenceId(String refId) {    this.m_referenceId = refId;    }

	@Override
	public List<HAPChangeItem> patch(String path, Object value) {
		List<HAPChangeItem> out = super.patch(path, value); 
		if(out!=null)  return out; 
		else {
			if(REFERENCEID.equals(path)) {
				this.m_referenceId = (String)value;
				return new ArrayList<HAPChangeItem>();
			}
		}
		return null;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_referenceId = (String)jsonObj.opt(REFERENCEID);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCEID, this.m_referenceId);
	}

}
