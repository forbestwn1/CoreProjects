package com.nosliw.core.application.common.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPInfoValuePort;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPRootStructureInValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePort1111;
import com.nosliw.core.application.common.valueport.HAPValuePortImp;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort11111;

@HAPEntityWithAttribute
public class HAPInteractiveResultTask extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	private List<HAPResultElementInInteractiveTask> m_output;
	
	private HAPValuePort1111 m_internalValuePort;
	private HAPValuePort1111 m_externalValuePort;

	public HAPInteractiveResultTask(){
		this.m_output = new ArrayList<HAPResultElementInInteractiveTask>();
		this.m_internalValuePort = new HAPValuePortInteractiveResult1(m_output, HAPConstantShared.IO_DIRECTION_IN);
		this.m_externalValuePort = new HAPValuePortInteractiveResult1(m_output, HAPConstantShared.IO_DIRECTION_OUT);
	}

	public void addOutput(HAPResultElementInInteractiveTask output) {   this.m_output.add(output);   }
	public List<HAPResultElementInInteractiveTask> getOutput(){   return this.m_output;  }
	
	public HAPValuePort1111 getInternalValuePort() {	return this.m_internalValuePort;	}
	public HAPValuePort1111 getExternalValuePort() {	return this.m_externalValuePort;	}
	
	public HAPInteractiveResultTask cloneInteractiveResult() {
		HAPInteractiveResultTask out = new HAPInteractiveResultTask();
		this.cloneToInteractiveResult(out);
		return out;
	}
	
	protected void cloneToInteractiveResult(HAPInteractiveResultTask result) {
		this.cloneToEntityInfo(result);
		for(HAPResultElementInInteractiveTask output : this.m_output) {
			result.addOutput(output.cloneInteractiveOutput());
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			JSONArray outputArray = objJson.getJSONArray(OUTPUT);
			for(int i=0; i<outputArray.length(); i++) {
				HAPResultElementInInteractiveTask output = new HAPResultElementInInteractiveTask();
				output.buildObject(outputArray.get(i), HAPSerializationFormat.JSON);
				this.addOutput(output);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OUTPUT, HAPUtilityJson.buildJson(this.m_output, HAPSerializationFormat.JSON));
	}

}

class HAPValuePortInteractiveResult1 extends HAPValuePortImp{

	private List<HAPResultElementInInteractiveTask> m_output;
	
	public HAPValuePortInteractiveResult1(List<HAPResultElementInInteractiveTask> output, String ioDirection) {
		super(new HAPInfoValuePort(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT, ioDirection));
		this.m_output = output;
	}

	@Override
	public HAPValueStructureInValuePort11111 getValueStructureDefintion(String valueStructureId) {
		HAPValueStructureInValuePort11111 out = new HAPValueStructureInValuePort11111();
		for(HAPResultElementInInteractiveTask element : this.m_output) {
			HAPRootStructureInValuePort root = new HAPRootStructureInValuePort(new HAPElementStructureLeafData(element.getCriteria()));
			element.cloneToEntityInfo(root);
			out.addRoot(root);
		}
		return out;
	}

	@Override
	protected List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria,
			HAPConfigureResolveElementReference configure) {
		return Lists.asList(HAPConstantShared.NAME_DEFAULT, new String[0]);
	}

	@Override
	public void updateElement(HAPIdElement elementId, HAPElementStructure structureElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HAPResultReferenceResolve extendValueStructure(String valueStructureInValuePort, String elementPath,
			HAPElementStructure structureEle, HAPConfigureResolveElementReference configure) {
		// TODO Auto-generated method stub
		return null;
	}
}
