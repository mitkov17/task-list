package com.mitkov.task_list.controllers

import com.mitkov.task_list.services.StatisticsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @Operation(
        summary = "Get task statistics",
        description = "Returns statistics about tasks, including total, completed, and unfinished tasks, as well as user-specific statistics.",
        tags = ["Statistics"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Statistics retrieved successfully",
                content = [Content(schema = Schema(implementation = Map::class))]
            ),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getTaskStatistics(): ResponseEntity<Map<String, Any>> {
        val stats = statisticsService.getTaskStatistics()
        return ResponseEntity.ok(stats)
    }

    @Operation(
        summary = "Export statistics to CSV",
        description = "Exports task statistics to a CSV file.",
        tags = ["Statistics"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "CSV file generated successfully",
                content = [Content(mediaType = "text/csv")]
            ),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    fun exportStatisticsToCSV(response: HttpServletResponse) {
        statisticsService.exportStatisticsToCSV(response)
    }

    @Operation(
        summary = "Export all tasks sorted by deadline to CSV",
        description = "Exports all tasks sorted by their deadlines in descending order to a CSV file.",
        tags = ["Statistics"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "CSV file generated successfully",
                content = [Content(mediaType = "text/csv")]
            ),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @GetMapping("/export-sorted")
    @PreAuthorize("hasRole('ADMIN')")
    fun exportSortedTasks(response: HttpServletResponse) {
        statisticsService.exportAllTasksSortedByDeadlineToCSV(response)
    }
}
