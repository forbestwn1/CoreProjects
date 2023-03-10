package com.nosliw.common.test;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;

public abstract class HAPTestInfo extends HAPSerializableImp{
	//id used to identify test, it is unique globally
	private int m_id;
	
	//description infor for this test
	private HAPTestDescription m_description;

	//
	private int m_sequence;
	
	private boolean m_inited = false;;
	
	//parent test info
	private HAPTestInfo m_parentTestInfo;
	
	//test enviroment data for test suite
	private HAPTestEnv m_testEnv;
	
	public HAPTestInfo(HAPTestDescription description, int sequence, HAPTestEnv testEnv){
		this.m_description = description;
		this.m_sequence = sequence;
		this.m_id = HAPTestManager.getInstance().createId();
		if(testEnv==null)  this.m_testEnv = HAPTestManager.getInstance().createTestEnv();
		else   this.m_testEnv = testEnv;
	}

	public HAPTestInfo(HAPTestDescription description, int sequence){
		this(description, sequence, null);
	}

	abstract public String getType();
	abstract public HAPResult run(HAPResult parentResult);
	abstract public void beforeRunTest();

	/*
	 * update description info: update variable place holder
	 */
	public void resolveTestDescription(){
		if(this.getTestDescription()!=null){
			Map<String, Object> interpolateDatas = new LinkedHashMap<String, Object>();
			interpolateDatas.put(com.nosliw.common.interpolate.HAPPatternProcessorDocVariable.class.getName(), this.getTestEnv().getConfigure());
			interpolateDatas.put(com.nosliw.common.interpolate.HAPPatternProcessorDocExpression.class.getName(), this.getTestEnv().getValues());
			this.getTestDescription().resolveByPattern(interpolateDatas);
		}
	}
	
	public int getId(){ return this.m_id; }
	public HAPTestDescription getTestDescription(){  return this.m_description; }
	protected void setTestDescription(HAPTestDescription desc){  this.m_description = desc; }
	public int getSequence(){  return this.m_sequence;  }
	public String getDescription(){ return this.getTestDescription().getDescription(); }
	protected void setDescription(String description){ this.getTestDescription().updateAtomicChildStrValue(HAPTestDescription.ENTITY_PROPERTY_DESCRIPTION, description);}
	public String getName(){ return this.getTestDescription().getName(); }
	protected void setName(String name){ this.getTestDescription().updateAtomicChildStrValue(HAPTestDescription.ENTITY_PROPERTY_NAME, name);}
	public boolean inited(){ return this.m_inited; }
	protected void setInited(){  this.m_inited = true; }
	
	protected void setSequence(int seq){ this.m_sequence = seq; }
	
	protected HAPTestEnv getTestEnv(){ return this.m_testEnv; }
	public void softMergeTestEnv(HAPTestEnv testEnv){
		this.m_testEnv = this.m_testEnv.softMerge(testEnv);
	}

	protected HAPTestInfo getParentTestInfo(){ return this.m_parentTestInfo; }
	protected void setParentTestInfo(HAPTestInfo testInfo){  
		this.m_parentTestInfo = testInfo;
		//merge test env
		this.softMergeTestEnv(testInfo.getTestEnv());
	}
	
	protected HAPResult addToParentResult(HAPResultTestSuite parentResult, HAPResult result){
		HAPResult out = result;
		if(parentResult!=null){
			parentResult.addTestResult(result);
			out = parentResult;
		}
		return out;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof HAPTestInfo){
			HAPTestInfo t = (HAPTestInfo)obj;
			return HAPUtilityBasic.isEquals(this.getType(), t.getType()) && HAPUtilityBasic.isEquals(this.getName(), t.getName());
		}
		else return false;
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> jsonTypeMap){
		jsonMap.put("type", this.getType());
		jsonMap.put("id", String.valueOf(this.m_id));
		jsonMap.put("sequence", String.valueOf(this.m_sequence));
		jsonMap.put("inited", String.valueOf(this.m_inited));
		jsonMap.put("testDescription", this.m_description.toStringValue(HAPSerializationFormat.JSON));
	}
}
