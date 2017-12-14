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
var node_dataUtility;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_utility = function(){
	
	var loc_getChildAppDataRequest = function(opData, segs, i){
		var operationParms = [];
		
		var pathSeg = segs.next();
		i++;
		var size = segs.getSegmentSize();

		operationParms.push(new node_OperationParm(opData, "base"));
		operationParms.push(new node_OperationParm({
			dataTypeId: "test.string;1.0.0",
			value : pathSeg
		}, "name"));

		var request = nosliw.runtime.getExpressionService().getExecuteOperationRequest(
				opData.dataTypeId, 
				node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATA, 
				operationParms, {
					success : function(request, childData){
						if(i>=size)  return node_dataUtility.createDataOfAppData(childData);
						if(childData==undefined)  return;
						return loc_getChildAppDataRequest(childData, segs, i);
					}
				});
		return request;
	};
		
	var loc_out = {

			getGetChildAppDataRequest : function(appData, path, handlers, requester_parent){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetChildAppData", {"appData":appData, "path":path}), handlers, requester_parent);
				out.addRequest(loc_getChildAppDataRequest(appData, node_parseSegment(path), 0));
				return out;
			},
	};	
	
	
	return loc_out;

}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
