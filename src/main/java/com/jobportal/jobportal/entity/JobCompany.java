package com.jobportal.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_company")
public class JobCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String logo;

    public JobCompany(){}
    public JobCompany(int id, String name, String logo){
        this.id=id;
        this.name=name;
        this.logo=logo;
    }

    public int getId() {
        return id;
    }

    public void setJobCompanyId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
