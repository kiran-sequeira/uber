package com.company.uber.util;

import com.company.uber.model.Location;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public class LocationConverter implements AttributeConverter<Location> {

  @Override
  public AttributeValue transformFrom(Location location) {
    Map<String, AttributeValue> locationMap = new HashMap<>();
    locationMap.put("latitude", AttributeValue.builder()
        .n(String.valueOf(location.getLatitude())).build());
    locationMap.put("longitude", AttributeValue.builder()
        .n(String.valueOf(location.getLongitude())).build());
    return AttributeValue.builder().m(locationMap).build();
  }

  @Override
  public Location transformTo(AttributeValue attributeValue) {
    Map<String, AttributeValue> locationMap = attributeValue.m();
    Location location = new Location();
    location.setLatitude(Double.parseDouble(locationMap.get("latitude").n()));
    location.setLongitude(Double.parseDouble(locationMap.get("longitude").n()));
    return location;
  }

  @Override
  public EnhancedType<Location> type() {
    return EnhancedType.of(Location.class);
  }

  @Override
  public AttributeValueType attributeValueType() {
    return AttributeValueType.M;
  }
}