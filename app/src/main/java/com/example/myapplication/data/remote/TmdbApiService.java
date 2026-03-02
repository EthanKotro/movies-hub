package com.example.myapplication.data.remote;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.CreditsResponse;
import com.example.myapplication.data.model.Movie;
import com.example.myapplication.data.model.MovieDetail;
import com.example.myapplication.data.model.TvShow;
import com.example.myapplication.data.model.VideoResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApiService {

        // Movies
        @GET("trending/movie/week")
        Single<ApiResponse<Movie>> getTrendingMovies(@Query("page") int page);

        @GET("movie/top_rated")
        Single<ApiResponse<Movie>> getTopRatedMovies(@Query("page") int page);

        @GET("movie/upcoming")
        Single<ApiResponse<Movie>> getUpcomingMovies(@Query("page") int page);

        @GET("movie/popular")
        Single<ApiResponse<Movie>> getPopularMovies(@Query("page") int page);

        @GET("movie/now_playing")
        Single<ApiResponse<Movie>> getNowPlayingMovies(@Query("page") int page);

        @GET("search/movie")
        Single<ApiResponse<Movie>> searchMovies(
                        @Query("query") String query,
                        @Query("page") int page);

        // Movie Details
        @GET("movie/{movie_id}")
        Single<MovieDetail> getMovieDetails(
                        @Path("movie_id") int movieId,
                        @Query("append_to_response") String appendToResponse);

        @GET("movie/{movie_id}/credits")
        Single<CreditsResponse> getMovieCredits(@Path("movie_id") int movieId);

        @GET("movie/{movie_id}/videos")
        Single<VideoResponse> getMovieVideos(@Path("movie_id") int movieId);

        @GET("movie/{movie_id}/recommendations")
        Single<ApiResponse<Movie>> getMovieRecommendations(@Path("movie_id") int movieId, @Query("page") int page);

        // TV Shows
        @GET("trending/tv/week")
        Single<ApiResponse<TvShow>> getTrendingTvShows(@Query("page") int page);

        @GET("tv/top_rated")
        Single<ApiResponse<TvShow>> getTopRatedTvShows(@Query("page") int page);

        @GET("search/tv")
        Single<ApiResponse<TvShow>> searchTvShows(
                        @Query("query") String query,
                        @Query("page") int page);

        // Discover
        @GET("discover/movie")
        Single<ApiResponse<Movie>> discoverMovies(
                        @Query("page") int page,
                        @Query("sort_by") String sortBy,
                        @Query("with_genres") String withGenres);
}
