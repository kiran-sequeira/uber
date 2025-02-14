package com.company.uber.model;

import java.io.Serializable;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@DynamoDbBean
public class Location implements Serializable{
    private static final long serialVersionUID = 1L;
    private double longitude;
    private double latitude;
}
