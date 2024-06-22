package com.nosliw.core.application.common.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPInfoValuePort;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.common.valueport.HAPRootStructureInValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePortImp;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort;

@HAPEntityWithAttribute
public class HAPInteractiveRequest extends HAPSerializableImp{

	@HAPAttribute
	public static String PARM = "parm";
	
	private List<HAPRequestParmInInteractive> m_requestParms;

	private HAPValuePort m_internalValuePort;
	private HAPValuePort m_externalValuePort;
	
	public HAPInteractiveRequest() {
		this.m_requestParms = new ArrayList<HAPRequestParmInInteractive>();
	}
	
	public HAPInteractiveRequest(List<HAPRequestParmInInteractive> requestParms) {
		this();
		this.m_requestParms = requestParms;
		this.initValuePort();
	}

	private void initValuePort() {
		this.m_internalValuePort = new HAPValuePortInteractiveRequest(this.m_requestParms, HAPConstantShared.IO_DIRECTION_BOTH);
		this.m_externalValuePort = new HAPValuePortInteractiveRequest(this.m_requestParms, HAPConstantShared.IO_DIRECTION_IN);
	}
	
	public List<HAPRequestParmInInteractive> getRequestParms() {   return this.m_requestParms;  }
	
	public HAPValuePort getInternalValuePort() {	return this.m_internalValuePort;	}
	
	public HAPValuePort getExternalValuePort() {	return this.m_externalValuePort;	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONArray parmsArray = (JSONArray)json;
		for(int i=0; i<parmsArray.length(); i++) {
			HAPRequestParmInInteractive parm = HAPRequestParmInInteractive.buildParmFromObject(parmsArray.get(i));
			m_requestParms.add(parm);
		}
		this.initValuePort();
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PARM, HAPManagerSerialize.getInstance().toStringValue(this.getRequestParms(), HAPSerializationFormat.JSON));
	}
}

class HAPValuePortInteractiveRequest extends HAPValuePortImp{

	private List<HAPRequestParmInInteractive> m_requestParms;
	
	public HAPValuePortInteractiveRequest(List<HAPRequestParmInInteractive> requestParms, String ioDirection) {
		super(new HAPInfoValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST, ioDirection));
		this.m_requestParms = requestParms;
	}

	@Override
	public HAPValueStructureInValuePort getValueStructureDefintion(String valueStructureId) {
		HAPValueStructureInValuePort out = new HAPValueStructureInValuePort();
		for(HAPRequestParmInInteractive parm : this.m_requestParms) {
			HAPRootStructureInValuePort root = new HAPRootStructureInValuePort(new HAPElementStructureLeafData(parm.getCriteria()));
			parm.cloneToEntityInfo(root);
			out.addRoot(root);
		}
		return out;
	}

	@Override
	protected List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria,
			HAPConfigureResolveElementReference configure) {
		return Lists.asList(HAPConstantShared.NAME_DEFAULT, new String[0]);
	}
}

