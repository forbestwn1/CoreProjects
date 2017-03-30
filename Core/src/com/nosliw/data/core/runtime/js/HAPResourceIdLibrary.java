package com.nosliw.data.core.runtime.js;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdLibrary extends HAPResourceId{

	private String m_name;
	
	private String m_version;

	public HAPResourceIdLibrary(String idLiterate, String alias){
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY, idLiterate, alias);
	}

	public HAPResourceIdLibrary(String name, String version, String alias){
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY, null, alias);
		this.m_id = HAPNamingConversionUtility.cascadeSegments(name, version);
		this.m_name = name;
		this.m_version = version;
	}
	
	@Override
	protected void setId(String id){
		super.setId(id);
		String[] segs = HAPNamingConversionUtility.parseSegments(id);
		this.m_name = segs[0];
		if(segs.length>=2){
			this.m_version = segs[1];
		}
	}
	
	public String getName(){		return this.m_name;	}
	
	public String getVersion(){   return this.m_version;  }
	
}
