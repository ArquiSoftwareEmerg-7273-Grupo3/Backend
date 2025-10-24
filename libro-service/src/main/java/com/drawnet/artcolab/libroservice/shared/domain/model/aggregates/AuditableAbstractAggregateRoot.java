package com.drawnet.artcolab.libroservice.shared.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditableAbstractAggregateRoot<T extends AbstractAggregateRoot<T>> extends AbstractAggregateRoot<T> {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   //@Getter
   //@CreatedDate
   //@Column(nullable = false, updatable = false)
   //private Date createdAt;

   //@Getter
   //@LastModifiedDate
   //@Column(nullable = false)
   //private Date updatedAt;


    public Long getId() {
        return id;
    }
}

