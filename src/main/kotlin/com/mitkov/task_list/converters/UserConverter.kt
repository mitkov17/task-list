package com.mitkov.task_list.converters

import com.mitkov.task_list.dto.RegistrationDTO
import com.mitkov.task_list.dto.UserDTO
import com.mitkov.task_list.entities.Role
import com.mitkov.task_list.entities.User
import org.springframework.stereotype.Component

@Component
class UserConverter {

    fun convertToDTO(user: User): UserDTO {
        return UserDTO(
            id = user.id,
            username = user.username,
            role = user.role,
        )
    }

    fun convertFromDTO(userDTO: UserDTO): User {
        return User(
            id = userDTO.id,
            username = userDTO.username,
            password = "",
            role = userDTO.role,
        )
    }

    fun convertFromRegistrationDTO(registrationDTO: RegistrationDTO): User {
        return User(
            username = registrationDTO.username,
            password = registrationDTO.password,
            role = Role.ROLE_USER
        )
    }
}
