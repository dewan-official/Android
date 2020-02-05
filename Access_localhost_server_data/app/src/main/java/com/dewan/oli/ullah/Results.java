package com.dewan.oli.ullah;

import java.util.List;

class Results {
    private String status;
    private int totalResult;
    List<Students> studentLists;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<Students> getStudentLists() {
        return studentLists;
    }

    public void setStudentLists(List<Students> studentLists) {
        this.studentLists = studentLists;
    }
}
