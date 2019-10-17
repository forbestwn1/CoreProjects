//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_getObjectType;
	var node_createIODataSet;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	var loc_out = {

		//get ioData's data set value
		//ioData maybe dataSet, value, or dataAssociation
		getGetIODataValueRequest : function(ioData, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getGetDataSetValueRequest", {}), handlers, request);
			var ioDataType = node_getObjectType(ioData);
			if(ioDataType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_IODATASET){
				//for io data set, get directly
				out.addRequest(ioData.getGetDataSetValueRequest());
			}
			else if(ioDataType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION){
				//for data association, execute data association first
				out.add(ioData.getExecuteRequest({
					success : function(request, ioDataSet){
						return ioDataSet.getGetDataSetValueRequest();
					}
				}));
			}
			else{
				//for value, build io data set first
				out.addRequest(node_createIODataSet(ioData).getGetDataSetValueRequest());
			}
			
			return out;
		},
		
		getContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE 
			];
		},

		getReversedContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
			];
		},
		
		//merge context 
		//if isFlat is true, then treat it is as object
		//if isFlat is false, then treat it as context with group
		mergeContext : function(source, target, isFlat){
			if(target==undefined)   target = {};
			if(isFlat==true){
				_.each(source, function(value, name){
					target[name] = value;
				});
			}
			else{
				_.each(source, function(c, categary){
					var cc = target[categary];
					if(cc==undefined){
						cc = {};
						target[categary] = cc;
					}
					_.each(c, function(ele, name){
						cc[name] = ele;
					});
				});
			}
			return target;
		},
		
		//assigned value to outputIODataSet
		outputToDataSetIORequest : function(outputIODataSet, value, dataSetName, isOutputFlat, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(value==undefined){
				//if outputvalue is undefined, then no impact on outputTarget
//				out.addRequest(loc_outputIODataSet.getGetDataValueRequest(dataSetName));
			}
			else{
				out.addRequest(outputIODataSet.getMergeDataValueRequest(dataSetName, value, isOutputFlat));
			}
			return out;
		},

		//apply matchers for different path on value
		processMatchersRequest : function(value, matchersByPath, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(value!=undefined && matchersByPath!=undefined){
				var matchersByPathRequest = node_createServiceRequestInfoSet(undefined, {
					success : function(request, resultSet){
						_.each(resultSet.getResults(), function(result, path){
							node_objectOperationUtility.operateObject(value, path, node_CONSTANT.WRAPPER_OPERATION_SET, result);
						});
						return value;
					}
				});
				_.each(matchersByPath, function(matchers, path){
					var valueByPath = node_objectOperationUtility.getObjectAttributeByPath(value, path);
					matchersByPathRequest.addRequest(path, nosliw.runtime.getExpressionService().getMatchDataRequest(valueByPath, matchers));
				});
				out.addRequest(matchersByPathRequest);
			}
			else{
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){ return value;  })); 
			}
			return out;
		},
	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioTaskUtility", node_utility); 

})(packageObj);
