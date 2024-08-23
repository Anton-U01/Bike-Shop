package softuni.bg.bikeshop.stripeApi;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;




@Service
public class StripeServiceImpl implements StripeService{
    @Value("${stripe.api.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public Session createCheckoutSession(double totalAmount) throws StripeException {
        Stripe.apiKey = secretKey;

        long amountInCents = (long) (totalAmount * 100);
        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Total Order Payment")
                                .build())
                .setUnitAmount(amountInCents)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(priceData)
                .setQuantity(1L)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/result?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080/result?error=Payment+cancelled")
                .addLineItem(lineItem)
                .build();
        return Session.create(params);

    }
}
