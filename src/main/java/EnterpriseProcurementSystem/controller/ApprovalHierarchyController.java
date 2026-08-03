package EnterpriseProcurementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/{id}")
    public ApprovalHierarchy updateApprovalHierarchy(@PathVariable Long id,
            @RequestBody ApprovalHierarchy approvalHierarchy) {
        return approvalHierarchyService.updateApprovalHierarchy(id, approvalHierarchy);
    }

    @DeleteMapping("/{id}")
    public void deleteApprovalHierarchy(@PathVariable Long id) {
        approvalHierarchyService.deleteApprovalHierarchy(id);
    }
}