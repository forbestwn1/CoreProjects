package com.nosliw.data.core.event;

import java.util.Map;

public class HAPEventProcessManager {

	private Map<String, HAPEventTaskInfo> m_tasks;

	public void unregisterEventTask(String taskId) {
		this.m_tasks.remove(taskId);
	}

	public void registerEventTask(HAPEventTaskInfo task) {
		String taskId = task.getId();
		this.m_tasks.put(taskId, task);
		task.getEventSource().registerListener(new HAPEventSourceListener() {

			@Override
			public void onEvent(HAPEvent event) {
				processEvent(task, event);
			}

			@Override
			public void onDestroy() {
				m_tasks.remove(taskId);
			}
		});
		
	}

	private void processEvent(HAPEventTaskInfo taskInfo, HAPEvent event) {
	}
	
}
