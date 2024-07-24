function showOrHideReviews() {
    const showBtn = document.getElementById('showBtn');
    const hideBtn = document.getElementById('hideBtn');
    const reviewList = document.getElementById('reviewList');
    const reviewSection = document.getElementById('reviews-section');

    if (showBtn.classList.contains('hidden')) {
        showBtn.classList.remove('hidden');
        hideBtn.classList.add('hidden');
        reviewList.classList.add('hidden');
    } else {
        showBtn.classList.add('hidden');
        hideBtn.classList.remove('hidden');
        reviewList.classList.remove('hidden');
        fetchReviews(reviewSection);
    }
}

function fetchReviews(reviewSection) {
    const productId = document.getElementById('idContainer').value;
    fetch(`/products/details/${productId}/reviews`)
        .then(response => response.json())
        .then(reviews => {
            reviewSection.innerHTML = '';
            if (reviews.length === 0) {
                const noReviews = document.createElement('p');
                noReviews.classList.add('font-italic', 'font-weight-bold')
                noReviews.innerText = 'This product has no reviews!!';
                reviewSection.appendChild(noReviews);
            } else {

                reviews.forEach(review => {
                    const reviewDiv = document.createElement('div');
                    reviewDiv.classList.add('review', 'border', 'p-2', 'mb-3');
                    reviewDiv.innerHTML = `
                        <div class="review-author text-success mt-2">
                            <strong>Author: </strong><span>${review.author}</span>
                        </div>
                        <div class="review-created text-muted">
                            <strong>Created on: </strong><span>${review.createdOn}</span>
                        </div>
                        <div class="review-content bg-light p-2 border border-dark rounded">
                            <p>${review.content}</p>
                        </div>
                    `;
                    reviewSection.appendChild(reviewDiv);
                });
            }

        })
        .catch(error => console.error('Error fetching reviews:', error));
}

