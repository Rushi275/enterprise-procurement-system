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

    public ApprovalHierarchy updateApprovalHierarchy(Long id, ApprovalHierarchy approvalHierarchy) {
        ApprovalHierarchy existingApprovalHierarchy = approvalHierarchyRepository.findById(id).orElse(null);

        if (existingApprovalHierarchy != null) {
            existingApprovalHierarchy.setDepartment(approvalHierarchy.getDepartment());
            existingApprovalHierarchy.setLevel(approvalHierarchy.getLevel());

            return approvalHierarchyRepository.save(existingApprovalHierarchy);
        }

        return null;
    }

    public void deleteApprovalHierarchy(Long id) {
        approvalHierarchyRepository.deleteById(id);
    }
}