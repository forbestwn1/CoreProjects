package com.nosliw.core.application.division.story.change;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPStoryParserChange {

	private static final Map<String, Class<? extends HAPStoryChangeItem>> m_changeItemClas = new LinkedHashMap<String, Class<? extends HAPStoryChangeItem>>();
	
	static {
		m_changeItemClas.put(HAPStoryChangeItemNew.MYCHANGETYPE, HAPStoryChangeItemNew.class);
		m_changeItemClas.put(HAPStoryChangeItemPatch.MYCHANGETYPE, HAPStoryChangeItemPatch.class);
		m_changeItemClas.put(HAPStoryChangeItemDelete.MYCHANGETYPE, HAPStoryChangeItemDelete.class);
		m_changeItemClas.put(HAPStoryChangeItemAlias.MYCHANGETYPE, HAPStoryChangeItemAlias.class);
		m_changeItemClas.put(HAPStoryChangeItemStoryInfo.MYCHANGETYPE, HAPStoryChangeItemStoryInfo.class);
	}
	
	public static HAPStoryChangeItem parseChangeItem(JSONObject jsonObj) {
		String changeType = jsonObj.getString(HAPStoryChangeItem.CHANGETYPE);
		HAPStoryChangeItem out = null;
		try {
			out = m_changeItemClas.get(changeType).newInstance();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
}
