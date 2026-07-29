package EnterpriseProcurementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.ApprovalHierarchy;
import EnterpriseProcurementSystem.repository.ApprovalHierarchyRepository;

@Service
public class ApprovalHierarchyService {

    @Autowired
    private ApprovalHierarchyRepository approvalHierarchyRepository;

    public ApprovalHierarchy saveApprovalHierarchy(ApprovalHierarchy approvalHierarchy) {
        return approvalHierarchyRepository.save(approvalHierarchy);
    }

    public List<ApprovalHierarchy> getAllApprovalHierarchies() {
        return approvalHierarchyRepository.findAll();
    }
}