package com.example.restapi.events;


import lombok.*;

import java.time.LocalDateTime;

// 모임이벤트를 의미
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Event {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollDateTime;
    private LocalDateTime closeEnrollDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // 없으면 온라인 모임
    private int basePrice;
    private int maxPrice;
    private int LimitOfEnrollment;
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus;

}
