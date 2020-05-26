package com.nosliw.miniapp.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.common.utils.HAPBasicUtility;

public class HAPUserMiniAppInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String GROUP = "group";

	@HAPAttribute
	public static String MINIAPP = "miniApp";
	
	@HAPAttribute
	public static String GROUPMINIAPP = "groupMiniApp";

	private List<HAPGroup> m_groups;
	
	private List<HAPMiniApp> m_miniApps;
	
	private LinkedHashMap<String, HAPUserGroupMiniApp> m_groupMiniApp;
	
	public HAPUserMiniAppInfo() {
		this.m_groups = new ArrayList<HAPGroup>();
		this.m_miniApps = new ArrayList<HAPMiniApp>();
		this.m_groupMiniApp = new LinkedHashMap<String, HAPUserGroupMiniApp>();
	}

	public void addGroup(HAPGroup group) {  
		this.m_groups.add(group);
		this.m_groupMiniApp.put(group.getId(), new HAPUserGroupMiniApp(group));
	}
	public void addGroups(List<HAPGroup> groups) {
		for(HAPGroup group : groups)  this.addGroup(group);
	}
	
	public List<HAPGroup> getGroups(){   return this.m_groups;   }
	
	public void addMiniApps(List<HAPMiniApp> miniApps, String group) {
		for(HAPMiniApp miniApp : miniApps) {
			this.addMiniApp(miniApp, group);
		}
	}
	public void addMiniApp(HAPMiniApp miniApp, String group) {
		if(HAPBasicUtility.isStringEmpty(group)) {
			this.m_miniApps.add(miniApp);	
		}
		else {
//			if(HAPConstant.MINIAPP_DATAOWNER_GROUP.equals(miniApp.getCategary())){
//				//app data belong to group
//				miniApp.setDataOwnerId(group);
//			}
			this.m_groupMiniApp.get(group).addMiniApp(miniApp);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUP, HAPJsonUtility.buildJson(this.m_groups, HAPSerializationFormat.JSON));
		jsonMap.put(MINIAPP, HAPJsonUtility.buildJson(m_miniApps, HAPSerializationFormat.JSON));
		
		List<HAPUserGroupMiniApp> groupApps = new ArrayList<HAPUserGroupMiniApp>();
		for(HAPGroup group : this.m_groups) {
			groupApps.add(this.m_groupMiniApp.get(group.getId()));
		}
//		jsonMap.put(GROUPMINIAPP, HAPJsonUtility.buildJson(m_groupMiniApp.values().toArray(), HAPSerializationFormat.JSON));
		jsonMap.put(GROUPMINIAPP, HAPJsonUtility.buildJson(groupApps, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_groups = HAPSerializeUtility.buildListFromJsonArray(HAPGroup.class.getName(), jsonObj.optJSONArray(GROUP));
		this.m_miniApps = HAPSerializeUtility.buildListFromJsonArray(HAPMiniApp.class.getName(), jsonObj.optJSONArray(MINIAPP));
		JSONArray groupMinAppArray = jsonObj.optJSONArray(GROUPMINIAPP);
		if(groupMinAppArray!=null) {
			for(int i=0; i<groupMinAppArray.length(); i++) {
				//kkkk
			}
		}
		return true;
	}

}
