//get/create package
var packageObj = library.getChildPackage("utility");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_basicUtility = 
{
		isValueEqual : function(data1, data2){
			if(data1==undefined && data2==undefined)   return true;
			if(data1==undefined || data2==undefined)   return false;
			return JSON.stringify(data1)==JSON.stringify(data2);
		},
		
		buildNosliwFullName : function(name){
			return node_COMMONCONSTANT.NOSLIW_NAME_PREFIX+name;
		},
		
		getNosliwCoreName : function(name){
			var index = name.indexOf(node_COMMONCONSTANT.NOSLIW_NAME_PREFIX);
			if(index==0)  return name.substring(node_COMMONCONSTANT.NOSLIW_NAME_PREFIX.length);
		},
		
		getParmsInUrl : function(){
			var parms = {};
			var searchParams = new URLSearchParams(window.location.search).entries();
			var next = searchParams.next();
			while(!next.done){
				var p = next.value;
				parms[p[0]] = p[1];
				next = searchParams.next();
			}
			return parms;
		},
		
		/*
		 * create an value with meaning of empty
		 */
		createEmptyValue : function(){return "alkfjalsdkfjsafjoweiurwerwelkjdlsjdf";},
		
		isEmptyValue : function(value){ return value== "alkfjalsdkfjsafjoweiurwerwelkjdlsjdf";},
		
		emptyStringIfUndefined : function(value){ 
			if(value==undefined)  return "";
			return value;
		},
		
		/*
		 * merge two object and create a new one
		 * specificOne will override the defaultone object
		 */
		mergeObjects : function(defaultOne, specificOne){
			
			var out = {};
			_.each(defaultOne, function(attr, name, list){
				out[name] = attr;
			});

			_.each(specificOne, function(attr, name, list){
				out[name] = attr;
			});
			return out;
		},
		
		cloneObject : function(object){
			var newObject = jQuery.extend(true, {}, object);	
			return newObject;
		},
		
		isStringEmpty : function(str){
			if(str==undefined || str+''=='')  return true;
			else false;
		},

		htmlDecode : function(input){
			var e = document.createElement('div');
			e.innerHTML = input;
			return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
		},

		isAtomData : function(data){
			if(_.isString(data) || _.isNumber(data) || _.isBoolean(data))  return true;
		},
		
		capitalizeFirstLetter : function(string) {
		    return string.charAt(0).toUpperCase() + string.slice(1);
		},
		
		isEmptyObject :function (obj) {
			if(obj==undefined)  return true;
			for(var prop in obj) {
			    if (Object.prototype.hasOwnProperty.call(obj, prop)) {
			    	return false;
				}
			}
			return true;
		},
		
		stringify : function(value){
			if(value==undefined)   return "undefined";
			try{
				return JSON.stringify(value);
			}
			catch(e){
				return value.toString();
			}
		},
		
		clone : function(src) {
			if(src==undefined)  return undefined;
			if(src==null)  return null;
			return JSON.parse(JSON.stringify(src));
		},
		
		isNumeric : function(str) {
			  if (typeof str != "string") return false // we only process strings!  
			  return !isNaN(str) && // use type coercion to parse the _entirety_ of the string (`parseFloat` alone does not do this)...
			         !isNaN(parseFloat(str)) // ...and ensure strings of whitespace fail
		},
		
		isStringValue : function(value){
			if (typeof value === 'string' || value instanceof String)  return true;
			else return false;
		},
		
		isArray : function(value){
			return Array.isArray(value);
		},
		
		parsePostSegment : function(value){
			var segments;
			if(node_basicUtility.isArray(value)){
				segments = value;
			}
			else{
				if(node_basicUtility.isStringEmpty(value)) return;
				segments = node_namingConvensionUtility.parsePathInfos(value);
			}
			
			var i = 0;
			var path = [];
			while(i<segments.length-1){
				path.push(segments[i]);
				i++;
			}
			return {
				segments : path,
				post : segments[segments.length-1],
			}			
		},
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("basicUtility", node_basicUtility); 
	
})(packageObj);
