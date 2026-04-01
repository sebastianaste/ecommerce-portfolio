document.querySelectorAll('.sub').forEach(btn => {
    btn.addEventListener('click', () => {
        const qtty = btn.nextElementSibling;
        if (parseInt(qtty.textContent) > 0) qtty.textContent = parseInt(qtty.textContent) - 1;
    });
});

document.querySelectorAll('.add').forEach(btn => {
    btn.addEventListener('click', () => {
        const qtty = btn.previousElementSibling;
        qtty.textContent = parseInt(qtty.textContent) + 1;
    });
});

document.querySelectorAll('.add-to-cart').forEach(btn => {
    btn.addEventListener('click', () => {
        const qtty = btn.parentElement.querySelector('.qtty');
        const quantity = parseInt(qtty.textContent);
        if (quantity <= 0) return;

        const productId = btn.getAttribute('data-product-id');

        fetch('/cart/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `productId=${productId}&quantity=${quantity}`
        }).then(res => {
            if (res.ok) {
                qtty.textContent = '0';
                showToast('Added to cart!');
                updateCartCount();
            } else {
                res.text().then(msg => showToast(msg, true));
            }
        });
    });
});

function showToast(msg, isError = false) {
    const toast = document.getElementById('cartToast');
    toast.textContent = msg;
    toast.style.background = isError ? '#dc3545' : '#198754';
    toast.style.display = 'block';
    setTimeout(() => toast.style.display = 'none', 2000);
}

function updateCartCount() {
    fetch('/cart/count')
        .then(r => r.text())
        .then(count => {
            const el = document.getElementById('cartIcon');
            if (el) el.textContent = count;
        });
}
