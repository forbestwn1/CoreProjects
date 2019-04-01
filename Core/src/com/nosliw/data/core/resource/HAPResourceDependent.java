package com.nosliw.data.core.resource;

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
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

/**
 * This class is for resource that other resource have dependent on it
 * It has alias so that it can be refered by resource through alias 
 */
@HAPEntityWithAttribute
public class HAPResourceDependent extends HAPSerializableImp{

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
	
	public HAPResourceDependent(){	}

	public HAPResourceDependent(HAPResourceId resourceId){
		this.m_id = resourceId;
	}

	public HAPResourceDependent(HAPResourceId resourceId, String alias){
		this(resourceId);
		if(HAPBasicUtility.isStringNotEmpty(alias))		this.m_alias.add(alias);
	}

	public HAPResourceId getId(){  return this.m_id;  }
	
	public Set<String> getAlias(){  return this.m_alias;  }
	public void addAlias(String alias){		this.m_alias.add(alias);	}
	private void setAlias(String aliasLiterate){		this.m_alias = new HashSet<String>((List<String>)HAPLiterateManager.getInstance().stringToValue(aliasLiterate, HAPConstant.STRINGABLE_ATOMICVALUETYPE_ARRAY, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING));	}
	
	public void addAlias(Collection alias){		this.m_alias.addAll(alias);	}
	
	public void removeAlias(String alias){		this.m_alias.remove(alias);	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, HAPJsonUtility.buildArrayJson(this.getAlias().toArray(new String[0])));
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
		String idLiterate = HAPSerializeManager.getInstance().toStringValue(this.m_id, HAPSerializationFormat.LITERATE);
		return HAPNamingConversionUtility.cascadeLevel3(new String[]{idLiterate, aliasLiterate});
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPNamingConversionUtility.parseLevel3(literateValue);
		this.m_id = HAPResourceHelper.getInstance().buildResourceIdObject(segs[0]);
		if(segs.length>=2)   this.setAlias(segs[1]);
		return true;  
	}
	
	public HAPResourceDependent clone(){
		HAPResourceDependent out = new HAPResourceDependent();
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceDependent resourceId){
		this.m_id = resourceId.getId().clone();
		this.m_alias.addAll(resourceId.m_alias);
	}
	
}
