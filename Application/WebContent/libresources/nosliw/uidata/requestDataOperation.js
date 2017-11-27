//get/create package
var packageObj = library.getChildPackage("data.entity");    

(function(packageObj){
//get used node
var node_makeObjectWithType;
var node_CONSTANT;	
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_createDataOperationRequest = function(context, handlers, requester_parent){

	var loc_constructor = function(context, handlers, requester_parent){
		//all the child requests service  
		loc_out.pri_childRequestServices = [];
		
		//data context
		loc_out.pri_context = context;
	};
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		_.each(loc_out.pri_childRequestServices, function(dataOperationService, index){
			var target = dataOperationService.target;
			if(node_getObjectType(target)==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
				//target is wrapper
				target.requestDataOperation(dataOperationService, requestInfo);
			}
			else if(node_getObjectType(target)==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
				//target is variable
				target.requestDataOperation(dataOperationService, requestInfo);
			}
			else{
				//target is context variable
				loc_out.pri_context.requestDataOperation(dataOperationService, requestInfo);
			}
		}, this);

		loc_out.executeSuccessHandler({}, loc_out);
	};
		
	var loc_out = {
		addOperationRequest : function(dataOperationService){
			this.pri_childRequestServices.push(DataOperationService);
		},
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(new node_ServiceInfo("ExecuteDataOperation"), handlers, requester_parent), loc_out);
	
	loc_constructor(context, handlers, requester_parent);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_DATAOPERATION);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};

//Entity to define data operation info
var node_DataOperationService = function(target, operation, data){
	this.target = target;
	this.operation = operation;
	this.data = data;
}

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});


//Register Node by Name
packageObj.createChildNode("createDataOperationRequest", node_createDataOperationRequest); 

})(packageObj);
