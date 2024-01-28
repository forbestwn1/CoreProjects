//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_CONSTANT;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * The request's process is done by a function
 */
var node_createServiceRequestInfoSimple = function(service, processor, handlers, requester_parent){

	service = node_requestUtility.buildService(service);
	var loc_processorFun = processor;
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		var out = loc_processorFun.call(this, requestInfo);
		loc_out.successFinish(out, loc_out);
	};
		
	var loc_out = {
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_SIMPLE);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoSimple", node_createServiceRequestInfoSimple); 

})(packageObj);
