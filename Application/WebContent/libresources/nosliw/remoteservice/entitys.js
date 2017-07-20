//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
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
	this.type = serviceTask.type;
	this.service = serviceTask.service;
	this.children = [];
	this.requestId = serviceTask.requestId;
	
	if(this.type==NOSLIWCOMMONCONSTANT.CONS_REMOTESERVICE_TASKTYPE_GROUP){
		for(var i in serviceTask.children){
			this.children.push(new NosliwRemoteServiceRequest(serviceTask.children[i]));
		}
	}
};

/*
 * single request object
 */
var node_RemoteServiceTask = function(syncName, service, handlers, requestInfo, setting){
	this[NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_REQUEST_TYPE] = NOSLIWCOMMONCONSTANT.CONS_REMOTESERVICE_TASKTYPE_NORMAL;
	this.syncName = syncName;
	this.requestInfo = requestInfo;
	this.service = service;
	this.setting = setting;
	this.handlers = handlers;
	this.infos = {
	};
	this.requestId = nosliw.generateId();
	
	//used for ajax request
	this.remoteRequest = new NosliwRemoteServiceRequest(this); 
};

node_RemoteServiceTask.prototype = {
	setServiceParm : function(name, value){
		this.service[name]=value;
	},
	
	getRemoteServiceRequest : function(){
		return this.remoteRequest;
	},
};


/*
 * a group of requests
 * 
 */
var node_RemoteServiceGroupTask = function(syncName, handlers, requestInfo, setting){
	this[NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_REQUEST_TYPE] = NOSLIWCOMMONCONSTANT.CONS_REMOTESERVICE_TASKTYPE_GROUP;
	this.syncName = syncName;
	this.requestInfo = requestInfo;
	this.setting = setting;
	this.children = [];
	this.handlers = handlers;
	this.metaData = {};
	this.infos = {
	};
	this.requestId = nosliw.generateId();

	//used for ajax request
	this.remoteRequest = new NosliwRemoteServiceRequest(this); 
};

node_RemoteServiceGroupTask.prototype = {
	addTask : function(task){
		task.type = NOSLIWCOMMONCONSTANT.CONS_REMOTESERVICE_TASKTYPE_GROUPCHILD;
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
//Register Node by Name
packageObj.createChildNode("RemoteServiceSetting", node_RemoteServiceSetting); 
packageObj.createChildNode("RemoteServiceRequest", node_RemoteServiceRequest); 
packageObj.createChildNode("RemoteServiceTask", node_RemoteServiceTask); 
packageObj.createChildNode("RemoteServiceGroupTask", node_RemoteServiceGroupTask); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);

