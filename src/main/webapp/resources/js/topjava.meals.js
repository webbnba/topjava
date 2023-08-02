const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "ordarable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

$(document).ready(function() {
    $("#filterButton").on("click", function() {
        let formData = $("#filterForm").serialize();

        $.ajax({
            type: "GET",
            url: ctx.ajaxUrl + "filter",
            data: formData,
            dataType: "json",
            success: function(response) {
                let tableBody = $("#dataTable").find("tbody");
                tableBody.empty();

                for (let i = 0; i < response.length; i++) {
                    let meal = response[i];
                    let row = $("<tr>");
                    row.append("<td>" + moment(meal.dateTime).format("YYYY-MM-DD HH:mm") + "</td>");
                    row.append("<td>" + meal.description + "</td>");
                    row.append("<td>" + meal.calories + "</td>");
                    row.append("<td><a><span class='fa fa-pencil'></span></a></td>");
                    row.append("<td><a class='delete'><span class='fa fa-remove'></span></a></td>");
                    tableBody.append(row);
                }
            }
        });
    });
});

$(document).ready(function () {
    $("#resetFilterButton").on("click", function () {
        updateTable();
    })
})