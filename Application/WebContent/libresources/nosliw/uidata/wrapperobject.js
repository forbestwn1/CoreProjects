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
var node_makeObjectWithId;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_parseSegment;
//*******************************************   Start Node Definition  ************************************** 	
var node_createWraperObject = function(){
	
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

	/*
	 * do operation on object
	 * 		obj : root object
	 * 		prop : path from root object
	 * 		command : what to do
	 * 		data : data for command
	 */
	var loc_operateObject = function(obj, prop, command, data){
		var baseObj = obj;
		var attribute = prop;
		
		if(node_basicUtility.isStringEmpty(prop)){
			baseObj = obj;
		}
		else if(prop.indexOf('.')==-1){
			baseObj = obj;
			attribute = prop;
		}
		else{
			var segs = node_parseSegment(prop);
			var size = segs.getSegmentSize();
			for(var i=0; i<size-1; i++){
				var attr = segs.next();
				var obj = baseObj[attr];
				if(obj==undefined){
					obj = {};
					baseObj[attr] = obj; 
				}
				baseObj = obj;
			}
			attribute = segs.next();
		}
		
		var out = obj;
		if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
			if(node_basicUtility.isStringEmpty(attribute))	out = data;
			else	baseObj[attribute] = data;
		}
		else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
			var opObj;
			if(node_basicUtility.isStringEmpty(attribute))	opObj = obj; 
			else{
				//if container does not exist, then create a map
				if(baseObj[attribute]==undefined)	baseObj[attribute] = {};
				opObj = baseObj[attribute];
			}

			if(data.index!=undefined){
				if(_.isArray(opObj)){
					opObj.splice(data.index, 0, data.data);
				}
				else{
					opObj[data.elePath]=data.data;
				}
			}
			else{
				//if index is not specified, for array, just append it
				if(_.isArray(opObj)){
					opObj.push(data.data);
				}
			}
		}
		else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
			var opObj;
			if(node_basicUtility.isStringEmpty(attribute))	opObj = obj; 
			else  opObj = baseObj[attribute];

			if(_.isArray(opObj)){
				opObj.splice(data.index, 1);
			}
			else{
//				delete opObj[data];
			}
		}			
		return out;
	};
	
	var loc_out = {		

			//get child value by path
			getChildValueRequest : function(parentValue, path, handlers, requester_parent){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetChildValueFromObjectValue", {"parent":parentValue, "path":path}), function(requestInfo){
					var out = loc_getObjectAttributeByPath(parentValue, path);
					return node_dataUtility.cloneValue(out);
				}, handlers, requester_parent);
			},
			
			getDataOperationRequest : function(baseValue, dataOperationService, handlers, requester_parent){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetDataOperationFromObjectValue", {"baseValue":baseValue, "dataOperation":dataOperationService}), function(requestInfo){
					var command = dataOperationService.command;
					var dataOperation = dataOperationService.parms;
					var out = baseValue;
					if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
						//change value
						if(_.isObject(baseValue)){
							out = loc_operateObject(baseValue, dataOperation.path, node_CONSTANT.WRAPPER_OPERATION_SET, dataOperation.value);
						}
						else{
							out = dataOperation.value;
						}
					}
					else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
						var operationData = {
								data : dataOperation.value,
								index : dataOperation.index,
								elePath : dataOperation.elePath
							};
						out = loc_operateObject(baseValue, dataOperation.path, node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, operationData);
					}
					else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
						out = loc_operateObject(baseValue, dataOperation.path, node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT, dataOperation.index);
					}
					return out;
				
				}, handlers, requester_parent);
			},
			
			getGetElementsRequest : function(baseValue, handlers, request){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetElements", {"baseValue":baseValue}), function(requestInfo){
					var elements = [];
					if(_.isArray(baseValue)){
						//for array
						_.each(baseValue, function(eleValue, index){
							elements.push({
								value : node_dataUtility.cloneValue(eleValue)
							});
						}, this);
					}
					else if(_.isObject(baseValue)){
						//for object
						_.each(baseValue, function(eleValue, name){
							elements.push({
								path : name,
								id : name,
								value : node_dataUtility.cloneValue(eleValue)
							});
						}, this);
					} 
					return elements;
				}, handlers, request);
			}, 
			
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
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});


nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerWrapperFactoryByDataType([node_CONSTANT.DATA_TYPE_OBJECT], node_createWraperObject);
});

//Register Node by Name
packageObj.createChildNode("createWraperObject", node_createWraperObject); 

})(packageObj);
