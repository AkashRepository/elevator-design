package com.akash.runner;

import com.akash.model.Direction;
import com.akash.model.Elevator;
import com.akash.model.State;
import com.akash.model.Request;
import com.akash.service.ElevatorRequestService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElevatorRunner implements Runnable {


    Elevator elevator;
    ElevatorRequestService elevatorRequestService;

    TreeSet<Request> queue;
    TreeSet<Request> externalRequests;
    int bottomFloor;
    int topFloor;

    public ElevatorRunner(Elevator elevator, ElevatorRequestService elevatorRequestService, TreeSet<Request> externalRequests,
                          int bottomFloor, int topFloor) {
        this.elevator = elevator;
        this.elevatorRequestService = elevatorRequestService;
        this.externalRequests = externalRequests;
        this.bottomFloor = bottomFloor;
        this.topFloor = topFloor;
        this.queue = new TreeSet<Request>((a, b) -> {
            return Integer.compare(a.getFloorToStop(), b.getFloorToStop());
        });
    }

    @SneakyThrows
    @Override
    public void run() {
        Request request = null;
        while (true) {
            if (elevator.getCurrentDirection().equals(Direction.HOLD)) {
                while (request == null) {
                    System.out.println("Waiting for requests " + elevator.getName() + " at Floor "+elevator.getCurrentFloor());
                    request = elevatorRequestService.getNextRequest(queue, elevator);
                    Request externalRequest = elevatorRequestService.getExternalRequest(externalRequests);
                    if (request != null && externalRequest != null) {
                        int earliest = request.getTime().compareTo(externalRequest.getTime());
                        if(earliest>=0){
                            request = externalRequest;
                        }
                    } else if(request==null && externalRequest!=null){
                        request = externalRequest;
                    }
                    Thread.sleep(500);
                }
                this.elevator.setCurrentDirection(request.getDirection());
            } else if (elevator.getCurrentDirection().equals(Direction.UP)) {
                this.elevator.setState(State.RUNNING);
                while (elevator.getCurrentFloor() != topFloor && request != null) {
                    elevator.moveUp();
                    Optional<Request> external = externalRequests.stream().filter(er -> er.getFloorToStop() == elevator.getCurrentFloor()).findAny();
                    if (request.getFloorToStop() == elevator.getCurrentFloor()
                            || external.isPresent()) {
                        elevator.openDoor();
                        //wait
                        elevator.closeDoor();
                        if (request.getFloorToStop() == elevator.getCurrentFloor())
                            queue.remove(request);
                        external.ifPresent(value -> externalRequests.remove(value));
                    }
                    request = elevatorRequestService.getNextRequest(queue, elevator);
                }
                this.elevator.setState(State.STOPPED);
                elevator.setCurrentDirection(Direction.HOLD);
                request = null;
            } else {
                this.elevator.setState(State.RUNNING);
                while (elevator.getCurrentFloor() != bottomFloor && request != null) {
                    elevator.moveDown();
                    Optional<Request> external = externalRequests.stream().filter(er -> er.getFloorToStop() == elevator.getCurrentFloor()).findAny();
                    if (request.getFloorToStop() == elevator.getCurrentFloor()
                            || external.isPresent()) {
                        elevator.openDoor();
                        //wait
                        elevator.closeDoor();
                        if (request.getFloorToStop() == elevator.getCurrentFloor())
                            queue.remove(request);
                        external.ifPresent(value -> externalRequests.remove(value));
                    }
                    request = elevatorRequestService.getNextRequest(queue, elevator);
                }
                this.elevator.setState(State.STOPPED);
                elevator.setCurrentDirection(Direction.HOLD);
                request = null;
            }
        }

    }
}
