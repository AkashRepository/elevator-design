package com.akash.model;

import com.akash.model.Direction;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Builder
@Data
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Request {

    int floorToStop;
    Direction direction;
    @EqualsAndHashCode.Exclude
    Instant time;

}
