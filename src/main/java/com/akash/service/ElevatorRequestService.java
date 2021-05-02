package com.akash.service;

import com.akash.model.Direction;
import com.akash.model.Elevator;
import com.akash.model.Request;

import java.util.Set;
import java.util.TreeSet;

public interface ElevatorRequestService {

    public Request getNextRequest(TreeSet<Request> internalQueue, Elevator elevator);

    public Request getExternalRequest(TreeSet<Request> externalRequests);

}
