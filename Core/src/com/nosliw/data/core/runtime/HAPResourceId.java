package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPBasicUtility;

/**
 * Resource Id to identify resource 
 */
public class HAPResourceId extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String TYPE = "type";
	
	private String m_type;
	private String m_id;
	
	public HAPResourceId(String literate){
		this.buildObjectByLiterate(literate);
	}

	public HAPResourceId(String type, String id){
		this.m_type = type;
		this.m_id = id;
	}
	
	public String getId() {		return this.m_id;	}
	
	public String getType() {  return this.m_type;  }

	@Override
	protected String buildLiterate(){
		return HAPNamingConversionUtility.cascadeDetail(this.getType(), this.getId());
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPNamingConversionUtility.parseDetails(literateValue);
		this.m_type = segs[0];
		this.m_id = segs[1];
		return true;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof HAPResourceId){
			HAPResourceId resourceId = (HAPResourceId)o;
			return HAPBasicUtility.isEquals(this.m_type, resourceId.getType()) &&
					HAPBasicUtility.isEquals(this.m_id, resourceId.getId());
		}
		else{
			return false;
		}
	}
	
}
