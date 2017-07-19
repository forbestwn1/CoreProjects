/*
 * entity for context variable
 * 		name + path
 * 		contextVariable object
 * 		string
 */
var nosliwCreateContextVariable = function(n, p){
	var path = p;
	var name = n;
	if(path==undefined){
		//if second parms does not exist, then try to parse name to get path info
		if(nosliwTypedObjectUtility.getObjectType(name)==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_CONTEXTVARIABLE){
			//if firs parm is context variable object
			path = name.path;
			name = name.name;
		}
		else{
			path="";
			var index = name.indexOf(NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PATH);
			if(index!=-1){
				path = name.substring(index+1);
				name = name.substring(0, index);
			}
		}
	}
	
	path = nosliwCommonUtility.emptyStringIfUndefined(path);
	
	var loc_out = {
		//context item name
		name : name,
		//path
		path : path,
		//key
		key : nosliwNamingConversionUtility.cascadePath(name, path),
	};
	
	loc_out = nosliwTypedObjectUtility.makeTypedObject(loc_out, NOSLIWCONSTANT.TYPEDOBJECT_TYPE_CONTEXTVARIABLE);
	
	return loc_out;
};


