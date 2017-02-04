package com.aminiam.moviekade.other;

import java.util.ArrayList;

public class MovieInformationStructure {
    public long id;
    public String title;
    public String genres;
    public String status;
    public String overview;
    public String runtime;
    public String language;
    public boolean adult;
    public String releaseDate;
    public String website;
    public long revenue;
    public ArrayList<String> trailers = new ArrayList<>();
    public String[][] reviews;
}
