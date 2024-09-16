document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchQuery');
    const searchResults = document.getElementById('searchResults');

    searchInput.addEventListener('keyup', function() {
        const query = searchInput.value.trim();
        if (query.length > 1) {
            fetch(`/products/search?query=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(data => {
                    searchResults.innerHTML = '';
                    searchResults.style.display = 'block';


                    if (data.length > 0) {
                        data.forEach(product => {
                            const resultItem = document.createElement('a');
                            resultItem.className = 'list-group-item list-group-item-action';
                            resultItem.href = `/products/details/${product.id}`;
                            resultItem.textContent = product.name;
                            searchResults.appendChild(resultItem);
                        });
                    } else {
                        const noResult = document.createElement('div');
                        noResult.className = 'list-group-item';
                        noResult.textContent = 'No products found';
                        searchResults.appendChild(noResult);
                    }
                })
                .catch(error => {
                    console.error('Error fetching search results:', error);
                });
        } else {
            searchResults.style.display = 'none';
        }
    });
});