document.addEventListener("DOMContentLoaded", () => {

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
        });

    });

});