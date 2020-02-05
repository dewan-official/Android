package com.dewan.oli.ullah;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api_Interface {
    @GET("querys/")
    Call<Results> getStudentList();
}
