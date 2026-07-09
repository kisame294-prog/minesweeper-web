document.addEventListener("DOMContentLoaded", () => {

    setupFlagEvents();

    setupAjax();

    setupTimer();

});


// Ajax化
function setupAjax() {

    document.querySelectorAll("form").forEach(form => {

        form.addEventListener("submit", async (e) => {

            e.preventDefault();

            const formData = new FormData(form);

            const response = await fetch(form.action, {
                method: form.method,
                body: formData
            });

            const html = await response.text();

            const parser = new DOMParser();
            const doc = parser.parseFromString(html, "text/html");

            document.body.innerHTML = doc.body.innerHTML;

            // 差し替え後に再設定
            setupFlagEvents();
            setupTimer();

        });
    });
}


// 右クリック旗
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


// タイマー
function setupTimer() {

    const timerElement = document.getElementById("timer");

    if (!timerElement) {
        return;
    }

    let elapsedTime = Number(timerElement.dataset.time);

    setInterval(() => {

        elapsedTime++;

        timerElement.textContent =
            String(elapsedTime).padStart(3, "0");

    }, 1000);
}