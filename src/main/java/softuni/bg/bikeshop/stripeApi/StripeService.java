package softuni.bg.bikeshop.stripeApi;

import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.checkout.Session;

import java.util.Map;

public interface StripeService {
    Session createCheckoutSession(double totalAmount) throws StripeException;
}
