package com.nosliw.data.core.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

/**
 * Resource Id to identify resource 
 */
public class HAPResourceId extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String TYPE = "type";
	
	@HAPAttribute
	public static String ALIAS = "alias";
	
	protected String m_type;
	protected String m_id;
	protected Set<String> m_alias;
	
	public HAPResourceId(){
		this.m_alias = new HashSet<String>();
	}
	
	public HAPResourceId(String type, String id, String alias){
		this.m_type = type;
		this.m_alias = new HashSet<String>((List<String>)HAPLiterateManager.getInstance().stringToValue(alias, HAPConstant.STRINGABLE_ATOMICVALUETYPE_ARRAY, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING));
		this.setId(id);
	}
	
	public String getId() {		return this.m_id;	}
	
	public String getType() {  return this.m_type;  }

	public Set<String> getAlias(){  return this.m_alias;  }
	public void addAlias(String alias){
		this.m_alias.add(alias);
	}
	
	public void addAlias(Collection alias){
		this.m_alias.addAll(alias);
	}
	
	public void removeAlias(String alias){
		this.m_alias.remove(alias);
	}
	
	protected void setId(String id){  this.m_id = id; }
	
	@Override
	protected String buildLiterate(){
		String aliasLiterate = HAPLiterateManager.getInstance().valueToString(this.m_alias);
		return HAPNamingConversionUtility.cascadeDetail(new String[]{this.getType(), this.getId(), aliasLiterate});
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof HAPResourceId){
			HAPResourceId resourceId = (HAPResourceId)o;
			return HAPBasicUtility.isEquals(this.getType(), resourceId.getType()) &&
					HAPBasicUtility.isEquals(this.getId(), resourceId.getId());
		}
		else{
			return false;
		}
	}
	
	public HAPResourceId clone(){
		HAPResourceId out = new HAPResourceId();
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceId resourceId){
		this.m_id = resourceId.m_id;
		this.m_type = resourceId.m_type;
		this.m_alias.addAll(resourceId.m_alias);
	}
}
