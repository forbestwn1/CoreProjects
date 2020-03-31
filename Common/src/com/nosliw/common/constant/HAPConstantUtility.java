package com.nosliw.common.constant;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.clss.HAPClassProcessor;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPNosliwUtility;

public class HAPConstantUtility {

	static public String getBaseName(Class cs){
		String out = null;
		if(cs.isAnnotationPresent(HAPEntityWithAttribute.class)){
			HAPEntityWithAttribute entityWithAttr = (HAPEntityWithAttribute)cs.getAnnotation(HAPEntityWithAttribute.class);
			String parent = entityWithAttr.parent();
			if(HAPBasicUtility.isStringEmpty(parent)){
				out = entityWithAttr.baseName();
				if(HAPBasicUtility.isStringEmpty(out)){
					//if not defined, then use class name as base name
					out = HAPNosliwUtility.getHAPBaseClassName(cs);
				}
			}
			else{
				//if parent is defined, get base name from parent
				try {
					out = getBaseName(Class.forName(parent));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		else{
			//get base from parent class first
			Class superCs = cs.getSuperclass();
			if(superCs!=null){
				out = getBaseName(superCs);
				if(HAPBasicUtility.isStringEmpty(out)){
					//if not found, then get from interface
					Class[] infs = cs.getInterfaces();
					for(Class inf : infs){
						out = getBaseName(inf);
						if(HAPBasicUtility.isStringNotEmpty(out))  break;
					}
				}
			}
		}
		
		if(out!=null)  out = out.toUpperCase();
		return out;
	}

	static public Set<String> getAttributes(Class cs){
		Set<String> out = new HashSet<String>();
		new HAPClassProcessor(){
			@Override
			protected void processClass(Class cls, Object data) {
				Set<String> d = (Set<String>)data;
				Field[] fields = cls.getDeclaredFields();
				for(Field field : fields){
					String fieldName = field.getName();
					if(field.isAnnotationPresent(HAPAttribute.class)){
						d.add(fieldName);
					}
				}
			}

			@Override
			protected boolean isValid(Class cls) {
				return true;
			}
		}.process(cs, out);
		return out;
	}
}
