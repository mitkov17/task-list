package com.mitkov.task_list.entities

import jakarta.persistence.*
import org.hibernate.annotations.Cascade

@Entity
@Table(name = "users")
data class User(

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(name = "username")
        var username: String,

        @Column(name = "password")
        var password: String,

        @Column(name = "role")
        @Enumerated(EnumType.STRING)
        var role: Role,

        @OneToMany(mappedBy = "user")
        @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
        val tasks: MutableList<Task> = mutableListOf()
)
