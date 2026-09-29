============================================================
EXERCISE 9 – STUDENT MANAGEMENT SYSTEM
SPRING MVC + HIBERNATE + POSTGRESQL
============================================================


============================================================
1. pom.xml
============================================================

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>StudentManagement</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <!-- Spring MVC -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>6.2.10</version>
        </dependency>

        <!-- Spring ORM -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>6.2.10</version>
        </dependency>

        <!-- Spring Transaction -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>6.2.10</version>
        </dependency>

        <!-- Hibernate -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>6.6.33.Final</version>
        </dependency>

        <!-- PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.7</version>
        </dependency>

        <!-- JPA -->
        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>3.1.0</version>
        </dependency>

        <!-- Servlet -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>6.0.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSP -->
        <dependency>
            <groupId>jakarta.servlet.jsp</groupId>
            <artifactId>jakarta.servlet.jsp-api</artifactId>
            <version>3.1.1</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSTL -->
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
            <version>3.0.2</version>
        </dependency>

        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>3.0.1</version>
        </dependency>

    </dependencies>

    <build>

        <finalName>StudentManagement</finalName>

        <plugins>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.14.0</version>

                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
            </plugin>

        </plugins>

    </build>

</project>


============================================================
2. Student.java
Location:
src/main/java/com/example/entity/Student.java
============================================================

package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "regno")
    private String regno;

    @Column(name = "name")
    private String name;

    @Column(name = "cgpa")
    private double cgpa;

    public Student() {
    }

    public Student(String regno, String name, double cgpa) {
        this.regno = regno;
        this.name = name;
        this.cgpa = cgpa;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }
}


============================================================
3. persistence.xml
Location:
src/main/resources/META-INF/persistence.xml
============================================================

<?xml version="1.0" encoding="UTF-8"?>

<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
             https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">

    <persistence-unit name="studentPU"
                      transaction-type="RESOURCE_LOCAL">

        <provider>
            org.hibernate.jpa.HibernatePersistenceProvider
        </provider>

        <class>com.example.entity.Student</class>

        <properties>

            <property name="jakarta.persistence.jdbc.driver"
                      value="org.postgresql.Driver"/>

            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:postgresql://localhost:5432/studentdb"/>

            <property name="jakarta.persistence.jdbc.user"
                      value="postgres"/>

            <property name="jakarta.persistence.jdbc.password"
                      value="YOUR_PASSWORD"/>

            <property name="hibernate.dialect"
                      value="org.hibernate.dialect.PostgreSQLDialect"/>

            <property name="hibernate.hbm2ddl.auto"
                      value="update"/>

            <property name="hibernate.show_sql"
                      value="true"/>

        </properties>

    </persistence-unit>

</persistence>


============================================================
4. AppConfig.java
Location:
src/main/java/com/example/config/AppConfig.java
============================================================

package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.example")
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactory() {

        LocalEntityManagerFactoryBean factory =
                new LocalEntityManagerFactoryBean();

        factory.setPersistenceUnitName("studentPU");

        return factory;
    }

    @Bean
    public JpaTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {

        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();

        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");

        return resolver;
    }
}


============================================================
5. StudentDAO.java
Location:
src/main/java/com/example/dao/StudentDAO.java
============================================================

package com.example.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class StudentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addStudent(Student student) {
        entityManager.persist(student);
    }

    public List<Student> getAllStudents() {

        return entityManager
                .createQuery("from Student", Student.class)
                .getResultList();
    }

    public Student getStudent(String regno) {

        return entityManager.find(Student.class, regno);
    }

    @Transactional
    public void updateStudent(Student student) {

        entityManager.merge(student);
    }

    @Transactional
    public void deleteStudent(String regno) {

        Student student =
                entityManager.find(Student.class, regno);

        if (student != null) {
            entityManager.remove(student);
        }
    }
}


============================================================
6. StudentController.java
Location:
src/main/java/com/example/controller/StudentController.java
============================================================

package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dao.StudentDAO;
import com.example.entity.Student;

@Controller
public class StudentController {

    @Autowired
    private StudentDAO studentDAO;

    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/add")
    public String addPage(Model model) {

        model.addAttribute("student", new Student());

        return "add";
    }

    @PostMapping("/save")
    public String saveStudent(
            @ModelAttribute("student") Student student) {

        studentDAO.addStudent(student);

        return "redirect:/students";
    }

    @GetMapping("/students")
    public String getStudents(Model model) {

        model.addAttribute(
                "students",
                studentDAO.getAllStudents());

        return "students";
    }

    @GetMapping("/search")
    public String searchPage() {

        return "search";
    }

    @PostMapping("/searchStudent")
    public String searchStudent(
            @RequestParam("regno") String regno,
            Model model) {

        Student student =
                studentDAO.getStudent(regno);

        model.addAttribute("student", student);

        return "search";
    }

