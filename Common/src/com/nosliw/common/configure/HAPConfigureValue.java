package com.nosliw.common.configure;

import java.util.List;

public interface HAPConfigureValue{

	public String getStringContent();

	public String getStringValue();

	public Boolean getBooleanValue();

	public Integer getIntegerValue(); 

	public Float getFloatValue();

	public <T> List<T> getListValue(Class<T> cs);
	
	public HAPConfigureValue clone();
}
