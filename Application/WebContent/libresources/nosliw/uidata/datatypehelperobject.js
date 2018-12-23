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

	
	var loc_getOperationBase = function(value, path){
		var baseObj = value;
		var attribute = path;
		
		if(node_basicUtility.isStringEmpty(path)){
			baseObj = value;
		}
		else if(path.indexOf('.')==-1){
			baseObj = value;
			attribute = path;
		}
		else{
			var segs = node_parseSegment(path);
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
		return {
			base : baseObj, 
			attribute : attribute
		}
	}
	
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
					var command = dataOperationService.command;
					var operationData = dataOperationService.parms;
					
					//get operation base
					var operationBase = loc_getOperationBase(value, operationData.path);
					
					var out = value;
					if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
						if(_.isObject(value) || _.isArray(value)){
							//for array or object
							if(node_basicUtility.isStringEmpty(operationBase.attribute))	out = operationData.value;
							else	operationBase.base[operationBase.attribute] = operationData.value;
						}
						else{
							//for basic value
							out = operationData.value;
						}
					}
					else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
						if(_.isArray(operationBase.base) || _.isObject(operationBase.base)){
							//get container object to add element to it
							var containerObj;
							if(node_basicUtility.isStringEmpty(operationBase.attribute))	containerObj = operationBase.base; 
							else{
								containerObj = operationBase.base[operationBase.attribute];
								if(containerObj==undefined){
									//if container does not exist, then create it
									if(operationData.id!=undefined)		operationBase.base[operationBase.attribute] = {};
									else		operationBase.base[operationBase.attribute] = [];
								}
								containerObj = operationBase.base[operationBase.attribute];
							}
							
							if(operationData.index!=undefined){
								if(_.isArray(containerObj))		containerObj.splice(operationData.index, 0, operationData.value);
								else if(_.isObject(containerObj)) containerObj[operationData.id]=operationData.value;
							}
							else{
								//if index is not specified, for array, just append it
								if(_.isArray(containerObj))		containerObj.push(operationData.value);
								else if(_.isObject(containerObj)) containerObj[operationData.id]=operationData.value;
							}
						}
						else{
							//not valid operation 
						}
					}
					else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
						var containerObj;
						if(node_basicUtility.isStringEmpty(operationBase.attribute))	containerObj = operationBase.base; 
						else	containerObj = operationBase.base[operationBase.attribute];

						if(containerObj!=undefined){
							if(_.isArray(containerObj))		containerObj.splice(operationData.index, 1);
							else if(_.isObject(containerObj)) delete containerObj[operationData.id];
						}
						else{
							//not valid operation 
						}
					}
					else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETE){
						if(_.isArray(operationBase.base)){
							operationBase.base.splice(parseInt(operationBase.attribute), 1);
						}
						else if(_.isObject(operationBase.base)){
							delete operationBase.base[operationBase.attribute];
						}
					}
					return out;
				
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
	this.getData().registerDataTypeHelper([node_CONSTANT.DATA_TYPE_OBJECT], node_createDataTypeHelperObject());
});

})(packageObj);
