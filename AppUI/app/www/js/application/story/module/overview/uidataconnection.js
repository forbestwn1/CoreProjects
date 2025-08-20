/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createEventObject;
	var node_storyUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIDataConnectionLink = function(uiNodeId, parentContextDef, module){
	
	var loc_uiNodeId = uiNodeId;
	var loc_module = module;

	var loc_links = {};
	
	var loc_init = function(){
		var story = loc_module.getStory();
		var storyUINode = node_storyUtility.getNodeById(story, loc_uiNodeId);
		var storyUINodeType = storyUINode[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];
		
		if(storyUINodeType==node_COMMONCONSTANT.STORYNODE_TYPE_UITAGDATA){
			var contextPath = storyUINode[node_COMMONATRIBUTECONSTANT.STORYNODEUITAGDATA_DATAINFO][node_COMMONATRIBUTECONSTANT.STORYUIDATAINFO_IDPATH];
			var rootReference = storyUINode[node_COMMONATRIBUTECONSTANT.STORYNODEUITAGDATA_DATAINFO][node_COMMONATRIBUTECONSTANT.STORYUIDATAINFO_ROOTREFERENCE];
			var nodeDataContextDef = storyUINode[node_COMMONATRIBUTECONSTANT.STORYNODEUI_DATASTRUCTURE][node_COMMONATRIBUTECONSTANT.STORYUIDATASTRUCTUREINFO_CONTEXT];
			var nodeDataDef = parentContextDef[node_COMMONATRIBUTECONSTANT.VALUESTRUCTUREDEFINITIONGROUP_GROUP][rootReference[node_COMMONATRIBUTECONSTANT.REFERENCEROOTINGROUP_CATEGARY]][node_COMMONATRIBUTECONSTANT.VALUESTRUCTUREDEFINITIONFLAT_FLAT][rootReference[node_COMMONATRIBUTECONSTANT.REFERENCEROOTINGROUP_NAME]];
			var varStoryNodeId = nodeDataDef[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_SOLIDNODEREF][node_COMMONATRIBUTECONSTANT.INFOPATHTOSOLIDROOT_ROOTNODEID];
			var varStoryNode = node_storyUtility.getNodeById(story, varStoryNodeId);

			var varNodeElement = loc_module.getNodeElementById(varStoryNodeId);
			var uiNodeElement = loc_module.getNodeElementById(loc_uiNodeId);


			var link1 = new joint.shapes.standard.Link({
				  attrs: {
				      '.connection': { 'stroke-dasharray': '2,5' },
		            line: {
		                stroke: 'blue',
		                strokeWidth: 1,
		            },
				  }
			});
			link1.labels([{
			    attrs: {
			        text: {
			            text: node_storyUtility.getElementIdWithoutNodeType(uiNodeElement.getStoryNode().id)
			        }
			    }
			}]);

			var link2 = new joint.shapes.standard.Link({
				  attrs: {
				      '.connection': { 'stroke-dasharray': '2,5' },
			            line: {
			                stroke: 'blue',
			                strokeWidth: 1,
			                sourceMarker: {
			                    'type': 'path',
			                    'stroke': 'black',
			                    'fill': 'red',
			                    'd': 'M 10 -5 0 0 10 5 Z'
			                },
			                targetMarker: {
			                    'type': 'path',
			                    'stroke': 'black',
			                    'fill': 'yellow',
			                    'd': 'M 10 -5 0 0 10 5 Z'
			                },
			            },
				  }
			});
			link2.labels([{
			    attrs: {
			        text: {
			            text: node_storyUtility.getElementIdWithoutNodeType(uiNodeElement.getStoryNode().id)
			        }
			    }
			}]);

			var link;
			var uiStoryNode = uiNodeElement.getStoryNode();
			var uiDataFlow = uiStoryNode[node_COMMONATRIBUTECONSTANT.STORYNODEUITAG_ATTRIBUTES][node_COMMONATRIBUTECONSTANT.STORYNODEUITAGDATA_ATTRIBUTE_DATAFLOW];
			if(uiDataFlow==undefined)  uiDataFlow = node_COMMONCONSTANT.DATAFLOW_IO;
			if(uiDataFlow == node_COMMONCONSTANT.DATAFLOW_IN){
				link1.source(varNodeElement.getElement());
				link1.target(uiNodeElement.getElement());
				link = link1;
			}
			else if(uiDataFlow == node_COMMONCONSTANT.DATAFLOW_OUT){
				link1.target(varNodeElement.getElement());
				link1.source(uiNodeElement.getElement());
				link = link1;
			}
			else if(uiDataFlow == node_COMMONCONSTANT.DATAFLOW_IO){
				link2.target(varNodeElement.getElement());
				link2.source(uiNodeElement.getElement());
				link = link2;
			}
			
			loc_links[varStoryNodeId] = link; 
		}
		
	};
	
	var loc_out = {
		
		getLinks : function(){	return loc_links;	},

		addToPaper : function(graph){
			_.each(loc_links, function(link, varNodeId){
				graph.addCells(link);
			});
		},
	};
	
	loc_init();
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIDataConnectionLink", node_createUIDataConnectionLink); 

})(packageObj);
