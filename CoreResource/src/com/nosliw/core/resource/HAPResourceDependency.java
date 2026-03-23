package com.nosliw.core.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

/**
 * This class is for resource that other resource have dependent on it
 * It has alias so that it can be refered by resource through alias 
 */
@HAPEntityWithAttribute
public class HAPResourceDependency extends HAPSerializableImp{

	//alias is used when one resource A depend on another resource B
	//we can give alias to resource B so that resource A can access resource B using alias
	//resource A does not need alias, only resource B need alias
	//resource B can have multiple alias
	@HAPAttribute
	public static String ALIAS = "alias";

	@HAPAttribute
	public static String ID = "id";
	
	protected Set<String> m_alias = new HashSet<String>();

	private HAPResourceId m_id;
	
	public HAPResourceDependency(){	}

	public HAPResourceDependency(HAPResourceId resourceId){
		this.m_id = resourceId;
	}

	public HAPResourceDependency(HAPResourceId resourceId, String alias){
		this(resourceId);
		if(HAPUtilityBasic.isStringNotEmpty(alias))		this.m_alias.add(alias);
	}

	public HAPResourceId getId(){  return this.m_id;  }
	
	public Set<String> getAlias(){  return this.m_alias;  }
	public void addAlias(String alias){		this.m_alias.add(alias);	}
	private void setAlias(String aliasLiterate){		this.m_alias = new HashSet<String>((List<String>)HAPLiterateManager.getInstance().stringToValue(aliasLiterate, HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_ARRAY, HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_STRING));	}
	
	public void addAlias(Collection alias){		this.m_alias.addAll(alias);	}
	
	public void removeAlias(String alias){		this.m_alias.remove(alias);	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, HAPUtilityJson.buildArrayJson(this.getAlias().toArray(new String[0])));
	}

	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = HAPResourceHelper.getInstance().buildResourceIdObject(jsonObj.optJSONObject(ID));
		
		JSONArray alaisArray = jsonObj.optJSONArray(ALIAS);
		for(int i=0; i<alaisArray.length(); i++){
			String aliais = alaisArray.optString(i);
			this.m_alias.add(aliais);
		}
		return true; 
	}
	
	@Override
	protected String buildLiterate(){
		String aliasLiterate = HAPLiterateManager.getInstance().valueToString(this.m_alias);
		String idLiterate = HAPManagerSerialize.getInstance().toStringValue(this.m_id, HAPSerializationFormat.LITERATE);
		return HAPUtilityNamingConversion.cascadeLevel3(new String[]{idLiterate, aliasLiterate});
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPUtilityNamingConversion.parseLevel3(literateValue);
		this.m_id = HAPResourceHelper.getInstance().buildResourceIdObject(segs[0]);
		if(segs.length>=2)   this.setAlias(segs[1]);
		return true;  
	}
	
	public HAPResourceDependency cloneResourceDependency(){
		HAPResourceDependency out = new HAPResourceDependency();
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceDependency resourceId){
		this.m_id = resourceId.getId().clone();
		this.m_alias.addAll(resourceId.m_alias);
	}
	
}
