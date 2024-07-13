function showAdditionalFields() {
    var type = document.getElementById("type").value;
    document.getElementById("tiresFields").classList.add("hidden");
    document.getElementById("chainFields").classList.add("hidden");
    document.getElementById("frameFields").classList.add("hidden");

    if (type === "TIRES") {
        document.getElementById("tiresFields").classList.remove("hidden");
    } else if (type === "CHAIN") {
        document.getElementById("chainFields").classList.remove("hidden");
    } else if (type === "FRAME") {
        document.getElementById("frameFields").classList.remove("hidden");
    }
}

function submitForm(event) {
    event.preventDefault();


    var formData = {
        name: document.getElementById("name").value,
        description: document.getElementById("description").value,
        price: document.getElementById("price").value,
        manufacturer: document.getElementById("manufacturer").value,
        type: document.getElementById("type").value,
        dynamicFields: {}
    };

    if (formData.type === "TIRES") {
        formData.dynamicFields.tireSize = document.getElementById("size").value;
    } else if (formData.type === "CHAIN") {
        formData.dynamicFields.chainLinks = document.getElementById("chainLinks").value;
        formData.dynamicFields.speedsCount = document.getElementById("speedsCount").value;
    } else if (formData.type === "FRAME") {
        formData.dynamicFields.weight = document.getElementById("weight").value;
        formData.dynamicFields.material = document.getElementById("material").value;
    }

    fetch('/products/add-part', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if(!response.ok){
                return response.json().then(errors => displayErrors(errors));
            }
            window.location.href = "/products";
        });
}
function displayErrors(errors){
    clearErrorMessages();

    for(const field in errors){
        const errorMessage = errors[field];
        const errorElement = document.querySelector(`#${field} + small`);
        if (errorElement) {
            errorElement.textContent = errorMessage;
            errorElement.classList.remove('hidden');
        }
    }
}
function clearErrorMessages() {
    document.querySelectorAll('small.danger').forEach(element => {
        element.textContent = '';
        element.classList.add('hidden');
    });
}