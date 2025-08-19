//get/create package
var packageObj = library.getChildPackage("wrapper.appdata");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_basicUtility;
var node_parseSegment;
var node_createServiceRequestInfoSequence;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_OperationParm;
var node_dataUtility;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_utility = function(){
	
	var loc_getSegmentChildAppDataRequest = function(opData, segs, i){
		var size = segs.getSegmentSize();
		if(i>=size){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return node_dataUtility.createDataOfAppData(opData);
			}); 
		}
		else{
			var operationParms = [];
			operationParms.push(new node_OperationParm(opData, "base"));
			operationParms.push(new node_OperationParm({
				dataTypeId: "test.string;1.0.0",
				value : segs.next()
			}, "name"));

			return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
					opData.dataTypeId, 
					node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATA, 
					operationParms, {
						success : function(request, childData){
							if(childData==undefined)  node_dataUtility.createDataOfAppData();;
							i++;
							return loc_getSegmentChildAppDataRequest(childData, segs, i);
						}
					});
		}
		
	};
		
	var loc_out = {

			getGetChildAppDataRequest : function(appData, path, handlers, requester_parent){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetChildAppData", {"appData":appData, "path":path}), handlers, requester_parent);
				out.addRequest(loc_getSegmentChildAppDataRequest(appData, node_parseSegment(path), 0));
				return out;
			},

			getSetChildAppDataRequest : function(baseData, path, opData, handlers, requester_parent){
				var index = path.lastIndexOf(node_COMMONCONSTANT.SEPERATOR_PATH);
				var basePath = undefined;
				var leafAttr = path;
				if(index!=-1){
					basePath = path.subString(0, index);
					leafAttr = path.subString(index+1);
				}
				
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("SetChildAppData", {"baseData":baseData, "opData":opData, "path":path}), handlers, requester_parent);
				
				var getRequest = this.getGetChildAppDataRequest(baseData, basePath, {
					success : function(requestInfo, data){
						var appData = data.value;
						var operationParms = [];
						operationParms.push(new node_OperationParm({
							dataTypeId: "test.string;1.0.0",
							value : leafAttr
						}, "name"));
						operationParms.push(new node_OperationParm(appData, "base"));
						operationParms.push(new node_OperationParm(opData, "value"));
						return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
								appData.dataTypeId, 
								node_COMMONCONSTANT.DATAOPERATION_COMPLEX_SETCHILDDATA, 
								operationParms, {
									success : function(request, data){
										return data;
									}
								});
					}
				}, requester_parent);
				
				out.addRequest(getRequest)
				return out;
			}
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.utility", function(){node_dataUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("xxxxutility", node_utility); 

})(packageObj);
