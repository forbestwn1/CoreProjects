package com.nosliw.core.application.division.story;

import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.division.story.xxx.brick.HAPStoryElementGroup;

public class HAPStoryUtilityGroup {

	public static String buildInsertElementPath(int index) {
		return HAPUtilityNamingConversion.cascadeLevel1(new String[]{HAPStoryElementGroup.ELEMENTS, index+""});
	}
	
	public static String buildAppendElementPath() {
		return HAPStoryElementGroup.ELEMENTS;
	}
	
	public static String buildDeleteElementPath(int id) {
		return HAPUtilityNamingConversion.cascadeLevel1(new String[]{HAPStoryElementGroup.ELEMENTS, "-"+id+""});
	}	
	
//	public static HAPGroupElementPatchPathInfo parseGroupElementPath(String path) {
//		HAPGroupElementPatchPathInfo out = null;
//		int index = path.indexOf(HAPStoryElementGroup.ELEMENT);
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
