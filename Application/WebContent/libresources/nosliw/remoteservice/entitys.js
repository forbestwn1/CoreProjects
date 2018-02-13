//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/*
 * setting object for remote service task 
 */
var node_RemoteServiceSetting = function(mode){
	this.mode = mode;
};

/*
 * remote service request object (for ajax)
 */
var node_RemoteServiceRequest = function(serviceTask){
	//id of remote task
	this.id = nosliw.runtime.getIdService().generateId();
	//task id
	this.taskId = serviceTask.id;
	//type of request: group or normal
	this.type = serviceTask.type;
	//service : command and parms
	this.service = serviceTask.service;
	//children request for group request
	this.children = [];
	//unique id within nosliw client
	this.requestId = serviceTask.requestId;
	
	//build children request for group request
	if(this.type==node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_GROUP){
		for(var i in serviceTask.children){
			this.children.push(new node_RemoteServiceRequest(serviceTask.children[i]));
		}
	}
};

/*
 * remote request object --- normal
 */
var node_RemoteServiceTask = function(syncName, service, handlers, requestInfo, setting){
	//unique id
	this.id = nosliw.runtime.getIdService().generateId();
	//normal task
	this[node_COMMONATRIBUTECONSTANT.SERVICESERVLET_REQUEST_TYPE] = node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_NORMAL;
	//sync task name, different sync name have different configuration
	this.syncName = syncName;
	//parent requestInfo
	this.requestInfo = requestInfo;
	//service : command and parms
	this.service = service;
	//
	this.setting = setting;
	//handlers
	this.handlers = handlers;
	this.infos = {};
	//unique id within nosliw client
	this.requestId = nosliw.generateId();
	
	//used for ajax request
	this.remoteRequest = new node_RemoteServiceRequest(this); 
};

node_RemoteServiceTask.prototype = {
	getRemoteServiceRequest : function(){
		return this.remoteRequest;
	},
};


/*
 * a group of requests
 * 
 */
var node_RemoteServiceGroupTask = function(syncName, handlers, requestInfo, setting){
	//unique id
	this.id = nosliw.runtime.getIdService().generateId();
	//task type : group
	this[node_COMMONATRIBUTECONSTANT.SERVICESERVLET_REQUEST_TYPE] = node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_GROUP;
	//sync task name, different sync name have different configuration
	this.syncName = syncName;
	//parent requestInfo
	this.requestInfo = requestInfo;
	//
	this.setting = setting;
	//children remote service task
	this.children = [];
	//handlers
	this.handlers = handlers;
	this.metaData = {};
	this.infos = {	};
	//unique id within nosliw client
	this.requestId = nosliw.generateId();

	//used for ajax request
	this.remoteRequest = new node_RemoteServiceRequest(this); 
};

node_RemoteServiceGroupTask.prototype = {
	addTask : function(task){
		//mark task as group child
		task.type = node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_GROUPCHILD;
		this.children.push(task);
		this.remoteRequest.children.push(task.getRemoteServiceRequest());
	},
	
	setMetaData : function(name, data){
		this.metaData[name] = data;
	},
	
	getMetaData : function(name){
		return this.metaData[name];
	},
	
	getRemoteServiceRequest : function(){
		return this.remoteRequest;
	},
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("RemoteServiceSetting", node_RemoteServiceSetting); 
packageObj.createChildNode("RemoteServiceRequest", node_RemoteServiceRequest); 
packageObj.createChildNode("RemoteServiceTask", node_RemoteServiceTask); 
packageObj.createChildNode("RemoteServiceGroupTask", node_RemoteServiceGroupTask); 

})(packageObj);

