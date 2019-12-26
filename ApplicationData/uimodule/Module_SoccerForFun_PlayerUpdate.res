{
	"name": "Module_SoccerForFun_PlayerUpdate",
	"description": "",
	"external": {
		"uiResource" : [
			{
				"name": "playerUpdatePage",
				"id": "Page_SoccerForFun_PlayerUpdate"
			}
		]
	},
	"context": {
		"group" : {
			"public" : {
				"element" : {
					"player" : {
						"definition" : {
							"criteria":"test.string;1.0.0"
						},
						"defaultValue": {
							dataTypeId: "test.string;1.0.0",
							value: "ning"
						}
					}
				}
			}
		}
	},
	"process": {
		"nosliw_INIT_ACTIVE": {
			"activity": [{
					"id": "startActivityId",
					"name": "startActivity",
					"type": "start",
					"flow": {
						"target": "syncInData"
					}
				},
				{
					"id": "syncInData",
					"name": "syncInData",
					"type": "UI_executeCommand",
					"partId": "ui.main",
					"command": "nosliw_syncin_data",
					"input": {
					},
					"result": [{
						"name": "success",
						"flow": {
							"target": "refreshUI"
						}
					}]
				},
				{
					"id": "refreshUI",
					"name": "refreshUI",
					"type": "UI_executeCommand",
					"partId": "ui.main",
					"command": "updateData",
					"input": {
					},
					"result": [{
						"name": "success",
						"flow": {
							"target": "presentUI"
						}
					}]
				},
				{
					"id": "presentUI",
					"name": "presentUI",
					"type": "UI_presentUI",
					"ui": "main",
					"result": [{
						"name": "success",
						"flow": {
							"target": "successEndId"
						}
					}]
				},
				{
					"id": "successEndId",
					"name": "successEnd",
					"type": "end"
				}
			]
		}
	
	},
	ui :[
		{
			"name": "main",
			"page": "playerUpdatePage",
			"inputMapping": {
				"element": {
					"player": {
						"definition": {
							"path": "player"
						},
					},
				}
			},
			"outputMapping": {
				"element": {
				}
			},
			"serviceMapping": {},
		}
	]
}
