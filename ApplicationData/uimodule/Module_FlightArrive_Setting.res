{
	"name": "ModuleFlightArriveSetting",
	"description": "",
	"info": {
		"setting": "setting"
	},
	"context": {
		"group": {
			"public": {
				"element": {
					"flightNumber": {
						"definition": {
							"criteria": "test.string;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.string;1.0.0",
							"value": "A4H2M1"
						}
					},
					"date" : {
						"definition": {
							"criteria": "test.date;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.date;1.0.0",
							"value": "2019"
						}
					}
				}
			}
		}
	},
	"lifecycle" : [
		{
			"name" : "nosliw_INIT_ACTIVE",
			"process" : "nosliw_INIT_ACTIVE"
		}
	], 
	ui :[
		{
			"name": "settingUI",
			"type": "setting",
			"page": "settingPage",
			"info" : {
				"syncout" : "auto1"
			},
			"inputMapping": {
				"element": {
					"flightNumber": {
						"definition": {
							"path": "flightNumber"
						},
						"info": {
							"relativeConnection": "physical"
						}
					},
					"date": {
						"definition": {
							"path": "date"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			},
			"outputMapping": {
				"element": {
					"flightNumber": {
						"definition": {
							"path": "flightNumber"
						},
						"info": {
							"relativeConnection": "physical"
						}
					},
					"date": {
						"definition": {
							"path": "date"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			},
			"serviceMapping": {},
		}
	],
	"attachment": {
		"uiResource" : [
			{
				"name": "settingPage",
				"id": "Page_FlightArrive_Query"
			}
		],
		"process": [
			{
				"name" : "nosliw_INIT_ACTIVE",
				"entity" : {
					"activity": [{
							"id": "startActivityId",
							"name": "startActivity",
							"type": "start",
							"flow": {
								"target": "refreshPage"
							}
						},
						{
							"id": "refreshPage",
							"name": "refreshPage",
							"type": "UI_executeCommand",
							"partId": "ui.settingUI",
							"command": "nosliw_refresh",
							"input": {
								"element": {
									"flightNumber": {
										"definition": { 
											"path": "flightNumber"
										}
									},
									"date": {
										"definition": { 
											"path": "date"
										}
									}
								}
							},
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
				},
			}
		],
	},
}
