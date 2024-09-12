package com.nosliw.data.core.story;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPIdElementInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String CATEGARY = "categary";

	@HAPAttribute
	public static final String ID = "id";

	@HAPAttribute
	public static final String TYPE = "type";

	private String m_categary;
	
	private String m_id;
	
	private String m_type;
	
	public HAPIdElementInfo() {}
	
	public HAPIdElementInfo(String categary, String type, String id) {
		this.m_categary = categary;
		this.m_id = id;
		this.m_type = type;
	}
	
	public String getCategary() {    return this.m_categary;     }
	
	public String getId() {    return this.m_id;    }
	
	public String getType() {   return this.m_type;   }

}
