package com.nosliw.common.path;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPPath {

	private String m_path;
	
	private String[] m_pathSegs = new String[0];

	public HAPPath(){}

	public HAPPath(HAPPath path) {
		if(!(path==null||path.isEmpty())){
			init(path.getPath());
		}
	}
	
	public HAPPath(String path){
		if(HAPUtilityBasic.isStringNotEmpty(path)) {
			init(path);
		}
	}
	
	private void init(String path) {
		this.m_path = path;
		this.m_pathSegs = HAPUtilityNamingConversion.parsePaths(this.m_path);
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
	
	public HAPPath getRemainingPath(int startIndex) {
		HAPPath out = new HAPPath();
		for(int i=startIndex; i<this.m_pathSegs.length; i++) {
			out.appendSegment(m_pathSegs[i]);
		}
		return out;
	}
	
	public Pair<HAPPath, String> trimLast() {
		HAPPath path = new HAPPath();
		for(int i=0; i<this.m_pathSegs.length-1; i++) {
			path = path.appendSegment(m_pathSegs[i]);
		}
		return Pair.of(path, this.m_pathSegs[this.m_pathSegs.length-1]);
	}
	
	public HAPPath clonePath() {
		HAPPath out = new HAPPath(this.m_path);
		return out;
	}
	
	@Override
	public int hashCode() {
		if(this.m_path==null) {
			return 0;
		}
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
		return this.getPath()!=null?this.getPath():"";
	}
}
