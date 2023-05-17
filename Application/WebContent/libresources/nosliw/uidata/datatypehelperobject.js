//get/create package
var packageObj = library.getChildPackage("wrapper.object");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_createEventObject;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_parseSegment;
var node_objectOperationUtility;
//*******************************************   Start Node Definition  ************************************** 	
var node_createDataTypeHelperObject = function(){
	
	/*
	 * get attribute value according to the path
	 */
	var loc_getObjectAttributeByPath = function(obj, prop) {
		if(obj==undefined)  return;
		if(prop==undefined || prop=='')  return obj;
		
	    var parts = prop.split('.'),
	        last = parts.pop(),
	        l = parts.length,
	        i = 1,
	        current = parts[0];

	    if(current==undefined)  return obj[last];
	    
	    while((obj = obj[current]) && i < l) {
	        current = parts[i];
	        i++;
	    }

	    if(obj) {
	        return obj[last];
	    }
	};

	
	var loc_out = {		

			//get child value by path
			getChildValueRequest : function(parentValue, path, handlers, requester_parent){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetChildValueFromObjectValue", {"parent":parentValue, "path":path}), function(requestInfo){
					var out = loc_getObjectAttributeByPath(parentValue, path);
					return node_dataUtility.cloneValue(out);
				}, handlers, requester_parent);
			},
			
			//do operation on value
			getDataOperationRequest : function(value, dataOperationService, handlers, requester_parent){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetDataOperationFromObjectValue", {"value":value, "dataOperationService":dataOperationService}), function(requestInfo){
					return node_objectOperationUtility.objectOperation(value, dataOperationService);
				}, handlers, requester_parent);
			},
			
			//loop through elements under value
			getGetElementsRequest : function(value, handlers, request){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetElements", {"value":value}), function(requestInfo){
					var elements = [];
					if(_.isArray(value)){
						//for array
						_.each(value, function(eleValue, index){
							elements.push({
								value : node_dataUtility.cloneValue(eleValue)
							});
						}, this);
					}
					else if(_.isObject(value)){
						//for object
						_.each(value, function(eleValue, name){
							elements.push({
								id : name,
								value : node_dataUtility.cloneValue(eleValue)
							});
						}, this);
					} 
					return elements;
				}, handlers, request);
			}, 
			
			//clean up resource in value
			destroyValue : function(value){},
			
			getWrapperType : function(){	return node_CONSTANT.DATA_TYPE_OBJECT;		},
	
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});


nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerDataTypeHelper([node_CONSTANT.DATA_TYPE_OBJECT], node_createDataTypeHelperObject());
});

})(packageObj);
