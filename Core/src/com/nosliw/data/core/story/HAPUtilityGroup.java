package com.nosliw.data.core.story;

import com.nosliw.common.utils.HAPNamingConversionUtility;

public class HAPUtilityGroup {

	public static String buildInsertElementPath(int index) {
		return HAPNamingConversionUtility.cascadeLevel1(new String[]{HAPElementGroup.ELEMENTS, index+""});
	}
	
	public static String buildAppendElementPath() {
		return HAPElementGroup.ELEMENTS;
	}
	
	public static String buildDeleteElementPath(int id) {
		return HAPNamingConversionUtility.cascadeLevel1(new String[]{HAPElementGroup.ELEMENTS, "-"+id+""});
	}	
	
//	public static HAPGroupElementPatchPathInfo parseGroupElementPath(String path) {
//		HAPGroupElementPatchPathInfo out = null;
//		int index = path.indexOf(HAPElementGroup.ELEMENT);
//		if(index!=-1) {
//			out = new HAPGroupElementPatchPathInfo();
//			String[] segs = HAPNamingConversionUtility.parseLevel1(path);
//			if(segs.length==1) {
//				out.isAdd = true;
//			}
//			else {
//				path.indexOf("-")
//			}
//		}
//		return out;
//	}
	
}
