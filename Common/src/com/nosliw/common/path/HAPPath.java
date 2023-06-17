package com.nosliw.common.path;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPPath {

	private String m_path;
	
	private String[] m_pathSegs = new String[0];

	public HAPPath(){}

	public HAPPath(String path){
		if(HAPUtilityBasic.isStringNotEmpty(path)) {
			this.m_path = path;
			this.m_pathSegs = HAPUtilityNamingConversion.parsePaths(this.m_path);
		}
	}
	
	public boolean isEmpty() {  return this.m_pathSegs.length==0;    }
	
	public String getPath(){
		return this.m_path;
	}

	public String[] getPathSegments(){
		return this.m_pathSegs;
	}
	
	public int getLength() {  return this.m_pathSegs.length;  }
	
	public HAPPath appendSegment(String segment) {
		return new HAPPath(HAPUtilityNamingConversion.cascadePath(this.m_path, segment));
	}
	
	public HAPPath appendPath(HAPPath path) {
		return new HAPPath(HAPUtilityNamingConversion.cascadePath(this.m_path, path.m_path));
	}
	
	public HAPPath clonePath() {
		HAPPath out = new HAPPath(this.m_path);
		return out;
	}
	
	@Override
	public int hashCode() {
		if(this.m_path==null)  return 0;
		return this.m_path.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HAPPath) {
			HAPPath path = (HAPPath)obj;
			if(HAPUtilityBasic.isEquals(this.getPath(), path.getPath())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return this.getPath();
	}
}
