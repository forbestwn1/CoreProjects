/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_storyChangeUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_createStoryService;
	var node_requestServiceProcessor;
	var node_storyUtility;
	var node_storyUIUtility;
	var node_designUtility;
	var node_CONSTANT;
	var node_createData;
	var node_createContextElementInfo;
	var node_createContext;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemConstant = function(){

	var loc_storyService = node_createStoryService();
	
	var loc_vueComponent = {
		data : function(){
			return {
				uiNodeView : {},
				context : {},
				tagInfo : {},
				dataInfo : {},
				data : {},
				tagData : {},
			};
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
			onDataChange : function(data){
				node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATA, data, this.question.answer);
				this.$emit("answerChange", data);
				this.data = data;
			},
		},
		computed: {
		},
		created : function(){
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			this.dataInfo = element[node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATATYPE];
		},
		mounted: function () {
			var that = this;
			var request = node_createServiceRequestInfoSequence(undefined, {
				success : function(request, uiTag){
					
				}
			});
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			request.addRequest(loc_storyService.getDefaultUITagRequest(element[node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATATYPE][node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_CRITERIA], {
				success : function(request, tagInfo){
					that.tagInfo = tagInfo;
				}
			}));
			node_requestServiceProcessor.processRequest(request);
		},	
		template : `
			<div>
				{{question.question}}: 
				<div>
				dataTag: <uitag_data v-bind:uitaginfo="tagInfo" dynamicdata="true" v-bind:data="tagData"  v-bind:datainfo="dataInfo" v-on:dataChange="onDataChange"/>
				</div>
			</div>
		`
	};
		
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("application.instance.story.service.createStoryService", function(){node_createStoryService = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUIUtility", function(){node_storyUIUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.designUtility", function(){node_designUtility = this.getData();});

nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.entity.createData", function(){node_createData = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemConstant", node_createComponentQuestionItemConstant); 

})(packageObj);
