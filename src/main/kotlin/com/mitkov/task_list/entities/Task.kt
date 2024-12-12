package com.mitkov.task_list.entities

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "task")
data class Task(

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(name = "title")
        var title: String,

        @Column(name = "description")
        var description: String,

        @Column(name = "deadline")
        @Temporal(TemporalType.TIMESTAMP)
        var deadline: Date,

        @Column(name = "priority")
        @Enumerated(EnumType.STRING)
        var priority: Priority,

        @Column(name = "status")
        @Enumerated(EnumType.STRING)
        var status: Status,

        @ManyToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        var user: User
)
