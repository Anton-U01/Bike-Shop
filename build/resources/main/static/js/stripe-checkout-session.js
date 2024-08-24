document.addEventListener('DOMContentLoaded', function () {
    const stripe = Stripe(document.getElementById("publicKey").value);
    const totalAmount = document.getElementById("totalAmount").innerText.trim();
    document.querySelector("#checkout-button").addEventListener("click", () => {
        fetch('/api/create-checkout-session', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                totalAmount: totalAmount
            }),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then((data) => {
                const sessionId = data.sessionId;
                return stripe.redirectToCheckout({sessionId: sessionId});
            })
            .then((result) => {
                if (result.error) {
                    console.error(result.error.message);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    });
});