package softuni.bg.bikeshop.models.orders;

public class OrderWithDeliveryDetailsDto {
    private OrderViewDto order;
    private DeliveryDetailsDto deliverDetails;

    public OrderViewDto getOrder() {
        return order;
    }

    public void setOrder(OrderViewDto order) {
        this.order = order;
    }

    public DeliveryDetailsDto getDeliverDetails() {
        return deliverDetails;
    }

    public void setDeliverDetails(DeliveryDetailsDto deliverDetails) {
        this.deliverDetails = deliverDetails;
    }
}
