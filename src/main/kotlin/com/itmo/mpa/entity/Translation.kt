package com.itmo.mpa.entity;

import javax.persistence.Column
import javax.persistence.Entity;
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "translation")
class Translation : LongIdEntity() {
    @Column(name = "key_name", nullable = false)
    lateinit var keyName: String

    @Column(name = "entity_id", nullable = false)
    var entityId: Long = 0

    @Column(name = "language", nullable = false)
    lateinit var language: String

    @Column(name = "value", nullable = false)
    lateinit var value: String
}
