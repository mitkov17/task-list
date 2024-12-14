package com.mitkov.task_list.config

import com.mitkov.task_list.services.UserService
import org.modelmapper.ModelMapper
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper()
    }

    @Bean
    fun initDefaultAdmin(userService: UserService): CommandLineRunner {
        return CommandLineRunner {
            userService.createDefaultAdminIfNotExists()
        }
    }
}
