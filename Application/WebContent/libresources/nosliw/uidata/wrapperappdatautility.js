//get/create package
var packageObj = library.getChildPackage("wrapper.appdata");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_basicUtility;
var node_parseSegment;
var node_createServiceRequestInfoSequence;
var node_ServiceInfo;
var node_OperationParm;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_utility = {
	
	getGetChildAppDataRequest : function(appData, path, handlers, requester_parent){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetChildAppData", {"appData":appData, "path":path}), handlers, requester_parent);
		
		var segs = node_parseSegment(path);
		var size = segs.getSegmentSize();
		var opData = appData;
		for(var i=0; i<size; i++){
			var pathSeg = segs.next();
			var operationParms = [];
			
			operationParms.push(new node_OperationParm(opData, "base"));
			operationParms.push(new node_OperationParm(pathSeg, "name"));
			
			var childRequest = nosliw.runtime.getExpressionService().getExecuteOperationRequest(
					opData.dataTypeId, 
					node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATA, 
					operationParms, 
					{
						success : function(request, childData){
							opData = childData; 
						}
					});
			out.addRequest(childRequest);
		}
		
		return out;
	},

};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
