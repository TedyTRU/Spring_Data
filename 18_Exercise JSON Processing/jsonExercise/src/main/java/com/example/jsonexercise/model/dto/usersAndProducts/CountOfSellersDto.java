package com.example.jsonexercise.model.dto.usersAndProducts;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Set;

public class CountOfSellersDto {

    @Expose
    private Integer count;
    @Expose
    private List<UsersDto> users;

    public CountOfSellersDto() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<UsersDto> getUsers() {
        return users;
    }

    public void setUsers(List<UsersDto> users) {
        this.users = users;
    }
}
