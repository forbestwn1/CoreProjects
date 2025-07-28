package com.nosliw.entity.operation;

public class HAPEntityOperationId {

	private static long m_idSerial = System.currentTimeMillis();

	private long m_id;
	
	public HAPEntityOperationId(){
		this.m_id = m_idSerial;
		m_idSerial++;
	}
	
	public HAPEntityOperationId(long id){
		this.m_id = id;
	}

	
	public boolean equal(HAPEntityOperationId id){
		if(this.m_id == id.m_id)  return true;
		else return false;
	}
}
