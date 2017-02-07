package com.aminiam.moviekade.other;

public interface Callback {
    void onItemSelected(long movieId, String movieTitle, String posterPath, String backdropPath);
}
