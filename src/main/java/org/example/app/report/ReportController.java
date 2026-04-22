package org.example.app.report;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReportRepository reportRepository;

    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public String reportList() {
        return "report/list";
    }

    @GetMapping("/request-status")
    public String requestStatusSummary(Model model) {
        model.addAttribute("rows", reportRepository.requestStatusSummary());
        return "report/request-status";
    }

    @GetMapping("/overdue-tasks")
    public String overdueTasksReport(Model model) {
        model.addAttribute("rows", reportRepository.overdueTasksReport());
        return "report/overdue-tasks";
    }

    @GetMapping("/department-workload")
    public String departmentWorkloadReport(Model model) {
        model.addAttribute("rows", reportRepository.departmentWorkloadReport());
        return "report/department-workload";
    }

    @GetMapping("/document-processing")
    public String documentProcessingReport(Model model) {
        model.addAttribute("rows", reportRepository.documentProcessingReport());
        return "report/document-processing";
    }
}
