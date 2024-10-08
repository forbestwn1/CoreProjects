package com.nosliw.core.application.division.story.brick.group;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.division.story.HAPStoryInfoElement;
import com.nosliw.core.application.division.story.brick.HAPStoryElementGroupImp;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItemPatch;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPStoryElementGroupSwitch extends HAPStoryElementGroupImp{

	public final static String GROUP_TYPE = HAPConstantShared.STORYGROUP_TYPE_SWITCH; 

	@HAPAttribute
	public static final String CHOICE = "choice";

	private Object m_choice;

	public HAPStoryElementGroupSwitch() {
		super(GROUP_TYPE);
	}
	
	public Object getChoice() {    return this.m_choice;     }
	public void setChoice(Object choice) {     this.m_choice = choice;     }
	
	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPStoryChangeResult out = super.patch(path, value, runtimeEnv); 
		if(out!=null)  return out; 
		else {
			if(CHOICE.equals(path)) {
//				if(value!=null)  value = value.toString();
				out = new HAPStoryChangeResult();
				if(!HAPUtilityBasic.isEquals(value, this.m_choice)) {
					out.addRevertChange(HAPStoryUtilityChange.buildChangePatch(this, CHOICE, this.m_choice));
					if(this.getElements().size()>=2) {
						//multiple choice
						for(HAPStoryInfoElement eleInfo : this.getElements()) {
							HAPStoryElement ele = this.getStory().getElement(eleInfo.getElementId());
							if(eleInfo.getName().equals(value)) {
								if(!ele.isEnable()) {
									out.addExtendChange(new HAPStoryChangeItemPatch(ele.getElementId(), HAPStoryElement.ENABLE, true));
								}
							}
							else {
								if(ele.isEnable()) {
									out.addExtendChange(new HAPStoryChangeItemPatch(ele.getElementId(), HAPStoryElement.ENABLE, false));
								}
							}
						}
					}
					else {
						//single choice
						HAPStoryInfoElement eleInfo = this.getElements().get(0);
						HAPStoryElement ele = this.getStory().getElement(eleInfo.getElementId());
						if(HAPUtilityBasic.isEquals(value, true)) {
							out.addExtendChange(new HAPStoryChangeItemPatch(ele.getElementId(), HAPStoryElement.ENABLE, true));
						}
						else {
							out.addExtendChange(new HAPStoryChangeItemPatch(ele.getElementId(), HAPStoryElement.ENABLE, false));
						}
					}
					this.m_choice = value;
				}
				
				return out;
			}
		}
		return null;
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryElementGroupSwitch out = new HAPStoryElementGroupSwitch();
		super.cloneTo(out);
		out.m_choice = this.m_choice;
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_choice = jsonObj.opt(CHOICE);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_choice!=null) {
			jsonMap.put(CHOICE, HAPUtilityJson.buildJson(this.m_choice+"", HAPSerializationFormat.JSON));
			typeJsonMap.put(CHOICE, this.m_choice.getClass());
		}
	}
	
}
