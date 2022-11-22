package com.example.restapi.events;


import com.example.restapi.accounts.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

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
