package com.akash.model;


import com.akash.model.button.InternalButton;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Elevator {

    String name;
    Integer capacity;
    Direction currentDirection;
    State state;
    int currentFloor;


    public void moveUp() {
        currentFloor = currentFloor + 1;
        System.out.println("Elevator " + this.name + " is moving up -> Floor No. " + currentFloor);
    }

    public void moveDown() {
        currentFloor = currentFloor - 1;
        System.out.println("Elevator " + this.name + " is moving down -> Floor No. " + currentFloor);
    }

    public void openDoor() {
        // open door
        System.out.println("Door open for " + this.name);
    }

    public void closeDoor() {
        // close door
        System.out.println("Door close for " + this.name);
    }
}

