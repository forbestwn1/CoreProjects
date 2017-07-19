/**
 * 
 */
NosliwDataTypeInfo = function(categary, type, version){
	this.categary = categary;
	this.type = type;
	this.version = version;
	this.key = nosliwCommonUtility.cascadePart(type, categary);
};

NosliwDataTypeInfo.prototype = {
	equals : function(dataTypeInfo){
		if(this.categary==dataTypeInfo.categary && this.type==dataTypeInfo.type && this.version==dataTypeInfo.version){
			return true;
		}
		else{
			return false;
		}
	},
	equalsWithoutVersion : function(dataTypeInfo){
		if(this.categary==dataTypeInfo.categary && this.type==dataTypeInfo.type){
			return true;
		}
		else{
			return false;
		}
	},
};

 