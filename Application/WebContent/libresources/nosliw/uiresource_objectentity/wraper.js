
/*
 * parent wrapper + path
 * 
 */	
var nosliwCreateWraper = function(data, path, request){

	if(data==undefined)   return undefined;
	
	var wrapperType = undefined;
	
	var dataType = nosliwTypedObjectUtility.getObjectType(data);
	if(dataType==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
		//
		wrapperType = data.getWrapperType();
	}
	else{
		if(dataType==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VALUE){
			wrapperType = NOSLIWCONSTANT.WRAPPER_TYPE_OBJECT;
		}
	}
	
	var wrapper = undefined;
	if(wrapperType==NOSLIWCONSTANT.WRAPPER_TYPE_OBJECT){
		wrapper = nosliwCreateObjectWraper();
	}
	
	var out = _.extend(nosliwCreateWraperCommon(data, path, request), wrapper);
	
	if(out.pri_isDataBased()){
		nosliwLogging.debug("create wrapper", out.getObjectId(), "dataBased", out.pri_getPath(), out.pri_getRootData());
	}
	else{
		nosliwLogging.debug("create wrapper", out.getObjectId(), "parentBased", out.pri_getPath(), out.pri_getParent().getObjectId());
	}
	
	return out;
};	
