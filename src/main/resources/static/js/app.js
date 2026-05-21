(function () {
    function ready(callback) {
        if (window.jQuery) {
            window.jQuery(callback);
            return;
        }
        if (document.readyState === "loading") {
            document.addEventListener("DOMContentLoaded", callback);
            return;
        }
        callback();
    }

    ready(function () {
        const clientSearch = document.getElementById("client-search");
        const clientTable = document.getElementById("client-table");

        if (clientSearch && clientTable) {
            clientSearch.addEventListener("input", function () {
                const query = clientSearch.value.toLowerCase();
                clientTable.querySelectorAll("tr").forEach(function (row) {
                    row.hidden = !row.textContent.toLowerCase().includes(query);
                });
            });
        }

        const runAnalysis = document.getElementById("run-analysis");
        const analysisResult = document.getElementById("analysis-result");

        if (runAnalysis && analysisResult) {
            runAnalysis.addEventListener("click", function () {
                runAnalysis.disabled = true;
                fetch("/analytics/sample")
                    .then(function (response) {
                        if (!response.ok) {
                            throw new Error("Analyzer request failed");
                        }
                        return response.json();
                    })
                    .then(function (response) {
                        const items = response.recommendations.map(function (item) {
                            const li = document.createElement("li");
                            li.textContent = item;
                            return li;
                        });

                        analysisResult.replaceChildren();
                        const client = document.createElement("small");
                        client.textContent = response.clientName;
                        const label = document.createElement("span");
                        label.textContent = response.riskLevel + " risk | " + response.migrationPath;
                        const summary = document.createElement("strong");
                        summary.textContent = response.readinessScore + "% readiness, " + response.estimatedSprintCount + " sprint plan";
                        const list = document.createElement("ul");
                        items.forEach(function (item) {
                            list.appendChild(item);
                        });
                        analysisResult.append(client, label, summary, list);
                    })
                    .catch(function () {
                        analysisResult.innerHTML = "<span>Error</span><strong>Unable to run analysis.</strong>";
                    })
                    .finally(function () {
                        runAnalysis.disabled = false;
                    });
            });
        }
    });
})();
