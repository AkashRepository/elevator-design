package com.akash.model;

import com.akash.runner.ElevatorRunner;
import com.akash.model.button.ExternalButton;
import com.akash.model.button.InternalButton;
import com.akash.service.ElevatorRequestService;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ElevatorController {

    List<ElevatorRunner> elevators;
    int numberOfElevators;
    int numberOfFloors;
    ExecutorService executorService;
    List<ExternalButton> externalButtons;
    Map<String, List<InternalButton>> internalButtonsMap;
    ElevatorRequestService elevatorRequestService;
    TreeSet<Request> externalRequests;

    public ElevatorController(int numberOfFloors, int numberOfElevators, ElevatorRequestService elevatorRequestService) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        this.elevators = new ArrayList<>(numberOfElevators);
        this.externalButtons = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(numberOfElevators);

        this.internalButtonsMap = new HashMap<>();
        this.externalRequests = new TreeSet<>((a,b) -> a.getTime().compareTo(b.getTime()));
        this.elevatorRequestService = elevatorRequestService;
        initExternalButton();
    }

    private void initExternalButton() {
        for (int i = 1; i <= numberOfFloors; i++) {
            this.externalButtons.add(ExternalButton.builder()
                    .sourceFloor(i)
                    .externalRequests(externalRequests)
                    .name("BTN-UP")
                    .direction(Direction.UP)
                    .build());
            this.externalButtons.add(ExternalButton.builder()
                    .sourceFloor(i)
                    .externalRequests(externalRequests)
                    .name("BTN-DOWN")
                    .direction(Direction.DOWN)
                    .build());
        }
    }

    public void startElevators() {

        for (int i = 1; i <= numberOfElevators; i++) {
            Elevator elevator = new Elevator(
                    "Elevator - " + i, 10, Direction.HOLD, State.STOPPED, 0);
            ElevatorRunner elevatorRunner = new ElevatorRunner(elevator, elevatorRequestService, externalRequests, 0, numberOfFloors);
            internalButtonsMap.put(elevator.getName(), initInternalButtons(elevatorRunner));
            elevators.add(elevatorRunner);
        }

        for (ElevatorRunner elevator : elevators) {
            executorService.execute(elevator);
        }
    }

    private List<InternalButton> initInternalButtons(ElevatorRunner elevator) {
        List<InternalButton> buttons = new ArrayList<>();
        for (int i = 1; i <= numberOfFloors; i++) {
            buttons.add(InternalButton.builder().floorNumber(i)
                    .name("BTN-" + i)
                    .queue(elevator.getQueue())
                    .build());
        }
        return buttons;
    }

    public boolean callElevator(int floorNumber, Direction direction) {
        Optional<ExternalButton> first = externalButtons.stream().filter(eb -> eb.getDirection().equals(direction)
                && eb.getSourceFloor() == floorNumber).findFirst();
        if (first.isPresent()) {
            ExternalButton button = first.get();
            System.out.println("Calling elevator at Floor "+floorNumber+" and direction "+direction);
            button.makeRequest();
            return true;
        }
        return false;
    }

    public boolean moveToFloor(String elevatorName, int floorNumber) {
        List<InternalButton> buttons = internalButtonsMap.get(elevatorName);
        if (buttons != null) {
            Optional<InternalButton> button = buttons.stream().filter(b -> b.getFloorNumber().equals(floorNumber)).findFirst();
            if (button.isPresent()) {
                InternalButton btn = button.get();
                System.out.println("Moving elevator "+elevatorName+" to Floor "+floorNumber);
                btn.makeRequest();
                return true;
            }
        }
        return false;
    }

    public void stopElevators() throws InterruptedException {
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
            executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }
    }


}

//list of Elevators
// elevator -> List<buttons>, capacity, name, direction, state
// button -> name, (direction OR floorNumber)

// sendRequest(Request)
// request -> button
