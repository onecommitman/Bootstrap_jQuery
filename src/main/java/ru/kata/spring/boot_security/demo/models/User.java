package ru.kata.spring.boot_security.demo.models;

import javax.persistence.*;
import javax.validation.constraints.*;


@Entity
@Table(name = "security_users")
public class User {
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    @NotEmpty
    @Size(min = 6, max = 255)
    private String username;

    @Column(name = "password")
    @NotEmpty
    @Size(min = 8)
    private String password;

    @Column(name = "name")
    @NotEmpty(message = "Name shouldn't be empty")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$", message = "The name must consist of letters only!")
    @Size(min = 2, max = 30, message = "The name should be between 2 and 30 characters")
    private String name;

    @Column(name = "lastname")
    @NotEmpty(message = "Surname shouldn't be empty")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$", message = "The surname must consist of letters only!")
    @Size(min = 2, max = 30, message = "The surname should be between 2 and 30 characters")
    private String lastName;

    @Column(name = "age")
    @NotNull(message = "Shouldn't be empty!")
    @Min(value = 18, message = "Must be over 18 years of age!")
    private Byte age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }


}
