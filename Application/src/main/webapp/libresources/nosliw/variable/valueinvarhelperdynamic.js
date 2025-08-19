//get/create package
var packageObj = library.getChildPackage("valueinvar");    

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
//*******************************************   Start Node Definition  ************************************** 	
var node_createDataTypeHelperDynamic = function(){
	
	var loc_out = {		

			//get child value by path
			getChildValueRequest : function(parentValue, path, handlers, requester_parent){
				return node_createServiceRequestInfoSimple({}, function(){
					return parentValue;
				}, handlers, requester_parent);
			},
			
			//do operation on value
			getDataOperationRequest : function(value, dataOperationService, handlers, requester_parent){
			},
			
			//loop through elements under value
			getGetElementsRequest : function(value, handlers, request){
			}, 
	
			destroyValue : function(value){},
			
			getWrapperType : function(){	return node_CONSTANT.DATA_TYPE_DYNAMIC;		},
	
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
nosliw.registerSetNodeDataEvent("variable.valueinvar.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});


nosliw.registerSetNodeDataEvent("variable.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerDataTypeHelper([node_CONSTANT.DATA_TYPE_DYNAMIC], node_createDataTypeHelperDynamic());
});

})(packageObj);
