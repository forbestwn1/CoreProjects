package com.nosliw.common.constant;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPSystemUtility;

public class HAPConstantUtility {

	static public String getBaseName(Class cs){
		String out = null;
		if(cs.isAnnotationPresent(HAPEntityWithAttribute.class)){
			HAPEntityWithAttribute entityWithAttr = (HAPEntityWithAttribute)cs.getAnnotation(HAPEntityWithAttribute.class);
			out = entityWithAttr.baseName();
			if(HAPBasicUtility.isStringEmpty(out)){
				out = HAPSystemUtility.getHAPBaseClassName(cs);
			}
		}
		else{
			Class superCs = cs.getSuperclass();
			out = getBaseName(superCs);
		}
		
		if(out!=null)  out = out.toUpperCase();
		return out;
	}
	
}
