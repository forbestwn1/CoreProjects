package com.nosliw.common.utils;

/*
 * class to parse the path information
 */
public class HAPSegmentParser {

	private String[] m_segments;
	private String m_seperator;
	private int m_index;
	private boolean m_isEmpty = false;
	
	public HAPSegmentParser(String path, String seperator){
		if(HAPBasicUtility.isStringEmpty(path))  this.m_isEmpty = true;
		else{
			this.m_seperator = seperator;
			if(seperator.equals("."))  seperator="\\.";
			m_segments = path.split(seperator);
			m_index = 0;
		}
	}

	public HAPSegmentParser(String path){
		this(path, HAPConstant.SEPERATOR_PATH);
	}

	public boolean isEmpty(){return this.m_isEmpty;}
	
	public String next(){
		if(!this.hasNext())  return null;
		String out = this.m_segments[m_index];
		m_index++;
		return out;
	}
	
	public boolean hasNext(){
		if(this.isEmpty())  return false;
		if(m_index>=this.getSegmentSize())  return false;
		return true;
	}
	
	public String[] getSegments(){
		if(this.isEmpty())  return new String[0];
		return this.m_segments;
	}
	
	public String getRestPath(){
		if(this.isEmpty())  return null;
		StringBuffer out = new StringBuffer();
		for(int i=m_index; i<this.getSegmentSize(); i++){
			if(i!=m_index)  out.append(this.m_seperator);
			out.append(this.m_segments[i]);
		}
		return out.toString();
	}
	
	public String getPreviousPath(){
		if(this.isEmpty())  return null;
		StringBuffer out = new StringBuffer();
		for(int i=0; i<this.m_index; i++){
			if(i!=0)  out.append(this.m_seperator);
			out.append(this.m_segments[i]);
		}
		return out.toString();
	}
	
	public int getSegmentSize(){
		if(this.isEmpty())  return 0;
		return this.m_segments.length;
	}
}
