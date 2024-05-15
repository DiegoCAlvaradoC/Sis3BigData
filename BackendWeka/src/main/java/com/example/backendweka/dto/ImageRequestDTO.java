package com.example.backendweka.dto;

public class ImageRequestDTO {

    private String imageName;

    public ImageRequestDTO() {
    }

    public ImageRequestDTO(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
