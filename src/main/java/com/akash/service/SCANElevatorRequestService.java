package com.akash.service;

import com.akash.model.Direction;
import com.akash.model.Elevator;
import com.akash.model.Request;

import java.util.TreeSet;

public class SCANElevatorRequestService  implements ElevatorRequestService{

    public Request getNextRequest(TreeSet<Request> internalQueue, Elevator elevator) {

        if(elevator.getCurrentDirection().equals(Direction.UP) && !internalQueue.isEmpty()){
            return internalQueue.higher(Request.builder().floorToStop(elevator.getCurrentFloor()).build());
        } else if(elevator.getCurrentDirection().equals(Direction.DOWN) && !internalQueue.isEmpty()){
            return internalQueue.lower(Request.builder().floorToStop(elevator.getCurrentFloor()).build());
        } else if(elevator.getCurrentDirection().equals(Direction.HOLD) && !internalQueue.isEmpty()){
            return internalQueue.first();
        }
        return  null;
    }

    public Request getExternalRequest(TreeSet<Request> externalRequests) {
        if(!externalRequests.isEmpty()){
            return externalRequests.first();
        }
        return null;
    }
}
