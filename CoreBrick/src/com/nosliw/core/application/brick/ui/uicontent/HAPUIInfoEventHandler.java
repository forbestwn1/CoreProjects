package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.HAPIdBrickInBundle;

public abstract class HAPUIInfoEventHandler extends HAPSerializableImp{

	@HAPAttribute
	public static final String TYPE = "type";
	
	public abstract String getHandlerType();
	
	public static HAPUIInfoEventHandler parseHandlerInfo(Object obj) {
		if(obj instanceof String) {
			String str = (String)obj;
			String[] segs = HAPUtilityNamingConversion.parseParts(str);
			String handlerType = segs[0];
			if(HAPConstantShared.UICONTENT_EVENTHANDLERTYPE_TASK.equals(handlerType)) {
				HAPUIInfoEventHandlerTask out = new HAPUIInfoEventHandlerTask();
				HAPIdBrickInBundle taskId = new HAPIdBrickInBundle();
				taskId.buildObject(segs[1], HAPSerializationFormat.LITERATE);
				out.setTaskBrickId(taskId);
				return out;
			}
			else if(HAPConstantShared.UICONTENT_EVENTHANDLERTYPE_SCRIPT.equals(handlerType)) {
				HAPUIInfoEventHandlerScript out = new HAPUIInfoEventHandlerScript();
				out.setFunctionName(segs[1]);
				return out;
			}
		}
		return null;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getHandlerType());
	}
	
}
