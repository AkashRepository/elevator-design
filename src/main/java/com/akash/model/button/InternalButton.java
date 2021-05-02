package com.akash.model.button;

import com.akash.model.Direction;
import com.akash.model.Request;
import com.akash.service.ElevatorRequestService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import sun.reflect.generics.tree.Tree;

import java.time.Instant;
import java.util.Queue;
import java.util.TreeSet;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Setter
public class InternalButton extends Button{
    Integer floorNumber;
    TreeSet<Request> queue;
    @Builder
    public InternalButton(int floorNumber, String name,
                          Direction direction,
                          TreeSet<Request> queue){
        super(name, direction);
        this.floorNumber = floorNumber;
        this.queue = queue;
    }

    @Override
    public void makeRequest() {
        Request request = Request.builder().floorToStop(this.floorNumber)
                .direction(this.direction).time(Instant.now()).build();
        queue.add(request);
    }
}
