package com.franchises.demo.dto;

import java.util.List;

public class FranchiseDTO {
    private String name;
    private List<BranchDTO> branches;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDTO> branches) {
        this.branches = branches;
    }
}