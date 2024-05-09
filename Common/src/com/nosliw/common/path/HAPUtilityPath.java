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
		
		int index = 0;
		StringBuffer out = new StringBuffer();
		for(int j=i; j<baseEntityIdPathSegs.length; j++) {
			if(index!=0) {
				out.append(HAPConstantShared.SEPERATOR_LEVEL2);
			}
			out.append(HAPConstantShared.NAME_PARENT);
			index++;
		}
		
		for(int j=i; j<entityIdPathSegs.length; j++) {
			if(index!=0) {
				out.append(HAPConstantShared.SEPERATOR_LEVEL2);
			}
			out.append(HAPConstantShared.NAME_CHILD);
			out.append(HAPConstantShared.SEPERATOR_LEVEL1);
			out.append(entityIdPathSegs[j]);
			index++;
		}
		return out.toString();
	}
	
	public static String fromRelativeToAbsolutePath(String relativePath, String basePath) {
		HAPPath path = new HAPPath(basePath);
		String[] segs = relativePath.split("\\"+HAPConstantShared.SEPERATOR_LEVEL2);
		for(String seg : segs) {
			if(seg.startsWith(HAPConstantShared.NAME_PARENT)) {
				path = path.trimLast();
			}
			else if(seg.startsWith(HAPConstantShared.NAME_CHILD)) {
				String[] ss = seg.split("\\"+HAPConstantShared.SEPERATOR_LEVEL1);
				path = path.appendSegment(ss[1]);
			}
		}
		return path.toString();
	}
	
	public static void main(String[] args) {
		String basePath = "a.b.c";
		debug("", basePath);
		debug("a.b.c.d.e.f", basePath);
		debug("a.b.x.y.z", basePath);
	}
	
	private static void debug(String absolutePath, String basePath) {
		System.out.println();
		System.out.println("--------------------------------");
		String relativePath = fromAbsoluteToRelativePath(absolutePath, basePath);
		System.out.println(relativePath);
		System.out.println(fromRelativeToAbsolutePath(relativePath, basePath));
		System.out.println();
	}
	
}
