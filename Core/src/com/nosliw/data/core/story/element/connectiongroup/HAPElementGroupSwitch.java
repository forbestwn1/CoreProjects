package com.nosliw.data.core.story.element.connectiongroup;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPElementGroupImp;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.change.HAPChangeItemPatch;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

@HAPEntityWithAttribute
public class HAPElementGroupSwitch extends HAPElementGroupImp{

	public final static String GROUP_TYPE = HAPConstant.STORYGROUP_TYPE_SWITCH; 

	@HAPAttribute
	public static final String CHOICE = "choice";

	private Object m_choice;

	public HAPElementGroupSwitch() {
		super(GROUP_TYPE);
	}
	
	public Object getChoice() {    return this.m_choice;     }
	public void setChoice(Object choice) {     this.m_choice = choice;     }
	
	@Override
	public HAPChangeResult patch(String path, Object value) {
		HAPChangeResult out = super.patch(path, value); 
		if(out!=null)  return out; 
		else {
			if(CHOICE.equals(path)) {
//				if(value!=null)  value = value.toString();
				out = new HAPChangeResult();
				if(!HAPBasicUtility.isEquals(value, this.m_choice)) {
					out.addRevertChange(HAPUtilityChange.buildChangePatch(this, CHOICE, this.m_choice));
					if(this.getElements().size()>=2) {
						//multiple choice
						for(HAPInfoElement eleInfo : this.getElements()) {
							HAPStoryElement ele = this.getStory().getElement(eleInfo.getElementId());
							if(eleInfo.getName().equals(value)) {
								if(!ele.isEnable()) {
									out.addExtraChange(new HAPChangeItemPatch(ele.getElementId(), HAPStoryElement.ENABLE, true));
								}
							}
							else {
								if(ele.isEnable()) {
									out.addExtraChange(new HAPChangeItemPatch(ele.getElementId(), HAPStoryElement.ENABLE, false));
								}
							}
						}
					}
					else {
						//single choice
						HAPInfoElement eleInfo = this.getElements().get(0);
						HAPStoryElement ele = this.getStory().getElement(eleInfo.getElementId());
						if(HAPBasicUtility.isEquals(value, true)) {
							out.addExtraChange(new HAPChangeItemPatch(ele.getElementId(), HAPStoryElement.ENABLE, true));
						}
						else {
							out.addExtraChange(new HAPChangeItemPatch(ele.getElementId(), HAPStoryElement.ENABLE, false));
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
		HAPElementGroupSwitch out = new HAPElementGroupSwitch();
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
			jsonMap.put(CHOICE, HAPJsonUtility.buildJson(this.m_choice+"", HAPSerializationFormat.JSON));
			typeJsonMap.put(CHOICE, this.m_choice.getClass());
		}
	}
	
}
