{
	"name": "Module_SoccerForFun_PlayerLineup",
	"description": "",
	"pageInfo": [
		{
			"name": "playerLineupPage",
			"id": "Page_SoccerForFun_PlayerLineup"
		}
	],
	"context": {
		"group" : {
		}
	},
	"process": {
		"nosliw_INIT_ACTIVE": {
			"activity": [{
					"id": "startActivityId",
					"name": "startActivity",
					"type": "start",
					"flow": {
						"target": "refreshUI"
					}
				},
				{
					"id": "refreshUI",
					"name": "refreshUI",
					"type": "UI_executeCommand",
					"partId": "ui.main",
					"command": "updateData",
					"input": {
						"element": {
						}
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
			"page": "playerLineupPage",
		}
	],
	
	
}
