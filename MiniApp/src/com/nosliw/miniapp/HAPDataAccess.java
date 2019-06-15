package com.nosliw.miniapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.imp.io.HAPDBSource;
import com.nosliw.miniapp.entity.HAPGroup;
import com.nosliw.miniapp.entity.HAPMiniApp;
import com.nosliw.miniapp.entity.HAPMiniAppSettingData;
import com.nosliw.miniapp.entity.HAPOwnerInfo;
import com.nosliw.miniapp.entity.HAPSettingData;
import com.nosliw.miniapp.entity.HAPUser;
import com.nosliw.miniapp.entity.HAPUserInfo;

public class HAPDataAccess {

	private HAPDBSource m_dbSource;

	private long i = System.currentTimeMillis();
	
	public HAPDataAccess(HAPDBSource dbSource) {
		this.m_dbSource = dbSource;
	}

	public void createSampleDataForUser(String userId) {
		try {
//			HAPGroup soccerGroup = new HAPGroup(this.generateId(), "Soccer");
//			HAPGroup schoolGroup = new HAPGroup(this.generateId(), "School");
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_GROUP (ID, NAME) VALUES ('"+soccerGroup.getId()+"', '"+soccerGroup.getName()+"');").execute();
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_GROUP (ID, NAME) VALUES ('"+schoolGroup.getId()+"', '"+schoolGroup.getName()+"');").execute();

			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERGROUP (ID, USERID, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SoccerGroup"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERGROUP (ID, USERID, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SchoolGroup"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERGROUP (ID, USERID, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SoccerForFun"+"');").execute();

			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"MyRealtor"+"', '"+"MyRealtor"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"Airplane Arrival"+"', '"+"Airplane Arrival"+"');").execute();

			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SoccerApp1"+"', '"+"SoccerApp1"+"', '"+"SoccerGroup"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SoccerApp2"+"', '"+"SoccerApp2"+"', '"+"SoccerGroup"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SchoolApp1"+"', '"+"SchoolApp1"+"', '"+"SchoolGroup"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SchoolApp2"+"', '"+"SchoolApp2"+"', '"+"SchoolGroup"+"');").execute();

			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"PlayerInformation"+"', '"+"PlayerInformation"+"', '"+"SoccerForFun"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"Register"+"', '"+"Register"+"', '"+"SoccerForFun"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"Position"+"', '"+"Position"+"', '"+"SoccerForFun"+"');").execute();

			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"AppMySchool;main"+"', '"+"MySchool"+"');").execute();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public HAPUser createUser() {
		HAPUser out = new HAPUser();
		out.setId(this.generateId());
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("INSERT INTO MINIAPP_USER (NAME, ID) VALUES ('"+out.getId()+"', '"+out.getId()+"');");
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPUser getUserById(String id) {
		HAPUser out = null;
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_user where id='"+id+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out = new HAPUser();
				out.setId((String)resultSet.getObject(HAPUser.ID));
				out.setName((String)resultSet.getObject(HAPUser.NAME));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPGroup getGroupById(String id) {
		HAPGroup out = null;
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_group where id='"+id+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out = new HAPGroup();
				out.setId((String)resultSet.getObject(HAPGroup.ID));
				out.setName((String)resultSet.getObject(HAPGroup.NAME));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public List<HAPGroup> getUserGroups(String userId){
		List<HAPGroup> out = new ArrayList<HAPGroup>();
		try {
			List<String> groupIds = new ArrayList<String>();
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_usergroup where userid='"+userId+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				groupIds.add(resultSet.getString("groupid"));
			}
			
			for(String groupId : groupIds) {
				HAPGroup group = this.getGroupById(groupId);
				out.add(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPMiniApp getMiniApp(String id) {
		HAPMiniApp out = null;
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_miniapp where id='"+id+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out = new HAPMiniApp((String)resultSet.getObject(HAPGroup.ID), (String)resultSet.getObject(HAPMiniApp.NAME));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public void updateUserInfoWithMiniApp(HAPUserInfo userInfo) {
		PreparedStatement statement;
		try {
			statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_userapp where userid='"+userInfo.getUser().getId()+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				String miniAppId = resultSet.getString("appid");
				HAPMiniApp miniApp = getMiniApp(miniAppId); 
				String groupId = resultSet.getString("groupid");
				userInfo.addMiniApp(miniApp, groupId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HAPMiniAppSettingData getSettingData(HAPOwnerInfo ownerInfo) {
		HAPMiniAppSettingData out = new HAPMiniAppSettingData(ownerInfo);
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM MINIAPP_INSTANCEDATA_SETTING where userid='"+ownerInfo.getUserId()+"' AND ownerid='"+ownerInfo.getComponentId()+"' AND ownertype='"+ownerInfo.getComponentType()+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out.addData(buildSettingDataFromResult(resultSet));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPMiniAppSettingData getSettingData(HAPOwnerInfo ownerInfo, String[] dataNames) {
		HAPMiniAppSettingData out = new HAPMiniAppSettingData(ownerInfo);
		try {
			for(String dataName : dataNames) {
				PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM MINIAPP_INSTANCEDATA_SETTING where userid='"+ownerInfo.getUserId()+"' AND ownerid='"+ownerInfo.getComponentId()+"' AND ownertype='"+ownerInfo.getComponentType()+"' AND dataname='"+dataName+"';");
				ResultSet resultSet = statement.executeQuery();
				while(resultSet.next()) {
					out.addData(buildSettingDataFromResult(resultSet));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPMiniAppSettingData updateSettingData(HAPMiniAppSettingData miniAppSettingData) {
		HAPMiniAppSettingData out = miniAppSettingData;
		
		Map<String, HAPSettingData> datas = out.getDatas();
		for(String dataName : datas.keySet()) {
			try {
				HAPSettingData data = datas.get(dataName);
				if(data.getId()==null) {
					//create
					String id = this.generateId();
					PreparedStatement statement = this.getConnection().prepareStatement("INSERT INTO MINIAPP_INSTANCEDATA_SETTING (ID,USERID,OWNERID,OWNERTYPE, DATANAME,DATA) VALUES ('"+
								id+"', '"+miniAppSettingData.getOwnerInfo().getUserId()+"', '"+out.getOwnerInfo().getComponentId()+"', '"+out.getOwnerInfo().getComponentType()+"', '"+dataName+"', '"+data.getDataStr()+"');");
					statement.execute();
					data.setId(id);
				}
				else {
					//update
					PreparedStatement statement = this.getConnection().prepareStatement("UPDATE MINIAPP_INSTANCEDATA_SETTING SET DATA='"+data.getDataStr()+"'  WHERE ID='"+data.getId()+"'");
					statement.execute();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return out;
	}

	private HAPSettingData buildSettingDataFromResult(ResultSet resultSet) {
		HAPSettingData data = new HAPSettingData();
		try {
			data.setData(resultSet.getString("data"));
			data.setName(resultSet.getString("name"));
			data.setOwnerId(resultSet.getString("ownerid"));
			data.setOwnerType(resultSet.getString("ownertype"));
			data.setId(resultSet.getString("id"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return data;
	}
/*
	public HAPMiniAppSettingData createSettingData(String userId, HAPMiniAppSettingData miniAppSettingData) {
		HAPMiniAppSettingData out = miniAppSettingData;
		
		Map<String, HAPSettingData> datas = out.getDatas();
		for(String dataName : datas.keySet()) {
			HAPSettingData data = datas.get(dataName);
			try {
				String id = this.generateId();
				PreparedStatement statement = this.getConnection().prepareStatement("INSERT INTO MINIAPP_INSTANCEDATA_SETTING (ID,USERID,OWNERID,OWNERTYPE, DATANAME,DATA) VALUES ('"+
							id+"', '"+userId+"', '"+out.getOwnerId()+"', '"+out.getOwnerType()+"', '"+dataName+"', '"+data.getDataStr()+"');");
				statement.execute();
				data.setId(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return out;
	}
*/
	public void deleteSettingData(String dataId) {
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("DELETE FROM MINIAPP_INSTANCEDATA_SETTING WHERE ID='"+dataId+"';");
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String generateId() {  return i+++"";   }
	
	protected Connection getConnection(){		return this.m_dbSource.getConnection();	}

	public HAPDBSource getDBSource(){  return this.m_dbSource;  }

}
