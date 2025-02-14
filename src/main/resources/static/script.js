document.getElementById('imageForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const imageName = document.getElementById('imageName').value;
    const imageElement = document.getElementById('resultImage');

    try {
        const response = await fetch(`/images/${imageName}`);
        if (response.ok) {
            const imageBlob = await response.blob();
            const imageUrl = URL.createObjectURL(imageBlob);
            imageElement.src = imageUrl;
            imageElement.style.display = 'block';
        } else {
            alert('Imagen no encontrada.');
            imageElement.style.display = 'none';
        }
    } catch (error) {
        console.error('Error al obtener la imagen:', error);
    }
});

document.getElementById("getForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const name = document.getElementById("name").value;
    const responseDiv = document.getElementById("response");

    fetch(`/hello?name=${encodeURIComponent(name)}`)
        .then(response => response.text())
        .then(data => {
            responseDiv.innerHTML = data;
        })
        .catch(error => console.error("Error al obtener los datos:", error));
});

document.addEventListener("DOMContentLoaded", function () {
    const randomForm = document.getElementById("randomForm");

    randomForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Evita la recarga de la página

        fetch("/random?min=1&max=100")
            .then(number => number.text())
            .then(data => {
                document.getElementById("number").textContent = data;
            })
            .catch(error => console.error("Error al obtener el número aleatorio:", error));
    });
});