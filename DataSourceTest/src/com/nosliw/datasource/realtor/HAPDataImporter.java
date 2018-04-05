package com.nosliw.datasource.realtor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPDataImporter {

	public static final double minLat = 43.758307;
	public static final double maxLat = 43.858130;
	public static final double minLon = -79.355032;
	public static final double maxLon = -79.257924;

	public static void main(String[] args) throws Exception{
		StringBuffer out = new StringBuffer();
		out.append("[");

		String parms;
		int total;
		int page;
		
		parms = "BuildingTypeId=1";
		page = 1;
		total = sendPost(page, out, parms);
		while(page<total){
			page++;
			out.append(",");
			total = sendPost(page, out, parms);
		}
		
		out.append(",");
		parms = "BuildingTypeId=16";
		page = 1;
		total = sendPost(page, out, parms);
		while(page<total){
			page++;
			out.append(",");
			total = sendPost(page, out, parms);
		}
		
		out.append("]");
		HAPFileUtility.writeFile("homesArray.js", HAPJsonUtility.formatJson(out.toString()));
	}
	
	
	// HTTP POST request
	private static int sendPost(int page, StringBuffer out, String parms) throws Exception {

		String url = "https://api2.realtor.ca/Listing.svc/PropertySearch_Post";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("PropertyTypeGroupID", "application/x-www-form-urlencoded; charset=UTF-8");
//		con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		con.setRequestProperty("Accept", "*/*");

//		String urlParameters = "PriceMin=400000&PriceMax=700000&CultureId=1&ApplicationId=1&RecordsPerPage=90&MaximumResults=90&PropertySearchTypeId=1&TransactionTypeId=2&StoreyRange=0-0&BedRange=0-0&BathRange=0-0&LongitudeMin=-80.444052&LongitudeMax=-78.570883&LatitudeMin=43.168231&LatitudeMax=44.107412&SortOrder=A&SortBy=1&viewState=m&Longitude=-79.312311&Latitude=43.755543&ZoomLevel=5&PropertyTypeGroupID=1&CurrentPage="+page+"&"+parms;
		String urlParameters = "PriceMin=400000&PriceMax=700000&CultureId=1&ApplicationId=1&RecordsPerPage=90&MaximumResults=90&PropertySearchTypeId=1&TransactionTypeId=2&StoreyRange=0-0&BedRange=0-0&BathRange=0-0&LongitudeMin="+minLon+"&LongitudeMax="+maxLon+"&LatitudeMin="+minLat+"&LatitudeMax="+maxLat+"&SortOrder=A&SortBy=1&viewState=m&Longitude=-79.312311&Latitude=43.755543&ZoomLevel=5&PropertyTypeGroupID=1&CurrentPage="+page+"&"+parms;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		String responseStr = response.toString();
		JSONObject jsonResponse = new JSONObject(responseStr);
		
		String resuleArrayStr = jsonResponse.optJSONArray("Results").toString();
		int startI = resuleArrayStr.indexOf("[");
		int endI = resuleArrayStr.lastIndexOf("]");
		String resultStr = resuleArrayStr.substring(startI+1, endI);
		out.append(resultStr);
		
		int totalPage = jsonResponse.optJSONObject("Paging").optInt("TotalPages");
		return totalPage;
	}	

	
}
