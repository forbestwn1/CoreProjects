package com.nosliw.core.application.brick.interactive.interfacee;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.division.manual.executable.HAPBrickBlockSimple;

@HAPEntityWithAttribute
public class HAPBlockInteractiveInterface extends HAPBrickBlockSimple{

	@HAPAttribute
	public static String VALUE = "value";

	@Override
	public void init() {
		this.setAttributeValueWithValue(VALUE, new HAPInteractiveTask());;
	}
	
	public HAPInteractiveTask getValue(){	return (HAPInteractiveTask)this.getAttributeValueOfValue(VALUE);	}
	
	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		out.addValuePortGroup(this.getValue().getExternalValuePortGroup(), true);
		return out;
	}
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		out.addValuePortGroup(this.getValue().getInternalValuePortGroup(), true);
		return out;
	}
	
	@Override
	protected boolean buildBrickFormatJson(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		this.getValue().buildObject(jsonObj, HAPSerializationFormat.JSON);
		return true;
	}

}
