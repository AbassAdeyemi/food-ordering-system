package com.hayba.order.service.domain;

import com.hayba.order.service.domain.dto.track.TrackOrderQuery;
import com.hayba.order.service.domain.dto.track.TrackOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderTrackCommandHandler {
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {}

}