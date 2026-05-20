$(function () {
    $("#client-search").on("input", function () {
        const query = $(this).val().toLowerCase();
        $("#client-table tr").each(function () {
            const rowText = $(this).text().toLowerCase();
            $(this).toggle(rowText.includes(query));
        });
    });

    $("#run-analysis").on("click", function () {
        const request = {
            clientName: "Horizon Financial",
            workloads: ["payments", "fraud analytics", "customer portal"],
            legacySystemCount: 5,
            complianceRequired: true,
            currentCloudUsagePercent: 35,
            automationMaturity: 4,
            integrationComplexity: 6,
            dataSensitivity: "HIGH"
        };

        $.ajax({
            url: "/api/analytics/recommendations",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(request)
        }).done(function (response) {
            const items = response.recommendations.map(function (item) {
                return "<li>" + item + "</li>";
            }).join("");
            $("#analysis-result").html(
                "<span>" + response.riskLevel + " risk | " + response.migrationPath + "</span>" +
                "<strong>" + response.readinessScore + "% readiness, " + response.estimatedSprintCount + " sprint plan</strong>" +
                "<ul>" + items + "</ul>"
            );
        }).fail(function () {
            $("#analysis-result").html("<span>Error</span><strong>Unable to run analysis.</strong>");
        });
    });
});
