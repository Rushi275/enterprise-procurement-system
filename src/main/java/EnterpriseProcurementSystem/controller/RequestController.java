package EnterpriseProcurementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.dto.RequestStatusResponse;
import EnterpriseProcurementSystem.dto.RequestStatusUpdate;
import EnterpriseProcurementSystem.entity.Request;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.repository.UserRepository;
import EnterpriseProcurementSystem.service.RequestService;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Request raiseRequest(@RequestBody Request request) {
        return requestService.raiseRequest(request);
    }

    @GetMapping
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{id}")
    public Request getRequestById(@PathVariable Long id) {
        return requestService.getRequestById(id);
    }

    @GetMapping("/pending")
    public List<Request> getPendingRequests() {
        return requestService.getPendingRequests();
    }

    @PutMapping("/{id}/status")
    public RequestStatusResponse updateRequestStatus(
            @PathVariable Long id,
            @RequestBody RequestStatusUpdate requestStatusUpdate) {

        return requestService.updateRequestStatus(
                id,
                requestStatusUpdate.getStatus()
        );
    }

    @GetMapping("/my/download")
    public ResponseEntity<byte[]> downloadMyRequestsCsv() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] csvBytes =
                requestService.downloadMyRequestsCsv(user.getUserId());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=my-requests.csv"
                )
                .contentType(
                        MediaType.parseMediaType("text/csv")
                )
                .body(csvBytes);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadAllRequestsCsv() {

        byte[] csvBytes =
                requestService.downloadAllRequestsCsv();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=all-requests.csv"
                )
                .contentType(
                        MediaType.parseMediaType("text/csv")
                )
                .body(csvBytes);
    }

    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
    }
}