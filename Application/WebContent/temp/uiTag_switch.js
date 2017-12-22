
nosliw.runtime.getResourceService().importResource({"id":{"id":"switch",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"switch",
"context":{"inherit":true,
"public":{}
},
"attributes":{},
"script":
function (context, parentResourceView, uiTagResource, attributes, tagEnv) {
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var node_createDataOperationRequest = nosliw.getNodeData("uidata.dataoperation.createDataOperationRequest");
    var node_DataOperationService = nosliw.getNodeData("uidata.dataoperation.DataOperationService");
    var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
    var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
    var node_createUIResourceViewFactory = nosliw.getNodeData("uiresource.createUIResourceViewFactory");
    var node_createContextElementInfo = nosliw.getNodeData("uidata.context.createContextElementInfo");
    var node_createContext = nosliw.getNodeData("uidata.context.createContext");
    var loc_context = context;
    var loc_uiTagResource = uiTagResource;
    var loc_parentResourceView = parentResourceView;
    var loc_tagEnv = tagEnv;
    var loc_resourceView;
    var loc_view;
    var loc_startEle;
    var loc_endEle;
    var loc_valueContextEleName = "internal_switchVariable";
    var loc_valueVariable = loc_context.createVariable(node_createContextVariable(loc_valueContextEleName));
    var loc_updateView = function () {
        var data = loc_valueVariable.getData();
        var value;
        if (data != undefined) {
            value = data.value;
        }
        var found = false;
        var caseTags = loc_resourceView.getTagsByName("case");
        _.each(caseTags, function (caseTag, name) {
            var matched = caseTag.getTagObject().valueChanged(value);
            if (matched == true) {
                found = true;
            }
        });
        var defaultTags = loc_resourceView.getTagsByName("casedefault");
        _.each(defaultTags, function (defaultTag, name) {
            defaultTag.getTagObject().found(found);
        });
    };
    var loc_out = {ovr_postInit: function (requestInfo) {
        loc_valueVariable.registerDataChangeEventListener(undefined, function () {
            loc_updateView();
        }, this);
        loc_updateView();
    }, ovr_preInit: function (requestInfo) {
    }, ovr_initViews: function (startEle, endEle, requestInfo) {
        loc_startEle = startEle;
        loc_endEle = endEle;
        loc_resourceView = node_createUIResourceViewFactory().createUIResourceView(loc_uiTagResource, loc_tagEnv.getId(), loc_parentResourceView, loc_context, requestInfo);
        return loc_resourceView.getViews();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

