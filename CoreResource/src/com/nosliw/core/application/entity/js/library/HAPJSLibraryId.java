package com.nosliw.core.application.entity.js.library;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPJSLibraryId  extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String VERSION = "version";
	
	private String m_name;
	
	private String m_version;

	public HAPJSLibraryId(String literate){
		this.buildObjectByLiterate(literate);
	}
	
	public HAPJSLibraryId(String name, String version){
		this.m_name = name;
		this.m_version = version;
	}
	
	public String getName(){ return this.m_name; }
	public String getVersion(){  return this.m_version;  }
	
	@Override
	protected String buildLiterate(){
		return HAPUtilityNamingConversion.cascadeLevel1(this.m_name, this.m_version);
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPUtilityNamingConversion.parseLevel1(literateValue);
		this.m_name = segs[0];
		if(segs.length>=2){
			this.m_version = segs[1];
		}
		return true;
	}
}
