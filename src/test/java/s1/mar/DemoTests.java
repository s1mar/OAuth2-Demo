package s1.mar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.junit.jupiter.api.*;
import org.springframework.security.test.context.support.WithMockUser;
import s1.mar.entities.Meal;
import s1.mar.entities.Token;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoTests {

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();
    MediaType JSON = MediaType.parse("application/json");
    static int idOfRecordAddedInScenarioOne=0;

    @Test
    @DisplayName("Scenario 1: Only an authenticated user can add records for themselves only.")
    @WithMockUser(username = "simar")
    @Order(1)
    void testScenario_One() throws Exception {

        ///Authenticate and Get Access Token for User Simar
        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("grant_type", "password")
                .addEncoded("username", "simar")
                .addEncoded("password", "bez123@")
                .addEncoded("scope", "sevenfiveapp")
                .addEncoded("client_id", "sevenfiveapp").build();


        Request requestGetToken = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url("http://localhost:8080/auth/realms/master/protocol/openid-connect/token")
                .post(requestBody)
                .build();

        String access_token = "";
        try (Response response = client.newCall(requestGetToken).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String jsonBody = response.body().string();
            System.out.println(jsonBody);
            assertTrue(response.isSuccessful());
            Token tokenObj = gson.fromJson(jsonBody, Token.class);
            access_token = tokenObj.getAccessToken();
        }


        ///Use Simar's access token to log a meal into Simar's account
        Meal meal = new Meal("simar", LocalDateTime.now().toString(), "Pasta", 1000);
        String mealAsJson = gson.toJson(meal);
        Request requestLogMeal = new Request.Builder()
                .addHeader("Authorization", "Bearer "+access_token)
                //.addHeader("Content-Type","application/json")
                .url("http://localhost:4567/meal/")
                .post(RequestBody.create(mealAsJson,JSON))
                .build();

        ///If all goes well, you'll see that the log was sucessfully added
        try (Response response = client.newCall(requestLogMeal).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            assertTrue(response.isSuccessful());
            assertEquals(200, response.code());
            System.out.println("Entry Saved, with body:\n"+mealAsJson);
        }
    }

    @Test
    @DisplayName("Scenario 2: A user cannot update the records of some different user.")
    @WithMockUser(username = "simar")
    @Order(2)
    void testScenario_Two() throws Exception {


        ///Getting token for account Simar
        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("grant_type", "password")
                .addEncoded("username", "simar")
                .addEncoded("password", "bez123@")
                .addEncoded("scope", "sevenfiveapp")
                .addEncoded("client_id", "sevenfiveapp").build();


        Request requestGetToken = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url("http://localhost:8080/auth/realms/master/protocol/openid-connect/token")
                .post(requestBody)
                .build();

        String access_token = "";
        try (Response response = client.newCall(requestGetToken).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String jsonBody = response.body().string();
            System.out.println(jsonBody);
            assertTrue(response.isSuccessful());
            Token tokenObj = gson.fromJson(jsonBody, Token.class);
            access_token = tokenObj.getAccessToken();
        }


        ///Using Simar's token to update Amreen's records
        Meal meal = new Meal("amreen", LocalDateTime.now().toString(), "Pasta", 1000);
        String mealAsJson = gson.toJson(meal);
        Request requestLogMeal = new Request.Builder()
                .addHeader("Authorization", "Bearer "+access_token)
                //.addHeader("Content-Type","application/json")
                .url("http://localhost:4567/meal/")
                .post(RequestBody.create(mealAsJson,JSON))
                .build();


        ///We should get 403 Forbidden Access. As Simar is not authorized to add/update Amreen's account and vice-versa
        try (Response response = client.newCall(requestLogMeal).execute()) {
            if(response.code()!=403) throw new IOException("Unexpected code " + response);
            assertEquals(403, response.code());
            System.out.println("403: A user cannot update another user's records ");
        }
    }

    @Test
    @DisplayName("Scenario 3: A User can only access their records, and not of any other user.")
    @WithMockUser(username = "simar")
    @Order(3)
    void testScenario_Three() throws Exception {
        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("grant_type", "password")
                .addEncoded("username", "simar")
                .addEncoded("password", "bez123@")
                .addEncoded("scope", "sevenfiveapp")
                .addEncoded("client_id", "sevenfiveapp").build();

        ///Authenticate and Get Access Token for User Simar
        Request requestGetToken = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url("http://localhost:8080/auth/realms/master/protocol/openid-connect/token")
                .post(requestBody)
                .build();

        String access_token = "";
        try (Response response = client.newCall(requestGetToken).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String jsonBody = response.body().string();
            System.out.println(jsonBody);
            assertTrue(response.isSuccessful());
            Token tokenObj = gson.fromJson(jsonBody, Token.class);
            access_token = tokenObj.getAccessToken();
        }

        ///Fetch meal logs for User Simar using their access token
        Request fetchMealLogs = new Request.Builder()
                .addHeader("Authorization", "Bearer "+access_token)
                .url("http://localhost:4567/meal/")
                .get()
                .build();

        ///If all goes well, we should get a status code of 200
        try (Response response = client.newCall(fetchMealLogs).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            assertTrue(response.isSuccessful());
            assertEquals(200, response.code());
            String jsonBodyAsString = response.body().string();
            List<Meal> meals = gson.fromJson(jsonBodyAsString,new TypeToken<List<Meal>>(){}.getType());
            idOfRecordAddedInScenarioOne = meals.get(meals.size()-1).getId();
            System.out.println("User Meal Logs:\n"+jsonBodyAsString);
        }
    }
    @Test
    @DisplayName("Scenario 4: Only admin users can delete the meal logs in the meal database.")
    @WithMockUser(username = "simar")
    @Order(4)
    void testScenario_Four() throws Exception {

        ///We'll try to delete the meal log added in Scenario 1:

        ///CASE 1 :When a non admin user tries to delete the meal log.

        ///User Amreen is not an Admin, we'll first attempt to delete with her credentials

        RequestBody requestBodyAmreen  = new FormBody.Builder()
                .addEncoded("grant_type", "password")
                .addEncoded("username", "amreen")
                .addEncoded("password", "qwerty123@")
                .addEncoded("scope", "sevenfiveapp")
                .addEncoded("client_id", "sevenfiveapp").build();

        ///Authenticate and Get Access Token for User Amreen; Amreen is not an admin user
        Request requestGetTokenAmreen = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url("http://localhost:8080/auth/realms/master/protocol/openid-connect/token")
                .post(requestBodyAmreen)
                .build();

        String access_token_amreen = "";
        try (Response response = client.newCall(requestGetTokenAmreen).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String jsonBody = response.body().string();
            Token tokenObj = gson.fromJson(jsonBody, Token.class);
            access_token_amreen = tokenObj.getAccessToken();
        }


        ///Attempting to delete the entry with Amreen
        Request deleteRecordOne_Amreen = new Request.Builder()
                .addHeader("Authorization", "Bearer "+access_token_amreen)
                .url("http://localhost:4567/meal/"+String.valueOf(idOfRecordAddedInScenarioOne))
                .delete()
                .build();

        try(Response response = client.newCall(deleteRecordOne_Amreen).execute()){

            if(response.code()!=403) throw new IOException("Unexpected code " + response);
             assertEquals(403,response.code());
             System.out.println("Amreen couldn't delete: "+response.body().string());
        }


        ///Now,we'll attempt delete with Admin Simar

        RequestBody requestBodyAuthSimar = new FormBody.Builder()
                .addEncoded("grant_type", "password")
                .addEncoded("username", "simar")
                .addEncoded("password", "bez123@")
                .addEncoded("scope", "sevenfiveapp")
                .addEncoded("client_id", "sevenfiveapp").build();

        ///Authenticate and Get Access Token for User Simar; Simar is an admin user
        Request requestGetTokenSimar = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url("http://localhost:8080/auth/realms/master/protocol/openid-connect/token")
                .post(requestBodyAuthSimar)
                .build();

        String access_token_simar= "";
        try (Response response = client.newCall(requestGetTokenSimar).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String jsonBody = response.body().string();
            Token tokenObj = gson.fromJson(jsonBody, Token.class);
            access_token_simar = tokenObj.getAccessToken();
        }

        ///Attempting to delete the entry with Simar
        Request deleteRecordOne_Simar = new Request.Builder()
                .addHeader("Authorization", "Bearer "+access_token_simar)
                .url("http://localhost:4567/meal/"+String.valueOf(idOfRecordAddedInScenarioOne))
                .delete()
                .build();

        try(Response response = client.newCall(deleteRecordOne_Simar).execute()){

            if(response.code()!=200) throw new IOException("Unexpected code " + response);
            assertEquals(200,response.code());
            System.out.println("Simar (an Admin) was able to delete");
        }
    }

}
