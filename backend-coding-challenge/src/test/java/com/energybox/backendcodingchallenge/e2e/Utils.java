package com.energybox.backendcodingchallenge.e2e;

import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

  public static <T> List<T> mapCollectionOntoDto(Collection collection,
                                                               Class dtoClass) {
    List<T> expectedResponseDto =
        (List) collection.stream().map(
            sensorEntity -> new ModelMapper().map(sensorEntity, dtoClass)
        ).collect(Collectors.toList());

    return expectedResponseDto;
  }
}

@Data
class ClassWithId {
  Long id;
}