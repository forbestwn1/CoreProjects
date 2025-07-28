package com.nosliw.entity.query;

/*
 * used to describe a part of query result
 */
public class HAPQuerySegmentInfo {
	//total query result size
	public int size;
	
	//start and end index for this segment
	public int from;
	public int to;

	
	public void reduce(){
		this.size--;
		this.to--;
	}
	
	public void increase(){
		this.size++;
		this.to++;
	}
	
	public boolean ifWithinSegment(int index){
		return index>=from && index<=to;
	}
}
