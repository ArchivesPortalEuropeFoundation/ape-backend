function updateRightsText() {
    var rightsId = $('#rights').val();
    $.ajax({
        url: "${pageContext.request.contextPath}/fetchRightsDescriptionFromDb.action",
        type: "GET",
        contentType: "text/plain;charset=UTF-8",
        data: {
            'rightsId': rightsId

        },
        success: function (data) {
            $('#description').html(data);
        },
        error: function () {
            alert("AJAX call not successful");
        }
    });
}