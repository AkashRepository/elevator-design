package com.akash.model.button;

import com.akash.model.Direction;
import com.akash.service.ElevatorRequestService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public abstract class Button {
    String name;
    Direction direction;

    abstract void makeRequest();
}
