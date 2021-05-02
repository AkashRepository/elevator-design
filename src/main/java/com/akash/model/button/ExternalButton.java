package com.akash.model.button;

import com.akash.model.Direction;
import com.akash.model.Request;
import com.akash.service.ElevatorRequestService;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ExternalButton extends Button {

    int sourceFloor;
    Set<Request> externalRequests;

    @Builder
    public ExternalButton(int sourceFloor, String name,
            Direction direction,
            Set<Request> externalRequests){
        super(name, direction);
        this.sourceFloor = sourceFloor;
        this.externalRequests = externalRequests;
    }
    @Override
    public void makeRequest() {

        Request request = Request.builder().floorToStop(this.sourceFloor)
                .direction(this.direction).time(Instant.now()).build();

        externalRequests.add(request);
    }
}
