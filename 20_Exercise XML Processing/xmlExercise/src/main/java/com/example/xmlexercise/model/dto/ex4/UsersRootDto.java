package com.example.xmlexercise.model.dto.ex4;

import com.example.xmlexercise.model.dto.seed.UserSeedDto;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersRootDto {

    @XmlAttribute(name = "count")
    private Integer count;
    @XmlElement(name = "user")
    private List<UsersDto> users;

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
