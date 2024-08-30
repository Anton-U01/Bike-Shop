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

    let form = document.getElementById('form');
    let formData = new FormData(form);



    let type = formData.get("type");

    if (type === "TIRES") {
        formData.append("dynamicFields[tireSize]", document.getElementById("size").value);
    } else if (type === "CHAIN") {
        formData.append("dynamicFields[chainLinks]", document.getElementById("chainLinks").value);
        formData.append("dynamicFields[speedsCount]", document.getElementById("speedsCount").value);
    } else if (type === "FRAME") {
        formData.append("dynamicFields[weight]", document.getElementById("weight").value);
        formData.append("dynamicFields[material]", document.getElementById("material").value);
    }


    fetch('/products/add-part', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if(!response.ok){

                return response.json().then(errors =>{
                    displayErrors(errors);
                    if(errors.noImageError){
                        const fileErrorElement = document.getElementById('imageError');
                        if (fileErrorElement) {
                            fileErrorElement.textContent = errors['noImageError'];
                            fileErrorElement.classList.remove('hidden');
                        }
                    }
                });
            }
            window.location.href = "/products/product-management";
        })
        .catch(error => {
            console.error('Error during fetch:', error);
        });
}
function displayErrors(errors) {
    clearErrorMessages();

    for (const field in errors) {
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