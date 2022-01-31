function initPage() {
    showHideRightsConversion();
    showHideEacExtractionFromEad3();
    checkActionMessages();
    $("#profileCb").off("change").on("change", function () {
        var params = {profilelist: $("#profileCb").val()};
        $.get("ingestionprofiles.action", params, function (data) {
            createColorboxForProcessing();
            $("#principal").replaceWith(data);
            $("body meta").remove();
            $("body title").remove();
        });
    });

}

function checkActionMessages() {
    if ($("#actionMessages span").length == 1) {
        var text = $("#actionMessages span").text();
        if (text.length > 0 && text.substring(0, 1) == '[' && text.substring(text.length - 1) == ']') {
            text = text.substring(1, text.length - 1);
            $("#actionMessages").text(text);
        }
        setTimeout('$("#actionMessages").fadeOut("slow");', "5000");
    }
}

function hideAndShow(idPrefix, shown) {
    $("div[id^='" + idPrefix + "']").each(function () {
        $(this).hide();
    });
    $("ul#ingestionprofileTabsContainer li a[href^='#tab']").each(function () {
        $(this).removeClass("ingestionprofileCurrenttab");
    });
    $("div[id='" + shown + "']").show();
    $("ul#ingestionprofileTabsContainer li a[href='#" + shown + "']").addClass("ingestionprofileCurrenttab");
}

function validateAndSave(profileNameError, dataProviderError, edmDaoError, languageError, europeanaLicenseError, alertRights) {
    avoidCancellations();

    var continueCheck = true;

    var profilename = $("#profilename").attr("value");
    if (profilename == null || profilename == "") {
        alertAndDecode(profileNameError);
        enableButtons();
        continueCheck = false;
    }

    // First of all delete the old checks over the rights information.
    deleteChecks();

    // Second check the filled options if the file type is an EAD.
    var assocType = $("#associatedFiletypeCb").val();
    if (assocType != 2) {
        continueCheck = checkFilledConversionOptions(alertRights);
    }

    // Check if the checks should continue or not.
    if (!continueCheck) {
        enableButtons();
        return;
    }

    var upFileAction = $("#uploadedFileAction").attr("value");
    if (upFileAction == "2") {
        var dataProvider = $("#textDataProvider").attr("value");
        if (dataProvider == null || dataProvider == "") {
            alertAndDecode(dataProviderError);
            enableButtons();
            return;
        }
        var edmDaoType = $("#edmDaoType").attr("value");
        if (edmDaoType == null || edmDaoType == "") {
            alertAndDecode(edmDaoError);
            enableButtons();
            return;
        }
        var languageSelectionMaterial = $("#languageSelectionMaterial").attr("value");
        if (languageSelectionMaterial == null || languageSelectionMaterial == "") {
            alertAndDecode(languageError);
            enableButtons();
            return;
        }
        var languageDescriptionSameAsMaterial = $("#languageDescriptionSameAsMaterialCheck").attr("checked");
        if (languageDescriptionSameAsMaterial != "checked") {
            var languageSelectionDescription = $("#languageSelectionDescription").attr("value");
            if (languageSelectionDescription == null || languageSelectionDescription == "") {
                alertAndDecode(languageError);
                enableButtons();
                return;
            }
        }
        var license = $("#licenseeuropeana").attr("checked");
        if (license == "checked") {
            var europeanaLicense = $("#europeanaLicense").attr("value");
            if (europeanaLicense == null || europeanaLicense == "") {
                alertAndDecode(europeanaLicenseError);
                enableButtons();
                return;
            }
        }
    }
    $('#webformIngestionprofile').submit();
}

function avoidCancellations() {
    $('#ingestionprofilesSave').attr("disabled", "disabled");
    $('#ingestionprofilesCancel').attr("disabled", "disabled");
}

function enableButtons() {
    $('#ingestionprofilesSave').removeAttr("disabled");
    $('#ingestionprofilesCancel').removeAttr("disabled");
}

/**
 * Function to encapsulate the changes in the available options when the file
 * type is changed.
 */
function changeAvailableOptions() {
    // Call function that changes the available options for selector of
    // "Default action for uploaded files".
    changeDefaultActionsUploadedFiles();

    // Call function that shows or hide the conversion options.
    showHideRightsConversion();
    showHideEacExtractionFromEad3();

}

function changeDefaultActionsUploadedFiles() {
    var assocType = $("#associatedFiletypeCb").val();
    var upFileAct = $("#uploadedFileAction").val();
    if (assocType != "0" && upFileAct == "2") {
        $("#uploadedFileAction").val("1");
    }
    if (assocType == "0") {
        var optionText = "Publish to Archives Portal Europe and Europeana";
        $('#uploadedFileAction option[value="1"]').after('<option value="2">' + optionText + '</option>');
    } else {
        $("#uploadedFileAction option[value='2']").remove();
    }
}

/**
 * Function to show/hide the rights statements options for conversion based on
 * the associated file type.
 *
 * If file type is EAC-CPF, the options are hidden.
 *
 * If file type is any EAD type, the options are shown.
 */
function showHideRightsConversion() {
    var assocType = $("#associatedFiletypeCb").val();

    if (assocType == 2) {
        $("tr#trRightForDigitalObject").hide();
        $("tr#trDescriptionRightForDigitalObject").hide();
        $("tr#trHolderRightForDigitalObject").hide();
        $("tr#defaultDaoType").hide();
        $("tr#xslConversion").hide();
        $("#labelEadData").hide();
        $("#labelEaccpfData").show();

        // $("tr#trRightForEADData").hide();
        // $("tr#trDescriptionRightForEADData").hide();
        // $("tr#trHolderRightForEADData").hide();
    } else {
        $("tr#trRightForDigitalObject").show();
        $("tr#trDescriptionRightForDigitalObject").show();
        $("tr#trHolderRightForDigitalObject").show();
        $("tr#defaultDaoType").show();
        $("tr#xslConversion").show();
        $("#labelEadData").show();
        $("#labelEaccpfData").hide();

        // $("tr#trRightForEADData").show();
        // $("tr#trDescriptionRightForEADData").show();
        // $("tr#trHolderRightForEADData").show();
    }
}

