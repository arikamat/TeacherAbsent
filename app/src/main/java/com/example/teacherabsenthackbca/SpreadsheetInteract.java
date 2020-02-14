package com.example.teacherabsenthackbca;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class SpreadsheetInteract {
    private String spreadsheetId;
    private String range;
    private String valueRenderOption = "";
    private String dateTimeRenderOption="";
    private static ArrayList<String> listData = new ArrayList<String>();
    private static ArrayList<String> finalListData;
    public SpreadsheetInteract(String id, String spreadsheetRange) {
        spreadsheetId = id;
        range = spreadsheetRange;
    }

    public ArrayList<String> init() throws IOException,GeneralSecurityException,JSONException {

            Sheets sheetsService = createSheetsService();
            Sheets.Spreadsheets.Values.Get request =
                    sheetsService.spreadsheets().values().get(spreadsheetId, range);
            request.setValueRenderOption(valueRenderOption);
            request.setDateTimeRenderOption(dateTimeRenderOption);
            ValueRange response = request.execute();
            finalListData = new ArrayList<String>(jsonToArray(response));
            return finalListData;
    }

    public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = null;

        return new Sheets.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Google-SheetsSample/0.1")
                .build();
    }

    public static ArrayList<String> jsonToArray(ValueRange theObject) throws IOException, JSONException {

            JSONObject name = new JSONObject(theObject);
            JSONArray nameArr = name.getJSONArray("values");
            if (name != null) {
                for (int i = 0; i < nameArr.length(); i++) {
                    listData.add(nameArr.getString(i));
                }
            }
        return listData;
    }
}
