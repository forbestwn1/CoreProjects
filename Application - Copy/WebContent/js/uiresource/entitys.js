/*
 * store requestor infor object
 */
var NosliwUITagRequesterInfo = function(parentResource, tag, context){
	this.parentResource = parentResource;
	this.tag = tag;
	this.context = context;
};

/*
 * store information about parent tag for ui resource view
 */
var NosliwParentTagInfo = function(name, tag){
	//tag name
	this.name = name;
	//tag object
	this.tag = tag;
	
};

