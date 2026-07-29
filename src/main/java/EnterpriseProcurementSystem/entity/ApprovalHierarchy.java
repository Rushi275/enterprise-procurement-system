package EnterpriseProcurementSystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval_hierarchy")
public class ApprovalHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_hierarchy_id")
    private Long approvalHierarchyId;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "level")
    private Integer level;

    public ApprovalHierarchy() {
    }

    public Long getApprovalHierarchyId() {
        return approvalHierarchyId;
    }

    public void setApprovalHierarchyId(Long approvalHierarchyId) {
        this.approvalHierarchyId = approvalHierarchyId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}