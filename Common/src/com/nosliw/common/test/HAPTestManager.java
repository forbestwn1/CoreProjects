package com.nosliw.common.test;

import com.nosliw.common.configure.HAPConfigurableImp;

public class HAPTestManager extends HAPConfigurableImp{
	private static HAPTestManager m_instance;

	private HAPTestSuiteInfo m_rootTestSuite;
	
	//id seed
	private int m_id;
	
	private HAPTestManager(){
		super(null);
		this.m_id = 0;
	}
	
	public static HAPTestManager getInstance(){
		if(m_instance==null){
			m_instance = new HAPTestManager();
		}
		return m_instance;
	}
	
	public int createId(){
		return this.m_id++;
	}
	
	public HAPTestEnv createTestEnv(){
		HAPTestEnv out = new HAPTestEnv(this.getConfiguration());
		return out;
	}
}
