package com.nosliw.data.core.runtime.js.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.resource.HAPResourceDataImp;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;

@HAPEntityWithAttribute
public class HAPResourceDataJSLibrary extends HAPResourceDataImp{

	@HAPAttribute
	public static String URIS = "uris";

	private List<URI> m_uris;
	
	public HAPResourceDataJSLibrary(List<URI> uris){
		this.m_uris = new ArrayList<URI>();
		this.m_uris.addAll(uris);
	}
	
	public List<URI> getURIs(){		return this.m_uris;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(URIS, HAPUtilityJson.buildJson(this.m_uris.toArray(new URI[0]), HAPSerializationFormat.JSON_FULL));
	}

	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {  throw new RuntimeException();  }

}
