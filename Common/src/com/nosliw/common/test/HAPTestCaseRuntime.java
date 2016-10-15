package com.nosliw.common.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerialiableImp;
import com.nosliw.common.utils.HAPJsonUtility;

/*
 * this class store all the information execution related with test case
 */
public class HAPTestCaseRuntime extends HAPSerialiableImp{
	private Class m_testCaseClass;
	
	private List<Method> m_testCaseBeforeMethods;
	
	private List<Method> m_testCaseAfterMethods;

	//method to run the test case directly
	private Method m_testCaseMethod;
	
	//factory method to create test case items
	private Method m_testCaseItemFactoryMethod;
	
	
	public HAPTestCaseRuntime(Class testCaseClass, Method testCaseMethod, List<Method> testCaseBeforeMethods, List<Method> testCaseAfterMethods, boolean isTestCaseMethod){
		this.m_testCaseClass = testCaseClass;
		this.m_testCaseBeforeMethods = testCaseBeforeMethods;
		this.m_testCaseAfterMethods = testCaseAfterMethods;
		if(isTestCaseMethod)		this.m_testCaseMethod = testCaseMethod;
		else   this.m_testCaseItemFactoryMethod = testCaseMethod;
	}

	public String getMethodName(){
		if(this.m_testCaseMethod!=null)		return this.m_testCaseMethod.getName();
		if(this.m_testCaseItemFactoryMethod!=null)  return this.m_testCaseItemFactoryMethod.getName();
		return null;
	}
	
	/*
	 * create test case object what 
	 */
	public Object getTestCaseObject(HAPTestEnv testEnv){
		Object out = null;
		try{
			out = this.m_testCaseClass.newInstance();
			if(this.m_testCaseBeforeMethods!=null){
				for(Method method : this.m_testCaseBeforeMethods){
					method.invoke(out, testEnv);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	public HAPResultTestCase run(HAPResultTestCase result, HAPTestEnv testEnv){
		try{
			Object object = this.getTestCaseObject(testEnv); 
			if(object!=null){
				if(this.m_testCaseMethod!=null){
					//execute test case method directly
					this.m_testCaseMethod.invoke(object, result, testEnv);
				}
				else if(this.m_testCaseItemFactoryMethod!=null){
					//through test items
					List<HAPTestItemInfo> testItemInfos = (List<HAPTestItemInfo>)this.m_testCaseItemFactoryMethod.invoke(object, testEnv);
					for(int i=0; i<testItemInfos.size(); i++){
						HAPTestItemInfo testItem = testItemInfos.get(i);
						testItem.setSequence(i);
						testItem.beforeRunTest();
						testItem.run(result);
					}
				}
				
				if(this.m_testCaseAfterMethods!=null){
					for(Method method : this.m_testCaseAfterMethods){
						method.invoke(object, testEnv);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.addException(e);
		}
		return result;
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put("className", this.m_testCaseClass.getName());
		if(this.m_testCaseMethod!=null) jsonMap.put("testMethod", this.m_testCaseMethod.getName());
		if(this.m_testCaseItemFactoryMethod!=null)  jsonMap.put("testItemFactoryMethod", this.m_testCaseItemFactoryMethod.getName());
		
		List<String> beforeJson = new ArrayList<String>();
		for(Method m : this.m_testCaseBeforeMethods)	beforeJson.add(m.getName());
		jsonMap.put("beforeMethods", HAPJsonUtility.buildArrayJson(beforeJson.toArray(new String[0])));
		
		List<String> afterJson = new ArrayList<String>();
		for(Method m : this.m_testCaseAfterMethods)	afterJson.add(m.getName());
		jsonMap.put("afterMethods", HAPJsonUtility.buildArrayJson(afterJson.toArray(new String[0])));
	}
}
