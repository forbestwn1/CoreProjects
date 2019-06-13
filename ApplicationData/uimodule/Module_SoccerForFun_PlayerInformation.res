{
	"name": "Module_SoccerForFun_PlayerInformation",
	"description": "",
	"pageInfo": [
		{
			"name": "playerInfoPage",
			"id": "Page_SoccerForFun_PlayerInformation"
		}
	],
	"context": {
		"group": {
			"public": {
				"element": {
					"playerInModule": {
						"definition": {
							"child" : {
								"registered" : {criteria:"test.boolean;1.0.0"},
								"name" : {criteria:"test.string;1.0.0"},
								"email" : {criteria:"test.string;1.0.0"},
							}
						},
						"defaultValue": {
							registered : {
								dataTypeId: "test.boolean;1.0.0",
								value: true
							},
							name : {
								dataTypeId: "test.string;1.0.0",
								value: "Kaida"
							},
							email : {
								dataTypeId: "test.string;1.0.0",
								value: "kaida@hotmail.com"
							},
						}
					},
				}
			}
		}
	},
	"process": {
		"active": {
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
					"command": "refresh",
					"input": {
						"element": {
							"player": {
								"definition": { 
									"path": "playerInModule"
								}
							}
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
			"page": "playerInfoPage",
			"inputMapping": {
				"element": {
					"player": {
						"definition": {
							"path": "playerInModule"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			},
			"outputMapping": {
				"element": {
					"playerInModule": {
						"definition": {
							"path": "player"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			},
			"serviceMapping": {},
		}
	]
}
