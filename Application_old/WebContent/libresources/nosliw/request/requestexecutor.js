//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * The request's process is done by a function
 */
var node_createServiceRequestInfoExecutor = function(service, executor, handlers, requester_parent){

	var loc_executorFun = executor;
	
	var loc_out = {
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_EXECUTOR);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_executorFun, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoExecutor", node_createServiceRequestInfoExecutor); 

})(packageObj);
