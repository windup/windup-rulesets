package com.jboss.windup.test;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbNumberFormat;
        
public class JsonBinding {

    private int id;

    @JsonbProperty("person-name")
    private String name;

    @JsonbProperty(nillable = true)
    private String email;

    @JsonbTransient
    private int age;

    @JsonbDateFormat("dd-MM-yyyy")
    private LocalDate registeredDate;

    private BigDecimal salary;

    @JsonbNumberFormat(locale = "en_US", value = "#0.0")
    public BigDecimal getSalary() {
        return salary;
    }
}