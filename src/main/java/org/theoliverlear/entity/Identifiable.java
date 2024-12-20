package org.theoliverlear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
    public Identifiable() {
        this.id = null;
    }
    public Identifiable(Long id) {
        this.id = id;
    }
}
