function shareEagWithWikimediaOpenDialog(){
    var message = "test";
    if($(".ui-dialog").length>0){
        $(".ui-dialog").remove();
    }
    $("body").unbind("click");
    var dialog = $("#shareEagWithWikimediaDialog").dialog({
        closeOnEscape : true,
        width: 700,
        resizable: false,
        buttons: {
            "Save" : function() {
                $( "#uploadFormWithWikimedia" ).prepend('<input type="hidden" name="shareEagWithWikimediaLicence" value="'+$("#shareEagWithWikimediaLicence").val()+'"/>')
                $( "#uploadFormWithWikimedia" ).submit();
                $(this).dialog("close");
            },
            "Cancel" : function() {
                $("body").unbind("click");
                $(this).dialog("close");
            }
        }
    });
    setTimeout("putClickOutsideCloseDialog();","100");
    return false;
}

function shareEagWithWikimediaOpenDialog2(){
    var message = "test";
    if($(".ui-dialog").length>0){
        $(".ui-dialog").remove();
    }
    $("body").unbind("click");
    var dialog = $("#shareEagWithWikimediaDialog2").dialog({
        closeOnEscape : true,
        width: 700,
        resizable: false,
        buttons: {
            "Save" : function() {
                if ($('#shouldShareEagWithCC0').is(':checked')) {
                    $("#uploadFormWithWikimedia").prepend('<input type="hidden" name="shouldShareEagWithCC0" value="true"/>')
                }
                if ($('#doNotShowPopupAgain').is(':checked')) {
                    $("#uploadFormWithWikimedia").prepend('<input type="hidden" name="doNotShowPopupAgain" value="true"/>')
                }
                $( "#uploadFormWithWikimedia" ).submit();
                $(this).dialog("close");
            },
            "Cancel" : function() {
                $("body").unbind("click");
                $(this).dialog("close");
            }
        },
        open: function() {
            $('.ui-dialog-buttonpane').append("<div style='height: 44px;float: left;line-height: 50px;margin-left: 15px;'><input type=\"checkbox\" id=\"doNotShowPopupAgain\" name=\"doNotShowPopupAgain\" value=\"doNotShowPopupAgain\">\n" +
                "    <label for=\"doNotShowPopupAgain\">Do not show this message again</label></div>");
        }
    });
    setTimeout("putClickOutsideCloseDialog();","100");
    return false;
}

function shareEagWithWikimediaCheckedClick(){
    $("#shareEagWithWikimediaLicence option[value=2]").attr('disabled','disabled');

    var shareEagWithWikimediaChecked = false;
    $("#shareEagWithWikimediaDialog input[type='radio']").each(function (index, element) {
        if ($(element).is(':checked')) {
            $("#shareEagWithWikimediaLicenceTable").show();

            if (index==0){
                $("#shareEagWithWikimediaLicence").val("2");
                $("#shareEagWithWikimediaLicence").attr('disabled','disabled');
            }
            else {
                var currentVal = $("#shareEagWithWikimediaLicence").val();
                if (currentVal == "2") {
                    $("#shareEagWithWikimediaLicence").val("4");
                }
                $("#shareEagWithWikimediaLicence").removeAttr('disabled');
            }
            // $("#licenceRightsForEagRightsStmt").html($( "#shareEagWithWikimediaLicence option:selected" ).text());
            // $("#shareEagWithWikimediaError_required").hide();
        }
    });
}

$( document ).ready(function() {
    $("#uploadBtn").click(function(e){
         e.preventDefault();

        // if ($('#httpFile').get(0).files.length === 0) {
        //     $("#shareEagWithWikimediaError_required").show();
        // }
        // else {
        //     shareEagWithWikimediaOpenDialog();
        // }
        shareEagWithWikimediaOpenDialog();
    });

    $("#uploadBtn2").click(function(e){
        if ($("#uploadBtn2").attr("data-popup") == "true") {
            e.preventDefault();
            shareEagWithWikimediaOpenDialog2();
        }
        else {
            //do nothing
        }
    });

    // $("#httpFile").change(function(e){
    //     if ($('#httpFile').get(0).files.length === 0) {
    //         $("#shareEagWithWikimediaError_required").show();
    //     }
    //     else {
    //         $("#shareEagWithWikimediaError_required").hide();
    //     }
    // });

    $("#shareEagWithWikimediaLicence").val("2");
    // $('#shareEagWithWikimediaLicence option[value="2"]').attr("disabled", true);
});
