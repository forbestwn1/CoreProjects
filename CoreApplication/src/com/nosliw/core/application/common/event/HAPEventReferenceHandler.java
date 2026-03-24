package com.nosliw.core.application.common.event;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.HAPPackageBrickInBundle;

@HAPEntityWithAttribute
public abstract class HAPEventReferenceHandler extends HAPSerializableImp{

	@HAPAttribute
	public static final String TYPE = "type";
	
	public abstract String getHandlerType();
	
	public static HAPEventReferenceHandler parseHandlerInfo(Object obj) {
		if(obj instanceof String) {
			String str = (String)obj;
			String[] segs = HAPUtilityNamingConversion.parseDetails(str);
			String handlerType = segs[0];
			if(HAPConstantShared.EVENT_HANDLERTYPE_TASK.equals(handlerType)) {
				HAPEventReferenceHandlerTask out = new HAPEventReferenceHandlerTask();
				HAPPackageBrickInBundle taskPackage = new HAPPackageBrickInBundle();
				taskPackage.buildObject(segs[1], HAPSerializationFormat.LITERATE);
				out.setTaskBrickPackage(taskPackage);
				return out;
			}
			else if(HAPConstantShared.EVENT_HANDLERTYPE_SCRIPT.equals(handlerType)) {
				HAPEventReferenceHandlerScript out = new HAPEventReferenceHandlerScript();
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
