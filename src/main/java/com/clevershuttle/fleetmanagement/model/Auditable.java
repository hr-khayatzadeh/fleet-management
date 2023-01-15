package com.clevershuttle.fleetmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @Column(name = "CREATED_AT", updatable = false)
    @Temporal(TIMESTAMP)
    @CreatedDate
    protected Date createdAt;

    @Column(name = "LAST_UPDATED_AT")
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected Date lastUpdatedAt;

    @Version
    protected Integer version;

}
