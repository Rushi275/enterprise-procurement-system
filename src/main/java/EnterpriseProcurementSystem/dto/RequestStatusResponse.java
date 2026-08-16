package EnterpriseProcurementSystem.dto;

public class RequestStatusResponse {

    private Long productId;
    private Long requestId;
    private String status;

    public RequestStatusResponse() {
    }

    public RequestStatusResponse(
            Long productId,
            Long requestId,
            String status) {

        this.productId = productId;
        this.requestId = requestId;
        this.status = status;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}