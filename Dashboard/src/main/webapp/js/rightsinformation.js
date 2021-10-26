$( document ).ready(function() {
    updateRightsText();
});

function updateRightsText() {
    var rightsId = $('#rights').val();
    if (rightsId == -2){
        //Show the second dropdown menu
        $('#entitlementRights').show();

        $('#changeRightsInformationCheckPlaceholder').html($('#entitlementRights :selected').text());
    }
    else {
        //Hide the second dropdown menu
        $('#entitlementRights').hide();

        $('#changeRightsInformationCheckPlaceholder').html($('#rights :selected').text());

        //Fetch licence description, not used for the time being
        // $.ajax({
        //     url: "${pageContext.request.contextPath}/fetchRightsDescriptionFromDb.action",
        //     type: "GET",
        //     contentType: "text/plain;charset=UTF-8",
        //     data: {
        //         'rightsId': rightsId
        //
        //     },
        //     success: function (data) {
        //         $('#description').html(data);
        //     },
        //     error: function () {
        //         alert("AJAX call not successful");
        //     }
        // });
    }
}

function updateEntitlementRightsText() {
    $('#changeRightsInformationCheckPlaceholder').html($('#entitlementRights :selected').text());
}
