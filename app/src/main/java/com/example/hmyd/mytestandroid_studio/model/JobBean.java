package com.example.hmyd.mytestandroid_studio.model;

import android.databinding.BaseObservable;

/**
 * @author kongdy
 * @date 2017-04-24 09:31
 * @TIME 9:31
 **/

public class JobBean extends BaseObservable{
    private String name;
    private String job;
    private Long ageTime;
    private String image;

    public JobBean() {
    }

    public JobBean(String name, String job, Long ageTime,String image) {
        this.name = name;
        this.job = job;
        this.ageTime = ageTime;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getAgeTime() {
        return ageTime;
    }

    public void setAgeTime(Long ageTime) {
        this.ageTime = ageTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
