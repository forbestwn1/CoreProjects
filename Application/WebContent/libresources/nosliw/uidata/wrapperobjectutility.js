//get/create package
var packageObj = library.getChildPackage("wrapper.object");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_basicUtility;
var node_parseSegment;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_utility = {
	
		/*
		 * get attribute value according to the path
		 */
		getObjectAttributeByPath : function(obj, prop) {
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
		},

		/*
		 * do operation on object
		 * 		obj : root object
		 * 		prop : path from root object
		 * 		command : what to do
		 * 		data : data for command
		 */
		operateObject : function(obj, prop, command, data){
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
			
			if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
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
				delete baseObj[attribute][data];
			}			
		},
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