function showHideEacExtractionFromEad3() {
    var assocType = $("#associatedFiletypeCb").val();
    // if (assocType == 4) {
    //     $("tr#extractEacObject").show();
    //     $("tr#xslConversion").hide();
    //     $("#tab-europeana").hide();
    // } else {
    //     $("tr#extractEacObject").hide();
    //     $("tr#xslConversion").show();
    //     $("#tab-europeana").show();
    // }
    // $("tr#xslConversion").show();
    $("tr#extractEacObject").hide();
    if (assocType == 2) {
        $("#tab-europeana").hide();
    } else {
        $("#tab-europeana").show();
    }
}

/**
 * Function to display the processing information.
 */
function createColorboxForProcessing() {
    $("#colorbox_load_finished").each(function () {
        $(this).remove();
    });
    // Create colorbox.
    $(document).colorbox({
        html: function () {
            var htmlCode = $("#processingInfoDiv").html();
            return htmlCode;
        },
        overlayClose: false, // Prevent close the colorbox when clicks on window.
        escKey: false, // Prevent close the colorbox when hit escape key.
        innerWidth: "150px",
        innerHeight: "36px",
        initialWidth: "0px",
        initialHeight: "0px",
        open: true,
        onLoad: function () {
            $("#colorbox").show();
            $("#cboxOverlay").show();

        },
        onComplete: function () {
            if (!$("#colorbox_load_finished").length) {
                $("#processingInfoDiv").append("<input type=\"hidden\" id=\"colorbox_load_finished\" value=\"true\" />");
            }
        }
    });

    // Remove the close button from colorbox.
    $("#cboxClose").remove();
    // Prevent reload page.
    $(document).on("keydown", disableReload);
}
/**
 * Function to prevent reload the page using F5.
 */
function disableReload(e) {
    if (((e.which || e.keyCode) == 116)
            || (((e.ctrlKey && e.which) || (e.ctrlKey && e.keyCode)) == 82)) {
        e.preventDefault();
    }
}
;

/**
 * Function to close the processing information.
 */
function deleteColorboxForProcessing() {
    if ($("input#colorbox_load_finished").length) {
        //removes flag
        $("#colorbox_load_finished").each(function () {
            $(this).remove();
        });
        // Close colorbox.
        $.colorbox.close();
        // Enable the page reload using F5.
        $(document).off("keydown", disableReload);
    } else {
        setTimeout(function () {
            deleteColorboxForProcessing();
        }, 500);
    }
    $("div[id='cboxOverlay']").each(function (i) {
        $(this).fadeOut(1000);

    });
    $("div[id='colorbox']").each(function (i) {
        $(this).fadeOut(1000);
    });
}

/**
 * Function to remove the old pending messages.
 */
function deleteChecks() {
    $('.fieldRequired').remove();
}

/**
 * Function to remove the message associated to the passed element.
 */
function deleteMessage(element) {
    var id = $(element).attr("id");

    $("p#" + id + "_required").remove();
}

/**
 * Function to check if the user has filled the description and/or the rights
 * holder but not the rights statement for both digital objects and EAD data.
 *
 * @param alertRights Warning message about the rights field.
 *
 * @returns Result of the check. If TRUE fields filled correctly, if FALSE
 * some needed field is not filled.
 */
function checkFilledConversionOptions(alertRights) {
    var result = true;

    // Clean white spaces.
    trimWitheSpaces();

    // Check the filled status for rights for digital objects section.
    if (($("textarea#rightDigitalDescription").val() != ''
            || $("input#rightDigitalHolder").val() != '')
            && $("select#rightDigitalObjects").val() == '---') {
        addWarnignMessage($("select#rightDigitalObjects").attr("id"), alertRights);
        result = false;
    }

    // Check the filled status for rights for EAD data section.
    if (($("textarea#rightEadDescription").val() != ''
            || $("input#rightEadHolder").val() != '')
            && $("select#rightEadData").val() == '---') {
        addWarnignMessage($("select#rightEadData").attr("id"), alertRights);
        result = false;
    }

    return result;
}

/**
 * Function to trim the whitespaces from the textarea and input fields.
 */
function trimWitheSpaces() {
    // Clean white spaces in description of rights for digital objects.
    $("textarea#rightDigitalDescription").val($.trim($("textarea#rightDigitalDescription").val()));
    // Clean white spaces in holder of rights for digital objects.
    $("input#rightDigitalHolder").val($.trim($("input#rightDigitalHolder").val()));

    // Clean white spaces in description of rights for EAD data.
    $("textarea#rightEadDescription").val($.trim($("textarea#rightEadDescription").val()));
    // Clean white spaces in holder of rights for EAD data.
    $("input#rightEadHolder").val($.trim($("input#rightEadHolder").val()));
}

/**
 * Function to create a new warning message under the field in which exits the
 * problem.
 *
 * @param fieldId Field with the problem described in the warning message.
 * @param alertRights Warning message about the rights field.
 */
function addWarnignMessage(fieldId, alertRights) {
    var element = document.getElementById(fieldId);
    var subelement = document.createElement('p');
    subelement.appendChild(document.createTextNode(alertRights));
    subelement.id = fieldId + '_required';
    subelement.className = "fieldRequired";
    element.parentNode.insertBefore(subelement, element.nextSibling);
}
