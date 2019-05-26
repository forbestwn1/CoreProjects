//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_CONSTANT;
	var node_RemoteServiceTask;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * The request is based on a remote request
 */
var node_createServiceRequestInfoRemote = function(syncName, service, setting, handlers, requester_parent){

	var loc_syncName = syncName;
	var loc_service = node_requestUtility.buildService(service);
	var loc_setting = setting;
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		var remoteServiceTask = new node_RemoteServiceTask(loc_syncName, loc_service, 
				{
					success : function(request, data){
						loc_out.executeSuccessHandler(data, this);
					},
					error : function(request, serviceData){
						loc_out.executeErrorHandler(serviceData, this);
					},
					exception : function(request, serviceData){
						loc_out.executeExceptionHandler(serviceData, this);
					}
				}
		, loc_out, loc_setting);
		return remoteServiceTask;
	};
		
	var loc_out = {
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_REMOTE);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("remote.entity.RemoteServiceTask", function(){node_RemoteServiceTask = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoRemote", node_createServiceRequestInfoRemote); 

})(packageObj);
