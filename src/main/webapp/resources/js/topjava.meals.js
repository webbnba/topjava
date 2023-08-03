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

$(document).ready(function () {
    $("#filterButton").on("click", function () {
        let formData = $("#filterForm").serialize();

        $.ajax({
            type: "GET",
            url: ctx.ajaxUrl + "filter",
            data: formData,
            dataType: "json",
            success: function (data) {
                ctx.datatableApi.clear().rows.add(data).draw();
            }
        });
    });
});

$(document).ready(function () {
    $("#resetFilterButton").on("click", function () {
        updateTable();
    })
})