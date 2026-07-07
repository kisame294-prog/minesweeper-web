document.addEventListener("DOMContentLoaded", () => {

    setupFlagEvents();
    // open / flag フォームを全部AJAX化
    document.querySelectorAll("form").forEach(form => {

        form.addEventListener("submit", async (e) => {
            e.preventDefault();

            const formData = new FormData(form);

            const response = await fetch(form.action, {
                method: form.method,
                body: formData
            });

            // 返ってきたHTMLでページ差し替え
            const html = await response.text();

            const parser = new DOMParser();
            const doc = parser.parseFromString(html, "text/html");

            document.body.innerHTML = doc.body.innerHTML;

            setupFlagEvents()

        });
    });
});

function setupFlagEvents() {

    document.querySelectorAll(".closed, .flag").forEach(cell => {

        cell.addEventListener("contextmenu", event => {

            event.preventDefault();

            const td = cell.closest("td");

            const flagForm = td.querySelector(".hidden-flag-form");

            if (flagForm) {
                flagForm.submit();
            }
        });
    });
}


