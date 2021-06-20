package com.tosun.medipub.service;

import com.tosun.medipub.model.Article;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@Service
public class ArticleFetchServiceImpl implements ArticleFetchService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int fetchArticleIDs(){

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS articles_lookup(\n" +
                "article_id varchar(255) NOT NULL, PRIMARY KEY (article_id)\n)");

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&RetMax=50000&term=brain&RetStart=1"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String xmlResponse = response.body();
            JSONObject jsonResponse = XML.toJSONObject(xmlResponse);
            JSONObject jsonResponse1 = jsonResponse.getJSONObject("eSearchResult");

            //System.out.println("Total number of articles " + (int) jsonResponse1.get("Count")); //2065705
            JSONArray jsonResponse2 = jsonResponse1.getJSONObject("IdList").getJSONArray("Id");

            String query = "INSERT INTO articles_lookup(article_id) VALUES(?) ON CONFLICT DO NOTHING";
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (int i = 0; i < jsonResponse2.length(); i++) {
                preparedStatement.setString(1,jsonResponse2.get(i).toString());
                preparedStatement.addBatch();
            }
            preparedStatement.close();
            connection.close();

            return 0;

        } catch (IOException | InterruptedException | JSONException | SQLException e) {
            e.printStackTrace();
            return 99;
        }
    }

    @Override
    public int fetchArticles(){

       // sendPost();

       // for(int i= 0; i < 500; i++) {
        for(int i= 0; i < 1; i++) {
            String query = "select *\n" +
                    "from public.articles_lookup\n" +
                    "order by article_id asc\n" +
                    "limit 100 offset " + (100*i);
            Connection connection = null;
            try {

                connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.addBatch();
                ResultSet result = preparedStatement.executeQuery();

                ArrayList<String> articleIDs = new ArrayList<>();

                while (result.next()) {
                    String articleID = result.getString(1);
                    articleIDs.add(articleID);
                }
                connection.close();

                String articleList = String.join(",", articleIDs);
                sendPost(articleList);
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
            return 0;
        }

    private StringBuffer sendPost(String articleIDList) {

        StringBuffer response = new StringBuffer();
        String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi";
        String content = "?db=pubmed&api_key=87c1eb16114c216869c55a19d5e3023fec08&retmode=xml&id=";
        content = content + articleIDList;
        final String CONTENT_TYPE = "application/xml";

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
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);

            //read in the response data
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while((inputLine = input.readLine()) != null) {
                response.append(inputLine.toString());
            }

            //close input stream
            input.close();

            JSONObject jsonResponse = XML.toJSONObject(response.toString());
            JSONArray articleList =  jsonResponse.getJSONObject("PubmedArticleSet").optJSONArray("PubmedArticle");

            ArrayList<Article> articles = new ArrayList<Article>();

            if(articleList != null){
                for(int i=0; i < articleList.length(); i++){
                    Object article = articleList.get(i);
                    Article tempArticle = convertObjectToArticle(article);
                    if(tempArticle != null){
                        articles.add(tempArticle);
                    }
                }
            }
            writeArticlesToDB(articles);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void writeArticlesToDB(ArrayList<Article> articles){

        String query = "INSERT INTO articles (article_id, title, journal, publish_date, revision_date, lang, abstract, authors, keywords )" +
                " VALUES(?,?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING";

        Connection connection = null;
        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for(int i = 0; i < articles.size(); i++){

                preparedStatement.setString(1, articles.get(i).getPMID());
                preparedStatement.setString(2, articles.get(i).getTitle());
                preparedStatement.setString(3, articles.get(i).getJournal());
                preparedStatement.setDate(4, new java.sql.Date(articles.get(i).getPublishDate().getTime()));
                preparedStatement.setDate(5, new java.sql.Date(articles.get(i).getRevisionDate().getTime()));
                preparedStatement.setString(6, articles.get(i).getLanguage());
                preparedStatement.setString(7, articles.get(i).getArticleAbstract());

                Array authors = connection.createArrayOf("VARCHAR", articles.get(i).getAuthorList().toArray());
                preparedStatement.setArray(8, authors);

                Array keywords = connection.createArrayOf("VARCHAR", articles.get(i).getKeywords().toArray());
                preparedStatement.setArray(9, keywords);
                preparedStatement.addBatch();
                //System.out.println(preparedStatement.executeUpdate());
                //preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Article convertObjectToArticle(Object object){

        try {
            JSONObject jsonObject = (JSONObject) object;
            jsonObject = jsonObject.getJSONObject("MedlineCitation");

            String PMID = jsonObject.getJSONObject("PMID").get("content").toString();
            String language = jsonObject.getJSONObject("Article").get("Language").toString();
            String journal = jsonObject.getJSONObject("Article").getJSONObject("Journal").get("Title").toString();
            String title = jsonObject.getJSONObject("Article").get("ArticleTitle").toString();

            JSONObject ISSNObject = jsonObject.getJSONObject("Article").getJSONObject("Journal").optJSONObject("ISSN");

            String ISSN = "";
            if(ISSNObject != null){
                ISSN = ISSNObject.opt("content").toString();
            }

            JSONObject articleDate = jsonObject.getJSONObject("Article").optJSONObject("ArticleDate");

            Date publishDate = parseDate("1899-12-30");

            if (articleDate != null){
                String day = articleDate.opt("Day").toString();
                String month = articleDate.opt("Month").toString();
                String year = articleDate.opt("Year").toString();

                publishDate = parseDate(year + "-" + month + "-" + day);
            }

            JSONObject revisionDateObject = jsonObject.getJSONObject("Article").optJSONObject("DateRevised");

            Date revisionDate = parseDate("1999-12-30");

            if (revisionDateObject != null){
                String day = revisionDateObject.opt("Day").toString();
                String month = revisionDateObject.opt("Month").toString();
                String year = revisionDateObject.opt("Year").toString();

                revisionDate = parseDate(year + "-" + month + "-" + day);
            }

            JSONObject abstractObject = jsonObject.getJSONObject("Article").optJSONObject("Abstract");

            if(abstractObject == null){
                return null;
            }

            String articleAbstract;
            JSONObject abstractJSONObject = abstractObject.optJSONObject("AbstractText");
            if(abstractJSONObject == null){
                JSONArray jsonAbstractArray = abstractJSONObject.optJSONArray("AbstractText");
                if(jsonAbstractArray == null){
                    articleAbstract = abstractObject.get("AbstractText").toString();
                }else{
                    return null;
                }
            }else{
                return null;
            }

            ArrayList<String> authors = new ArrayList<>();

            JSONObject authorObject = jsonObject.getJSONObject("Article").getJSONObject("AuthorList");
            JSONArray authorList = authorObject.optJSONArray("Author");

            String authorName = "";
            if(authorList != null){
                for(int i = 0; i < authorList.length(); i++){
                    authorName = "";
                    if(authorList.getJSONObject(i).opt("ForeName") != null){
                        authorName += authorList.getJSONObject(i).opt("ForeName");
                        authorName += " ";
                    }
                    if(authorList.getJSONObject(i).opt("LastName") != null){
                        authorName += authorList.getJSONObject(i).opt("LastName");
                        authorName += " ";
                    }
                    if(authorList.getJSONObject(i).opt("CollectiveName") != null){
                        authorName += authorList.getJSONObject(i).opt("CollectiveName");
                    }
                    authors.add(authorName);
                }
            }else {
                JSONObject authorList2 = authorObject.optJSONObject("Author");

                if (authorList2.opt("ForeName") != null) {
                    authorName += authorList2.opt("ForeName");
                    authorName += " ";
                }
                if (authorList2.opt("LastName") != null) {
                    authorName += authorList2.opt("LastName");
                    authorName += " ";
                }
                if (authorList2.opt("CollectiveName") != null) {
                    authorName += authorList2.opt("CollectiveName");
                }
            }

            ArrayList<String> keywords = new ArrayList<>();
            JSONObject keywordsObject = jsonObject.optJSONObject("KeywordList");

            JSONArray keywordsArray = new JSONArray();

            if(keywordsObject != null){
                keywordsArray = keywordsObject.optJSONArray("Keyword");
            }

            if((keywordsArray != null) && (keywordsArray.length() > 0)){
                for(int i = 0; i < keywordsArray.length(); i++){
                    keywords.add((String) keywordsArray.getJSONObject(i).get("content"));
                }
            }

            Article article = new Article(PMID, title, articleAbstract);
            article.setAuthorList(authors);
            article.setISSN(ISSN);
            article.setJournal(journal);
            article.setKeywords(keywords);
            article.setLanguage(language);
            article.setPublishDate(publishDate);
            article.setRevisionDate(revisionDate);
            return article;
         //   article.writeToDB(jdbcTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occured when article object created");
            return null;
        }

    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
