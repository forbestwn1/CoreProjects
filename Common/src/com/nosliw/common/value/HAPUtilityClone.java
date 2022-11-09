package com.nosliw.common.value;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.interfac.HAPCloneable;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public class HAPUtilityClone {

	public static Object cloneValue(Object obj) {
		
		Object out = null;

		if(obj instanceof List) {
			out = new ArrayList();
			List listObj = (List)obj;
			for(Object ele : listObj) {
				((List)out).add(cloneValue(ele));
			}
		}
		else if(obj instanceof Set) {
			out = new HashSet();
			Set settObj = (Set)obj;
			for(Object ele : settObj) {
				((Set)out).add(cloneValue(ele));
			}
		}
		else if(obj instanceof Map) {
			out = new LinkedHashMap();
			Map mapObj = (Map)obj;
			for(Object key : mapObj.keySet()) {
				((Map)out).put(key, cloneValue(mapObj.get(key)));
			}
		}
		else if(obj instanceof HAPSerializable) {
			out = HAPSerializeManager.getInstance().buildObject(obj.getClass().toString(), HAPSerializeManager.getInstance().toStringValue(obj, HAPSerializationFormat.JSON), HAPSerializationFormat.JSON);
		}
		else if(obj instanceof HAPCloneable) {
			out = ((HAPCloneable)obj).cloneValue();
		}
		else {
			out = obj;
		}
		
		return out;
	}
	
}
