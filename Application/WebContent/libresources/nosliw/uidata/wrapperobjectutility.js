//get/create package
var packageObj = library.getChildPackage("wrapper.object");    

(function(packageObj){
//get used node
	
//*******************************************   Start Node Definition  ************************************** 	
node_utility = function(){
	
	return {
		//object data type
		dataTypeInfo : 	new NosliwDataTypeInfo(NOSLIWCOMMONCONSTANT.CONS_DATATYPE_CATEGARY_OBJECT),

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
			
			if(nosliwCommonUtility.isStringEmpty(prop)){
				baseObj = obj;
			}
			else if(prop.indexOf('.')==-1){
				baseObj = obj;
				attribute = prop;
			}
			else{
				var segs = nosliwCreateSegmentParser(prop);
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
			
			if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_SET){
				baseObj[attribute] = data;
			}
			else if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_ADDELEMENT){
				if(baseObj[attribute]==undefined)  baseObj[attribute] = {};
				baseObj[attribute][data.index]=data.data;
			}
			else if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
				delete baseObj[attribute][data];
			}			
		},
	};	
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
