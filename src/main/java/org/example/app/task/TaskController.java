package org.example.app.task;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.app.staff.StaffMemberRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskRepository taskRepository;
    private final StaffMemberRepository staffMemberRepository;

    public TaskController(TaskRepository taskRepository,
                          StaffMemberRepository staffMemberRepository) {
        this.taskRepository = taskRepository;
        this.staffMemberRepository = staffMemberRepository;
    }

    @GetMapping
    public String list(Model model) {
        var tasks = new ArrayList<Task>();
        taskRepository.findAll().forEach(tasks::add);
        tasks.sort(Comparator.comparing(Task::taskId));
        model.addAttribute("tasks", tasks);
        return "task/list";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Task not found.");
            return "redirect:/task";
        }
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", TaskStatusForm.from(task));
        }
        model.addAttribute("task", task);
        model.addAttribute("staffMembers", staffMemberRepository.findAll());
        return "task/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateStatus(@PathVariable Long id,
                               @Valid @ModelAttribute("form") TaskStatusForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        var task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Task not found.");
            return "redirect:/task";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            model.addAttribute("staffMembers", staffMemberRepository.findAll());
            return "task/edit";
        }
        taskRepository.save(task.withStatus(
                form.taskStatus(),
                form.completionDate(),
                form.staffId()
        ));
        redirectAttributes.addFlashAttribute("successMessage",
                "Task #" + id + " status updated to \"" + form.taskStatus() + "\".");
        return "redirect:/task";
    }

    public record TaskStatusForm(
            @NotBlank(message = "Task status is required.")
            String taskStatus,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate completionDate,

            Long staffId
    ) {
        public static TaskStatusForm from(Task task) {
            return new TaskStatusForm(
                    task.taskStatus() != null ? task.taskStatus() : "Open",
                    task.completionDate(),
                    task.staffId()
            );
        }
    }
}
