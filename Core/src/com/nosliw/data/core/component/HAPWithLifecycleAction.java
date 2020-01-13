package com.nosliw.data.core.component;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithLifecycleAction {

	@HAPAttribute
	public static String LIFECYCLE = "lifecycle";
	
	public Set<HAPHandlerLifecycle> getLifecycleAction();
	
	public void addLifecycleAction(HAPHandlerLifecycle lifecycleAction);
}
