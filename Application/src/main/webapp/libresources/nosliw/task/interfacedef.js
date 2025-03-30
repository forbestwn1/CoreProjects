//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node

//*******************************************   Start Node Definition  ************************************** 	

var node_createTaskImpInterface = function(rawInterfaceObj){
	
	var interfaceDef = {
		
		getTaskExecuteRequest : function(runtimeEnv, handlers, request){},

	};
	return _.extend({}, interfaceDef, rawInterfaceObj);

};

var node_createTaskLifecycleInterface = function(rawInterfaceObj){
	
	var interfaceDef = {
		
		onTaskCreate : function(){},
		
		onTaskInit : function(){},
		
		onTaskExecute : function(){},
		
		onTaskFinish : function(){},
		
	};
	return _.extend({}, interfaceDef, rawInterfaceObj);

};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("createTaskImpInterface", node_createTaskImpInterface); 
packageObj.createChildNode("createTaskLifecycleInterface", node_createTaskLifecycleInterface); 

})(packageObj);
