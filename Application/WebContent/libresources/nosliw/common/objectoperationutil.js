//get/create package
var packageObj = library.getChildPackage("utility");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_basicUtility;
	var node_parseSegment;
//*******************************************   Start Node Definition  ************************************** 	

var node_objectOperationUtility = 
{
		assignObjectAttributeByPath : function(outputObj, outputPathSegs, inputObj, intpuPathSegs){
			var inputEleValue = node_objectOperationUtility.getObjectAttributeByPathSegs(inputObj, intpuPathSegs);
			node_objectOperationUtility.operateObjectByPathSegs(outputObj, outputPathSegs, node_CONSTANT.WRAPPER_OPERATION_SET, inputEleValue);
			return outputObj;
		},
		
		/*
		 * get attribute value according to the path
		 */
		getObjectAttributeByPath : function(obj, prop) {
			if(obj==undefined)  return;
			if(prop==undefined || prop=='')  return obj;
		    var parts = prop.split('.');
		    return this.getObjectAttributeByPathSegs(obj, parts);
		},

		getObjectAttributeByPathSegs : function(obj, propSegs) {
			if(obj==undefined)  return;
			if(propSegs==undefined || propSegs.length==0)  return obj;
			
		    var parts = propSegs,
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
		},

		/*
		 * do operation on object
		 * 		obj : root object
		 * 		prop : path from root object
		 * 		command : what to do
		 * 		data : data for command
		 */
		operateObject : function(obj, prop, command, data){
			return this.operateObjectByPathSegs(obj, prop.split('.'), command, data);
		},

		operateObjectByPathSegs : function(obj, pathSegs, command, data){
			var out;
			var baseObj = obj;
			var attribute = "";
			
			if(pathSegs==undefined || pathSegs.length==0){
				baseObj = obj;
			}
			else if(pathSegs.length==1){
				baseObj = obj;
				attribute = pathSegs[0];
			}
			else{
				var segs = pathSegs;
				var size = segs.length;
				for(var i=0; i<size-1; i++){
					var attr = segs[i];
					var obj = baseObj[attr];
					if(obj==undefined){
						obj = {};
						baseObj[attr] = obj; 
					}
					baseObj = obj;
				}
				attribute = segs[i];
			}
			
			if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
				out = baseObj[attribute];
				baseObj[attribute] = data;
			}
			else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
				//if container does not exist, then create a map
				if(baseObj[attribute]==undefined)  baseObj[attribute] = {};
				if(data.index!=undefined){
					baseObj[attribute][data.index]=data.data;
				}
				else{
					//if index is not specified, for array, just append it
					if(_.isArray(baseObj[attribute])){
						baseObj[attribute].push(data.data);
					}
				}
			}
			else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
				out = baseObj[attribute][data];
				delete baseObj[attribute][data];
			}
			return out;
		},


};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});

//Register Node by Name
packageObj.createChildNode("objectOperationUtility", node_objectOperationUtility); 
	
})(packageObj);
