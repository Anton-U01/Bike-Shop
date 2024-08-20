document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('input[type="number"]').forEach(function (input) {
        input.addEventListener('change', function () {
            const max = parseInt(input.getAttribute('max'));
            const min = parseInt(input.getAttribute('min'));
            const value = parseInt(input.value);

            if (value < min) {
                alert(`Quantity cannot be less than ${min}`);
                input.value = min;
            } else if (value > max) {
                alert(`Quantity cannot exceed ${max}`);
                input.value = max;
            }
        });
    });
});