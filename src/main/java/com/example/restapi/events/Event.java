package com.example.restapi.events;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

// 모임이벤트를 의미
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Event {
    @Id @GeneratedValue
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
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        // Update Free
        if (basePrice == 0 && maxPrice ==0) {
            free = true;
        } else {
            free = false;
        }

        // Update Offline
        if (location == null || location.isBlank()) {
            offline = false;
        } else {
            offline = true;
        }
    }
}
