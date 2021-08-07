{
	"name": "infoTemplate",
	"description": "",
	"info": {
		"setting": "application"
	},
	"valuestructure": {
		"group": {
			"public": {
				"flat": {
				}
			},
			"protect": {
				"flat": {
					"selectData" : {
						"definition": {
						}
					}
				}
			}
		}
	},
	"ui": [
		{
			"name": "mainPage",
			"type": "list",
			"page": "mainPage",
			"status" : "disabled111",
			"inputMapping": [
				"type" : "mirror"
			],
			"outputMapping": [
				"type" : "mirror"
			],
			"eventHandler": [
				{
					"eventName" : "selectItem",
					"eventDataVariable" : "selectSchoolInfoInModule",
					"dataMapping" : {
						"selectData" : "EVENT.data"
					}
					"handler" : [
						{
							"stepType" : "process",
							"process" : "selectSchool"
						}
					]
				}
			],
		},
		{
			"name": "infoPage",
			"type": "info",
			"page": "infoPage",
			"status" : "disabled111",
			"inputMapping": [
				"mapping" : {
					"data" : "selectData"
				}
			],
			"outputMapping": [
				"type" : "mirror"
			],
		}
	],
	
	"handler": [
		{
			"name" : "init",
			"step" : [
				{
					"name" : "displayPage"
				},
			]
		},
		{
			"name" : "eventHandler",
			"step" : [
				{
					"name" : ""
				},
				{
					"name" : "pageTransit"
				}
			]
		}
	
	],
	
	"services":
	[
	],
	"lifecycle" : [
		{
			"name" : "nosliw_INIT_ACTIVE",
			"process" : "nosliw_INIT_ACTIVE"
		}
	], 
	"attachment": {
		"uiResource" : [
			{
				"name": "mainPage",
				"referenceId": "toBeDetermined"
			},
			{
				"name": "infoPage",
				"referenceId": "toBeDetermined",
			}
		],
	}
}
