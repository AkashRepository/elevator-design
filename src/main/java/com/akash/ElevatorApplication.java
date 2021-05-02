package com.akash;

import com.akash.model.Direction;
import com.akash.model.ElevatorController;
import com.akash.service.ElevatorRequestService;
import com.akash.service.SCANElevatorRequestService;

public class ElevatorApplication {

    public static void main(String[] args) throws InterruptedException {
        int numberOfFloors = 10;
        int numberOfElevators = 1;
        ElevatorController app = new ElevatorController(numberOfFloors, numberOfElevators,
                new SCANElevatorRequestService());
        app.startElevators();
        app.callElevator(3, Direction.UP);
        app.moveToFloor("Elevator - 1", 6);
        Thread.sleep(5000);

        app.stopElevators();

    }
}
