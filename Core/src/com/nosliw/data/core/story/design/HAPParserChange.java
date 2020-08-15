package com.nosliw.data.core.story.design;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParserChange {

	private static final Map<String, Class<? extends HAPChangeItem>> m_changeItemClas = new LinkedHashMap<String, Class<? extends HAPChangeItem>>();
	
	static {
		m_changeItemClas.put(HAPChangeItemNew.MYCHANGETYPE, HAPChangeItemNew.class);
		m_changeItemClas.put(HAPChangeItemPatch.MYCHANGETYPE, HAPChangeItemPatch.class);
		m_changeItemClas.put(HAPChangeItemDelete.MYCHANGETYPE, HAPChangeItemDelete.class);
	}
	
	public static HAPChangeItem parseChangeItem(JSONObject jsonObj) {
		String changeType = jsonObj.getString(HAPChangeItem.CHANGETYPE);
		HAPChangeItem out = null;
		try {
			out = m_changeItemClas.get(changeType).newInstance();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
}
