package com.nosliw.data.core.runtime.js;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.runtime.HAPResourceData;

@HAPEntityWithAttribute
public class HAPResourceDataJSLibrary extends HAPSerializableImp implements HAPResourceData{

	@HAPAttribute
	public static String URIS = "uris";

	private List<URI> m_uris;
	
	public HAPResourceDataJSLibrary(List<URI> uris){
		this.m_uris = new ArrayList<URI>();
		this.m_uris.addAll(uris);
	}
	
	public List<URI> getURIs(){		return this.m_uris;	}

	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(URIS, HAPJsonUtility.buildJson(this.m_uris.toArray(new URI[0]), HAPSerializationFormat.JSON_FULL));
	}
	
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

}
