package com.nosliw.data.core.runtime;

public abstract class HAPRuntimeTask {

	private HAPRuntimeTask m_parent;
	
	private String m_id;
	
	private Object m_result;
	
	public HAPRuntimeTask(){
	}
	
	public void setId(String id){  this.m_id = id; }
	
	public String getTaskId(){  
		return "Task__" + this.getTaskType() + "__" + this.m_id; 
	}
	
	public HAPRuntimeTask getParent(){		return this.m_parent;	}
	
	public void setParent(HAPRuntimeTask parent){
		this.m_parent = parent;
		this.m_id = this.m_parent.m_id + "__" + this.m_id;
	}

	public void setResult(Object result){ this.m_result = result;  }
	
	public Object getResult(){  return this.m_result;   }
	
	public void success(Object data){
		this.setResult(data);
		this.doSuccess();
		//invoke parent
		if(this.getParent()!=null){
			this.getParent().childSuccess(this);
		}
	}
	
	abstract protected void doSuccess();
	
	abstract public String getTaskType();
	
	protected void childSuccess(HAPRuntimeTask childTask){	}
	
}
