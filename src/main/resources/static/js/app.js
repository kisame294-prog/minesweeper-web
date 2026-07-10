document.addEventListener("DOMContentLoaded", () => {
    setupFlagEvents();
    setupAjax();
    setupTimer();
    setupResultEffects()
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
            setupResultEffects();
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
    const running = timerElement.dataset.running  === "true";

    if (running) {
        setInterval(() => {
            elapsedTime++;
            timerElement.textContent =
                String(elapsedTime).padStart(3, "0");
        }, 1000);
    }
}

function setupResultEffects() {
    const overPanel = document.querySelector('.game-over .result-panel');
    const overH1 = document.querySelector('.game-over h1');

    if (overPanel && overH1) {
        runHackerText(overH1, "GAME OVER");
        setInterval(() => {
            overPanel.style.animation = 'none';
            void overPanel.offsetWidth;
            overPanel.style.animation = 'dangerPulse 2s infinite ease-in-out, cyberGlitch .3s ease-out';
        }, Math.random() * 2000 + 1500);
    }

    const clearH1 = document.querySelector('.clear h1');
    if (clearH1) {
        runHackerText(clearH1, "CLEAR");
    }
}

function runHackerText(element, targetText) {
    const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%-=+*";
    let iterations = 0;

    const interval = setInterval(() => {
        element.textContent = targetText
            .split("")
            .map((char, index) => {
                if (char === " ") return " ";
                if (index < iterations) return targetText[index];
                return chars[Math.floor(Math.random() * chars.length)];
            })
            .join("");

        if (iterations >= targetText.length) {
            clearInterval(interval);
        }
        iterations += 1 / 3;
    }, 30);
}