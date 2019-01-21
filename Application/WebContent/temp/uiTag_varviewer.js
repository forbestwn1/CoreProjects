
nosliw.runtime.getResourceService().importResource({"id":{"id":"varviewer",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"varviewer",
"context":{"group":{"public":{"element":{}
},
"protected":{"element":{}
},
"internal":{"element":{}
},
"private":{"element":{}
}
},
"info":{}
},
"attributes":{},
"event":[],
"script":
function (env) {
    var loc_env = env;
    var loc_view;
    var loc_viewInput;
    var loc_viewData;
    var loc_out = {initViews: function (requestInfo) {
        loc_view = $("<div/>");
        loc_viewInput = $("<input type=\"text\"/>");
        loc_viewData = $("<textarea rows=\"15\" cols=\"150\" id=\"aboutDescription\" style=\"resize: none;\" data-role=\"none\"></textarea>");
        loc_view.append(loc_viewInput);
        loc_view.append(loc_viewData);
        loc_viewInput.bind("change", function () {
            var variable = nosliw.runtime.getUIVariableManager().getVariable(loc_viewInput.val());
            env.executeDataOperationRequestGet(variable, "", {success: function (requestInfo, data) {
                loc_viewData.val(JSON.stringify(data, null, 4));
            }});
        });
        return loc_view;
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

