package com.example.schoolproject.ui.cropsDistribution.ui.season.model;

public class SeasonModel {
    private int id;
    private String seasonname;
    private String startdate;
    private String enddate;

    public SeasonModel(int id, String seasonname, String startdate, String enddate) {
        this.id = id;
        this.seasonname = seasonname;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public int getId() {
        return id;
    }

    public String getSeasonname() {
        return seasonname;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }
}
