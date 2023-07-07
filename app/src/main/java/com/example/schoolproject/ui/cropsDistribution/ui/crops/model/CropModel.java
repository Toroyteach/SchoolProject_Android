package com.example.schoolproject.ui.cropsDistribution.ui.crops.model;

public class CropModel {
     private int id;
     private String cropname;
     private String plantingdate;
     private String harvestingdate;

    public CropModel(int id, String cropname, String plantingdate, String harvestingdate) {
        this.id = id;
        this.cropname = cropname;
        this.plantingdate = plantingdate;
        this.harvestingdate = harvestingdate;
    }

    public int getId() {
        return id;
    }

    public String getCropname() {
        return cropname;
    }

    public String getPlantingdate() {
        return plantingdate;
    }

    public String getHarvestingdate() {
        return harvestingdate;
    }
}
