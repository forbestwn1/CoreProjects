//get/create package
var packageObj = library.getChildPackage("utility");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_basicUtility = 
{
		buildNosliwFullName : function(name){
			return node_CONSTANT.NOSLIW_NAME_PREFIX+name;
		},
		
		getNosliwCoreName : function(name){
			var index = name.indexOf(node_CONSTANT.NOSLIW_NAME_PREFIX);
			if(index==-1)  return;
			return name.subString(index);
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
		}
		
		
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});


//Register Node by Name
packageObj.createChildNode("basicUtility", node_basicUtility); 
	
})(packageObj);
