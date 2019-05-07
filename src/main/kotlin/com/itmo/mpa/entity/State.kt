package com.itmo.mpa.entity

import javax.persistence.*

@Entity
@Table(name = "state")
class State : LongIdEntity() {

    @Column(name = "name", nullable = false)
    lateinit var name: String

    @Column(name = "description", nullable = false)
    lateinit var description: String

    @ManyToOne
    @JoinColumn(name = "disease_id", nullable = false)
    lateinit var disease: Disease
}
