package com.nosliw.data.core.story.element.connectiongroup;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPElementGroupImp;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.design.HAPChangeItemPatch;
import com.nosliw.data.core.story.design.HAPChangeResult;
import com.nosliw.data.core.story.design.HAPUtilityChange;

@HAPEntityWithAttribute
public class HAPElementGroupSwitch extends HAPElementGroupImp{

	public final static String GROUP_TYPE = HAPConstant.STORYGROUP_TYPE_SWITCH; 

	@HAPAttribute
	public static final String CHOICE = "choice";

	private String m_choice;

	public HAPElementGroupSwitch(HAPStory story) {
		super(GROUP_TYPE, story);
	}
	
	public String getChoice() {    return this.m_choice;     }
	public void setChoice(String choice) {     this.m_choice = choice;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_choice = (String)jsonObj.opt(CHOICE);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHOICE, HAPJsonUtility.buildJson(this.m_choice+"", HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPChangeResult patch(String path, Object value) {
		HAPChangeResult out = super.patch(path, value); 
		if(out!=null)  return out; 
		else {
			if(CHOICE.equals(path)) {
				out = new HAPChangeResult();
				if(!value.equals(this.m_choice)) {
					out.addRevertChange(HAPUtilityChange.buildChangePatch(this, CHOICE, this.m_choice));
					for(HAPInfoElement eleInfo : this.getElements()) {
						HAPStoryElement ele = this.getStory().getElement(eleInfo.getElementId());
						if(eleInfo.getId().equals(value)) {
							if(!ele.isEnable()) {
								out.addExtraChange(new HAPChangeItemPatch(ele.getCategary(), ele.getId(), HAPStoryElement.ENABLE, true));
							}
						}
						else {
							if(ele.isEnable()) {
								out.addExtraChange(new HAPChangeItemPatch(ele.getCategary(), ele.getId(), HAPStoryElement.ENABLE, false));
							}
						}
					}
					this.m_choice = (String)value;
				}
				
				return out;
			}
		}
		return null;
	}
	
}