    @GetMapping("/edit")
    public String editStudent(
            @RequestParam("regno") String regno,
            Model model) {

        Student student =
                studentDAO.getStudent(regno);

        model.addAttribute("student", student);

        return "add";
    }

    @PostMapping("/update")
    public String updateStudent(
            @ModelAttribute("student") Student student) {

        studentDAO.updateStudent(student);

        return "redirect:/students";
    }

    @GetMapping("/delete")
    public String deleteStudent(
            @RequestParam("regno") String regno) {

        studentDAO.deleteStudent(regno);

        return "redirect:/students";
    }
}


============================================================
7. web.xml
Location:
src/main/webapp/WEB-INF/web.xml
============================================================

<?xml version="1.0" encoding="UTF-8"?>

<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
         https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">

    <display-name>Student Management</display-name>

    <servlet>

        <servlet-name>dispatcher</servlet-name>

        <servlet-class>
            org.springframework.web.servlet.DispatcherServlet
        </servlet-class>

        <init-param>

            <param-name>contextClass</param-name>

            <param-value>
                org.springframework.web.context.support.AnnotationConfigWebApplicationContext
            </param-value>

        </init-param>

        <init-param>

            <param-name>contextConfigLocation</param-name>

            <param-value>
                com.example.config.AppConfig
            </param-value>

        </init-param>

        <load-on-startup>1</load-on-startup>

    </servlet>

    <servlet-mapping>

        <servlet-name>dispatcher</servlet-name>

        <url-pattern>/</url-pattern>

    </servlet-mapping>

</web-app>


============================================================
8. index.jsp
Location:
src/main/webapp/WEB-INF/views/index.jsp
============================================================

<!DOCTYPE html>

<html>

<head>
    <title>Student Management</title>
</head>

<body>

<h1>Student Management System</h1>

<hr>

<h3>Student Operations</h3>

<a href="add">Add Student</a>

<br><br>

<a href="students">View All Students</a>

<br><br>

<a href="search">Search Student</a>

</body>

</html>


============================================================
9. add.jsp
Location:
src/main/webapp/WEB-INF/views/add.jsp
============================================================

<!DOCTYPE html>

<html>

<head>
    <title>Student Details</title>
</head>

<body>

<h2>Student Details</h2>

<form action="${student.regno == null ? 'save' : 'update'}"
      method="post">

    <label>Register Number:</label>

    <input type="text"
           name="regno"
           value="${student.regno}"
           required>

    <br><br>

    <label>Name:</label>

    <input type="text"
           name="name"
           value="${student.name}"
           required>

    <br><br>

    <label>CGPA:</label>

    <input type="number"
           name="cgpa"
           value="${student.cgpa}"
           step="0.01"
           required>

    <br><br>

    <input type="submit"
           value="${student.regno == null
                   ? 'Save Student'
                   : 'Update Student'}">

</form>

<br>

<a href="students">View Students</a>

<br><br>

<a href="/">Home</a>

</body>

</html>


============================================================
10. students.jsp
Location:
src/main/webapp/WEB-INF/views/students.jsp
============================================================

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html>

<head>
    <title>All Students</title>
</head>

<body>

<h2>All Students</h2>

<table border="1" cellpadding="10">

    <tr>

        <th>Register Number</th>

        <th>Name</th>

        <th>CGPA</th>

        <th>Actions</th>

    </tr>

    <c:forEach var="student"
               items="${students}">

        <tr>

            <td>${student.regno}</td>

            <td>${student.name}</td>

            <td>${student.cgpa}</td>

            <td>

                <a href="edit?regno=${student.regno}">
                    Edit
                </a>

                |

                <a href="delete?regno=${student.regno}">
                    Delete
                </a>

            </td>

        </tr>

    </c:forEach>

</table>

<br>

<a href="add">Add Student</a>

<br><br>

<a href="search">Search Student</a>

<br><br>

<a href="/">Home</a>

</body>

</html>


============================================================
11. search.jsp
Location:
src/main/webapp/WEB-INF/views/search.jsp
============================================================

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html>

<head>
    <title>Search Student</title>
</head>

<body>

<h2>Search Student</h2>

<form action="searchStudent" method="post">

    <label>Register Number:</label>

    <input type="text"
           name="regno"
           required>

    <input type="submit"
           value="Search">

</form>

<br>

<c:if test="${student != null}">

    <h3>Student Found</h3>

    <table border="1" cellpadding="10">

        <tr>

            <th>Register Number</th>

            <th>Name</th>

            <th>CGPA</th>

        </tr>

        <tr>

            <td>${student.regno}</td>

            <td>${student.name}</td>

            <td>${student.cgpa}</td>

        </tr>

    </table>

</c:if>

<br>

<a href="students">View All Students</a>

<br><br>

<a href="add">Add Student</a>

<br><br>

<a href="/">Home</a>

</body>

</html>

