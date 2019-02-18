package io.github.alirezamht.authentication.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, name= "name")
    private String name;
    @Column(nullable = false, name= "last_name")
    private String lastName;
    @Column(nullable = false, name= "password")
    private String password;
    @Column(nullable = false, name= "username")
    private String username;
    @Column(nullable = false, name= "manager")
    private Long manager;

    public Long getManager() {
        return manager;
    }

    public User(Long id, String name, String lastName, String password , String username) {
        this(name, lastName,password,username);
        this.id = id;

    }

    public User(String name, String lastName, String password , String username) {
        this.name = name;
        this.username = username;
        this.lastName = lastName;
        this.password=password;
    }

public User(){

}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
