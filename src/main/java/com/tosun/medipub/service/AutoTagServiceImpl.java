package com.tosun.medipub.service;

import com.tosun.medipub.model.Article;
import com.tosun.medipub.model.AutoTag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@Service
public class AutoTagServiceImpl implements AutoTagService{

    String licenceKey = "e3a6351ba5e89f528e5bb13cc5df7d8b";
    ArticleService articleService;

    @Autowired
    public AutoTagServiceImpl(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public AutoTag autoTagArticle(String articleID) {

        StringBuffer response = new StringBuffer();
        String url = "https://api.meaningcloud.com/topics-2.0";
        String content = "?key=e3a6351ba5e89f528e5bb13cc5df7d8b&lang=en&tt=a&txt=";
        content = content + getArticleWords(articleID);
        content = content.replace(' ','+');

        try {
            //create http connection
            URL obj = new URL(url + content);
            HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();

            //add request header
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);

            //read in the response data
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while((inputLine = input.readLine()) != null) {
                response.append(inputLine.toString());
            }

            //close input stream
            input.close();

            JSONObject object = new JSONObject(response.toString());
            return  processObject(object);
        } catch (IOException e) {
            System.out.println("error occured");
            e.printStackTrace();
        }
        return null;
    }

    public AutoTag processObject(JSONObject object){

        ArrayList<String> tempSementityList = new ArrayList<>();
        ArrayList<String> tempSemldList = new ArrayList<>();
        ArrayList<String> tempVerbList = new ArrayList<>();
        ArrayList<String> tempSubjectList = new ArrayList<>();

        JSONArray entityListArray = object.getJSONArray("entity_list");

        if(entityListArray.length() != 0){
            for(int i = 0; i < entityListArray.length(); i++){
                JSONObject temp =entityListArray.getJSONObject(i);
                tempSementityList.add(temp.getJSONObject("sementity").getString("type"));

                JSONArray semldArray = temp.getJSONArray("semld_list");
                for(int k = 0; k < semldArray.length(); i++){
                    if(!semldArray.get(k).toString().equals("") && !semldArray.get(k).toString().contains("http")){
                        tempSemldList.add(semldArray.get(k).toString());
                    }
                }
            }
        }

        JSONArray conceptListArray = object.getJSONArray("concept_list");

        if(conceptListArray.length() != 0){
            for(int i = 0; i < conceptListArray.length(); i++){
                tempSementityList.add(conceptListArray.getJSONObject(i).getJSONObject("sementity").getString("type"));
            }
        }

        JSONArray relationArray = object.getJSONArray("relation_list");

        for(int i = 0; i < relationArray.length(); i++){
            tempVerbList.add(relationArray.getJSONObject(i).getJSONObject("verb").getString("form"));
            tempSubjectList.add(relationArray.getJSONObject(i).getJSONObject("subject").getString("form"));
        }

        AutoTag autoTag = new AutoTag();
        autoTag.setSemldList(tempSemldList);
        autoTag.setVerbList(tempVerbList);
        autoTag.setSubjectlist(tempSubjectList);
        autoTag.setSementityList(tempSementityList);

        return autoTag;
    }


    public String getArticleWords(String articleID){

        ArrayList<String> tempArrayList = new ArrayList<>();
        tempArrayList.add(articleID);

        Article article = articleService.getArticles(tempArrayList).get(0);
        String articleText = article.getTitle(); //+ " " + article.getArticleAbstract();

        return articleText;
    }
}
