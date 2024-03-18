package com.nosliw.data.core.structure.reference;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.structure.HAPElementStructure;

@HAPEntityWithAttribute
public class HAPInfoDesendantResolve extends HAPSerializableImp{

	@HAPAttribute
	public static final String RESOLVEDELEMENT = "resolvedElement";

	@HAPAttribute
	public static final String SOLVEDPATH = "solvedPath";

	@HAPAttribute
	public static final String REMAINPATH = "remainPath";

	//solved element 
	public HAPElementStructure resolvedElement;
	//solved path to element
	public HAPPath solvedPath;
	//unsolved path part
	public HAPPath remainPath = new HAPPath();
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RESOLVEDELEMENT, this.resolvedElement.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(SOLVEDPATH, this.solvedPath.toString());
		jsonMap.put(REMAINPATH, this.remainPath.toString());
	}
	
}
