package softuni.bg.bikeshop.stripeApi;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class PaymentController {

    private final StripeService stripeService;

    public PaymentController(StripeService paymentsService) {
        this.stripeService = paymentsService;
    }

    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> data) {
        double totalAmount = Double.parseDouble(data.get("totalAmount").toString());

        try {
            Session session = stripeService.createCheckoutSession(totalAmount);
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", session.getId());
            return response;
        } catch (StripeException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stripe error", e);
        }
    }

}
