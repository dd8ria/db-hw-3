package org.example.app.incomingRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.app.office.OfficeRepository;
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
@RequestMapping("/incoming-request")
public class IncomingRequestController {

    private final IncomingRequestRepository requestRepository;
    private final OfficeRepository officeRepository;

    public IncomingRequestController(IncomingRequestRepository requestRepository,
                                     OfficeRepository officeRepository) {
        this.requestRepository = requestRepository;
        this.officeRepository = officeRepository;
    }

    @GetMapping
    public String list(Model model) {
        var requests = new ArrayList<IncomingRequest>();
        requestRepository.findAll().forEach(requests::add);
        requests.sort(Comparator.comparing(IncomingRequest::requestId));
        model.addAttribute("requests", requests);
        return "incoming-request/list";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", RequestForm.empty());
        }
        populateFormModel(model, "Add Incoming Request", "/incoming-request/create", "Register Request");
        return "incoming-request/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") RequestForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateFormModel(model, "Add Incoming Request", "/incoming-request/create", "Register Request");
            return "incoming-request/form";
        }
        requestRepository.save(IncomingRequest.of(
                form.submissionDate(),
                form.senderName().trim(),
                form.senderType(),
                form.contactInformation() == null ? "" : form.contactInformation().trim(),
                form.submissionChannel(),
                form.subject().trim(),
                form.requestSummary() == null ? "" : form.requestSummary().trim(),
                form.priorityLevel(),
                form.officeId()
        ));
        redirectAttributes.addFlashAttribute("successMessage", "Request registered successfully.");
        return "redirect:/incoming-request";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!requestRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Request not found.");
            return "redirect:/incoming-request";
        }
        requestRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Request #" + id + " deleted. Related routing, assignment, and task records were removed automatically.");
        return "redirect:/incoming-request";
    }

    private void populateFormModel(Model model, String pageTitle, String formAction, String submitLabel) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("formAction", formAction);
        model.addAttribute("submitLabel", submitLabel);
        model.addAttribute("offices", officeRepository.findAll());
    }

    public record RequestForm(
            @NotNull(message = "Submission date is required.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate submissionDate,

            @NotBlank(message = "Sender name is required.")
            @Size(max = 255, message = "Sender name must be at most 255 characters.")
            String senderName,

            @NotBlank(message = "Sender type is required.")
            String senderType,

            String contactInformation,

            @NotBlank(message = "Submission channel is required.")
            String submissionChannel,

            @NotBlank(message = "Subject is required.")
            @Size(max = 255, message = "Subject must be at most 255 characters.")
            String subject,

            @Size(max = 2000, message = "Request summary must be at most 2000 characters.")
            String requestSummary,

            @NotBlank(message = "Priority level is required.")
            String priorityLevel,

            @NotNull(message = "Office is required.")
            Long officeId
    ) {
        public static RequestForm empty() {
            return new RequestForm(LocalDate.now(), "", "Citizen", "", "Email", "", "", "Medium", null);
        }
    }
}
