package cn.varin.springbootdataredis_demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class User {
     String name;
     Integer age;
     public User(){

     }
     public User(String name,Integer age){
          this. name = name;
          this.age = age;

     }

     public void setAge(Integer age) {
          this.age = age;
     }

     public void setName(String name) {
          this.name = name;
     }

     public Integer getAge() {
          return age;
     }

     public String getName() {
          return name;
     }

     @Override
     public String toString() {
          return "User{" +
                  "name='" + name + '\'' +
                  ", age=" + age +
                  '}';
     }
}
