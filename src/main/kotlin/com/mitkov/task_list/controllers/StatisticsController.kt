package com.mitkov.task_list.controllers

import com.mitkov.task_list.services.StatisticsService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/statistics")
class StatisticsController(
    private val statisticsService: StatisticsService
) {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getTaskStatistics(): ResponseEntity<Map<String, Any>> {
        val stats = statisticsService.getTaskStatistics()
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    fun exportStatisticsToCSV(response: HttpServletResponse) {
        statisticsService.exportStatisticsToCSV(response)
    }

    @GetMapping("/tasks/export")
    @PreAuthorize("hasRole('ADMIN')")
    fun exportSortedTasks(response: HttpServletResponse) {
        statisticsService.exportAllTasksSortedByDeadlineToCSV(response)
    }
}
