package com.nosliw.common.path;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPUtilityPath {

	public static String fromAbsoluteToRelativePath(String absolutePath, String basePath) {
		String[] baseEntityIdPathSegs = new HAPPath(basePath).getPathSegments();
		String[] entityIdPathSegs = new HAPPath(absolutePath).getPathSegments();
		
		int i=0;
		for(; i<baseEntityIdPathSegs.length; i++) {
			if(i>=entityIdPathSegs.length) {
				break;
			} else if(!baseEntityIdPathSegs[i].equals(entityIdPathSegs[i])) {
				break;
			}
		}
		
		StringBuffer out = new StringBuffer();
		for(int j=i; j<baseEntityIdPathSegs.length; j++) {
			if(j!=i) {
				out.append(HAPConstantShared.SEPERATOR_LEVEL2);
			}
			out.append(HAPConstantShared.NAME_PARENT);
		}
		
		for(int j=i; j<entityIdPathSegs.length; j++) {
			if(j!=i) {
				out.append(HAPConstantShared.SEPERATOR_LEVEL2);
			}
			out.append(HAPConstantShared.NAME_CHILD);
			out.append(HAPConstantShared.SEPERATOR_LEVEL1);
			out.append(entityIdPathSegs[j]);
		}
		return out.toString();
	}
	
	public static String fromRelativeToAbsolutePath(String basePath, String relativePath) {
		
	}
	
}
