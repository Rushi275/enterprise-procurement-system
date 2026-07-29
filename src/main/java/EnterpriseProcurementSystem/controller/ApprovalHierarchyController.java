package EnterpriseProcurementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.entity.ApprovalHierarchy;
import EnterpriseProcurementSystem.service.ApprovalHierarchyService;

@RestController
@RequestMapping("/approval-hierarchies")
public class ApprovalHierarchyController {

    @Autowired
    private ApprovalHierarchyService approvalHierarchyService;

    @PostMapping
    public ApprovalHierarchy createApprovalHierarchy(@RequestBody ApprovalHierarchy approvalHierarchy) {
        return approvalHierarchyService.saveApprovalHierarchy(approvalHierarchy);
    }

    @GetMapping
    public List<ApprovalHierarchy> getAllApprovalHierarchies() {
        return approvalHierarchyService.getAllApprovalHierarchies();
    }
}