


//entity to describe relative variable : parent + path to parent
var node_RelativeEntityInfo = function(parent, path){
	this.parent = parent;
	this.path = node_basicUtility.emptyStringIfUndefined(path);
	return this;
};

