package com.gghouse.wardah.wardahba.webservices;

import com.gghouse.wardah.wardahba.webservices.model.Dummy;
import com.gghouse.wardah.wardahba.webservices.request.AnswersRequest;
import com.gghouse.wardah.wardahba.webservices.request.ChangePasswordRequest;
import com.gghouse.wardah.wardahba.webservices.request.LoginRequest;
import com.gghouse.wardah.wardahba.webservices.request.QuestionerRequest;
import com.gghouse.wardah.wardahba.webservices.request.SalesEditRequest;
import com.gghouse.wardah.wardahba.webservices.request.SalesRequest;
import com.gghouse.wardah.wardahba.webservices.response.GenericResponse;
import com.gghouse.wardah.wardahba.webservices.response.LockingResponse;
import com.gghouse.wardah.wardahba.webservices.response.LoginResponse;
import com.gghouse.wardah.wardahba.webservices.response.NotificationsResponse;
import com.gghouse.wardah.wardahba.webservices.response.ProfileResponse;
import com.gghouse.wardah.wardahba.webservices.response.QuestionerResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesAverageResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesByMonthResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesHighestPerMonthResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesLatestResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesListResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesProductResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesTotalResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestAverageResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestListResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestQuestionsResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestRateResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestTodayResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestTotalResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by michael on 9/18/2016.
 * http://square.github.io/retrofit/
 */
public interface ApiService {
    // Michael Halim : Retrofit get only
    @GET("posts")
    Call<List<Dummy>> dummyGetList();

    // Michael Halim : Retrofit get with parameter
    @GET("posts/{id}")
    Call<Dummy> dummyGet(@Path("id") int id);

    // Michael Halim : Retrofit post
    @FormUrlEncoded
    @POST("posts")
    Call<Dummy> dummyPost(@Field("title") String title, @Field("body") String body, @Field("userId") int userId);

    // Michael Halim : Retrofit post with body
    @POST("posts")
    Call<Dummy> dummyPostBody(@Body Dummy dummy);


    /*
     * APIs
     */
    @POST("/users/login")
    Call<LoginResponse> apiLogin(@Body LoginRequest loginRequest);

    @POST("/users/{user_id}")
    Call<GenericResponse> apiChangePassword(@Path("user_id") long userId, @Body ChangePasswordRequest changePasswordRequest);

    @GET("users/{user_id}/profile")
    Call<ProfileResponse> apiProfile(@Path("user_id") long userId);

    @GET("/notifications")
    Call<NotificationsResponse> apiNotifications(@Query("location_id") long locationId, @Query("page") int page, @Query("size") int size, @Query("sort") String sort);

    /*
     * Sales APIs
     */
    @GET("/sales/{user_id}/latest")
    Call<SalesLatestResponse> apiSalesLatest(@Path("user_id") long userId);

    @GET("/sales/{user_id}/today")
    Call<GenericResponse> apiSalesToday(@Path("user_id") long userId);

    @GET("/sales")
    Call<SalesListResponse> apiSalesList(@Query("user_id") long userId, @Query("page") int page, @Query("size") int size, @Query("start_date") String startDate, @Query("end_date") String endDate);

    @GET("/sales/{user_id}/average")
    Call<SalesAverageResponse> apiSalesAverage(@Path("user_id") long userId);

    @GET("/sales/{user_id}/total-by-month")
    Call<SalesByMonthResponse> apiSalesByMonth(@Path("user_id") long userId);

    @GET("/sales/{user_id}/total")
    Call<SalesTotalResponse> apiSalesTotal(@Path("user_id") long userId);

    @GET("/sales/{user_id}/highest-per-month")
    Call<SalesHighestPerMonthResponse> apiSalesHighestPerMonth(@Path("user_id") long userId);

    @POST("/sales")
    Call<SalesResponse> apiSales(@Body SalesRequest salesRequest);

    @GET("users/{user_id}/product")
    Call<SalesProductResponse> apiSalesProductHighlight(@Path("user_id") long userId);

    @POST("/sales/edit")
    Call<SalesResponse> apiSalesEdit(@Body SalesEditRequest salesEditRequest);

    /*
     * Test APIs
     */

    @GET("/scores/{user_id}/today")
    Call<TestTodayResponse> apiTestToday(@Path("user_id") long userId);

    @GET("/scores")
    Call<TestListResponse> apiTestList(@Query("user_id") long userId, @Query("page") int page, @Query("size") int size, @Query("start_date") String startDate, @Query("end_date") String endDate);

    @GET("/questions")
    Call<TestQuestionsResponse> apiTestQuestions(@Query("user_id") long userId);

    @GET("/scores/{user_id}/average")
    Call<TestAverageResponse> apiTestAverage(@Path("user_id") long userId);

    @GET("/answers/{user_id}/rate")
    Call<TestRateResponse> apiTestRate(@Path("user_id") long userId);

    @GET("/answers/{user_id}/total")
    Call<TestTotalResponse> apiTestTotal(@Path("user_id") long userId);

    @POST("/answers")
    Call<GenericResponse> apiSubmitTest(@Body AnswersRequest answersRequest);

    /*
     * Questioner
     */
    @GET("/questionnaire")
    Call<QuestionerResponse> apiQuestioner();

    @POST("/questionnaire")
    Call<GenericResponse> apiPostQuestioner(@Body QuestionerRequest questionerRequest);


    /*
     * Locking
     */
    @GET("sales/{user_id}/check-locking")
    Call<LockingResponse> apiCheckLocking(@Path("user_id") long userId);
}
