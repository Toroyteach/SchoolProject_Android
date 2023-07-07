package com.example.schoolproject.ui.cropsDistribution.ui.distribution.model;

public class DistributionModel {
    private int id;
    private String cropid;
    private String seasonid;
    private String size;

    public DistributionModel(int id, String cropid, String seasonid, String size) {
        this.id = id;
        this.cropid = cropid;
        this.seasonid = seasonid;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public String getCropid() {
        return cropid;
    }

    public String getSeasonid() {
        return seasonid;
    }

    public String getSize() {
        return size;
    }
}
