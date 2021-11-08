function hideAndShow(idPrefix, shown) {
    //first check if the click action can happen
    if (!($("ul#eag2012TabsContainer li a[href='#" + shown + "']").hasClass("eag2012disabled"))) {
        $("div[id^='" + idPrefix + "']").each(function () {
            $(this).hide();
        });
        $("ul#eag2012TabsContainer li a[href^='#tab']").each(function () {
            $(this).removeClass("eag2012currenttab");
        });
        $("div[id='" + shown + "']").show();
        $("ul#eag2012TabsContainer li a[href='#" + shown + "']").addClass("eag2012currenttab");
        $("#currentTab").attr("value", shown);
        if (shown == "tab-accessAndServices" || shown == "tab-contact" || shown == "tab-description") {
            var id = "";
            $("ul#eag2012tabs_institution_tabs li a").each(function () {
                if ($(this).hasClass("eag2012currenttab")) {
                    id = $(this).attr("id");
                }
            });

            id = id.substring(id.lastIndexOf("_"));
            //hide all other tables and show target table
            $("div[id='" + shown + "'] table").each(function () {
                $(this).hide();
                var newId = $(this).attr("id");
                if (newId.match(id + "$")) {
                    $(this).show();
                }

                var showContactTables = new Array("contactTableVisitorsAddress", "contactTablePostalAddress");
                if (shown == "tab-contact") {
                    for (var i = 0; i < showContactTables.length; i++) {
                        // Show visitor address tables.
                        var parentId = $(this).parent().parent().parent().parent().parent().parent().attr("id");
                        if ($(this).attr("id").indexOf(showContactTables[i]) > -1 && ("contactTable" + id) == parentId) {
                            $(this).show();
                        }
                        // Show postal address tables.
                        parentId = $(this).parent().parent().parent().parent().attr("id");
                        if ($(this).attr("id").indexOf(showContactTables[i]) > -1 && ("contactTable" + id) == parentId) {
                            $(this).show();
                        }
                    }
                }
            });
        }
    }
}

function navigateToCurrentRepoTab(href) {
    var errorFieldText = $(".fieldRequired :not(#textContactCountyOfTheInstitution)");
    var parent = errorFieldText.parent();
    var counter = 50;
    var seekId = parent.attr("id");
    while (counter > 0 && ((typeof (parent) !== "undefined" && typeof (parent.prop) !== "undefined" && typeof (parent.prop("tagName")) != "undefined" && parent.prop("tagName").toUpperCase() != 'TABLE') || (typeof (seekId) !== "undefined" && seekId.indexOf('contactTableVisitorsAddress_') > -1))) {
        parent = parent.parent();
        seekId = parent.attr("id");
        counter--;
    }
    var id = parent.attr("id");
    var repoTab = 0;
    if (typeof (id) !== "undefined" && id.lastIndexOf("_") + 1) {
        repoTab = id.substring(id.lastIndexOf("_") + 1);
    } else {
        if ($("[id$='_required']").length > 0) {
            var targetTabFound = "";
            if ($("table[id^='contactTable_'] .fieldRequired").length > 0) {
                $("table[id^='contactTable_']").each(function () {
                    if (targetTabFound == "" && $(this).find("p.fieldRequired").length > 0) {
                        targetTabFound = $(this).attr("id");
                        repoTab = targetTabFound.substring(targetTabFound.lastIndexOf("_") + 1);
                    }
                });
            } else if ($("table[id^='accessAndServicesTable_'] .fieldRequired").length > 0) {
                $("table[id^='accessAndServicesTable_']").each(function () {
                    if (targetTabFound == "" && $(this).find("p.fieldRequired").length > 0) {
                        targetTabFound = $(this).attr("id");
                        repoTab = targetTabFound.substring(targetTabFound.lastIndexOf("_") + 1);
                    }
                });
            } else if ($("table[id^='descriptionTable_'] .fieldRequired").length > 0) {
                $("table[id^='descriptionTable_']").each(function () {
                    if (targetTabFound == "" && $(this).find("p.fieldRequired").length > 0) {
                        targetTabFound = $(this).attr("id");
                        repoTab = targetTabFound.substring(targetTabFound.lastIndexOf("_") + 1);
                    }
                });
            }
        }
    }
    if (repoTab > 0) {
        $("#tab_yourInstitutionTable_" + repoTab).trigger('click');
//		$("#tab_yourInstitutionTable_"+repoTab+" a[href='"+href+"']").trigger('click');
        $("a[href='" + href + "']").trigger('click');
    } else {
        $("#tab_yourInstitutionTable_1 a[href='" + href + "']").trigger('click');
    }
}

function clickSaveAction(form, text1, text2, error1, error2, error3, error4, error5, error6, error7, error8, error9, message, institutionName, saveOrExit, errorspecialcharacter, errorShareWikimedia) {
    //first check in which tab are the user, validate current tab and next the others
    var selectedHref = "";
    var tabsToCheck = new Array();
    $(".eag2012currenttab").each(function () {
        var href = $(this).attr("href");
        if (href.match("tab-")) {
            selectedHref = tabsToCheck.push(href);
        }
    });
    tabsToCheck.push(selectedHref);
    $("li[id^='tab-'] a").each(function () {
        var href = $(this).attr("href");
        if (href.match("tab-")) {
            tabsToCheck.push(href);
        }
    });
    var jsonDataYourInstitution, jsonDataIdentity, jsonDataContact, jsonDataAccessAndServices, jsonDataDescription, jsonDataControl;
    jsonDataYourInstitution = jsonDataIdentity = jsonDataContact = jsonDataAccessAndServices = jsonDataDescription = jsonDataControl = "";
    var exit = false;
    for (var i = 0; !exit && i < tabsToCheck.length; i++) {
        var href = tabsToCheck[i];
        switch (href) {
            case "#tab-yourInstitution":
                // Check fill mandatory fields in tab "your institution".
                var jsonDataYourInstitution = clickYourInstitutionAction(text1, message);
                if (!jsonDataYourInstitution) {
                    displayAlertDialog(error1);
                    if ($("#tab_yourInstitutionTable_1").length > 0) {
                        $("#tab_yourInstitutionTable_1").trigger('click');
                        $("#tab_yourInstitutionTable_1 a[href='#tab-yourInstitution']").trigger('click');
                    } else {
                        $("a[href='#tab-yourInstitution']").trigger('click');
                    }
                    exit = true;
                }
                break;
            case "#tab-identity":
                //Check if the values autform have special characters
                var errorTab = checkAutformIdentity();
                if (errorTab == 0) {
                    // Check fill mandatory fields in tab "identity".
                    var jsonDataIdentity = clickIdentityAction(text1);
                    if (!jsonDataIdentity) {
                        displayAlertDialog(error2);
                        if ($("#tab_yourInstitutionTable_1").length > 0) {
                            $("#tab_yourInstitutionTable_1").trigger('click');
                            $("#tab_yourInstitutionTable_1 a[href='#tab-identity']").trigger('click');
                        } else {
                            $("a[href='#tab-identity']").trigger('click');
                        }
                        exit = true;
                    }
                } else {
                    displayAlertDialog(errorspecialcharacter);
                    if (errorTab == 1) {
                        if ($("#tab_yourInstitutionTable_1").length > 0) {
                            $("#tab_yourInstitutionTable_1").trigger('click');
                            $("#tab_yourInstitutionTable_1 a[href='#tab-yourInstitution']").trigger('click');
                        } else {
                            $("a[href='#tab-yourInstitution']").trigger('click');
                        }
                        exit = true;
                    }
                    if (errorTab > 1) {
                        if ($("#tab_yourInstitutionTable_1").length > 0) {
                            $("#tab_yourInstitutionTable_1").trigger('click');
                            $("#tab_yourInstitutionTable_1 a[href='#tab-identity']").trigger('click');
                        } else {
                            $("a[href='#tab-identity']").trigger('click');
                        }
                        exit = true;
                    }
                }
                break;
            case "#tab-contact":
                // Check fill mandatory fields in tab "contact".
                var jsonDataContact = checkAllContactTabs(text1, message);
                if (!jsonDataContact) {
                    displayAlertDialog(error3);
                    navigateToCurrentRepoTab(href);
                    exit = true;
                }
                if (jsonDataContact === true) {
                    displayAlertDialog(error9);
                    navigateToCurrentRepoTab(href);
                    exit = true;
                }
                break;
            case "#tab-accessAndServices":
                // Check fill mandatory fields in tab "access and services".
                var jsonDataAccessAndServices = checkAllAccessAndServicesTabs(text1, message);
                if (!jsonDataAccessAndServices) {
                    displayAlertDialog(error4);
                    navigateToCurrentRepoTab(href);
                    exit = true;
                }
                break;
            case "#tab-description":
                // Check fill mandatory fields in tab "description".
                var jsonDataDescription = checkAllDescriptionTabs(text1);
                if (!jsonDataDescription) {
                    displayAlertDialog(error5);
                    navigateToCurrentRepoTab(href);
                    exit = true;
                }
                break;
            case "#tab-control":
                // Check fill mandatory fields in tab "control".
                var jsonDataControl = clickControlAction(text1);
                if (!jsonDataControl) {
                    displayAlertDialog(error6);
                    $("#tab_yourInstitutionTable_1").trigger('click');
                    $("#tab_yourInstitutionTable_1 a[href='#tab-control']").trigger('click');
                    exit = true;
                }
                break;
            case "#tab-relations":
                // Check fill mandatory fields in tab "relations".
                var jsonDataRelations = clickRelationsAction(text1, message);
                if (!jsonDataRelations) {
                    displayAlertDialog(error7);
                    if ($("#tab_yourInstitutionTable_1").length > 0) {
                        $("#tab_yourInstitutionTable_1").trigger('click');
                        $("#tab_yourInstitutionTable_1 a[href='#tab-relations']").trigger('click');
                    } else {
                        $("a[href='#tab-relations']").trigger('click');
                    }
                    exit = true;
                }
                break;
        }
    }
    if (!exit) {
        // Check if almost one of the authorized name of the institution is the same as the institution's name.
        var nameOfInstitution = checkNameOfInstitution(error8, institutionName);
        if (!nameOfInstitution) {
            return;
        }

        createColorboxForProcessing();

        // Create final json object.
        var jsonData = "{'yourInstitution':" + jsonDataYourInstitution + "," +
                "'identity':" + jsonDataIdentity + "," +
                "'contact':" + jsonDataContact + "," +
                "'accessAndServices':" + jsonDataAccessAndServices + "," +
                "'description':" + jsonDataDescription + "," +
                "'control':" + jsonDataControl + "," +
                "'relations':" + jsonDataRelations + "}";

        if (saveOrExit)
            $('#webformeag2012 input#saveOrExit').val("saveAndExit");
        else {
            $('#webformeag2012 input#saveOrExit').val("save");
        }

        $('#webformeag2012').append('<textarea name="form" style="display: none;">' + jsonData + '</textarea>');
        $('#webformeag2012').submit();
    }
}

function deleteChecks() {
    $('.fieldRequired').remove();
}

function checkWebpages(target, message) {
    var checkFails = false;
    var value = target.val();
    if (value && value.length > 0) {
        value = value.toLowerCase();
        value = $.trim(value);
        if (!(value.indexOf("https://") == 0 || value.indexOf("http://") == 0 || value.indexOf("ftp://") == 0)) {
            var pFieldError = "<p id=\"" + $(this).attr("id") + "_w_required\" class=\"fieldRequired\">" + message + "</p>";
            var id = target.attr("id");
            if (id.indexOf("textWebsiteOfResource") == "-1" && id.indexOf("textWebsiteOfDescription") == "-1") {
                target.after(pFieldError);
            } else {
                var parent = target.parent().parent().parent().parent().attr("id");
                $("table#" + parent + " textarea#" + id).after(pFieldError);
            }
            checkFails = true;
        }
    }
    return checkFails;
}

var clickYourInstitutionAction = function (text1, messageRightWeb) {
    // Delete old checks
    deleteChecks();

    // Mandatory elements
    var yiMandatoryElements = new Array("textYIInstitutionCountryCode", "textYIIdentifierOfTheInstitution", "textYINameOfTheInstitution");

    var jsonData = "{";
    // Common part.
    $("table#yourInstitutionTable_1 input[type='text']").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";

        // Check fill mandatory fields.
        if ($(this).attr("value") != '') {
            var position = $.inArray($(this).attr("id"), yiMandatoryElements);
            if (position != -1) {
                yiMandatoryElements.splice(position, 1);
            }
        }
    });
    //textarea 
    $("table#yourInstitutionTable_1 textarea").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";

        // Check fill mandatory fields.
        if ($(this).attr("value") != '') {
            var position = $.inArray($(this).attr("id"), yiMandatoryElements);
            if (position != -1) {
                yiMandatoryElements.splice(position, 1);
            }
        }
    });
    //select options selected
    $("table#yourInstitutionTable_1 select").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':') {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";

        // Check fill mandatory fields.
        if ($(this).attr("value") != 'none') {
            var position = $.inArray($(this).attr("id"), yiMandatoryElements);
            if (position != -1) {
                yiMandatoryElements.splice(position, 1);
            }
        }
    });

    //validation array
    var validationArray = new Array();

    // Visitors address.
    var visitorsAddress = new Array();
    $("table[id^='yiTableVisitorsAddress_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        visitorsAddress.push(id);
    });
    jsonData += ",'visitorsAddress':{";
    for (var j = 0; j < visitorsAddress.length; j++) {
        var yiMEVisitorsAddress = new Array("textYIStreet",
                "textYICity", "textYICountry");

        if (jsonData.substring(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + visitorsAddress[j] + "':{";
        //input type text
        $("#" + visitorsAddress[j] + " input[type='text']").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
            // Check fill mandatory fields.
            if ($(this).attr("value") != '' && j == 0) {
                var position = $.inArray($(this).attr("id"), yiMEVisitorsAddress);
                if (position != -1) {
                    yiMEVisitorsAddress.splice(position, 1);
                }
            }
        });
        //textarea
        $("#" + visitorsAddress[j] + " textarea").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
            // Check fill mandatory fields.
            if ($(this).attr("value") != '' && j == 0) {
                var position = $.inArray($(this).attr("id"), yiMEVisitorsAddress);
                if (position != -1) {
                    yiMEVisitorsAddress.splice(position, 1);
                }
            }
        });
        //select options selected
        $("#" + visitorsAddress[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";

            // Check fill mandatory fields.
            if ($(this).attr("value") != 'none' && j == 0) {
                var position = $.inArray($(this).attr("id"), yiMEVisitorsAddress);
                if (position != -1) {
                    yiMEVisitorsAddress.splice(position, 1);
                }
            }
        });
        if (yiMEVisitorsAddress.length > 0 && j == 0) {
            validationArray.push(visitorsAddress[j], yiMEVisitorsAddress);
        }
        jsonData += "}";
    }

    jsonData += "}";

    // Postal address.
    var postalAddress = new Array();
    $("table[id^='yiTablePostalAddress_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        postalAddress.push(id);
    });
    jsonData += ",'postalAddress':{";
    for (var j = 0; j < postalAddress.length; j++) {
        var yiMEPostalAddress = new Array("textYIPAStreet", "textYIPACity");

        if (jsonData.substring(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + postalAddress[j] + "':{";
        //textarea
        var counter = 0;  //count the fields empty in postal address
        $("#" + postalAddress[j] + " textarea").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
            // Check fill mandatory fields.
            if ($(this).attr("value") != '' && j == 0) {
                var position = $.inArray($(this).attr("id"), yiMEPostalAddress);
                if (position != -1) {
                    yiMEPostalAddress.splice(position, 1);
                }
            } else if ($.trim($(this).attr("value")).length == 0) {     //the field is empty
                counter++;
            }
            if (counter == 2) {  //if street and city are empty there aren't required fields 
                yiMEPostalAddress.splice(0, 2);
            }
        });
        //select options selected
        $("#" + postalAddress[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";

            // Check fill mandatory fields.
            if ($(this).attr("value") != 'none' && j == 0) {
                var position = $.inArray($(this).attr("id"), yiMEPostalAddress);
                if (position != -1) {
                    yiMEPostalAddress.splice(position, 1);
                }
            }
        });
        if (yiMEPostalAddress.length > 0 && j == 0) {
            validationArray.push(postalAddress[j], yiMEPostalAddress);
        }
        jsonData += "}";
    }

    jsonData += "}";

    // Other fields.
    var yiMERepositories = new Array("textYIOpeningTimes");

    //input type text
    $("#yiTableOthers input[type='text']").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':'
                && jsonData.charAt(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        // Check fill mandatory fields.
        if ($(this).attr("value") != '') {
            var position = $.inArray($(this).attr("id"), yiMERepositories);
            if (position != -1) {
                yiMERepositories.splice(position, 1);
            }
        }
    });
    //textarea
    $("#yiTableOthers textarea").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':'
                && jsonData.charAt(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        // Check fill mandatory fields.
        if ($(this).attr("value") != '') {
            var position = $.inArray($(this).attr("id"), yiMERepositories);
            if (position != -1) {
                yiMERepositories.splice(position, 1);
            }
        }
    });
    var additionalChecks = false;
    $("table#yiTableOthers textarea[id^='textReferencetoyourinstitutionsholdingsguide']").each(function () {
        var result = checkWebpages($(this), messageRightWeb);
        if (!additionalChecks) {
            additionalChecks = result;
        }
    });
    var wepageCheck = false;
    $("table#yiTableOthers textarea[id^='textYIWebpage']").each(function () {
        if ($(this).attr("id").indexOf("textYIWebpageLinkTitle") == "-1") {
            var result = checkWebpages($(this), messageRightWeb);
            if (!wepageCheck) {
                wepageCheck = result;
            }
        }
    });
    var openingHoursUrlCheck = false;
    $("table#yiTableOthers textarea[id^='linkYIOpeningTimes']").each(function () {
        if ($(this).attr("id").indexOf("textYIOpeningTimes") == "-1") {
            var result = checkWebpages($(this), messageRightWeb);
            if (!openingHoursUrlCheck) {
                openingHoursUrlCheck = result;
            }
        }
    });
    //select options selected
    $("#yiTableOthers select").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':') {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
    });
    if (yiMERepositories.length > 0) {
        validationArray.push("yiTableOthers", yiMERepositories);
    }

    var shareEagWithWikimediaChecked = false;
    $("#yiTableOthers input[type='radio']").each(function (index, element) {
        if ($(element).is(':checked')) {
            shareEagWithWikimediaChecked = true;

            if (jsonData.charAt(jsonData.length - 1) != ':'
                && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'shareEagWithWikimedia' : " + (index==0?true:false);
        }
    });
    if (!shareEagWithWikimediaChecked) {
        validationArray.push("yiTableOthers", new Array("shareEagWithWikimediaError"));
    }

    jsonData += "}";

    for (var i = 0; i < yiMandatoryElements.length; i++) {
        var element = document.getElementById(yiMandatoryElements[i].toString());
        var subelement = document.createElement('p');

        subelement.appendChild(document.createTextNode(text1));
        subelement.id = yiMandatoryElements[i].toString() + '_required';
        subelement.className = "fieldRequired";
        element.parentNode.insertBefore(subelement, element.nextSibling);
    }

    for (var i = 0; i < validationArray.length; i = (i + 2)) {
        var array = validationArray[i + 1];

        for (var j = 0; j < array.length; j++) {
            var subelement = document.createElement('p');

            var error = text1;

            if (array[j].toString() == 'shareEagWithWikimediaError')
                error = $("#shareWikimediaHiddenError").html();

            subelement.appendChild(document.createTextNode(error));
            subelement.id = array[j].toString() + '_required';
            subelement.className = "fieldRequired";

            $('#' + validationArray[i].toString()).find('#' + array[j].toString()).after(subelement);
        }
    }

    if (yiMandatoryElements.length != 0 || validationArray.length != 0 || additionalChecks || wepageCheck || openingHoursUrlCheck) {
        return false;
    }

    return jsonData;
};

var clickIdentityAction = function (text1) {
    var jsonData = "{";

    // Table identityTable.
    //content from texts
    $("table#identityTable input[type='text']").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
    });
    //content from textarea
    $("table#identityTable textarea").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
    });
    //content from selects
    $("table#identityTable select").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':') {
            jsonData += ",";
        }
        jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
    });

    // Table identityTable.
    //content from selects
    $("table#identitySelectTypeOfTheInstitution select").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':') {
            jsonData += ",";
        }
        var texts = "";
        $("select#" + $(this).attr("id") + " option").each(function () {
            if ($(this).attr("selected")) {
                texts += $.trim($(this).attr("value")) + '_';
            }
        });
        texts = texts.substring(0, (texts.length - 1));

        jsonData += "'" + $(this).attr("id") + "' : '" + texts + "'";
    });

    // Institution names.
    var institutionNames = new Array();
    $("table[id^='identityTableNameOfTheInstitution_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        institutionNames.push(id);
    });

    jsonData += ",'institutionNames':{";
    for (var j = 0; j < institutionNames.length; j++) {
        if (jsonData.substring(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + institutionNames[j] + "':{";
        //textarea
        $("#" + institutionNames[j] + " textarea").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        });
        //select options selected
        $("#" + institutionNames[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        });
        jsonData += "}";
    }
    jsonData += "}";

    // Parallel names.
    var parallelNames = new Array();
    $("table[id^='identityTableParallelNameOfTheInstitution_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        parallelNames.push(id);
    });

    jsonData += ",'parallelNames':{";
    for (var j = 0; j < parallelNames.length; j++) {
        if (jsonData.substring(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + parallelNames[j] + "':{";
        //textarea
        $("#" + parallelNames[j] + " textarea").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        });
        //select options selected
        $("#" + parallelNames[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        });
        jsonData += "}";
    }
    jsonData += "}";

    // Formerly names.
    var formerlyNames = new Array();
    $("table[id^='identityTableFormerlyUsedName_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        formerlyNames.push(id);
    });

    jsonData += ",'formerlyNames':{";
    for (var j = 0; j < formerlyNames.length; j++) {
        if (jsonData.substring(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + formerlyNames[j] + "':{";
        //textarea
        $("#" + formerlyNames[j] + " textarea").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        });
        //input type text
        var jsonDataYear = "";
        $("#" + formerlyNames[j] + " input[type='text']").each(function () {
            if ($(this).attr("id").indexOf("textYearWhenThisNameWasUsed") != "-1"
                    || $(this).attr("id").indexOf("textYearWhenThisNameWasUsedFrom") != "-1"
                    || $(this).attr("id").indexOf("textYearWhenThisNameWasUsedTo") != "-1") {
                if (jsonDataYear.length > 1) {
                    jsonDataYear += ",";
                }
                jsonDataYear += "'" + $(this).attr("id") + "' : '" + $.trim(escapeDate($(this))) + "'";
            } else {
                if (jsonData.charAt(jsonData.length - 1) != ':'
                        && jsonData.charAt(jsonData.length - 1) != '{') {
                    jsonData += ",";
                }
                jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
            }
        });
        jsonData += "," + jsonDataYear;

        //select options selected
        $("#" + formerlyNames[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        });
        jsonData += "}";
    }
    jsonData += "}}";

    return jsonData;
};

var clickContactAction = function (text1, message) {
    var currentTab = getCurrentTab();

    return checkContactTab(currentTab, text1, message);
};

function checkAllContactTabs(text1, message) {
    var counter = $("table[id^='contactTable_']").length;
    var jsonData = "{";

    for (var i = 1; i <= counter; i++) {
        if (jsonData.substring(jsonData.length - 1) == '}') {
            jsonData += ",";
        }
        jsonData += "'contactTable_" + i + "':";
        var check = checkContactTab("_" + i, text1, message);
        if (!check) {
            return false;
        }
        if (check === true) {
            return true;
        }

        jsonData += check;
    }

    jsonData += "}";

    return jsonData;
}
;

function checkContactTab(currentTab, text1, messageWebpage) {
    // Delete old checks
    deleteChecks();

    // Mandatory elements
    var contactMandatoryElements = new Array();
    var contactRepoMandatoryElements = new Array("textNameOfRepository", "selectRoleOfRepository");

    if (parseInt(currentTab.substring(currentTab.length - 1)) > '1') {
        contactMandatoryElements = contactMandatoryElements.concat(contactRepoMandatoryElements);
    }

    var jsonData = "{";
    //content from texts
    $("table#contactTable" + currentTab + " input[type='text']").each(function () {
        if (jsonData.length > 1) {
            if (jsonData.substring(jsonData.length - 1) != ',') {
                jsonData += ",";
            }
        }
        if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTableVisitorsAddress") == -1) {
            if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTablePostalAddress") == -1) {
                if ($(this).attr("id") == "textContactFaxOfTheInstitution") {
                    jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim(escapeApostrophe($(this))) + "'";
                } else {
                    jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
                }
            }
        }

        // Check fill mandatory fields.
        if ($.trim($(this).attr("value")) != '') {
            var position = $.inArray($(this).attr("id"), contactMandatoryElements);
            if (position != -1) {
                contactMandatoryElements.splice(position, 1);
            }
        }
    });
    //textarea
    $("table#contactTable" + currentTab + " textarea").each(function () {
        if (jsonData.length > 1) {
            if (jsonData.substring(jsonData.length - 1) != ',') {
                jsonData += ",";
            }
        }
        if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTableVisitorsAddress") == -1) {
            if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTablePostalAddress") == -1) {
                jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
            }
        }

        // Check fill mandatory fields.
        if ($.trim($(this).attr("value")) != '') {
            var position = $.inArray($(this).attr("id"), contactMandatoryElements);
            if (position != -1) {
                contactMandatoryElements.splice(position, 1);
            }
        }
    });
    //content from selects
    $("table#contactTable" + currentTab + " select").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':') {
            if (jsonData.substring(jsonData.length - 1) != ',') {
                jsonData += ",";
            }
        }
        if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTableVisitorsAddress") == -1) {
            if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTablePostalAddress") == -1) {
                jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
            }
        }

        // Check fill mandatory fields.
        if ($.trim($(this).attr("value")) != 'none') {
            var position = $.inArray($(this).attr("id"), contactMandatoryElements);
            if (position != -1) {
                contactMandatoryElements.splice(position, 1);
            }
        }
    });

    var failWebpageCheck = false;
    $("table#contactTable" + currentTab + " textarea[id^='textContactWebOfTheInstitution']").each(function () {
        var result = checkWebpages($(this), messageWebpage);
        if (!failWebpageCheck) {
            failWebpageCheck = result;
        }
    });

    //validation array
    var validationArray = new Array();

    // Visitors address.
    var visitorsAddress = new Array();
    $("table#contactTable" + currentTab + " table[id^='contactTableVisitorsAddress_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        visitorsAddress.push(id);
    });
    jsonData += ",'visitorsAddress':{";
    for (var j = 0; j < visitorsAddress.length; j++) {
        var contactVAMandatoryElements = new Array("textContactStreetOfTheInstitution", "textContactCityOfTheInstitution"
                );

        if (jsonData.substring(jsonData.length - 1) != '{') {
            if (jsonData.substring(jsonData.length - 1) != ',') {
                jsonData += ",";
            }
        }
        jsonData += "'" + visitorsAddress[j] + "':{";
        //input type text
        $("table#contactTable" + currentTab + " table#" + visitorsAddress[j] + " input[type='text']").each(function () {
//			if(!$(this).is(":disabled") && !$(this).prop("disabled")){
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                if (jsonData.substring(jsonData.length - 1) != ',') {
                    jsonData += ",";
                }
            }
            if ($(this).parent().parent().parent().parent().parent().parent().parent().parent().parent().attr("id").indexOf("divTempContainter") == -1) {
                jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
            }

            // Check fill mandatory fields.
            if ($.trim($(this).attr("value")) != '' && j == 0) {
                var position = $.inArray($(this).attr("id"), contactVAMandatoryElements);
                if (position != -1 || $(this).is(':disabled')) {
                    contactVAMandatoryElements.splice(position, 1);
                }
            }
//			}
        });
        //textarea
        $("table#contactTable" + currentTab + " table#" + visitorsAddress[j] + " textarea").each(function () {
            if (($(this).is(":disabled") || $(this).prop("disabled")) && $.inArray($(this).attr("id"), contactVAMandatoryElements)) {
                contactVAMandatoryElements.splice(position, $.inArray($(this).attr("id"), contactVAMandatoryElements));
            }
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                if (jsonData.substring(jsonData.length - 1) != ',') {
                    jsonData += ",";
                }
            }
            if ($(this).parent().parent().parent().parent().parent().parent().parent().parent().parent().attr("id").indexOf("divTempContainter") == -1) {
                jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
            }

            // Check fill mandatory fields.
            if ("textContactCountryOfTheInstitution" == $(this).attr("id") && !$(this).is(':disabled')) {
                contactVAMandatoryElements.push("textContactCountryOfTheInstitution");
            }
            if ($.trim($(this).attr("value")) != '' && j == 0) {
                var position = $.inArray($(this).attr("id"), contactVAMandatoryElements);
                if (position != -1 || $(this).is(':disabled')) {
                    contactVAMandatoryElements.splice(position, 1);
                }
            }
//			}
        });
        //select options selected
        $("table#contactTable" + currentTab + " table#" + visitorsAddress[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                if (jsonData.substring(jsonData.length - 1) != ',') {
                    jsonData += ",";
                }
            }
            if ($(this).parent().parent().parent().parent().parent().parent().parent().parent().parent().attr("id").indexOf("divTempContainter") == -1) {
                jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
            }

            // Check fill mandatory fields.
            if ($.trim($(this).attr("value")) != 'none' && j == 0) {
                var position = $.inArray($(this).attr("id"), contactVAMandatoryElements);
                if (position != -1) {
                    contactVAMandatoryElements.splice(position, 1);
                }
            }
        });
        if (contactVAMandatoryElements.length > 0 && j == 0) {
            $.each(contactVAMandatoryElements, function (key, element) {
                if (!$("#" + visitorsAddress[j] + " #" + element).is(":disabled")) {
                    contactVAMandatoryElements.splice(key, 1);
                }
            });
            validationArray.push(visitorsAddress[j], contactVAMandatoryElements);
        }
        jsonData += "}";
    }

    jsonData += "}";

    // Postal address.
    var postalAddress = new Array();
    $("table#contactTable" + currentTab + " table[id^='contactTablePostalAddress_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        postalAddress.push(id);
    });
    jsonData += ",'postalAddress':{";
    for (var j = 0; j < postalAddress.length; j++) {
//           var contactPAMandatoryElements = new Array("textContactPAStreet", "textContactPACity");
        if (jsonData.substring(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + postalAddress[j] + "':{";
        //textarea
        var counter = 0;  //count the fields empty in postal address
        $("table#contactTable" + currentTab + " table#" + postalAddress[j] + " textarea").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";

            // Check fill mandatory fields.
//			if ($.trim($(this).attr("value")) != '' && j == 0) {
//				var position = $.inArray($(this).attr("id"),contactPAMandatoryElements);
//				if (position != -1) {
//					contactPAMandatoryElements.splice(position, 1);
//				}
//			}else if ($.trim($(this).attr("value")).length == 0){     //the field is empty
//                              counter++;
//                         }
//                         if (counter==2){  //if street and city are empty there aren't required fields 
//                                  contactPAMandatoryElements.splice(0,2);
//                          }

        });
        //select options selected
        $("table#contactTable" + currentTab + " table#" + postalAddress[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        });
//        if(contactPAMandatoryElements.length>0 && j == 0){
//			validationArray.push(postalAddress[j],contactPAMandatoryElements);
//		}

        jsonData += "}";
    }

    jsonData += "}}";

    for (var i = 0; i < contactMandatoryElements.length; i++) {
        var pFieldError = "<p id=\"" + contactMandatoryElements[i] + "_required\" class=\"fieldRequired\">" + text1 + "</p>";
        $("table#contactTable" + currentTab + " #" + contactMandatoryElements[i]).after(pFieldError);
    }

    for (var i = 0; i < validationArray.length; i = (i + 2)) {
        var array = validationArray[i + 1];

        for (var j = 0; j < array.length; j++) {
            var pFieldError = "<p id=\"" + validationArray[i] + "_required\" class=\"fieldRequired\">" + text1 + "</p>";
            $("table#contactTable" + currentTab + " #" + validationArray[i] + " #" + array[j]).after(pFieldError);
        }
    }
    if (contactMandatoryElements.length != 0) {
        var position1 = $.inArray("textNameOfRepository", contactMandatoryElements);
        var position2 = $.inArray("selectRoleOfRepository", contactMandatoryElements);
        if (position1 != -1 && position2 != -1) {
            return true;
        }
    }
    if (contactMandatoryElements.length != 0 || validationArray.length != 0 || failWebpageCheck) {
        return false;
    }

    return jsonData;
}
;

var clickAccessAndServicesAction = function (text1, message) {
    var currentTab = getCurrentTab();

    return checkAccessAndServicesTab(currentTab, text1, message);
};

function checkAllAccessAndServicesTabs(text1, message) {
    var counter = $("table[id^='accessAndServicesTable_']").length;
    var jsonData = "{";

    for (var i = 1; i <= counter; i++) {
        if (jsonData.substring(jsonData.length - 1) == '}') {
            jsonData += ",";
        }

        jsonData += "'accessAndServicesTable_" + i + "':";

        var check = checkAccessAndServicesTab("_" + i, text1, message);

        if (!check) {
            return false;
        }

        jsonData += check;
    }

    jsonData += "}";

    return jsonData;
}
;

function checkAccessAndServicesTab(currentTab, text1, messageLink) {
    // Delete old checks
    deleteChecks();

    // Mandatory elements
    var aasMandatoryElements = new Array("textOpeningTimes_1");

    var jsonData = "{";
    //content from textareas
    $("table#accessAndServicesTable" + currentTab + " textarea").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        if ($(this).attr("id") == "textTravellingDirections"
                || $(this).attr("id") == "textASTermOfUse"
                || $(this).attr("id") == "textOpeningTimes"
                || $(this).attr("id") == "linkOpeningTimes"
                || $(this).attr("id") == "textClosingDates"
                || $(this).attr("id") == "textTravelLink"
                || $(this).attr("id") == "textASAccessRestrictions"
                || $(this).attr("id") == "textASTOULink"
                || $(this).attr("id") == "textASAccessibility"
                || $(this).attr("id") == "textDescriptionOfYourComputerPlaces"
                || $(this).attr("id") == "textASSRReadersTicket"
                || $(this).attr("id") == "textASSRRTLink"
                || $(this).attr("id") == "textASSRAdvancedOrders"
                || $(this).attr("id") == "textASSRAOLink"
                || $(this).attr("id") == "textASSRResearchServices"
                || $(this).attr("id") == "textASDescription"
                || $(this).attr("id") == "textASTSDescriptionOfRestaurationLab"
                || $(this).attr("id") == "textASTSDescriptionOfReproductionService"
                || $(this).attr("id") == "textASReSeExhibition"
                || $(this).attr("id") == "textASReSeWebpage"
                || $(this).attr("id") == "textASReSeWebpageLinkTitle"
                || $(this).attr("id") == "textASReSeToursAndSessions"
                || $(this).attr("id") == "textASReSeTSWebpage"
                || $(this).attr("id") == "textASReSeWebpageTSLinkTitle"
                || $(this).attr("id") == "textASReSeOtherServices"
                || $(this).attr("id") == "textASReSeOSWebpage"
                || $(this).attr("id") == "textASReSeWebpageOSLinkTitle"
                || $(this).attr("id") == "textASReSeRefreshment") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim(escapeApostrophe($(this))) + "'";
        } else {
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        }
        // Check fill mandatory fields.
        if ($.trim($(this).attr("value")) != '') {
            var position = $.inArray($(this).attr("id"), aasMandatoryElements);
            if (position != -1) {
                aasMandatoryElements.splice(position, 1);
            }
        }
    });
    //content from texts
    $("table#accessAndServicesTable" + currentTab + " input[type='text']").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        if ($(this).attr("id") == "textOpeningTimes"
                || $(this).attr("id") == "linkOpeningTimes"
                || $(this).attr("id") == "textClosingDates"
                || $(this).attr("id") == "textTravelLink"
                || $(this).attr("id") == "textASAccessRestrictions"
                || $(this).attr("id") == "textASTOULink"
                || $(this).attr("id") == "textASAccessibility"
                || $(this).attr("id") == "textDescriptionOfYourComputerPlaces"
                || $(this).attr("id") == "textASSRReadersTicket"
                || $(this).attr("id") == "textASSRRTLink"
                || $(this).attr("id") == "textASSRAdvancedOrders"
                || $(this).attr("id") == "textASSRAOLink"
                || $(this).attr("id") == "textASSRResearchServices"
                || $(this).attr("id") == "textASDescription"
                || $(this).attr("id") == "textASTSDescriptionOfRestaurationLab"
                || $(this).attr("id") == "textASTSDescriptionOfReproductionService"
                || $(this).attr("id") == "textASReSeExhibition"
                || $(this).attr("id") == "textASReSeWebpage"
                || $(this).attr("id") == "textASReSeWebpageLinkTitle"
                || $(this).attr("id") == "textASReSeToursAndSessions"
                || $(this).attr("id") == "textASReSeTSWebpage"
                || $(this).attr("id") == "textASReSeWebpageTSLinkTitle"
                || $(this).attr("id") == "textASReSeOtherServices"
                || $(this).attr("id") == "textASReSeOSWebpage"
                || $(this).attr("id") == "textASReSeWebpageOSLinkTitle"
                || $(this).attr("id") == "textASReSeRefreshment") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim(escapeApostrophe($(this))) + "'";
        } else {
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        }

        // Check fill mandatory fields.
        if ($.trim($(this).attr("value")) != '') {
            var position = $.inArray($(this).attr("id"), aasMandatoryElements);
            if (position != -1) {
                aasMandatoryElements.splice(position, 1);
            }
        }
    });
    var asOpeningHoursUrlCheck = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='linkOpeningTimes_']").each(function () {
            var result = checkWebpages($(this), messageLink);
            if (!asOpeningHoursUrlCheck) {
                asOpeningHoursUrlCheck = result;
            }
    });
    var failLinkCheck = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textTravelLink_']").each(function () {
        var result = checkWebpages($(this), messageLink);
        if (!failLinkCheck) {
            failLinkCheck = result;
        }
    });
    var failTermCheck = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASTOULink_']").each(function () {
        var result = checkWebpages($(this), messageLink);
        if (!failTermCheck) {
            failTermCheck = result;
        }
    });
    var failReaderCheck = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASSRRTLink_']").each(function () {
        var result = checkWebpages($(this), messageLink);
        if (!failReaderCheck) {
            failReaderCheck = result;
        }
    });
    var failFurtherCheck = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASSRAOLink_']").each(function () {
        var result = checkWebpages($(this), messageLink);
        if (!failFurtherCheck) {
            failFurtherCheck = result;
        }
    });
    var failwebpageSearchroom = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASSRWebpage']").each(function () {
        if ($(this).attr("id").indexOf("textASSRWebpageLinkTitle") == "-1") {
            failwebpageSearchroom = checkWebpages($(this), messageLink);
        }
    });
    var failwebpageInternetAccess = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASLWebpage']").each(function () {
        if ($(this).attr("id").indexOf("textASLWebpageLinkTitle") == "-1") {
            failwebpageInternetAccess = checkWebpages($(this), messageLink);
        }
    });
    var failwebpageDescription = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASRSWebpage']").each(function () {
        if ($(this).attr("id").indexOf("textASRSWebpageLinkTitle") == "-1") {
            failwebpageDescription = checkWebpages($(this), messageLink);
        }
    });
    var failwebpageTechnical = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASTSRSWebpage']").each(function () {
        if ($(this).attr("id").indexOf("textASTSRSWebpageLinkTitle") == "-1") {
            failwebpageTechnical = checkWebpages($(this), messageLink);
        }
    });
    var failwebpageExhibition = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASReSeWebpage_']").each(function () {
        var result = checkWebpages($(this), messageLink);
        if (failwebpageExhibition) {
            failwebpageExhibition = result;
        }
    });
    var failwebpageToursSession = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASReSeTSWebpage_']").each(function () {
        var result = checkWebpages($(this), messageLink);
        if (failwebpageToursSession) {
            failwebpageToursSession = result;
        }
    });
    var failwebpageServices = false;
    $("table#accessAndServicesTable" + currentTab + " textarea[id^='textASReSeOSWebpage_']").each(function () {
        var result = checkWebpages($(this), messageLink);
        if (failwebpageServices) {
            failwebpageServices = result;
        }
    });

    //content from selects
    $("table#accessAndServicesTable" + currentTab + " select").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':') {
            jsonData += ",";
        }
        if ($(this).attr("id)") == "selectASSRPhotographAllowance") {
            jsonData += "'" + $(this).attr("id") + "' : '" + $(this).text() + "'";
        } else if ($(this).attr("id") == "selectLanguageOpeningTimes"
                || $(this).attr("id") == "selectLanguageClosingDates"
                || $(this).attr("id") == "selectASATDSelectLanguage"
                || $(this).attr("id") == "selectASARSelectLanguage"
                || $(this).attr("id") == "selectASAFTOUSelectLanguage"
                || $(this).attr("id") == "selectASASelectLanguage"
                || $(this).attr("id") == "selectDescriptionOfYourComputerPlaces"
                || $(this).attr("id") == "selectReadersTickectLanguage"
                || $(this).attr("id") == "selectASSRAFOIUSelectLanguage"
                || $(this).attr("id") == "textASSRRSSelectLanguage"
                || $(this).attr("id") == "selectASDSelectLanguage"
                || $(this).attr("id") == "selectASTSSelectLanguage"
                || $(this).attr("id") == "selectASTSRSSelectLanguage"
                || $(this).attr("id") == "selectASReSeExhibitionSelectLanguage"
                || $(this).attr("id") == "selectASReSeToursAndSessionsSelectLanguage"
                || $(this).attr("id") == "selectASReSeOtherServicesSelectLanguage"
                || $(this).attr("id") == "selectASReSeRefreshmentSelectLanguage") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim($(this).attr("value")) + "'";
        } else {
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        }
    });
    jsonData += "}";

    for (var i = 0; i < aasMandatoryElements.length; i++) {
        var pFieldError = "<p id=\"" + aasMandatoryElements[i] + "_required\" class=\"fieldRequired\">" + text1 + "</p>";
        $("table#accessAndServicesTable" + currentTab + " #" + aasMandatoryElements[i]).after(pFieldError);
    }

    if (aasMandatoryElements.length != 0 || asOpeningHoursUrlCheck || failLinkCheck || failTermCheck || failReaderCheck || failFurtherCheck || failwebpageSearchroom || failwebpageInternetAccess || failwebpageDescription || failwebpageTechnical || failwebpageExhibition || failwebpageToursSession || failwebpageServices) {
        return false;
    }

    return jsonData;
}
;

var clickDescriptionAction = function (text1) {
    var currentTab = getCurrentTab();

    return checkDescriptionTab(currentTab, text1);
};

function checkAllDescriptionTabs(text1) {
    var counter = $("table[id^='descriptionTable_']").length;
    var jsonData = "{";

    for (var i = 1; i <= counter; i++) {
        if (jsonData.substring(jsonData.length - 1) == '}') {
            jsonData += ",";
        }

        jsonData += "'descriptionTable_" + i + "':";

        jsonData += checkDescriptionTab("_" + i, text1);
    }

    jsonData += "}";

    return jsonData;
}
;

function checkDescriptionTab(currentTab, text1) {
    var jsonData = "{";
    //content from textareas
    $("table#descriptionTable" + currentTab + " textarea").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        if ($(this).attr("id") == "textRepositoryHistory"
                || $(this).attr("id") == "textUnitOfAdministrativeStructure"
                || $(this).attr("id") == "textBuilding"
                || $(this).attr("id") == "textArchivalAndOtherHoldings"
                || $(this).attr("id") == "textRuleOfRepositoryFoundation"
                || $(this).attr("id") == "textRuleOfRepositorySuppression") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim(escapeApostrophe($(this))) + "'";
        } else {
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        }
    });
    //content from texts
    var jsonDataYear = "";
    $("table#descriptionTable" + currentTab + " input[type='text']").each(function () {
        if ($(this).attr("id") == "textDateOfRepositoryFoundation"
                || $(this).attr("id") == "textDateOfRepositorySuppression"
                || $(this).attr("id").indexOf("textYearWhenThisNameWasUsed") != "-1"
                || $(this).attr("id").indexOf("textYearWhenThisNameWasUsedFrom") != "-1"
                || $(this).attr("id").indexOf("textYearWhenThisNameWasUsedTo") != "-1") {
            if (jsonDataYear.length > 1) {
                jsonDataYear += ",";
            }
            jsonDataYear += "'" + $(this).attr("id") + "' : '" + $.trim(escapeDate($(this))) + "'";
        } else {
            if (jsonData.length > 1) {
                jsonData += ",";
            }

            if ($(this).attr("id") == "textRuleOfRepositoryFoundation"
                    || $(this).attr("id") == "textRuleOfRepositorySuppression") {
                jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim(escapeApostrophe($(this))) + "'";
            } else {
                jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
            }
        }
    });
    jsonData += "," + jsonDataYear;

    //content from selects
    $("table#descriptionTable" + currentTab + " select").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':') {
            jsonData += ",";
        }
        if ($(this).attr("id") == "selectLanguageRepositoryHistory"
                || $(this).attr("id") == "selectLanguageUnitOfAdministrativeStructure"
                || $(this).attr("id") == "selectLanguageBuilding"
                || $(this).attr("id") == "selectLanguageArchivalAndOtherHoldings"
                || $(this).attr("id") == "selectLanguageRuleOfRepositoryFoundation"
                || $(this).attr("id") == "selectLanguageRuleOfRepositorySuppression") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim($(this).attr("value")) + "'";
        } else {
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        }
    });
    jsonData += "}";

    return jsonData;
}

var clickControlAction = function (text1) {
    // Delete old checks
    deleteChecks();

    var jsonData = "{";
    //contents from textarea
    $("table#controlTable textarea").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        if ($(this).attr("id") == "textContactFullName") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim(escapeApostrophe($(this))) + "'";
        } else {
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        }
    });
    //content from texts
    $("table#controlTable input[type='text']").each(function () {
        if (jsonData.length > 1) {
            jsonData += ",";
        }
        if ($(this).attr("id") == "textContactAbbreviation") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim(escapeApostrophe($(this))) + "'";
        } else {
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        }
    });
    //content from selects
    $("table#controlTable select").each(function () {
        if (jsonData.charAt(jsonData.length - 1) != ':') {
            jsonData += ",";
        }
        if ($(this).attr("id") == "selectDescriptionLanguage") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim($(this).attr("value")) + "'";
        } else if ($(this).attr("id") == "selectDescriptionScript") {
            jsonData += "'" + $(this).attr("id") + "_1' : '" + $.trim($(this).attr("value")) + "'";
        } else {
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        }
    });
    jsonData += "}";

    return jsonData;
};

var clickRelationsAction = function (text1, messageWebpage) {
    // Delete old checks
    deleteChecks();

    var jsonData = "{";

    // Resource relations.
    var resourceRelations = new Array();
    $("table[id^='resourceRelationTable_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        resourceRelations.push(id);
    });
    jsonData += "'resourceRelations':{";
    for (var j = 0; j < resourceRelations.length; j++) {
        if (jsonData.substring(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + resourceRelations[j] + "':{";
        //input type text
        $("#" + resourceRelations[j] + " textarea").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        });
        //select options selected
        $("#" + resourceRelations[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        });
        jsonData += "}";
    }

    var failWebpageCheck = false;
    $("table[id^='resourceRelationTable_']").each(function () {
        $("table#" + $(this).attr("id") + " textarea[id^='textWebsiteOfResource']").each(function () {
            var result = checkWebpages($(this), messageWebpage);
            if (!failWebpageCheck) {
                failWebpageCheck = result;
            }
        });
    });

    jsonData += "}";

    // Institution relations.
    var institutionRelations = new Array();
    $("table[id^='institutionRelationTable_']").each(function () {
        var id = $(this).attr("id");
        if (id.indexOf("#") > -1) {
            id = id.substring(id.indexOf("#"));
        }
        institutionRelations.push(id);
    });
    jsonData += ",'institutionRelations':{";
    for (var j = 0; j < institutionRelations.length; j++) {
        if (jsonData.substring(jsonData.length - 1) != '{') {
            jsonData += ",";
        }
        jsonData += "'" + institutionRelations[j] + "':{";
        //input type text
        $("#" + institutionRelations[j] + " textarea").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':'
                    && jsonData.charAt(jsonData.length - 1) != '{') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim(escapeApostrophe($(this))) + "'";
        });
        //select options selected
        $("#" + institutionRelations[j] + " select").each(function () {
            if (jsonData.charAt(jsonData.length - 1) != ':') {
                jsonData += ",";
            }
            jsonData += "'" + $(this).attr("id") + "' : '" + $.trim($(this).attr("value")) + "'";
        });
        jsonData += "}";
    }

    /* Commented due to issue #561 specified that this field will be a complete webpage or a
     * <recordId> of an EAG file within the APE. 
     var failInstitutionWebpageCheck = false;
     $("table[id^='institutionRelationTable_']").each(function(){
     $("table#" + $(this).attr("id") + " input[id^='textWebsiteOfDescription']").each(function(){
     var result = checkWebpages($(this),messageWebpage);
     if (!failInstitutionWebpageCheck) {
     failInstitutionWebpageCheck = result;
     }
     });
     });*/

    jsonData += "}}";
    if (failWebpageCheck/* || failInstitutionWebpageCheck*/) {
        return false;
    }
    return jsonData;
};

function checkAndShowPreviousTab(table, text1, text2, messageWebpage) {
    //Check table passed.
    var id = $(table).attr("id");

    if (id == "relationsOtherTable") {
        if (!clickRelationsAction(text1, messageWebpage)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-control']").trigger('click');
        }
    } else if (id == "controlTable") {
        if (!clickControlAction(text1)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-description']").trigger('click');
        }
    } else if (id.indexOf("descriptionTable") != -1) {
        if (!clickDescriptionAction(text1)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-accessAndServices']").trigger('click');
        }
    } else if (id.indexOf("accessAndServicesTable") != -1) {
        if (!clickAccessAndServicesAction(text1, messageWebpage)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
        }
    } else if (id.indexOf("contactTable") != -1) {
        if (!clickContactAction(text1, messageWebpage)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-identity']").trigger('click');
        }
    } else if (id == "identitySelectTypeOfTheInstitution") {
        if (!clickIdentityAction(text1)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-yourInstitution']").trigger('click');
        }
    }
}
function checkAndShowNextTab(table, text1, text2, messageRightWeb) {
    // Check table passed.
    var id = $(table).attr("id");

    if (id == "yiTableOthers") {
        if (!clickYourInstitutionAction(text1, messageRightWeb)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-identity']").trigger('click');
        }
    } else if (id == "identitySelectTypeOfTheInstitution") {
        if (!clickIdentityAction(text1)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
        }
    } else if (id.indexOf("contactTable") != -1) {
        if (!clickContactAction(text1, messageRightWeb)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-accessAndServices']").trigger('click');
        }
    } else if (id.indexOf("accessAndServicesTable") != -1) {
        if (!clickAccessAndServicesAction(text1, messageRightWeb)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-description']").trigger('click');
        }
    } else if (id.indexOf("descriptionTable") != -1) {
        if (!clickDescriptionAction(text1)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-control']").trigger('click');
        }
    } else if (id == "controlTable") {
        if (!clickControlAction(text1)) {
            alertFillFieldsBeforeChangeTab(text2);
            return;
        } else {
            $("ul#eag2012TabsContainer a[href='#tab-relations']").trigger('click');
        }
    }
}

function checkFillValue(element) {
    if (($(element).attr("value") != null && $(element).attr("value") != "")
            || ($(element).attr("value") != 'none')) {
        return true;
    }
}

function yiAddVisitorsAddressTranslation(text1) {
    var counter = $("table[id^='yiTableVisitorsAddress_']").length;

    var street = $("table#yiTableVisitorsAddress_" + counter + " textarea#textYIStreet").attr("value");
    var city = $("table#yiTableVisitorsAddress_" + counter + " textarea#textYICity").attr("value");
    var country = $("table#yiTableVisitorsAddress_" + counter + " textarea#textYICountry").attr("value");
    var latitude = $("table#yiTableVisitorsAddress_" + counter + " input#textYILatitude").attr("value");
    var longitude = $("table#yiTableVisitorsAddress_" + counter + " input#textYILongitude").attr("value");

    if (street != null && street != ""
            && city != null && city != "" && country != null && country != "") {
        var clone = $("table[id^='yiTableVisitorsAddress_" + counter + "']").clone();
        clone = "<table id='" + ("yiTableVisitorsAddress_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
        $("table[id^='yiTableVisitorsAddress_" + counter + "']").after(clone);
        // Reset parametters.
        $("table#yiTableVisitorsAddress_" + (counter + 1) + " input[type='text']").each(function () {
            var current = $(this).attr("id");
            if (current != "textYILatitude" && current != "textYILongitude") {
                $(this).val(""); // Clean all input_text different from latitude or longitude
            } else if (current == "textYILatitude") {
                $(this).val(latitude);
                $("input#textYILatitude").attr("disabled", "disabled");
            } else if (current == "textYILongitude") {
                $(this).val(longitude);
                $("input#textYILongitude").attr("disabled", "disabled");
            }
        });
        $("table#yiTableVisitorsAddress_" + (counter + 1) + " textarea").each(function () {
            $(this).val(""); // Clean all input_text.
        });
        $("table#yiTableVisitorsAddress_" + (counter + 1) + " #selectYIVASelectLanguage").attr("value", "none");
        // Remove "*".
        $("table#yiTableVisitorsAddress_" + (counter + 1)).find("span").remove();
        $("table#yiTableVisitorsAddress_" + (counter + 1)).find("p").remove();
        $("table#yiTableVisitorsAddress_1 input#textYILatitude").removeAttr("disabled");
        $("table#yiTableVisitorsAddress_1 input#textYILongitude").removeAttr("disabled");

    } else {
        alertEmptyFields(text1);
    }
}

function yiAddPostalAddressTranslation(text1) {
    var counter = $("table[id^='yiTablePostalAddress_']").length;
    var street = $("table#yiTablePostalAddress_" + counter + " textarea#textYIPAStreet").attr("value");
    var city = $("table#yiTablePostalAddress_" + counter + " textarea#textYIPACity").attr("value");

    if (street != null && street != ""
            && city != null && city != "") {
        var clone = $("table[id^='yiTablePostalAddress_" + counter + "']").clone();
        clone = "<table id='" + ("yiTablePostalAddress_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
        $("table[id^='yiTablePostalAddress_" + counter + "']").after(clone);
        // Reset parametters.
        $("table#yiTablePostalAddress_" + (counter + 1) + " textarea").each(function () {
            $(this).val(""); // Clean all input_text.
        });
        $("table#yiTablePostalAddress_" + (counter + 1) + " #selectYIPASelectLanguage").attr("value", "none");
        // Remove "*".
        $("table#yiTablePostalAddress_" + (counter + 1)).find("span").remove();
        $("table#yiTablePostalAddress_" + (counter + 1)).find("p").remove();
    } else {
        alertEmptyFields(text1);
    }
}

function yiFutherAccessInformation() {
    $("#buttonFutherAccessInformation").remove();
    $("#orangeLineFurtherAccessInformation").remove();
    $("#trYIButtonFutherAccessInformation").show();
    $("#buttonFutherAccessInformation2").show();
    $("#orangeLineFutherAccessInformation2").show();
}
function yiFutherAccessInformation2(text1) {
    var count = $("tr[id^='trYIButtonFutherAccessInformation']").length;
    var target = null;
    if (count == 1) {
        target = "#trYIButtonFutherAccessInformation";
    } else {
        target = "#trYIButtonFutherAccessInformation_" + count;
    }

    var wrongField = false;
    var furtherValue = $(target).find("textarea[id^='futherAccessInformation']").val();
    /* var furtherSelect = null;
     if(count>1){
     furtherSelect = $("#selectFutherAccessInformation_"+count).attr("value");
     }else{
     furtherSelect = $("#selectFutherAccessInformation").attr("value");
     } */
    if (furtherValue.length <= 0/* || furtherSelect=="none"*/) {
        wrongField = true;
    }

    var clone = $(target).clone();
    clone.find("[id^='futherAccessInformation']").attr("id", "futherAccessInformation_" + (count + 1));
    clone.find("[id^='selectFutherAccessInformation']").attr("id", "selectFutherAccessInformation_" + (count + 1));
    clone.find("[id^='selectFutherAccessInformation']").removeAttr("onchange");
    clone.attr("id", "trYIButtonFutherAccessInformation_" + (count + 1));
    clone.find("[id^='futherAccessInformation']").attr("value", "");
    clone.find("[id^='futherAccessInformation']").removeAttr("onchange");
    clone.find("[id^='selectFutherAccessInformation']").attr("value", "none");
    clone.find("[id^='selectFutherAccessInformation']").removeAttr("onchange");
    if (wrongField) {
        displayAlertDialog(text1);
    } else {
        $(target).after(clone);
    }
}
function yiAddFutherInformationOnExistingFacilities() {
    $("tr#trButtonAddFutherInformationOnExistingFacilities").show();
    $("input#buttonAddFutherInformationOnExistingFacilities").hide();
    $("input#buttonAddFutherInformationOnExistingFacilities2").show();
}
function yiAddFutherInformationOnExistingFacilities2(text1) {
    var count = $("tr[id^='trButtonAddFutherInformationOnExistingFacilities']").length;
    var target = null;
    if (count == 1) {
        target = "tr#trButtonAddFutherInformationOnExistingFacilities";
    } else {
        target = "tr#trButtonAddFutherInformationOnExistingFacilities_" + count;
    }

    var wrongField = false;
    var furtherValue = $(target).find("textarea[id^='futherInformationOnExistingFacilities']").val();
    /* var furtherSelect = null;
     if(count>1){
     furtherSelect = $("#selectFutherAccessInformationOnExistingFacilities_"+count).attr("value");
     }else{
     furtherSelect = $("#selectFutherAccessInformationOnExistingFacilities").attr("value");
     } */
    if (furtherValue.length <= 0/* || furtherSelect=="none"*/) {
        wrongField = true;
    }

    var clone = $(target).clone();
    clone.find("[id^='futherInformationOnExistingFacilities']").attr("id", "futherInformationOnExistingFacilities_" + (count + 1));
    clone.find("[id^='selectFutherAccessInformationOnExistingFacilities']").attr("id", "selectFutherAccessInformationOnExistingFacilities_" + (count + 1));
    clone.attr("id", "trButtonAddFutherInformationOnExistingFacilities_" + (count + 1));
    clone.find("[id^='futherInformationOnExistingFacilities']").attr("value", "");
    clone.find("[id^='futherInformationOnExistingFacilities']").removeAttr("onchange");
    clone.find("[id^='selectFutherAccessInformationOnExistingFacilities']").attr("value", "none");
    clone.find("[id^='selectFutherAccessInformationOnExistingFacilities']").removeAttr("onchange");
    if (wrongField) {
        displayAlertDialog(text1);
    } else {
        $(target).after(clone);
    }
}
function yiAddReferencetoyourinstitutionsholdingsguide(text1) {
    var count = $("table#yiTableOthers tr[id^='trYIReferencetoHoldingsguide']").length;
    var clone = null;
    var clone2 = null;
    var target = null;
    var target2 = null;
    if (count == 1) {
        target = "table#yiTableOthers tr#trYIReferencetoHoldingsguide";
        target2 = "table#yiTableOthers tr#trYIReferenceto2Holdingsguide";
    } else {
        target = "table#yiTableOthers tr#trYIReferencetoHoldingsguide_" + count;
        target2 = "table#yiTableOthers tr#trYIReferenceto2Holdingsguide_" + count;
    }
    clone = $(target).clone();
    clone2 = $(target2).clone();
    clone.find("[id^='textReferencetoyourinstitutionsholdingsguide']").attr("id", "textReferencetoyourinstitutionsholdingsguide_" + (count + 1));
    clone.find("[id^='textYIHoldingsGuideLinkTitle']").attr("id", "textYIHoldingsGuideLinkTitle_" + (count + 1));
    clone.find("[id^='textYIEmailAddress']").attr("id", "textYIEmailAddress_" + (count + 1));
    clone.attr("id", "trYIReferencetoHoldingsguide_" + (count + 1));
    clone2.attr("id", "trYIReferenceto2Holdingsguide_" + (count + 1));
    clone2.find("[id^='selectYIReferencetoHoldingsguide']").attr("id", "selectYIReferencetoHoldingsguide_" + (count + 1));
    var wrongField = false;
    var title = $("#textYIHoldingsGuideLinkTitle" + ((count > 1) ? ("_" + count) : "")).val();
    // var select = $("#selectYIReferencetoHoldingsguide"+((count>1)?("_"+count):"")).val();
    var link = $("#textReferencetoyourinstitutionsholdingsguide" + ((count > 1) ? ("_" + count) : "")).val();
    if (title.length <= 0 /*|| select=="none" */ && link.length <= 0) {
        wrongField = true;
    }
    clone.find("[id^='textReferencetoyourinstitutionsholdingsguide']").removeAttr("onchange");
    clone.find("[id^='textYIHoldingsGuideLinkTitle']").attr("value", "");
    clone.find("[id^='textYIHoldingsGuideLinkTitle']").removeAttr("onchange");
    clone2.find("[id^='selectYIReferencetoHoldingsguide']").attr("value", "none");
    if (wrongField) {
        displayAlertDialog(text1);
    } else {
        $(target2).after(clone2);
        $(target2).after(clone);
    }
}

function addFurtherIds(text1, text2, text3, text4, text5) {
    var counter = $("textarea[id^='otherRepositorId']").length;
    var select = '<select id="selectOtherRepositorIdCodeISIL_' + ($("textarea[id^='otherRepositorId']").length) + '" onclick="codeISILChanged(\'' + text5 + '\',' + ($("textarea[id^='otherRepositorId']").length) + ');">' + $("#selectYICodeISIL").html() + '</select>';

    if (counter == 0) {
        $("input#buttonAddFutherIds").parent().parent().before("<tr><td><label for=\"otherRepositorId_" + ($("textarea[id^='otherRepositorId'] ").length) + "\" > " + text1 + ":</label></td><td><textarea id=\"otherRepositorId_" + ($("textarea[id^='otherRepositorId']").length) + "\" onclick=\"idOfInstitutionChanged('" + text4 + "','" + text5 + "','" + ($("textarea[id^='otherRepositorId']").length) + "');\" onkeyup=\"idOfInstitutionChanged('" + text4 + "','" + text5 + "','" + ($("textarea[id^='otherRepositorId']").length) + "');\"></textarea></td><td class=\"labelLeft\">" + text3 + "</td><td>" + select + "</td></tr>");
        $("select#selectOtherRepositorIdCodeISIL_" + counter).attr("value", "no");
    } else {
        var value = $("textarea#otherRepositorId_" + (counter - 1)).attr("value");

        if (value != null && value != "") {
            $("input#buttonAddFutherIds").parent().parent().before("<tr><td><label for=\"otherRepositorId_" + ($("textarea[id^='otherRepositorId']").length) + "\"> " + text1 + ":</label></td><td><textarea id=\"otherRepositorId_" + ($("textarea[id^='otherRepositorId']").length) + "\" onclick=\"idOfInstitutionChanged('" + text4 + "','" + text5 + "','" + ($("textarea[id^='otherRepositorId']").length) + "');\" onkeyup=\"idOfInstitutionChanged('" + text4 + "','" + text5 + "','" + ($("textarea[id^='otherRepositorId']").length) + "');\"></textarea></td><td class=\"labelLeft\">" + text3 + "</td><td>" + select + "</td></tr>");
            $("select#selectOtherRepositorIdCodeISIL_" + counter).attr("value", "no");
        } else {
            alertEmptyFields(text2);
        }
    }
    if ($("textarea[id^='otherRepositorId']").length > 0) {
        if ($("#selectYICodeISIL").val() == "yes") {
            $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                $(this).attr("disabled", "disabled");
            });
        } else {
            var stop = -1;
            $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function (i, v) {
                if ($(this).val() == "yes") {
                    stop = i;
                    $("#selectYICodeISIL").attr("disabled", "disabled");
                }
            });
            $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function (i, v) {
                if (stop > -1 && stop != i) {
                    $(this).attr("disabled", "disabled");
                } else {
                    $(this).removeAttr("disabled");
                }
            });
        }
    }
}

function showTabOfFirstInstitution() {
    hideAndShow("tab-", "tab-yourInstitution");
}

function loadRepositories(text1, text2, number) {
    var count = parseInt(number);
    if (count > 1) {
        for (var i = 0; i < count; i++) {
            var localId = "yourInstitutionTable_" + (i + 1);
            if (i == 0) {
                $("#eag2012tabs_institution_tabs").append("<li onclick=\"showTabOfFirstInstitution();\"><a id=\"tab_" + localId + "\" href=\"#repositories\" >" + text1 + "</a></li>");
            } else {
                $("#eag2012tabs_institution_tabs").append("<li><a id=\"tab_" + localId + "\" href=\"#repositories\" >" + text2 + " " + (i) + "</a></li>");
            }
        }

        var localId = "yourInstitutionTable_1";
        $("table#" + localId).show();
        $("a[id^='tab_']").click(function () {
            $("a[id^='tab_']").each(function () {
                $(this).removeClass("eag2012currenttab");
            });
            $(this).addClass("eag2012currenttab");
            var localId = $(this).attr("id");
            if (localId.indexOf("tab_") == 0) {
                localId = localId.substring("tab_".length);
            }
            $("table[id^='yourInstitutionTable_']").hide();
            $("table#" + localId).show();
            //show/hide right tabs and content

            var pos = localId.substring(localId.indexOf("_1"));
            if ((localId.indexOf("_1") > -1) && (pos.length < 3)) {
                $("ul#eag2012TabsContainer li a").each(function () {
                    var id = $(this).parent().attr("id");
                    if (id.indexOf("tab-yourInstitution") > -1) {
                        $(this).removeClass("eag2012disabled");
                    } else if (id.indexOf("tab-identity") > -1) {
                        $(this).removeClass("eag2012disabled");
                    } else if (id.indexOf("tab-control") > -1) {
                        $(this).removeClass("eag2012disabled");
                    } else if (id.indexOf("tab-relations") > -1) {
                        $(this).removeClass("eag2012disabled");
                    }
                });
            } else {
                $("ul#eag2012TabsContainer li a").each(function () {
                    var id = $(this).parent().attr("id");
                    if (id.indexOf("tab-yourInstitution") > -1 || id.indexOf("tab-identity") > -1 || id.indexOf("tab-control") > -1 || id.indexOf("tab-relations") > -1) {
                        $(this).addClass("eag2012disabled");
                    }
                });
                $("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
            }
        });
        $("a#tab_" + localId).trigger('click');
        $("a#tab-yourInstitution").parent().trigger('click');

        // Load the names of repositories as name of repository tabs.
        for (var i = 2; i <= count; i++) {
            var name = $("table#contactTable_" + i + " textarea#textNameOfRepository").val();

            if (name != null && name != "") {
                $("a#tab_yourInstitutionTable_" + i).text(name);
            }
        }
    }
}

function addRepositories(text1, text2, text3, text4, text5, text6, text7, property1, property2, property3, property4, property5) {
    $(document).scrollTop($("#eag2012tabs_institution_tabs").offset().top);
    var counter = $("table[id^='contactTable_']").length;
    var clone = $("table[id='yourInstitutionTable_" + counter + "']").clone();
    clone = "<table id='" + ("yourInstitutionTable_" + (counter + 1)) + "'>" + clone.html() + "</table>";
    $("table[id='yourInstitutionTable_" + counter + "']").after(clone);
    //reset parametters
    $("table#yourInstitutionTable_" + (counter + 1) + " input[type='text']").each(function () {
        $(this).val(""); //clean all input_text
    });
    $("table#yourInstitutionTable_" + (counter + 1) + " textarea").each(function () {
        $(this).val(""); //clean all textarea
    });
    $("table[id^='yourInstitutionTable_']").hide();
    var localId = "";
    if (counter == 1) {
        localId = "yourInstitutionTable_" + counter;
        $("#eag2012tabs_institution_tabs").append("<li onclick=\"showTabOfFirstInstitution();\"><a id=\"tab_" + localId + "\" href=\"#repositories\" >" + text1 + "</a></li>");
    }
    localId = "yourInstitutionTable_" + (counter + 1);
    $("#eag2012tabs_institution_tabs").append("<li><a id=\"tab_" + localId + "\" href=\"#repositories\" >" + text2 + " " + (counter) + "</a></li>");
    //clone and put the 3 new tables
    $("div#tab-contact").append("<table id=\"contactTable_" + (counter + 1) + "\" class=\"tablePadding\">" + $("table#contactTable").clone().html() + "</table>");
    $("table#contactTable_" + (counter + 1) + " input#currentContactNumberTab").attr("value", (counter + 1));
    $("div#tab-accessAndServices").append("<table id=\"accessAndServicesTable_" + (counter + 1) + "\" class=\"tablePadding\">" + $("table#accessAndServicesTable").clone().html() + "</table>");
    $("div#tab-description").append("<table id=\"descriptionTable_" + (counter + 1) + "\" class=\"tablePadding\">" + $("table#descriptionTable").clone().html() + "</table>");

    // Remove attr "disabled" for all elements in "contact" tab.
    $("table#contactTable_" + (counter + 1) + " textarea#textContactStreetOfTheInstitution").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " select#selectLanguageVisitorAddress").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactCityOfTheInstitution").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactCountryOfTheInstitution").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " input#textContactLatitudeOfTheInstitution").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " input#textContactLongitudeOfTheInstitution").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " select#selectContinentOfTheInstitution").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " input#textContactTelephoneOfTheInstitution").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactEmailOfTheInstitution").removeAttr("disabled");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactWebOfTheInstitution").removeAttr("disabled");

    // Remove attr "onchange" for all elements in "contact" tab.
    $("table#contactTable_" + (counter + 1) + " textarea#textContactStreetOfTheInstitution").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " select#selectLanguageVisitorAddress").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactCityOfTheInstitution").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactCountryOfTheInstitution").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " input#textContactLatitudeOfTheInstitution").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " input#textContactLongitudeOfTheInstitution").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " input#textContactTelephoneOfTheInstitution_1").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactEmailOfTheInstitution_1").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " select#selectEmailLanguageOfTheInstitution_1").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactLinkTitleForEmailOfTheInstitution_1").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactWebOfTheInstitution_1").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " textarea#textContactLinkTitleForWebOfTheInstitution_1").removeAttr("onchange");
    $("table#contactTable_" + (counter + 1) + " select#selectWebpageLanguageOfTheInstitution_1").removeAttr("onchange");

    // Add new attr "onchange" for latitude and longitude elements in "contact" tab.
    $("table#contactTable_" + (counter + 1) + " input#textContactLatitudeOfTheInstitution").change(latitudeOfRepoChanged);
    $("table#contactTable_" + (counter + 1) + " input#textContactLongitudeOfTheInstitution").change(longitudeOfRepoChanged);

    // Remove attr "onchange" for all elements in "access and servicess" tab.
    $("table#accessAndServicesTable_" + (counter + 1) + " textarea#textOpeningTimes_1").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " select#selectLanguageOpeningTimes_1").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " textarea#linkOpeningTimes_1").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " textarea#textClosingDates_1").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " select#selectLanguageClosingDates_1").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " select#selectASAccesibleToThePublic").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " textarea#textASAccessRestrictions_1").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " select#selectASARSelectLanguage_1").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " select#selectASFacilitiesForDisabledPeopleAvailable").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " textarea#textASAccessibility_1").removeAttr("onchange");
    $("table#accessAndServicesTable_" + (counter + 1) + " select#selectASASelectLanguage_1").removeAttr("onchange");

    // Remove "Next tab" button from tab "description".
    $("table#descriptionTable_" + (counter + 1) + " td#tdButtonsDescriptionTab #buttonDescriptionTabNext").remove();
    // Remove "Previous tab" button from tab "contact"
    $("table#contactTable_" + (counter + 1) + " td#tdButtonsContactTab #buttonContactTabPrevious").remove();

    //fill values with the current "your institution" values provided by user
    //contact tab
    var selectedIndex = document.getElementById('selectYIContinent').selectedIndex;
    var latitude = $("#textYILatitude").val();
    var longitude = $("#textYILongitude").val();
    var country = $("#textYICountry").val();
    var city = $("#textYICity").val();
    var street = $("#textYIStreet").val();
    var streetLanguage = $("#selectYIVASelectLanguage").val();
    var postalCity = $("#textYIPACity").val();
    var postalStreet = $("#textYIPAStreet").val();
    var postalStreetLanguage = $("#selectYIPASelectLanguage").val();
    var telephone = $("#textYITelephone").val();
    var email = $("#textYIEmailAddress").val();
    var emailLinkTitle = $("#textYIEmailLinkTitle").val();
    var emailLang = $("table#yiTableOthers #selectTextYILangEmail").val();
    var web = $("#textYIWebpage").val();
    var webLinkTitle = $("#textYIWebpageLinkTitle").val();
    var webLang = $("table#yiTableOthers #selectTextYILangWebpage").val();
    //contact table
    $("table#contactTable_" + (counter + 1) + " #selectContinentOfTheInstitution option").eq(selectedIndex).prop("selected", true);
    $("table#contactTable_" + (counter + 1) + " #selectLanguageVisitorAddress").attr("value", streetLanguage);
    $("table#contactTable_" + (counter + 1) + " #textContactLatitudeOfTheInstitution").attr("value", latitude);
    $("table#contactTable_" + (counter + 1) + " #textContactLongitudeOfTheInstitution").attr("value", longitude);
    $("table#contactTable_" + (counter + 1) + " #textContactCountryOfTheInstitution").attr("value", country);
    $("table#contactTable_" + (counter + 1) + " #textContactCityOfTheInstitution").attr("value", city);
    $("table#contactTable_" + (counter + 1) + " #textContactStreetOfTheInstitution").attr("value", street);
    $("table#contactTable_" + (counter + 1) + " #textContactTelephoneOfTheInstitution_1").attr("value", telephone);
    $("table#contactTable_" + (counter + 1) + " #textContactEmailOfTheInstitution_1").attr("value", email);
    $("table#contactTable_" + (counter + 1) + " #textContactLinkTitleForEmailOfTheInstitution_1").attr("value", emailLinkTitle);
    $("table#contactTable_" + (counter + 1) + " #selectEmailLanguageOfTheInstitution").attr("value", emailLang);
    $("table#contactTable_" + (counter + 1) + " #textContactWebOfTheInstitution_1").attr("value", web);
    $("table#contactTable_" + (counter + 1) + " #textContactLinkTitleForWebOfTheInstitution_1").attr("value", webLinkTitle);
    $("table#contactTable_" + (counter + 1) + " #selectWebpageLanguageOfTheInstitution_1").attr("value", webLang);
    if ((postalCity != null && postalCity != "")
            || (postalStreet != null && postalStreet != "")
            || (postalStreetLanguage != null && postalStreetLanguage != "")) {
        var selectContactLanguagePostalAddress = '<select id="selectContactLanguagePostalAddress">' + $("table#contactTable_" + (counter + 1) + " #selectLanguageVisitorAddress").html() + '</select>';

        $("table#contactTable_" + (counter + 1) + " input#buttonContactAddPostalAddressIfDifferent").hide();

        $("table#contactTable_" + (counter + 1) + " tr#trButtonContactAddPostalAddressIfDifferent").after('<tr><td><table id="contactTablePostalAddress_1">' +
                '<tr id="trContactPostalAddressLabel">' +
                '<td id="postalAddressLabel" colspan="4"><label>' + property1 + '</label>' +
                '</td>' +
                '</tr>' +
                '<tr id="contactPostalAddressStreet">' +
                '<td>' +
                '<label for="textContactPAStreet">' + property3 + '<span class="required">*</span>:</label>' +
                '</td>' +
                '<td colspan="2" class="textContact">' +
                '<textarea id="textContactPAStreet"></textarea>' +
                '</td>' +
                '<td id="contactPostalAddressLanguage">' +
                '<label class="language" for="selectContactLanguagePostalAddress">' + property2 + ':</label>' +
                selectContactLanguagePostalAddress +
                '</td>' +
                '</tr>' +
                '<tr id="contactPostalAddressCity">' +
                '<td>' +
                '<label for="textContactPACity">' + property4 + '<span class="required">*</span>:</label>' +
                '</td>' +
                '<td colspan="2" class="textContact">' +
                '<textarea id="textContactPACity"></textarea>' +
                '</td>' +
                '</tr></table></td></tr>');

        $("table#contactTable_" + (counter + 1) + " tr#trButtonContacPostalAddressTranslation").show();
        //	$("table#contactTable"+(counter+1)+" tr#orangeLineContacPostalAddressTranslation").show();
        $("table#contactTable_" + (counter + 1) + " table#contactTablePostalAddress_1 #selectContactLanguagePostalAddress").attr("value", "none");

        $("table#contactTable_" + (counter + 1) + " #textContactPACity").attr("value", postalCity);
        $("table#contactTable_" + (counter + 1) + " #textContactPAStreet").attr("value", postalStreet);
        $("table#contactTable_" + (counter + 1) + " #selectContactLanguagePostalAddress").attr("value", postalStreetLanguage);
    }

    // add name of repository to contact tab and the button Delete repository.
    $("table#contactTable_" + (counter + 1) + " tr#trVisitorsAddressLabel").before("<tr>" +
            "<td id=\"tdNameOfRepository\">" +
            "<label for=\"textNameOfRepository\">" + text3 + "<span class=\"required\">*</span>:</label>" +
            "</td>" +
            "<td>" +
            "<textarea id=\"textNameOfRepository\" onchange=\"contactChangeTabName('" + text2 + "');\"></textarea>" +
            "<td>" +
            "<label for=\"selectRoleOfRepository\">" + text4 + "<span class=\"required\">*</span>:</label>" +
            "</td>" +
            "<td>" +
            "<select id=\"selectRoleOfRepository\">" +
            "<option value=\"none\">---</option>" +
            "<option value=\"headquarters\">" + text5 + "</option>" +
            "<option value=\"branch\">" + text6 + "</option>" +
            "<option value=\"interim\">" + text7 + "</option>" +
            "</select>" +
            "</td></tr>" + "<tr>" + "<td colspan=\"2\">" + "<input type=\"button\" id=\"buttonDeleteRepository\" value=\"" + property5 + "\" onclick=\"deleteRepository();\">" + "</td></tr>");

    //access and services
    var openingContent = $("#textYIOpeningTimes").val();
    var openingLang = $("#selectTextYIOpeningTimes").val();
    var openingHref = $("#linkYIOpeningTimes").val();
    var closing = $("#yourInstitutionClosingDates").val();
    var closingLang = $("#selectTextYIClosingTimes").val();
    var accessPublicQuestion = document.getElementById('selectAccessibleToThePublic').selectedIndex;
    var accessPublicValue = $("#futherAccessInformation").val();
    var accessPublicLang = $("#selectFutherAccessInformation").val();
    var accessibilityDisabledPeopleQuestion = $("#selectFacilitiesForDisabledPeopleAvailable").val();
    var accessibilityDisabledPeopleValue = $("#futherInformationOnExistingFacilities").val();
    var accessibilityDisabledPeopleLang = $("#selectFutherAccessInformationOnExistingFacilities").val();
    $("table#accessAndServicesTable_" + (counter + 1) + " #textOpeningTimes_1").attr("value", openingContent);
    $("table#accessAndServicesTable_" + (counter + 1) + " #selectLanguageOpeningTimes_1").attr("value", openingLang);
    $("table#accessAndServicesTable_" + (counter + 1) + " #linkOpeningTimes_1").attr("value", openingHref);
    $("table#accessAndServicesTable_" + (counter + 1) + " #textClosingDates_1").attr("value", closing);
    $("table#accessAndServicesTable_" + (counter + 1) + " #selectLanguageClosingDates_1").attr("value", closingLang);
    $("table#accessAndServicesTable_" + (counter + 1) + " #selectASAccesibleToThePublic option").eq(accessPublicQuestion).prop("selected", true);
    $("table#accessAndServicesTable_" + (counter + 1) + " #textASAccessRestrictions_1").attr("value", accessPublicValue);
    $("table#accessAndServicesTable_" + (counter + 1) + " #selectASARSelectLanguage_1").attr("value", accessPublicLang);
    $("table#accessAndServicesTable_" + (counter + 1) + " #selectASFacilitiesForDisabledPeopleAvailable").attr("value", accessibilityDisabledPeopleQuestion);
    $("table#accessAndServicesTable_" + (counter + 1) + " #textASAccessibility_1").attr("value", accessibilityDisabledPeopleValue);
    $("table#accessAndServicesTable_" + (counter + 1) + " #selectASASelectLanguage_1").attr("value", accessibilityDisabledPeopleLang);
    $("table#" + localId).show();
    $("a[id^='tab_']").click(function () {
        $("a[id^='tab_']").each(function () {
            $(this).removeClass("eag2012currenttab");
        });
        $(this).addClass("eag2012currenttab");
        var localId = $(this).attr("id");
        if (localId.indexOf("tab_") == 0) {
            localId = localId.substring("tab_".length);
        }
        $("table[id^='yourInstitutionTable_']").hide();
        $("table#" + localId).show();

        //show/hide right tabs and content
        var pos = localId.substring(localId.indexOf("_1"));
        if ((localId.indexOf("_1") > -1) && (pos.length < 3)) {
            $("ul#eag2012TabsContainer li a").each(function () {
                var id = $(this).parent().attr("id");
                if (id.indexOf("tab-yourInstitution") > -1) {
                    $(this).removeClass("eag2012disabled");
                } else if (id.indexOf("tab-identity") > -1) {
                    $(this).removeClass("eag2012disabled");
                } else if (id.indexOf("tab-control") > -1) {
                    $(this).removeClass("eag2012disabled");
                } else if (id.indexOf("tab-relations") > -1) {
                    $(this).removeClass("eag2012disabled");
                }
            });
        } else {
            $("ul#eag2012TabsContainer li a").each(function () {
                var id = $(this).parent().attr("id");
                if (id.indexOf("tab-yourInstitution") > -1 || id.indexOf("tab-identity") > -1 || id.indexOf("tab-control") > -1 || id.indexOf("tab-relations") > -1) {
                    $(this).addClass("eag2012disabled");
                }
            });
//			if($("ul#eag2012TabsContainer a .eag2012currenttab").hasClass("eag2012disabled")){
//				$("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
//			}
            $("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
        }
    });
    $("a#tab_" + localId).trigger('click');
    $("a#tab-contact").parent().trigger('click');
    //current tab
    $("table#yourInstitutionTable_" + (counter + 1) + " input#buttonYourInstitutionTabCheck").click(clickYourInstitutionAction);
}
function deleteRepository() {
    var currentTab = getCurrentTab();
    var counter = currentTab.substring(1);
    //Delete the selected repository
    var numberRepositories = $("table[id^='contactTable_']").length;
    var localId = "yourInstitutionTable_" + counter;

    $("li a#tab_" + localId).parent().remove();
    $("table#contactTable_" + counter).remove();
    $("table#accessAndServicesTable_" + counter).remove();
    $("table#descriptionTable_" + counter).remove();
    $("table#yourInstitutionTable_" + counter).remove();
    if (numberRepositories == 2) { //there is one repository
        $("li a#tab_yourInstitutionTable_1").parent().remove();
        $("ul#eag2012TabsContainer li a").each(function () {
            var id = $(this).parent().attr("id");
            if (id.indexOf("tab-yourInstitution") > -1 || id.indexOf("tab-identity") > -1 || id.indexOf("tab-control") > -1 || id.indexOf("tab-relations") > -1) {
                $(this).removeClass("eag2012disabled");
            }
        });
        hideAndShow("tab-", "tab-yourInstitution");
        $("table#yourInstitutionTable_1").show();
    } else {
        var localId = "yourInstitutionTable_" + 1;
        $("table#" + localId).show();
        $("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
        $("a#tab_" + localId).trigger('click');
        $("a#tab-contact").parent().trigger('click');
        //current tab
        $("table#yourInstitutionTable_" + (counter + 1) + " input#buttonYourInstitutionTabCheck").click(clickYourInstitutionAction);

        if (counter < numberRepositories) {
            // There isn't the last repository, so I have to change the identifiers 
            $("ul#eag2012tabs_institution_tabs li a").each(function (i) {
                var newId = "tab_yourInstitutionTable_" + (i + 1);
                $(this).attr("id", newId);
            });
            $("table[id^='yourInstitutionTable_']").each(function (i) {
                var newId = "yourInstitutionTable_" + (i + 1);
                $(this).attr("id", newId);
            });
            $("table[id^='contactTable_']").each(function (i) {
                var newId = "contactTable_" + (i + 1);
                $(this).attr("id", newId);
            });
            $("table[id^='accessAndServicesTable_']").each(function (i) {
                var newId = "accessAndServicesTable_" + (i + 1);
                $(this).attr("id", newId);
            });
            $("table[id^='descriptionTable_']").each(function (i) {
                var newId = "descriptionTable_" + (i + 1);
                $(this).attr("id", newId);
            });
        }
    }
}
function getCurrentTab() {
    var currentTab = "";
    $("ul#eag2012tabs_institution_tabs li a").each(function () {
        if ($(this).hasClass("eag2012currenttab")) {
            currentTab = $(this).attr("id");
            currentTab = currentTab.substring(currentTab.lastIndexOf("_"));
        }
    });
    if (currentTab.length == 0) {
        currentTab = "_1";
    }
    return currentTab;
}

function addYIFurtherEmailsOfTheInstitution(text1) {
    var count = $("table#yiTableOthers tr[id^='trTextYIEmail']").length;
    var clone = null;
    var clone2 = null;
    var target = null;
    var target2 = null;
    if (count == 1) {
        target = "table#yiTableOthers tr#trTextYIEmail";
        target2 = "table#yiTableOthers tr#trTextYILangEmail";
    } else {
        target = "table#yiTableOthers tr#trTextYIEmail_" + count;
        target2 = "table#yiTableOthers tr#trTextYILangEmail_" + count;
    }

    //check values
    var wrongField = false;
    var textC1 = $(target).find("[id^='textYIEmailAddress']").val();
    // var textC2 = $("#selectTextYILangEmail"+((count>1)?("_"+count):"")).val();
    var textC3 = $(target2).find("[id^='textYIEmailLinkTitle']").val();
    if (textC1.length <= 0 /*|| textC2=="none" */ && textC3.length <= 0) {
        wrongField = true;
    }

    clone = $(target).clone();
    clone2 = $(target2).clone();
    clone2.find("[id^='textYIEmailLinkTitle']").attr("id", "textYIEmailLinkTitle_" + (count + 1));
    clone.find("[id^='textYIEmailAddress']").attr("id", "textYIEmailAddress_" + (count + 1));
    clone.find("[id^='selectTextYILangEmail']").attr("id", "selectTextYILangEmail_" + (count + 1));
    clone2.attr("id", "trTextYILangEmail_" + (count + 1));
    clone.attr("id", "trTextYIEmail_" + (count + 1));
    //clone2.attr("id","textYIEmailLinkTitle"+(count+1));
    clone.find("[id^='selectTextYILangEmail']").attr("value", "none");
    clone2.find("[id^='textYIEmailLinkTitle']").attr("value", "");
    if (wrongField) {
        displayAlertDialog(text1);
    } else {
        $(target2).after(clone2);
        $(target2).after(clone);
    }
}
function addYIFurtherWebsOfTheInstitution(text1) {
    var count = $("table#yiTableOthers tr[id^='trButtonYIWebpage']").length;
    var clone = null;
    var target = null;
    var target2 = null;
    if (count == 1) {
        target = "table#yiTableOthers tr#trButtonYIWebpage";
        target2 = "table#yiTableOthers tr#trButtonYILangWebpage";
    } else {
        target = "table#yiTableOthers tr#trButtonYIWebpage_" + count;
        target2 = "table#yiTableOthers tr#trButtonYILangWebpage_" + count;
    }

    //check values
    var wrongField = false;
    var textC1 = $(target2).find("[id^='textYIWebpageLinkTitle']").val();
    // var textC2 = $("#selectTextYILangWebpage"+((count>1)?("_"+count):"")).val();
    var textC3 = $(target).find("[id^='textYIWebpage']").val();
    if (textC1.length <= 0 /*|| textC2=="none" */ && textC3.length <= 0) {
        wrongField = true;
    }

    clone = $(target).clone();
    if (count > 1) {
        clone.find("[id^='textYIWebpage_']").attr("id", "textYIWebpage_" + (count + 1));
    } else {
        clone.find("#textYIWebpage").attr("id", "textYIWebpage_" + (count + 1));
    }

    clone.find("[id^='selectTextYILangWebpage']").attr("id", "selectTextYILangWebpage_" + (count + 1));
    clone.find("[id^='selectTextYILangWebpage']").attr("value", "none");
    clone.find("[id^='textYIWebpage']").attr("value", "");
    clone.attr("id", "trButtonYIWebpage_" + (count + 1));

    clone2 = $(target2).clone();
    clone2.find("[id^='textYIWebpageLinkTitle']").attr("id", "textYIWebpageLinkTitle_" + (count + 1));
    clone2.find("[id^='textYIWebpageLinkTitle']").attr("value", "");
    clone2.attr("id", "trButtonYILangWebpage_" + (count + 1));

    if (wrongField) {
        displayAlertDialog(text1);
    } else {
        $(target2).after(clone2);
        $(target2).after(clone);
    }
}
function yIAddOpeningTimes(text1) {
    var count = $("table#yiTableOthers tr[id^='trTextYIOpeningTimes']").length;
    var id = "";
    if (count > 1) {
        id = "_" + count;
    }

    var opening = $("table#yiTableOthers tr#trTextYIOpeningTimes" + id + " textarea#textYIOpeningTimes").attr("value");
    if (opening == null || opening == "") {
        alertEmptyFields(text1);
        return;
    }

    var newTrTextId = "trTextYIOpeningTimes_" + (count + 1);
    var newTrLinkId = "trLinkYIOpeningTimes_" + (count + 1);
    var trTextHtml = "<tr id=\"" + newTrTextId + "\">" + $("table#yiTableOthers tr[id='trTextYIOpeningTimes']").clone().html() + "</tr>";
    var trLinkHtml = "<tr id=\"" + newTrLinkId + "\">" + $("table#yiTableOthers tr[id='trLinkYIOpeningTimes']").clone().html() + "</tr>";
    var clonedContent = trTextHtml + trLinkHtml;
    var lastId = "table#yiTableOthers tr#trLinkYIOpeningTimes" + id;
    $(lastId).after(clonedContent);

    // update last content
    $("table#yiTableOthers tr#" + newTrTextId + " label[for='textOpeningTimes']").attr("for", "textOpeningTimes_" + (count + 1));
    $("table#yiTableOthers tr#" + newTrTextId + " textarea#textOpeningTimes").attr("id", "textOpeningTimes_" + (count + 1));
    $("table#yiTableOthers tr#" + newTrTextId + " label[for='selectLanguageOpeningTimes']").attr("for", "selectLanguageOpeningTimes_" + (count + 1));
    $("table#yiTableOthers tr#" + newTrTextId + " select#selectLanguageOpeningTimes").attr("id", "selectLanguageOpeningTimes_" + (count + 1));
    $("table#yiTableOthers tr#" + newTrTextId).find("span").remove();
    $("table#yiTableOthers tr#" + newTrLinkId + " label[for='linkOpeningTimes']").attr("for", "linkOpeningTimes_" + (count + 1));
    $("table#yiTableOthers tr#" + newTrLinkId + " textarea#linkOpeningTimes").attr("id", "linkOpeningTimes_" + (count + 1));

    // Reset parameters
    $("table#yiTableOthers tr#" + newTrTextId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#yiTableOthers tr#" + newTrLinkId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#yiTableOthers tr#" + newTrTextId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#yiTableOthers tr#" + newTrLinkId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#yiTableOthers tr#" + newTrTextId + " select#selectLanguageOpeningTimes_" + (count + 1)).attr("value", "none");

    // TODO: Duplicate created rows in AS tab!
}
function duplicateOpeningTimesLanguage(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        var value = $("#selectTextYIOpeningTimes" + id + " :selected").val();
        $("table#accessAndServicesTable_1 tr#trASOpeningTimes" + id + " #selectLanguageOpeningTimes" + id + " option").each(function () {
            if (value == $(this).val()) {
                $(this).attr("selected", "selected");
            }
        });
    } else {
        var value = $("#selectTextYIOpeningTimes :selected").val();
        $("table#accessAndServicesTable_1 tr#trASOpeningTimes_1 #selectLanguageOpeningTimes_1 option").each(function () {
            if (value == $(this).val()) {
                $(this).attr("selected", "selected");
            }
        });

    }
}
function duplicateClosingTimesLanguage(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        var value = $("#selectTextYIClosingTimes" + id + " :selected").val();
        $("table#accessAndServicesTable_1 tr#trASClosingDates" + id + " #selectLanguageClosingDates" + id + " option").each(function () {
            if (value == $(this).val()) {
                $(this).attr("selected", "selected");
            }
        });

    } else {
        var value = $("#selectTextYIClosingTimes :selected").val();
        $("table#accessAndServicesTable_1 tr#trASClosingDates_1 #selectLanguageClosingDates_1 option").each(function () {
            if (value == $(this).val()) {
                $(this).attr("selected", "selected");
            }
        });
    }
}
function duplicateAccessInformation(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        var value = $("tr#trYIButtonFutherAccessInformation" + id + " #selectFutherAccessInformation" + id + " :selected").val();
        $("table#accessAndServicesTable_1 tr#trASAccessRestrictions" + id + " #selectASARSelectLanguage" + id + " option").each(function () {
            if (value == $(this).val()) {
                $(this).attr("selected", "selected");
            }
        });
    } else {
        var value = $("tr#trYIButtonFutherAccessInformation #selectFutherAccessInformation :selected").val();
        $("table#accessAndServicesTable_1 tr#trASAccessRestrictions_1 #selectASARSelectLanguage_1 option").each(function () {
            if (value == $(this).val()) {
                $(this).attr("selected", "selected");
            }
        });
    }
}

function duplicateFutherAccessInformationOnExistingFacilitiesLanguage(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        var value = $("tr#trButtonAddFutherInformationOnExistingFacilities" + id + " #selectFutherAccessInformationOnExistingFacilities" + id + " :selected").val();
        $("table#accessAndServicesTable_1 tr#trAccessibilityInformation" + id + " #selectASASelectLanguage" + id + " option").each(function () {
            if (value == $(this).val()) {
                $(this).attr("selected", "selected");
            }
        });
    } else {
        var value = $("tr#trButtonAddFutherInformationOnExistingFacilities #selectFutherAccessInformationOnExistingFacilities :selected").val();
        $("table#accessAndServicesTable_1 tr#trAccessibilityInformation_1 #selectASASelectLanguage_1 option").each(function () {
            if (value == $(this).val()) {
                $(this).attr("selected", "selected");
            }
        });
    }
}

function yIAddClosingDates2(text1) {
    var count = $("table#yiTableOthers tr[id^='fieldClosingDates']").length;
    var clone = null;
    var target = "";
    if (count == 1) {
        target = "table#yiTableOthers tr[id='fieldClosingDates']";
    } else {
        target = "table#yiTableOthers tr[id='fieldClosingDates_" + count + "']";
    }

    //check values
    var wrongField = false;
    var textC1 = $(target).find("[id^='yourInstitutionClosingDates']").val();
    // var textC2 = $("#selectTextYIClosingTimes"+((count>1)?("_"+count):"")).val();
    if (textC1.length <= 0/* || textC2=="none"*/) {
        wrongField = true;
    }

    clone = $(target).clone();
    clone.find("[id^='yourInstitutionClosingDates']").attr("id", "yourInstitutionClosingDates_" + (count + 1));
    clone.find("[id^='selectTextYIClosingTimes']").attr("id", "selectTextYIClosingTimes_" + (count + 1));
    clone.find("[id^='selectTextYIClosingTimes']").removeAttr("onchange");
    clone.attr("id", "fieldClosingDates_" + (count + 1));
    clone.find("[id^='yourInstitutionClosingDates']").attr("value", "");
    clone.find("[id^='selectTextYIClosingTimes']").attr("value", "none");
    if (wrongField) {
        displayAlertDialog(text1);
    } else {
        $(target).after(clone);
    }

}

function addFurtherEmailsOfTheInstitution(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#contactTable" + currentTab + " tr[id^='trEmailOfTheInstitution']").length;

    var email = "";
    var title = "";

    if (count == 1) {
        email = $("table#contactTable" + currentTab + " tr#trEmailOfTheInstitution textarea#textContactEmailOfTheInstitution_" + count).attr("value");
        title = $("table#contactTable" + currentTab + " tr#trLanguageEmailOfTheInstitution textarea#textContactLinkTitleForEmailOfTheInstitution_" + count).attr("value");
    } else {

        email = $("table#contactTable" + currentTab + " tr#trEmailOfTheInstitution_" + count + " textarea#textContactEmailOfTheInstitution_" + count).attr("value");
        title = $("table#contactTable" + currentTab + " tr#trLanguageEmailOfTheInstitution_" + count + " textarea#textContactLinkTitleForEmailOfTheInstitution_" + count).attr("value");
    }

    if ((email == null || email == "")
            && (title == null || title == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target = "";
    var target2 = "";
    if (count == 1) {
        target = "table#contactTable" + currentTab + " tr[id='trEmailOfTheInstitution']";
        target2 = "table#contactTable" + currentTab + " tr[id='trLanguageEmailOfTheInstitution']";
    } else {
        target = "table#contactTable" + currentTab + " tr[id='trEmailOfTheInstitution_" + count + "']";
        target2 = "table#contactTable" + currentTab + " tr[id='trLanguageEmailOfTheInstitution_" + count + "']";
    }

    var clone = $(target).clone();
    var clone2 = $(target2).clone();

    // Rename, remove value, and remove elements.
    clone.attr("id", "trEmailOfTheInstitution_" + (count + 1));
    clone.find("[for^='textContactEmailOfTheInstitution']").attr("for", "textContactEmailOfTheInstitution_" + (count + 1));
    clone.find("[id^='textContactEmailOfTheInstitution']").attr("id", "textContactEmailOfTheInstitution_" + (count + 1));
    clone.find("[id^='textContactEmailOfTheInstitution']").attr("value", "");
    clone.find("[for^='selectEmailLanguageOfTheInstitution']").attr("for", "selectEmailLanguageOfTheInstitution_" + (count + 1));
    clone.find("[id^='selectEmailLanguageOfTheInstitution']").attr("id", "selectEmailLanguageOfTheInstitution_" + (count + 1));
    clone.find("[id^='selectEmailLanguageOfTheInstitution']").attr("value", "none");

    clone2.attr("id", "trLanguageEmailOfTheInstitution_" + (count + 1));
    clone2.find("[for^='textContactLinkTitleForEmailOfTheInstitution']").attr("for", "textContactLinkTitleForEmailOfTheInstitution_" + (count + 1));
    clone2.find("[id^='textContactLinkTitleForEmailOfTheInstitution']").attr("id", "textContactLinkTitleForEmailOfTheInstitution_" + (count + 1));
    clone2.find("[id^='textContactLinkTitleForEmailOfTheInstitution']").attr("value", "");

    $(target2).after(clone);
    $(clone).after(clone2);
}

function addAnotherFormOfTheAuthorizedName(text1, text2) {
    var counter = $("table[id^='identityTableNameOfTheInstitution_']").length;

    var nameOfInstitution = $("table#identityTableNameOfTheInstitution_" + counter + " textarea#textNameOfTheInstitution").attr("value");

    if (nameOfInstitution == null || nameOfInstitution == "") {
        alertEmptyFields(text1);
        return;
    }
    var clone = $("table[id^='identityTableNameOfTheInstitution_" + counter + "']").clone();
    clone = "<table id='" + ("identityTableNameOfTheInstitution_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='identityTableNameOfTheInstitution_" + counter + "']").after(clone);
    // Reset parametters.
    $("table#identityTableNameOfTheInstitution_" + (counter + 1) + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#identityTableNameOfTheInstitution_" + (counter + 1) + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#identityTableNameOfTheInstitution_" + (counter + 1) + " tr#trNameOfTheInstitution select#noti_languageList").attr("value", "none");
    $("table#identityTableNameOfTheInstitution_" + (counter + 1) + " tr#trNameOfTheInstitution textarea#textNameOfTheInstitution").removeAttr("disabled");
    $("table#identityTableNameOfTheInstitution_" + (counter + 1) + " tr#trNameOfTheInstitution td#tdNameOfTheInstitution").find("span").remove();
    $("table#identityTableNameOfTheInstitution_" + (counter + 1) + " tr#trNameOfTheInstitution select#noti_languageList").removeAttr("disabled");
    $("table#identityTableNameOfTheInstitution_" + (counter + 1) + " tr#trNameOfTheInstitution td#tdNameOfTheInstitutionLanguage").find("span").remove();

    $("table#identityTableNameOfTheInstitution_" + (counter + 1) + " tr#trNameOfTheInstitution textarea#textNameOfTheInstitution").on("input", function () {
        checkName(text2, $(this));
    });
}

function addParallelNameOfTheInstitution(text1) {
    var counter = $("table[id^='identityTableParallelNameOfTheInstitution_']").length;

    var nameOfInstitution = $("table#identityTableParallelNameOfTheInstitution_" + counter + " textarea#textParallelNameOfTheInstitution").attr("value");
    // var nameOfInstitutionLanguage = $("table#identityTableParallelNameOfTheInstitution_"+counter+" select#pnoti_languageList").attr("value");

    if (nameOfInstitution == null || nameOfInstitution == ""
            /*|| nameOfInstitutionLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var clone = $("table[id^='identityTableParallelNameOfTheInstitution_" + counter + "']").clone();
    clone = "<table id='" + ("identityTableParallelNameOfTheInstitution_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='identityTableParallelNameOfTheInstitution_" + counter + "']").after(clone);
    // Reset parametters.
    $("table#identityTableParallelNameOfTheInstitution_" + (counter + 1) + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#identityTableParallelNameOfTheInstitution_" + (counter + 1) + " tr#trParallelNameOfTheInstitution select#pnoti_languageList").attr("value", "none");
    $("table#identityTableParallelNameOfTheInstitution_" + (counter + 1) + " tr#trParallelNameOfTheInstitution textarea#textParallelNameOfTheInstitution").removeAttr("disabled");
    $("table#identityTableParallelNameOfTheInstitution_" + (counter + 1) + " tr#trParallelNameOfTheInstitution select#pnoti_languageList").removeAttr("disabled");
}

function addMoreAnotherFormerlyUsedName(text1, text2, text3, text4, text5, text6, text7, text8, text9) {
    var counter = $("table[id^='identityTableFormerlyUsedName_']").length;
    var select = '<select id="tfun_languageList">' + $("#pnoti_languageList").html() + '</select>';

    if (counter == 0) {
        $("table#identityButtonAddFormerlyUsedName").before('<table id="identityTableFormerlyUsedName_1" class="tablePadding">' +
                '<tr id="trTextFormerlyUsedName" class="marginTop">' +
                '<td>' +
                '<label for="textFormerlyUsedName">' + text1 + ':</label>' +
                '</td>' +
                '<td colspan="2">' +
                '<textarea id="textFormerlyUsedName"></textarea>' +
                '</td>' +
                '<td class="labelLeft">' +
                '<label class="language" for="tfun_languageList">' + text2 + ':</label>' +
                select +
                '</td>' +
                '</tr>' +
                '<tr id="trLabelDatesWhenThisNameWasUsed">' +
                '<td colspan="4">' +
                '<label for="textDatesWhenThisNameWasUsed">' + text3 + ':</label>' +
                '</td>' +
                '</tr>' +
                '<tr id="trYearWhenThisNameWasUsed_1">' +
                '<td>' +
                '<label for="textYearWhenThisNameWasUsed_1">' + text4 + ':</label>' +
                '</td>' +
                '<td>' +
                '<input type="text" id="textYearWhenThisNameWasUsed_1" value=""/>' +
                '</td>' +
                '<td colspan="2">' +
                '</td>' +
                '</tr>' +
                '<tr>' +
                '<td>' +
                '<input type="button" id="buttonAddSingleYear" value="' + text5 + '" onclick="addSingleYear($(this).parent().parent().parent().parent(), \'' + text9 + '\');" />' +
                '</td>' +
                '<td>' +
                '<input type="button" id="buttonAddYearRange" value="' + text6 + '" onclick="addRangeYear($(this).parent().parent().parent().parent(), \'' + text7 + '\', \'' + text8 + '\', \'' + text9 + '\');" />' +
                '</td>' +
                '<td colspan="2">' +
                '</td>' +
                '</tr></table>');
        $("table#identityTableFormerlyUsedName_1 tr#trTextFormerlyUsedName select#tfun_languageList").attr("value", "none");
    } else {
        var formerlyUsedName = $("table#identityTableFormerlyUsedName_" + counter + " textarea#textFormerlyUsedName").attr("value");
        // var formerlyUsedNameLanguage = $("table#identityTableFormerlyUsedName_"+counter+" select#tfun_languageList").attr("value");

        var counterYears = $("table#identityTableFormerlyUsedName_" + counter + " input[id^='textYearWhenThisNameWasUsed_']").length;
        var singleYear = $("table#identityTableFormerlyUsedName_" + counter + " input#textYearWhenThisNameWasUsed_" + counterYears).attr("value");
        var year = singleYear;

        if (year == null || year == "") {
            if (counterYears > 1) {
                year = $("table#identityTableFormerlyUsedName_" + counter + " input#textYearWhenThisNameWasUsed_" + (counterYears - 1)).attr("value");
            } else {
                var counterYearsFrom = $("table#identityTableFormerlyUsedName_" + counter + " input[id^='textYearWhenThisNameWasUsedFrom_']").length;
                var yearFrom = $("table#identityTableFormerlyUsedName_" + counter + " input#textYearWhenThisNameWasUsedFrom_" + counterYearsFrom).attr("value");
                var yearTo = $("table#identityTableFormerlyUsedName_" + counter + " input#textYearWhenThisNameWasUsedFrom_" + counterYearsFrom).attr("value");

                if (yearFrom == null || yearFrom == "" || yearTo == null || yearTo == "") {
                    if (counterYearsFrom > 1) {
                        year = $("table#identityTableFormerlyUsedName_" + counter + " input#textYearWhenThisNameWasUsedFrom_" + (counterYearsFrom - 1)).attr("value");
                    }
                } else {
                    year = yearFrom;
                }
            }
        }

        if (formerlyUsedName == null || formerlyUsedName == ""
                /*|| formerlyUsedNameLanguage == "none"*/
                || year == null || year == "") {
            alertEmptyFields(text9);
            return;
        }

        $("table#identityButtonAddFormerlyUsedName").before('<table id="identityTableFormerlyUsedName_' + (counter + 1) + '" class="tablePadding">' +
                '<tr id="trTextFormerlyUsedName" class="marginTop">' +
                '<td>' +
                '<label for="textFormerlyUsedName">' + text1 + ':</label>' +
                '</td>' +
                '<td colspan="2">' +
                '<textarea id="textFormerlyUsedName" value=""/>' +
                '</td>' +
                '<td class="labelLeft">' +
                '<label class="language" for="tfun_languageList">' + text2 + ':</label>' +
                select +
                '</td>' +
                '</tr>' +
                '<tr id="trLabelDatesWhenThisNameWasUsed">' +
                '<td colspan="4">' +
                '<label for="textDatesWhenThisNameWasUsed">' + text3 + ':</label>' +
                '</td>' +
                '</tr>' +
                '<tr id="trYearWhenThisNameWasUsed_1">' +
                '<td>' +
                '<label for="textYearWhenThisNameWasUsed_1">' + text4 + ':</label>' +
                '</td>' +
                '<td>' +
                '<input type="text" id="textYearWhenThisNameWasUsed_1" value=""/>' +
                '</td>' +
                '<td colspan="2">' +
                '</td>' +
                '</tr>' +
                '<tr>' +
                '<td>' +
                '<input type="button" id="buttonAddSingleYear" value="' + text5 + '" onclick="addSingleYear($(this).parent().parent().parent().parent(), \'' + text9 + '\');" />' +
                '</td>' +
                '<td>' +
                '<input type="button" id="buttonAddYearRange" value="' + text6 + '" onclick="addRangeYear($(this).parent().parent().parent().parent(), \'' + text7 + '\', \'' + text8 + '\', \'' + text9 + '\');" />' +
                '</td>' +
                '<td colspan="2">' +
                '</td>' +
                '</tr></table>');
        $("table#identityTableFormerlyUsedName_" + (counter + 1) + " tr#trTextFormerlyUsedName select#tfun_languageList").attr("value", "none");
    }
}

function addSingleYear(table, text1) {
    var id = $(table).attr("id");
    var counter = $("table#" + id + " tr[id^='trYearWhenThisNameWasUsed_']").length;

    var year = $("table#" + id + " tr#trYearWhenThisNameWasUsed_" + counter + " input#textYearWhenThisNameWasUsed_" + counter).attr("value");

    if (year == null || year == "") {
        alertEmptyFields(text1);
        return;
    }

    var clone = $("table#" + id + " tr[id^='trYearWhenThisNameWasUsed_" + counter + "']").clone();
    clone = "<tr id='" + ("trYearWhenThisNameWasUsed_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
    $("table#" + id + " tr[id^='trYearWhenThisNameWasUsed_" + counter + "']").after(clone);
    // Reset parametters and change IDs.
    $("table#" + id + " tr#trYearWhenThisNameWasUsed_" + (counter + 1) + " input[type='text']").each(function () {
        var tdId = $(this).attr("id");
        tdId = tdId.substring(0, (tdId.lastIndexOf("_") + 1)) + (counter + 1);
        $(this).attr("id", tdId);
        $(this).val(""); // Clean all input_text.
    });
    $("table#" + id + " tr#trYearWhenThisNameWasUsed_" + (counter + 1) + " label").each(function () {
        var labelFor = $(this).attr("for");
        labelFor = labelFor.substring(0, (labelFor.lastIndexOf("_") + 1)) + (counter + 1);
        $(this).attr("for", labelFor);
    });
}

function addRangeYear(table, text1, text2, text3) {
    var id = $(table).attr("id");
    var counter = $("table#" + id + " tr[id^='trYearRangeWhenThisNameWasUsed_']").length;

    if (counter == 0) {
        var count = $("table#" + id + " tr[id^='trYearWhenThisNameWasUsed_']").length;
        var yearRange = "<tr id=\"trYearRangeWhenThisNameWasUsed_1\">" +
                "<td>" +
                "<label for=\"textIdentityYearFrom_1\">" + text1 + ":</label>" +
                "</td>" +
                "<td>" +
                "<input type=\"text\" id=\"textYearWhenThisNameWasUsedFrom_1\" value=\"\" />" +
                "</td>" +
                "<td class=\"labelLeft\">" +
                "<label for=\"textYearWhenThisNameWasUsedTo_1\">" + text2 + ":</label>" +
                "</td>" +
                "<td>" +
                "<input type=\"text\" id=\"textYearWhenThisNameWasUsedTo_1\" value=\"\" />" +
                "</td>" +
                "</tr>";
        $("table#" + id + " tr[id^='trYearWhenThisNameWasUsed_" + count + "']").after(yearRange);
    } else {
        var yearFrom = $("table#" + id + " tr#trYearRangeWhenThisNameWasUsed_" + counter + " input#textYearWhenThisNameWasUsedFrom_" + counter).attr("value");
        var yearTo = $("table#" + id + " tr#trYearRangeWhenThisNameWasUsed_" + counter + " input#textYearWhenThisNameWasUsedTo_" + counter).attr("value");

        if (yearFrom == null || yearFrom == ""
                || yearTo == null || yearTo == "") {
            alertEmptyFields(text3);
            return;
        }

        var clone = $("table#" + id + " tr[id^='trYearRangeWhenThisNameWasUsed_" + counter + "']").clone();
        clone = "<tr id='" + ("trYearRangeWhenThisNameWasUsed_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
        $("table#" + id + " tr[id^='trYearRangeWhenThisNameWasUsed_" + counter + "']").after(clone);
        // Reset parametters and change IDs.
        $("table#" + id + " tr#trYearRangeWhenThisNameWasUsed_" + (counter + 1) + " input[type='text']").each(function () {
            var tdId = $(this).attr("id");
            tdId = tdId.substring(0, (tdId.lastIndexOf("_") + 1)) + (counter + 1);
            $(this).attr("id", tdId);
            $(this).val(""); // Clean all input_text.
        });
        $("table#" + id + " tr#trYearRangeWhenThisNameWasUsed_" + (counter + 1) + " label").each(function () {
            var labelFor = $(this).attr("for");
            labelFor = labelFor.substring(0, (labelFor.lastIndexOf("_") + 1)) + (counter + 1);
            $(this).attr("for", labelFor);
        });
    }
}

function contactChangeTabName(text1) {
    var currentTab = getCurrentTab();
    var counter = $("a[id^='tab_yourInstitutionTable_']").length;

    var name = $("table#contactTable" + currentTab + " textarea#textNameOfRepository").val();

    if (name != null && name != "") {
        $("a#tab_yourInstitutionTable" + currentTab).text(name);
    } else {
        $("a#tab_yourInstitutionTable" + currentTab).text(text1 + " " + (counter - 1));
    }
}

/***
 * Adds a new Visitors address in Contact Tab if there is filled Contact Address + Visitors Address
 * @param text1
 */
function contactAddVisitorsAddressTranslation(text1) {
    var currentTab = getCurrentTab();
    var counter = $("table#contactTable" + currentTab + " table[id^='contactTableVisitorsAddress_']").length;
    var counterP = $("table#contactTable" + currentTab + " table[id^='contactTablePostalAddress_']").length;

    var street = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counter + " textarea#textContactStreetOfTheInstitution").attr("value");
    var city = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counter + " textarea#textContactCityOfTheInstitution").attr("value");
//	var district = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactDistrictOfTheInstitution").attr("value");
//	var county = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactCountyOfTheInstitution").attr("value");
//	var region = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactRegionOfTheInstitution").attr("value");
    var country = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counter + " textarea#textContactCountryOfTheInstitution").attr("value");

    var latitude = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counter + " input#textContactLatitudeOfTheInstitution").attr("value");
    var longitude = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counter + " input#textContactLongitudeOfTheInstitution").attr("value");

    if (street == null || street.trim() == ""
            || city == null || city.trim() == "" /*|| district == null || district == ""
             || county == null || county == "" || region == null || region == "" */
            || country == null || country.trim() == "") {
        alertEmptyFields(text1);
        return;
    }

    //postal address validation
    var streetP = $("table#contactTable" + currentTab + " table#contactTablePostalAddress_" + counterP + " textarea#textContactPAStreet").attr("value");
    var cityP = $("table#contactTable" + currentTab + " table#contactTablePostalAddress_" + counterP + " textarea#textContactPACity").attr("value");
    if (streetP != null && cityP != null) {
        if (streetP.trim() == "" || cityP.trim() == "") {
            alertEmptyFields(text1);
            return;
        }
    }
    //end validation for Postal

    var clone = $("table#contactTable" + currentTab + " table[id^='contactTableVisitorsAddress_" + counter + "']").clone();
    clone = "<table id='" + ("contactTableVisitorsAddress_" + (counter + 1)) + "'>" + clone.html() + "</table>";
    $("table#contactTable" + currentTab + " table[id^='contactTableVisitorsAddress_" + counter + "']").after(clone);
    // Reset parametters and enable fields.
    $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + (counter + 1) + " input[type='text']").each(function () {
        var id = $(this).attr("id");
        if (id != "textContactLatitudeOfTheInstitution" && id != "textContactLongitudeOfTheInstitution") {
            $(this).val(""); // Clean all input_text.
            $(this).removeAttr("disabled");
        } else if (id == "textContactLatitudeOfTheInstitution") {
            $(this).val(latitude);
            $("input#textContactLatitudeOfTheInstitution").attr("disabled", "disabled");
        } else if (id == "textContactLongitudeOfTheInstitution") {
            $(this).val(longitude);
            $("input#textContactLongitudeOfTheInstitution").attr("disabled", "disabled");
        }
    });
    $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + (counter + 1) + " textarea").each(function () {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("disabled");
    });
    $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + (counter + 1) + " select").removeAttr("disabled");
    $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + (counter + 1) + " #selectLanguageVisitorAddress").attr("value", "none");
    // Remove "*".
    $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + (counter + 1)).find("span").remove();

    $("table#contactTableVisitorsAddress_1 input#textContactLatitudeOfTheInstitution").removeAttr("disabled");
    $("table#contactTableVisitorsAddress_1 input#textContactLongitudeOfTheInstitution").removeAttr("disabled");
}

/***
 * Adds a new Postal address in Contact Tab if there is filled Contact Address + Visitors Address
 * @param property1 postalAddress
 * @param property2 selectLanguage
 * @param property3 street
 * @param property4 cityTownWithPostalcode
 * @param text1 error message
 * @param control var control=true from webFormEAG2012.jsp
 */
function contactAddPostalAddressIfDifferent(property1, property2, property3, property4, text1, control) {
    var currentTab = getCurrentTab();
    var selectContactLanguagePostalAddressOnChange = '<select id="selectContactLanguagePostalAddress" onchange="contactAddressLanguageChanged($(this).parent().parent().parent().parent());">' + $("#selectLanguageVisitorAddress").html() + '</select>';
    var selectContactLanguagePostalAddressNoChange = '<select id="selectContactLanguagePostalAddress">' + $("#selectLanguageVisitorAddress").html() + '</select>';
    var selectContactLanguagePostalAddress = "";
    var textContactPAStreetOnChange = '<textarea id="textContactPAStreet" onchange="contactAddressStreetChanged($(this).parent().parent().parent().parent());" />';
    var textContactPAStreetNoChange = '<textarea id="textContactPAStreet" />';
    var textContactPAStreet = "";
    var textContactPACityOnChange = '<textarea id="textContactPACity" onchange="contactAddressCityChanged($(this).parent().parent().parent().parent());"/>';
    var textContactPACityNoChange = '<textarea id="textContactPACity" />';
    var textContactPACity = "";

    //Control Add Visitors address
    var counterV = $("table#contactTable" + currentTab + " table[id^='contactTableVisitorsAddress_']").length;
    var street = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counterV + " textarea#textContactStreetOfTheInstitution").attr("value");
    var city = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counterV + " textarea#textContactCityOfTheInstitution").attr("value");
    var country = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counterV + " textarea#textContactCountryOfTheInstitution").attr("value");
    if (street.trim() == "" || city.trim() == "" || country.trim() == "") {
        alertEmptyFields(text1);
        return;
    }
    //end control Add visitors Address

    if (currentTab == "_1") {
        textContactPAStreet = textContactPAStreetOnChange;
        textContactPACity = textContactPACityOnChange;
        selectContactLanguagePostalAddress = selectContactLanguagePostalAddressOnChange;
    } else {
        textContactPAStreet = textContactPAStreetNoChange;
        textContactPACity = textContactPACityNoChange;
        selectContactLanguagePostalAddress = selectContactLanguagePostalAddressNoChange;
    }

    $("table#contactTable" + currentTab + " input#buttonContactAddPostalAddressIfDifferent").hide();
    $("table#contactTable" + currentTab + " tr#trButtonContactAddPostalAddressIfDifferent").after('<tr><td colspan="4"><table id="contactTablePostalAddress_1">' +
            '<tr id="trContactPostalAddressLabel">' +
            '<td id="postalAddressLabel" colspan="4">' + property1 +
            '</td>' +
            '</tr>' +
            '<tr id="contactPostalAddressStreet">' +
            '<td>' +
            '<label for="textContactPAStreet">' + property3 + ':</label>' +
            '</td>' +
            '<td colspan="2" class="textContact">' + textContactPAStreet +
            '</td>' +
            '<td id="contactPostalAddressLanguage">' +
            '<label class="language" for="selectContactLanguagePostalAddress">' + property2 + ':</label>' +
            selectContactLanguagePostalAddress +
            '</td>' +
            '</tr>' +
            '<tr id="contactPostalAddressCity">' +
            '<td>' +
            '<label for="textContactPACity">' + property4 + ':</label>' +
            '</td>' +
            '<td colspan="2" class="textContact">' + textContactPACity +
            '</td>' +
            '</tr></table></td></tr>');

    $("table#contactTable" + currentTab + " tr#trButtonContacPostalAddressTranslation").show();
    $("table#contactTable" + currentTab + " tr#orangeLineContacPostalAddressTranslation").show();
    $("table#contactTable" + currentTab + " table#contactTablePostalAddress_1 #selectContactLanguagePostalAddress").attr("value", "none");
    if (!control && currentTab == "_1") {
        control = true;
    }
}

function contactAddPostalAddressTranslation(text1) {
    var currentTab = getCurrentTab();
    var counter = $("table#contactTable" + currentTab + " table[id^='contactTablePostalAddress_']").length;

    var street = $("table#contactTable" + currentTab + " table#contactTablePostalAddress_" + counter + " textarea#textContactPAStreet").attr("value");
    var city = $("table#contactTable" + currentTab + " table#contactTablePostalAddress_" + counter + " textarea#textContactPACity").attr("value");

    if (street == null || street.trim() == ""
            || city == null || city.trim() == "") {
        alertEmptyFields(text1);
        return;
    }

    //Control Add Visitors address
    var counterV = $("table#contactTable" + currentTab + " table[id^='contactTableVisitorsAddress_']").length;
    var streetV = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counterV + " textarea#textContactStreetOfTheInstitution").attr("value");
    var cityV = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counterV + " textarea#textContactCityOfTheInstitution").attr("value");
    var countryV = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_" + counterV + " textarea#textContactCountryOfTheInstitution").attr("value");
    if (streetV == null || streetV.trim() == ""
            || cityV == null || cityV.trim() == ""
            || countryV == null || countryV.trim() == "") {
        alertEmptyFields(text1);
        return;
    }
    //end control Add visitors Address 

    var clone = $("table#contactTable" + currentTab + " table[id^='contactTablePostalAddress_" + counter + "']").clone();
    clone = "<table id='" + ("contactTablePostalAddress_" + (counter + 1)) + "'>" + clone.html() + "</table>";
    $("table#contactTable" + currentTab + " table[id^='contactTablePostalAddress_" + counter + "']").after(clone);
    // Reset parametters and enable fields.
    $("table#contactTable" + currentTab + " table#contactTablePostalAddress_" + (counter + 1) + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#contactTable" + currentTab + " table#contactTablePostalAddress_" + (counter + 1) + " #selectContactLanguagePostalAddress").attr("value", "none");
//	// Remove "*".
//	$("table#contactTable"+currentTab+" table#contactTablePostalAddress_"+(counter+1)).find("span").remove();
//	$("table#contactTable"+currentTab+" tr#orangeLineContacPostalAddressTranslation")[0].hide();

}

function addFurtherTelephoneOfTheInstitution(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#contactTable" + currentTab + " tr[id^='trTelephoneOfTheInstitution']").length;

    var telephone = $("table#contactTable" + currentTab + " tr#trTelephoneOfTheInstitution_" + count + " input#textContactTelephoneOfTheInstitution_" + count).attr("value");

    if (telephone == null || telephone == "") {
        alertEmptyFields(text1);
        return;
    }

    var target = "#trTelephoneOfTheInstitution_" + count;
    var clone = $(target).clone();
    var lastId = "table#contactTable" + currentTab + " tr#trTelephoneOfTheInstitution_" + count;

    // Rename, remove value, and remove elements.
    clone.attr("id", "trTelephoneOfTheInstitution_" + (count + 1));
    clone.find("[id^='tdTelephoneOfTheInstitution']").attr("id", "tdTelephoneOfTheInstitution_" + (count + 1));
    clone.find("[for^='textContactTelephoneOfTheInstitution']").attr("for", "textContactTelephoneOfTheInstitution_" + (count + 1));
    clone.find("[id^='textContactTelephoneOfTheInstitution']").attr("id", "textContactTelephoneOfTheInstitution_" + (count + 1));
    clone.find("[id^='textContactTelephoneOfTheInstitution']").attr("value", "");
    clone.find("[id^='textContactTelephoneOfTheInstitution']").removeAttr("onchange");
    clone.find("[id^='tdAddFurtherTelephoneOfTheInstitution']").remove();

    $(lastId).after(clone);
}

function addFurtherFaxOfTheInstitution(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#contactTable" + currentTab + " tr[id^='trFaxOfTheInstitution']").length;

    var fax = $("table#contactTable" + currentTab + " tr#trFaxOfTheInstitution_" + count + " input#textContactFaxOfTheInstitution_" + count).attr("value");

    if (fax == null || fax == "") {
        alertEmptyFields(text1);
        return;
    }

    var target = "#trFaxOfTheInstitution_" + count;
    var clone = $(target).clone();
    var lastId = "table#contactTable" + currentTab + " tr#trFaxOfTheInstitution_" + count;

    // Rename, remove value, and remove elements.
    clone.attr("id", "trFaxOfTheInstitution_" + (count + 1));
    clone.find("[id^='tdFaxOfTheInstitution']").attr("id", "tdFaxOfTheInstitution_" + (count + 1));
    clone.find("[for^='textContactFaxOfTheInstitution']").attr("for", "textContactFaxOfTheInstitution_" + (count + 1));
    clone.find("[id^='textContactFaxOfTheInstitution']").attr("id", "textContactFaxOfTheInstitution_" + (count + 1));
    clone.find("[id^='textContactFaxOfTheInstitution']").attr("value", "");
    clone.find("[id^='tdAddFurtherFaxOfTheInstitution']").remove();

    $(lastId).after(clone);
}

function addFurtherWebsOfTheInstitution(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#contactTable" + currentTab + " tr[id^='trWebOfTheInstitution']").length;

    var web = "";
    var title = "";

    if (count == 1) {
        web = $("table#contactTable" + currentTab + " tr#trWebOfTheInstitution textarea#textContactWebOfTheInstitution_" + count).attr("value");
        title = $("table#contactTable" + currentTab + " tr#trLanguageWebpageOfTheInstitution textarea#textContactLinkTitleForWebOfTheInstitution_" + count).attr("value");
    } else {

        web = $("table#contactTable" + currentTab + " tr#trWebOfTheInstitution_" + count + " textarea#textContactWebOfTheInstitution_" + count).attr("value");
        title = $("table#contactTable" + currentTab + " tr#trLanguageWebpageOfTheInstitution_" + count + " textarea#textContactLinkTitleForWebOfTheInstitution_" + count).attr("value");
    }

    if ((web == null || web == "")
            && (title == null || title == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target = "";
    var target2 = "";
    if (count == 1) {
        target = "table#contactTable" + currentTab + " tr[id='trWebOfTheInstitution']";
        target2 = "table#contactTable" + currentTab + " tr[id='trLanguageWebpageOfTheInstitution']";
    } else {
        target = "table#contactTable" + currentTab + " tr[id='trWebOfTheInstitution_" + count + "']";
        target2 = "table#contactTable" + currentTab + " tr[id='trLanguageWebpageOfTheInstitution_" + count + "']";
    }

    clone = $(target).clone();
    var clone2 = $(target2).clone();
    // Rename, remove value, and remove elements.
    clone.attr("id", "trWebOfTheInstitution_" + (count + 1));
    clone.find("[for^='textContactWebOfTheInstitution']").attr("for", "textContactWebOfTheInstitution_" + (count + 1));
    clone.find("[id^='textContactWebOfTheInstitution']").attr("id", "textContactWebOfTheInstitution_" + (count + 1));
    clone.find("[id^='textContactWebOfTheInstitution']").attr("value", "");

    clone.find("[for^='selectWebpageLanguageOfTheInstitution']").attr("for", "selectWebpageLanguageOfTheInstitution_" + (count + 1));
    clone.find("[id^='selectWebpageLanguageOfTheInstitution']").attr("id", "selectWebpageLanguageOfTheInstitution_" + (count + 1));
    clone.find("[id^='selectWebpageLanguageOfTheInstitution']").attr("value", "none");

    clone.find("[id^='undefined_w_required']").remove();

    clone2.attr("id", "trLanguageWebpageOfTheInstitution_" + (count + 1));
    clone2.find("[for^='textContactLinkTitleForWebOfTheInstitution']").attr("for", "textContactLinkTitleForWebOfTheInstitution_" + (count + 1));
    clone2.find("[id^='textContactLinkTitleForWebOfTheInstitution']").attr("id", "textContactLinkTitleForWebOfTheInstitution_" + (count + 1));
    clone2.find("[id^='textContactLinkTitleForWebOfTheInstitution']").attr("value", "");

    $(target2).after(clone);
    $(clone).after(clone2);
}

function aSAddOpeningTimes(text1) {
    var currentTab = getCurrentTab();
    // trASOpeningTimes
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASOpeningTimes']").length;
    var id = "_" + count;

    var opening = $("table#accessAndServicesTable" + currentTab + " tr#trASOpeningTimes" + id + " textarea#textOpeningTimes" + id).attr("value");
    if (opening == null || opening == "") {
        alertEmptyFields(text1);
        return;
    }

    var newTrTextId = "trASOpeningTimes_" + (count + 1);
    var newTrLinkId = "tr2ASOpeningTimes_" + (count + 1);
    var trTextHtml = "<tr id=\"" + newTrTextId + "\">" + $("table#accessAndServicesTable" + currentTab + " tr[id='trASOpeningTimes_1']").clone().html() + "</tr>";
    var trLinkHtml = "<tr id=\"" + newTrLinkId + "\">" + $("table#accessAndServicesTable" + currentTab + " tr[id='tr2ASOpeningTimes_1']").clone().html() + "</tr>";
    var clonedContent = trTextHtml + trLinkHtml;
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#tr2ASOpeningTimes_" + count;
    $(lastId).after(clonedContent);

    // update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " td#tdOpeningTimes_1").attr("id", "tdOpeningTimes_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " label[for='textOpeningTimes_1']").attr("for", "textOpeningTimes_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " textarea#textOpeningTimes_1").attr("id", "textOpeningTimes_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " label[for='selectLanguageOpeningTimes_1']").attr("for", "selectLanguageOpeningTimes_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " select#selectLanguageOpeningTimes_1").attr("id", "selectLanguageOpeningTimes_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " td#tdOpeningTimes_" + (count + 1)).find("span").remove();
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrLinkId + " td#td2OpeningTimes_1").attr("id", "td2OpeningTimes_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrLinkId + " label[for='linkOpeningTimes_1']").attr("for", "linkOpeningTimes_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrLinkId + " textarea#linkOpeningTimes_1").attr("id", "linkOpeningTimes_" + (count + 1));

    // Reset parameters
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrLinkId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrLinkId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newTrTextId + " select#selectLanguageOpeningTimes_" + (count + 1)).attr("value", "none");
    
    //TODO: Duplicate new fields in YI tab!
}

function aSAddClosingDates(text1) {
    var currentTab = getCurrentTab();
    // trASClosingDates
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASClosingDates']").length;

    var id = "_" + count;

    var closing = $("table#accessAndServicesTable" + currentTab + " tr#trASClosingDates" + id + " textarea#textClosingDates" + id).attr("value");
    // var closingLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASClosingDates"+id+" select#selectLanguageClosingDates"+id).attr("value");

    if (closing == null || closing == ""
            /*|| closingLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trASClosingDates_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable" + currentTab + " tr[id='trASClosingDates_1']").clone().html() + "</tr>";
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#trASClosingDates_" + count;

    $(lastId).after(trHtml);
    // update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textClosingDates_1']").attr("for", "textClosingDates_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textClosingDates_1").attr("id", "textClosingDates_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='selectLanguageClosingDates_1']").attr("for", "selectLanguageClosingDates_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectLanguageClosingDates_1").attr("id", "selectLanguageClosingDates_" + (count + 1));

    // Reset parametters.
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectLanguageClosingDates_" + (count + 1)).attr("value", "none");
}

function aSAddTravellingDirections(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trTravellingDirections']").length;

    var id = "_" + count;

    var travel = $("table#accessAndServicesTable" + currentTab + " tr#trTravellingDirections" + id + " textarea#textTravellingDirections" + id).attr("value");
    // var travelLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trTravellingDirections"+id+" select#selectASATDSelectLanguage"+id).attr("value");
    var travelLink = $("table#accessAndServicesTable" + currentTab + " tr#tr2TravellingDirections" + id + " textarea#textTravelLink" + id).attr("value");

    if ((travel == null || travel == "")
            /*|| travelLanguage == "none"*/
            && (travelLink == null || travelLink == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trTravellingDirections_" + (count + 1);
    var target2 = "tr2TravellingDirections_" + (count + 1);
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#tr2TravellingDirections_" + count;

    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#accessAndServicesTable" + currentTab + " tr#trTravellingDirections_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#accessAndServicesTable" + currentTab + " tr#tr2TravellingDirections_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);
    //update elements
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='textTravellingDirections_1']").attr("for", "textTravellingDirections_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textTravellingDirections_1").attr("id", "textTravellingDirections_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textTravelLink_1']").attr("for", "textTravelLink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textTravelLink_1").attr("id", "textTravelLink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='selectASATDSelectLanguage_1']").attr("for", "selectASATDSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASATDSelectLanguage_1").attr("id", "selectASATDSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " p#undefined_w_required").remove();

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textTravellingDirections_" + (count + 1)).val("");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASATDSelectLanguage_" + (count + 1)).attr("value", "none");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
}

function addFutherAccessInformation(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASAccessRestrictions']").length;

    var id = "_" + count;

    var access = $("table#accessAndServicesTable" + currentTab + " tr#trASAccessRestrictions" + id + " textarea#textASAccessRestrictions" + id).attr("value");
    // var accessLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASAccessRestrictions"+id+" select#selectASARSelectLanguage"+id).attr("value");

    if (access == null || access == ""
            /*|| accessLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trASAccessRestrictions_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable" + currentTab + " tr#trASAccessRestrictions_1").clone().html() + "</tr>";
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#trASAccessRestrictions_" + count;
    $(lastId).after(trHtml);
    //update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textASAccessRestrictions_1']").attr("for", "textASAccessRestrictions_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textASAccessRestrictions_1").attr("id", "textASAccessRestrictions_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='selectASARSelectLanguage_1']").attr("for", "selectASARSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASARSelectLanguage_1").attr("id", "selectASARSelectLanguage_" + (count + 1));

    // Reset parametters.
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASARSelectLanguage_" + (count + 1)).attr("value", "none");
}

function aSAddFutherTermOfUse(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASAddFutherTermOfUse']").length;

    var id = "_" + count;

    var terms = $("table#accessAndServicesTable" + currentTab + " tr#trASAddFutherTermOfUse" + id + " textarea#textASTermOfUse" + id).attr("value");
    // var termsLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASAddFutherTermOfUse"+id+" select#selectASAFTOUSelectLanguage"+id).attr("value");
    var termsLink = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASAddFutherTermOfUse" + id + " textarea#textASTOULink" + id).attr("value");

    if ((terms == null || terms == "")
            /*|| termsLanguage == "none"*/
            && (termsLink == null || termsLink == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trASAddFutherTermOfUse_" + (count + 1);
    var target2 = "tr2ASAddFutherTermOfUse_" + (count + 1);
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#tr2ASAddFutherTermOfUse_" + count;

    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#accessAndServicesTable" + currentTab + " tr#trASAddFutherTermOfUse_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#accessAndServicesTable" + currentTab + " tr#tr2ASAddFutherTermOfUse_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);
    //update elements
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='textASTermOfUse_1']").attr("for", "textASTermOfUse_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textASTermOfUse_1").attr("id", "textASTermOfUse_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textTravelLink_1']").attr("for", "textTravelLink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASTOULink_1").attr("id", "textASTOULink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='textASTOULink_1']").attr("for", "textASTOULink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASAFTOUSelectLanguage_1").attr("id", "selectASAFTOUSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " p#undefined_w_required").remove();

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textASTermOfUse_" + (count + 1)).val("");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASAFTOUSelectLanguage_" + (count + 1)).attr("value", "none");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
}

function addAccessibilityInformation(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trAccessibilityInformation']").length;

    var id = "_" + count;

    var facilities = $("table#accessAndServicesTable" + currentTab + " tr#trAccessibilityInformation" + id + " textarea#textASAccessibility" + id).attr("value");
    // var facilitiesLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trAccessibilityInformation"+id+" select#selectASASelectLanguage"+id).attr("value");

    if (facilities == null || facilities == ""
            /*|| facilitiesLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trAccessibilityInformation_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable tr#trAccessibilityInformation_1").clone().html() + "</tr>";
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#trAccessibilityInformation_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textASAccessibility_1']").attr("for", "textASAccessibility_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textASAccessibility_1").attr("id", "textASAccessibility_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='selectASASelectLanguage']_1").attr("for", "selectASASelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASASelectLanguage_1").attr("id", "selectASASelectLanguage_" + (count + 1));

    // Reset parametters.
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASASelectLanguage_" + (count + 1)).attr("value", "none");
}

function aSSRAddDescriptionOfYourComputerPlaces(property1, property2, text1) {
    // add description of computer places.
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASSRDescriptionOfYourComputerPlaces']").length;
    var select = '<select id="selectDescriptionOfYourComputerPlaces_1">' + $("#selectLanguageOpeningTimes_1").html() + '</select>';

    if (count == 0) {
        $("table#accessAndServicesTable" + currentTab + " tr#trASSRAddDescriptionOfYourComputerPlaces").before("<tr id=\"trASSRDescriptionOfYourComputerPlaces_1\">" +
                "<td id=\"tdDescriptionOfYourComputerPlaces_1\">" +
                "<label for=\"textDescriptionOfYourComputerPlaces_1\">" + property1 + ":</label>" +
                "</td>" +
                "<td colspan=\"2\">" +
                "<textarea id=\"textDescriptionOfYourComputerPlaces_1\"></textarea>" +
                "<td id=\"tdSelectDescriptionOfYourComputerPlaces_1\" class=\"labelLeft\">" +
                "<label class=\"language\" for=\"selectDescriptionOfYourComputerPlaces_1\">" + property2 + ":</label>" +
                select +
                "</td></tr>");
        $("table#accessAndServicesTable" + currentTab + " tr#trASSRDescriptionOfYourComputerPlaces_1 select#selectDescriptionOfYourComputerPlaces_1").attr("value", "none");
    } else {
        var id = "_" + count;

        var facilities = $("table#accessAndServicesTable" + currentTab + " tr#trASSRDescriptionOfYourComputerPlaces" + id + " textarea#textDescriptionOfYourComputerPlaces" + id).attr("value");
        // var facilitiesLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASSRDescriptionOfYourComputerPlaces"+id+" select#selectDescriptionOfYourComputerPlaces"+id).attr("value");

        if (facilities == null || facilities == ""
                /*|| facilitiesLanguage == "none"*/) {
            alertEmptyFields(text1);
            return;
        }

        var newId = "trASSRDescriptionOfYourComputerPlaces_" + (count + 1);
        var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable" + currentTab + " tr[id='trASSRDescriptionOfYourComputerPlaces_1']").clone().html() + "</tr>";
        var lastId = "table#accessAndServicesTable" + currentTab + " tr#trASSRDescriptionOfYourComputerPlaces_" + count;

        $(lastId).after(trHtml);
        //update last content
        $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textDescriptionOfYourComputerPlaces_1']").attr("for", "textDescriptionOfYourComputerPlaces_" + (count + 1));
        $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textDescriptionOfYourComputerPlaces_1").attr("id", "textDescriptionOfYourComputerPlaces_" + (count + 1));
        $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='selectDescriptionOfYourComputerPlaces_1']").attr("for", "selectDescriptionOfYourComputerPlaces_" + (count + 1));
        $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectDescriptionOfYourComputerPlaces_1").attr("id", "selectDescriptionOfYourComputerPlaces_" + (count + 1));

        // Reset parametters and enable fields.
        $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
            $(this).val(""); // Clean all textarea.
        });
        $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectDescriptionOfYourComputerPlaces_" + (count + 1)).attr("value", "none");
    }
}

function aSSRAddReadersTicket(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASSRReadersTicket']").length;

    var id = "_" + count;

    var reader = $("table#accessAndServicesTable" + currentTab + " tr#trASSRReadersTicket" + id + " textarea#textASSRReadersTicket" + id).attr("value");
    // var readerLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASSRReadersTicket"+id+" select#selectReadersTickectLanguage"+id).attr("value");
    var readerLink = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASSRReadersTicket" + id + " textarea#textASSRRTLink" + id).attr("value");

    if ((reader == null || reader == "")
            /*|| readerLanguage == "none"*/
            && (readerLink == null || readerLink == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trASSRReadersTicket_" + (count + 1);
    var target2 = "tr2ASSRReadersTicket_" + (count + 1);
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#tr2ASSRReadersTicket_" + count;

    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#accessAndServicesTable" + currentTab + " tr#trASSRReadersTicket_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#accessAndServicesTable" + currentTab + " tr#tr2ASSRReadersTicket_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);

    //update elements
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='textASSRReadersTicket_1']").attr("for", "textASSRReadersTicket_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textASSRReadersTicket_1").attr("id", "textASSRReadersTicket_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='selectReadersTickectLanguage_1']").attr("for", "selectReadersTickectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectReadersTickectLanguage_1").attr("id", "selectReadersTickectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textASSRRTLink_1']").attr("for", "textASSRRTLink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASSRRTLink_1").attr("id", "textASSRRTLink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " p#undefined_w_required").remove();

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectReadersTickectLanguage_" + (count + 1)).attr("value", "none");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
}

function aSSRAddFurtherOrderInformation(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASSRAddFurtherOrderInformation']").length;

    var id = "_" + count;

    var further = $("table#accessAndServicesTable" + currentTab + " tr#trASSRAddFurtherOrderInformation" + id + " textarea#textASSRAdvancedOrders" + id).attr("value");
    // var furtherLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASSRAddFurtherOrderInformation"+id+" select#selectASSRAFOIUSelectLanguage"+id).attr("value");
    var furtherLink = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASSRAddFurtherOrderInformation" + id + " textarea#textASSRAOLink" + id).attr("value");

    if ((further == null || further == "")
            /*|| furtherLanguage == "none"*/
            && (furtherLink == null || furtherLink == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trASSRAddFurtherOrderInformation_" + (count + 1);
    var target2 = "tr2ASSRAddFurtherOrderInformation_" + (count + 1);
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#tr2ASSRAddFurtherOrderInformation_" + count;

    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#accessAndServicesTable" + currentTab + " tr#trASSRAddFurtherOrderInformation_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#accessAndServicesTable" + currentTab + " tr#tr2ASSRAddFurtherOrderInformation_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);

    //update elements
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='textASSRAdvancedOrders_1']").attr("for", "textASSRAdvancedOrders_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textASSRAdvancedOrders_1").attr("id", "textASSRAdvancedOrders_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='selectASSRAFOIUSelectLanguage_1']").attr("for", "selectASSRAFOIUSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASSRAFOIUSelectLanguage_1").attr("id", "selectASSRAFOIUSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textASSRAOLink_1']").attr("for", "textASSRAOLink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASSRAOLink_1").attr("id", "textASSRAOLink_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " p#undefined_w_required").remove();

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASSRAFOIUSelectLanguage_" + (count + 1)).attr("value", "none");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
}

function aSAddResearchServices(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASSRResearchServices']").length;

    var id = "_" + count;

    var research = $("table#accessAndServicesTable" + currentTab + " tr#trASSRResearchServices" + id + " textarea#textASSRResearchServices" + id).attr("value");
    // var researchLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASSRResearchServices"+id+" select#textASSRRSSelectLanguage"+id).attr("value");

    if (research == null || research == ""
            /*|| researchLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trASSRResearchServices_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable tr[id='trASSRResearchServices_1']").clone().html() + "</tr>";
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#trASSRResearchServices_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textASSRResearchServices_1']").attr("for", "textASSRResearchServices_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textASSRResearchServices_1").attr("id", "textASSRResearchServices_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textASSRRSSelectLanguage_1']").attr("for", "textASSRRSSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#textASSRRSSelectLanguage_1").attr("id", "textASSRRSSelectLanguage_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#textASSRRSSelectLanguage_" + (count + 1)).attr("value", "none");
}

function aSPIAAddInternetAccessInformation(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASPIAAddInternetAccessInformation']").length;

    var id = "_" + count;

    var description = $("table#accessAndServicesTable" + currentTab + " tr#trASPIAAddInternetAccessInformation" + id + " textarea#textASDescription" + id).attr("value");
    // var descriptionLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASPIAAddInternetAccessInformation"+id+" select#selectASDSelectLanguage"+id).attr("value");

    if (description == null || description == ""
            /*|| descriptionLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trASPIAAddInternetAccessInformation_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable tr[id='trASPIAAddInternetAccessInformation_1']").clone().html() + "</tr>";
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#trASPIAAddInternetAccessInformation_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textASDescription_1']").attr("for", "textASDescription_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textASDescription_1").attr("id", "textASDescription_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='selectASDSelectLanguage_1']").attr("for", "selectASDSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASDSelectLanguage_1").attr("id", "selectASDSelectLanguage_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASDSelectLanguage_" + (count + 1)).attr("value", "none");
}

function addADescriptionOfYourRestaurationLab(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASTSDescriptionOfRestaurationLab']").length;

    var id = "_" + count;

    var description = $("table#accessAndServicesTable" + currentTab + " tr#trASTSDescriptionOfRestaurationLab" + id + " textarea#textASTSDescriptionOfRestaurationLab" + id).attr("value");
    // var descriptionLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASTSDescriptionOfRestaurationLab"+id+" select#selectASTSSelectLanguage"+id).attr("value");

    if (description == null || description == ""
            /*|| descriptionLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trASTSDescriptionOfRestaurationLab_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable tr[id='trASTSDescriptionOfRestaurationLab_1']").clone().html() + "</tr>";
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#trASTSDescriptionOfRestaurationLab_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textASTSDescriptionOfRestaurationLab_1']").attr("for", "textASTSDescriptionOfRestaurationLab_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textASTSDescriptionOfRestaurationLab_1").attr("id", "textASTSDescriptionOfRestaurationLab_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='selectASTSSelectLanguage_1']").attr("for", "selectASTSSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASTSSelectLanguage_1").attr("id", "selectASTSSelectLanguage_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASTSSelectLanguage_" + (count + 1)).attr("value", "none");
}

function aSAddADescriptionOfYourReproductionService(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASTSDescriptionOfReproductionService']").length;

    var id = "_" + count;

    var description = $("table#accessAndServicesTable" + currentTab + " tr#trASTSDescriptionOfReproductionService" + id + " textarea#textASTSDescriptionOfReproductionService" + id).attr("value");
    // var descriptionLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASTSDescriptionOfReproductionService"+id+" select#selectASTSRSSelectLanguage"+id).attr("value");

    if (description == null || description == ""
            /*|| descriptionLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trASTSDescriptionOfReproductionService_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable tr[id='trASTSDescriptionOfReproductionService_1']").clone().html() + "</tr>";
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#trASTSDescriptionOfReproductionService_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textASTSDescriptionOfReproductionService_1']").attr("for", "textASTSDescriptionOfReproductionService_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textASTSDescriptionOfReproductionService_1").attr("id", "textASTSDescriptionOfReproductionService_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='selectASTSRSSelectLanguage_1']").attr("for", "selectASTSRSSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASTSRSSelectLanguage_1").attr("id", "selectASTSRSSelectLanguage_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASTSRSSelectLanguage_" + (count + 1)).attr("value", "none");
}

function aSReSeAddFurtherRefreshment(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASReSeRefreshment']").length;

    var id = "_" + count;

    var refreshment = $("table#accessAndServicesTable" + currentTab + " tr#trASReSeRefreshment" + id + " textarea#textASReSeRefreshment" + id).attr("value");
    // var refreshmentLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeRefreshment"+id+" select#selectASReSeRefreshmentSelectLanguage"+id).attr("value");

    if (refreshment == null || refreshment == ""
            /*|| refreshmentLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trASReSeRefreshment_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#accessAndServicesTable" + currentTab + " tr[id='trASReSeRefreshment_1']").clone().html() + "</tr>";
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#trASReSeRefreshment_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='textASReSeRefreshment_1']").attr("for", "textASReSeRefreshment_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea#textASReSeRefreshment_1").attr("id", "textASReSeRefreshment_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " label[for='selectASReSeRefreshmentSelectLanguage_1']").attr("for", "selectASReSeRefreshmentSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASReSeRefreshmentSelectLanguage_1").attr("id", "selectASReSeRefreshmentSelectLanguage_" + (count + 1));

    // Reset parametters.
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + newId + " select#selectASReSeRefreshmentSelectLanguage_" + (count + 1)).attr("value", "none");
}

function aSReSeAddExhibition(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASReSeExhibition']").length;

    var id = "_" + count;

    var exhibition = $("table#accessAndServicesTable" + currentTab + " tr#trASReSeExhibition" + id + " textarea#textASReSeExhibition" + id).attr("value");
    //var exhibitionLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeExhibition"+id+" select#selectASReSeExhibitionSelectLanguage"+id).attr("value");
    var web = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeExhibition" + id + " textarea#textASReSeWebpage" + id).attr("value");
    var webLink = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeExhibition" + id + " textarea#textASReSeWebpageLinkTitle" + id).attr("value");

    if ((exhibition == null || exhibition == "")
            /*|| exhibitionLanguage == "none"*/
            && (web == null || web == "")
            && (webLink == null || webLink == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trASReSeExhibition_" + (count + 1);
    var target2 = "tr2ASReSeExhibition_" + (count + 1);
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeExhibition_" + count;

    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#accessAndServicesTable" + currentTab + " tr#trASReSeExhibition_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeExhibition_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);
    //update new elements
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='textASReSeExhibition_1']").attr("for", "textASReSeExhibition_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textASReSeExhibition_1").attr("id", "textASReSeExhibition_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='selectASReSeExhibitionSelectLanguage_1']").attr("for", "selectASReSeExhibitionSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASReSeExhibitionSelectLanguage_1").attr("id", "selectASReSeExhibitionSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textASReSeWebpage_1']").attr("for", "textASReSeWebpage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASReSeWebpage_1").attr("id", "textASReSeWebpage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textASReSeWebpageLinkTitle_1']").attr("for", "textASReSeWebpageLinkTitle_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASReSeWebpageLinkTitle_1").attr("id", "textASReSeWebpageLinkTitle_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " p#undefined_w_required").remove();

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASReSeExhibitionSelectLanguage_" + (count + 1)).attr("value", "none");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
}

function aSReSeToursAndSessions(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASReSeToursAndSessions']").length;

    var id = "_" + count;

    var tours = $("table#accessAndServicesTable" + currentTab + " tr#trASReSeToursAndSessions" + id + " textarea#textASReSeToursAndSessions" + id).attr("value");
    // var toursLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeToursAndSessions"+id+" select#selectASReSeToursAndSessionsSelectLanguage"+id).attr("value");
    var web = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeToursAndSessions" + id + " textarea#textASReSeTSWebpage" + id).attr("value");
    var webLink = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeToursAndSessions" + id + " textarea#textASReSeWebpageTSLinkTitle" + id).attr("value");

    if ((tours == null || tours == "")
            /*|| toursLanguage == "none"*/
            && (web == null || web == "")
            && (webLink == null || webLink == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trASReSeToursAndSessions_" + (count + 1);
    var target2 = "tr2ASReSeToursAndSessions_" + (count + 1);
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeToursAndSessions_" + count;

    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#accessAndServicesTable" + currentTab + " tr#trASReSeToursAndSessions_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeToursAndSessions_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);
    //update new elements
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='textASReSeToursAndSessions_1']").attr("for", "textASReSeToursAndSessions_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textASReSeToursAndSessions_1").attr("id", "textASReSeToursAndSessions_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='selectASReSeToursAndSessionsSelectLanguage_1']").attr("for", "selectASReSeToursAndSessionsSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASReSeToursAndSessionsSelectLanguage_1").attr("id", "selectASReSeToursAndSessionsSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textASReSeTSWebpage_1']").attr("for", "textASReSeTSWebpage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASReSeTSWebpage_1").attr("id", "textASReSeTSWebpage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textASReSeWebpageTSLinkTitle_1']").attr("for", "textASReSeWebpageTSLinkTitle_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASReSeWebpageTSLinkTitle_1").attr("id", "textASReSeWebpageTSLinkTitle_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " p#undefined_w_required").remove();

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASReSeToursAndSessionsSelectLanguage_" + (count + 1)).attr("value", "none");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
}

function aSAddServices(text1) {
    var currentTab = getCurrentTab();
    var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASReSeOtherServices']").length;

    var id = "_" + count;

    var other = $("table#accessAndServicesTable" + currentTab + " tr#trASReSeOtherServices" + id + " textarea#textASReSeOtherServices" + id).attr("value");
    // var otherLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeOtherServices"+id+" select#selectASReSeOtherServicesSelectLanguage"+id).attr("value");
    var web = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeOtherServices" + id + " textarea#textASReSeOSWebpage" + id).attr("value");
    var webLink = $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeOtherServices" + id + " textarea#textASReSeWebpageOSLinkTitle" + id).attr("value");

    if ((other == null || other == "")
            /*|| otherLanguage == "none"*/
            && (web == null || web == "")
            && (webLink == null || webLink == "")) {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trASReSeOtherServices_" + (count + 1);
    var target2 = "tr2ASReSeOtherServices_" + (count + 1);
    var lastId = "table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeOtherServices_" + count;

    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#accessAndServicesTable" + currentTab + " tr#trASReSeOtherServices_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#accessAndServicesTable" + currentTab + " tr#tr2ASReSeOtherServices_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);
    //update new elements
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='textASReSeOtherServices_1']").attr("for", "textASReSeOtherServices_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea#textASReSeOtherServices_1").attr("id", "textASReSeOtherServices_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " label[for='selectASReSeOtherServicesSelectLanguage_1']").attr("for", "selectASReSeOtherServicesSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASReSeOtherServicesSelectLanguage_1").attr("id", "selectASReSeOtherServicesSelectLanguage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textASReSeOSWebpage_1']").attr("for", "textASReSeOSWebpage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASReSeOSWebpage_1").attr("id", "textASReSeOSWebpage_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " label[for='textASReSeWebpageOSLinkTitle_1']").attr("for", "textASReSeWebpageOSLinkTitle_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea#textASReSeWebpageOSLinkTitle_1").attr("id", "textASReSeWebpageOSLinkTitle_" + (count + 1));
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " p#undefined_w_required").remove();

    // Reset parametters and enable fields.
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#accessAndServicesTable" + currentTab + " tr#" + target1 + " select#selectASReSeOtherServicesSelectLanguage_" + (count + 1)).attr("value", "none");
    $("table#accessAndServicesTable" + currentTab + " tr#" + target2 + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
}

function descriptionAddHistoryDescription(text1) {
    var currentTab = getCurrentTab();
    // trAddHistory
    var count = $("table#descriptionTable" + currentTab + " tr[id^='trRepositoryHistory']").length;

    var id = "_" + count;

    var history = $("table#descriptionTable" + currentTab + " tr#trRepositoryHistory" + id + " textarea#textRepositoryHistory" + id).attr("value");
    // var historyLanguage = $("table#descriptionTable"+currentTab+" tr#trRepositoryHistory"+id+" select#selectLanguageRepositoryHistory"+id).attr("value");

    if (history == null || history == ""
            /*|| historyLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trRepositoryHistory_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#descriptionTable" + currentTab + " tr[id='trRepositoryHistory_1']").clone().html() + "</tr>";
    var lastId = "table#descriptionTable" + currentTab + " tr#trRepositoryHistory_" + count;

    $(lastId).after(trHtml);
    // update last content
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdRepositoryHistory_1").attr("id", "tdRepositoryHistory_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='textRepositoryHistory_1']").attr("for", "textRepositoryHistory_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textRepositoryHistory_1").attr("id", "textRepositoryHistory_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdLanguageRepositoryHistory_1").attr("id", "tdLanguageRepositoryHistory_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='selectLanguageRepositoryHistory_1']").attr("for", "selectLanguageRepositoryHistory_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageRepositoryHistory_1").attr("id", "selectLanguageRepositoryHistory_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textRepositoryHistory_" + (count + 1)).attr("value", "");
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageRepositoryHistory_" + (count + 1)).attr("value", "none");
}

function descriptionAddFoundationInformation(text1) {
    var currentTab = getCurrentTab();
    //trRuleOfRepositoryFoundation
    var count = $("table#descriptionTable" + currentTab + " tr[id^='trRuleOfRepositoryFoundation']").length;

    var id = "_" + count;

    var foundation = $("table#descriptionTable" + currentTab + " tr#trRuleOfRepositoryFoundation" + id + " textarea#textRuleOfRepositoryFoundation" + id).attr("value");
    // var foundationLanguage = $("table#descriptionTable"+currentTab+" tr#trRuleOfRepositoryFoundation"+id+" select#selectLanguageRuleOfRepositoryFoundation"+id).attr("value");

    if (foundation == null || foundation == ""
            /*|| foundationLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trRuleOfRepositoryFoundation_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#descriptionTable" + currentTab + " tr[id='trRuleOfRepositoryFoundation_1']").clone().html() + "</tr>";
    var lastId = "table#descriptionTable" + currentTab + " tr#trRuleOfRepositoryFoundation_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdRuleOfRepositoryFoundation_1").attr("id", "tdRuleOfRepositoryFoundation_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='textRuleOfRepositoryFoundation_1']").attr("for", "textRuleOfRepositoryFoundation_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textRuleOfRepositoryFoundation_1").attr("id", "textRuleOfRepositoryFoundation_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdLanguageRuleOfRepositoryFoundation_1").attr("id", "tdLanguageRuleOfRepositoryFoundation_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='selectLanguageRuleOfRepositoryFoundation_1']").attr("for", "selectLanguageRuleOfRepositoryFoundation_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageRuleOfRepositoryFoundation_1").attr("id", "selectLanguageRuleOfRepositoryFoundation_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#descriptionTable" + currentTab + " tr#" + newId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageRuleOfRepositoryFoundation_" + (count + 1)).attr("value", "none");
}

function descriptionAddSuppressionInformation(text1) {
    var currentTab = getCurrentTab();
    //trDescriptionAddSuppressionInformation
    var count = $("table#descriptionTable" + currentTab + " tr[id^='trDescriptionAddSuppressionInformation']").length;

    var id = "_" + count;

    var suppress = $("table#descriptionTable" + currentTab + " tr#trDescriptionAddSuppressionInformation" + id + " textarea#textRuleOfRepositorySuppression" + id).attr("value");
    // var suppressLanguage = $("table#descriptionTable"+currentTab+" tr#trDescriptionAddSuppressionInformation"+id+" select#selectLanguageRuleOfRepositorySuppression"+id).attr("value");

    if (suppress == null || suppress == ""
            /*|| suppressLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trDescriptionAddSuppressionInformation_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#descriptionTable" + currentTab + " tr[id='trDescriptionAddSuppressionInformation_1']").clone().html() + "</tr>";
    var lastId = "table#descriptionTable" + currentTab + " tr#trDescriptionAddSuppressionInformation_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdRuleOfRepositorySuppression_1").attr("id", "tdRuleOfRepositorySuppression_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='textRuleOfRepositorySuppression_1']").attr("for", "textRuleOfRepositorySuppression_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textRuleOfRepositorySuppression_1").attr("id", "textRuleOfRepositorySuppression_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdLanguageRuleOfRepositorySuppression_1").attr("id", "tdLanguageRuleOfRepositorySuppression_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='selectLanguageRuleOfRepositorySuppression_1']").attr("for", "selectLanguageRuleOfRepositorySuppression_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageRuleOfRepositorySuppression_1").attr("id", "selectLanguageRuleOfRepositorySuppression_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#descriptionTable" + currentTab + " tr#" + newId + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea").each(function () {
        $(this).val(""); // Clean all textarea.
    });
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageRuleOfRepositorySuppression_" + (count + 1)).attr("value", "none");
}

function descriptionAddAdministrationUnits(text1) {
    // trDescriptionAddAdministrationUnits
    var currentTab = getCurrentTab();
    var count = $("table#descriptionTable" + currentTab + " tr[id^='trDescriptionAddAdministrationUnits']").length;

    var id = "_" + count;

    var unit = $("table#descriptionTable" + currentTab + " tr#trDescriptionAddAdministrationUnits" + id + " textarea#textUnitOfAdministrativeStructure" + id).attr("value");
    // var unitLanguage = $("table#descriptionTable"+currentTab+" tr#trDescriptionAddAdministrationUnits"+id+" select#selectLanguageUnitOfAdministrativeStructure"+id).attr("value");

    if (unit == null || unit == ""
            /*|| unitLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trDescriptionAddAdministrationUnits_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#descriptionTable" + currentTab + " tr[id='trDescriptionAddAdministrationUnits_1']").clone().html() + "</tr>";
    var lastId = "table#descriptionTable" + currentTab + " tr#trDescriptionAddAdministrationUnits_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdUnitOfAdministrativeStructure_1").attr("id", "tdUnitOfAdministrativeStructure_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='textUnitOfAdministrativeStructure_1']").attr("for", "textUnitOfAdministrativeStructure_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textUnitOfAdministrativeStructure_1").attr("id", "textUnitOfAdministrativeStructure_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdLanguageUnitOfAdministrativeStructure_1").attr("id", "tdLanguageUnitOfAdministrativeStructure_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='selectLanguageUnitOfAdministrativeStructure_1']").attr("for", "selectLanguageUnitOfAdministrativeStructure_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageUnitOfAdministrativeStructure_1").attr("id", "selectLanguageUnitOfAdministrativeStructure_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textUnitOfAdministrativeStructure_" + (count + 1)).attr("value", "");
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageUnitOfAdministrativeStructure_" + (count + 1)).attr("value", "none");
}

function descriptionAddBuildingDescription(text1) {
    // trBuildingDescription
    var currentTab = getCurrentTab();
    var count = $("table#descriptionTable" + currentTab + " tr[id^='trBuildingDescription']").length;

    var id = "_" + count;

    var building = $("table#descriptionTable" + currentTab + " tr#trBuildingDescription" + id + " textarea#textBuilding" + id).attr("value");
    // var buildingLanguage = $("table#descriptionTable"+currentTab+" tr#trBuildingDescription"+id+" select#selectLanguageBuilding"+id).attr("value");

    if (building == null || building == ""
            /*|| buildingLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trBuildingDescription_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#descriptionTable" + currentTab + " tr[id='trBuildingDescription_1']").clone().html() + "</tr>";
    var lastId = "table#descriptionTable" + currentTab + " tr#trBuildingDescription_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdBuilding_1").attr("id", "tdBuilding_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='textBuilding_1']").attr("for", "textBuilding_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textBuilding_1").attr("id", "textBuilding_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdLanguageBuilding_1").attr("id", "tdLanguageBuilding_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='selectLanguageBuilding_1']").attr("for", "selectLanguageBuilding_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageBuilding_1").attr("id", "selectLanguageBuilding_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textBuilding_" + (count + 1)).attr("value", "");
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageBuilding_" + (count + 1)).attr("value", "none");
}

function descriptionAddAnotherArchivalDescription(text1) {
    // trArchivalAndOtherHoldings
    var currentTab = getCurrentTab();
    var count = $("table#descriptionTable" + currentTab + " tr[id^='trArchivalAndOtherHoldings']").length;

    var id = "_" + count;

    var archival = $("table#descriptionTable" + currentTab + " tr#trArchivalAndOtherHoldings" + id + " textarea#textArchivalAndOtherHoldings" + id).attr("value");
    // var archivalLanguage = $("table#descriptionTable"+currentTab+" tr#trArchivalAndOtherHoldings"+id+" select#selectLanguageArchivalAndOtherHoldings"+id).attr("value");

    if (archival == null || archival == ""
            /*|| archivalLanguage == "none"*/) {
        alertEmptyFields(text1);
        return;
    }

    var newId = "trArchivalAndOtherHoldings_" + (count + 1);
    var trHtml = "<tr id=\"" + newId + "\">" + $("table#descriptionTable" + currentTab + " tr[id='trArchivalAndOtherHoldings_1']").clone().html() + "</tr>";
    var lastId = "table#descriptionTable" + currentTab + " tr#trArchivalAndOtherHoldings_" + count;

    $(lastId).after(trHtml);
    //update last content
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdArchivalAndOtherHoldings_1").attr("id", "tdArchivalAndOtherHoldings_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='textArchivalAndOtherHoldings_1']").attr("for", "textArchivalAndOtherHoldings_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textArchivalAndOtherHoldings_1").attr("id", "textArchivalAndOtherHoldings_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " td#tdLanguageArchivalAndOtherHoldings_1").attr("id", "tdLanguageArchivalAndOtherHoldings_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " label[for='selectLanguageArchivalAndOtherHoldings_1']").attr("for", "selectLanguageArchivalAndOtherHoldings_" + (count + 1));
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageArchivalAndOtherHoldings_1").attr("id", "selectLanguageArchivalAndOtherHoldings_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#descriptionTable" + currentTab + " tr#" + newId + " textarea#textArchivalAndOtherHoldings_" + (count + 1)).attr("value", "");
    $("table#descriptionTable" + currentTab + " tr#" + newId + " select#selectLanguageArchivalAndOtherHoldings_" + (count + 1)).attr("value", "none");
}

function controlAddFurtherLangsAnsScripts(text1) {
    var count = $("table#controlTable tr[id^='trControlAddFurtherLangsAnsScriptsOne']").length;

    var id = "_" + count;

    var language = $("table#controlTable tr#trControlAddFurtherLangsAnsScriptsOne" + id + " select#selectDescriptionLanguage" + id).attr("value");
    var script = $("table#controlTable tr#trControlAddFurtherLangsAnsScriptsTwo" + id + " select#selectDescriptionScript" + id).attr("value");

    if (language == "none" || script == "none") {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trControlAddFurtherLangsAnsScriptsOne_" + (count + 1);
    var target2 = "trControlAddFurtherLangsAnsScriptsTwo_" + (count + 1);
    var lastId = "table#controlTable tr#trControlAddFurtherLangsAnsScriptsTwo_" + count;
    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#controlTable tr#trControlAddFurtherLangsAnsScriptsOne_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#controlTable tr#trControlAddFurtherLangsAnsScriptsTwo_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);
    //update rest of new elements
    $("table#controlTable tr#" + target1 + " td#tdDescriptionLanguage_1").attr("id", "tdDescriptionLanguage_" + (count + 1));
    $("table#controlTable tr#" + target1 + " label[for='selectDescriptionLanguage_1']").attr("for", "selectDescriptionLanguage_" + (count + 1));
    $("table#controlTable tr#" + target1 + " select#selectDescriptionLanguage_1").attr("id", "selectDescriptionLanguage_" + (count + 1));
    $("table#controlTable tr#" + target1 + " td#tdDescriptionScript_1").attr("id", "tdDescriptionScript_" + (count + 1));
    $("table#controlTable tr#" + target2 + " label[for='selectDescriptionScript_1']").attr("for", "selectDescriptionScript_" + (count + 1));
    $("table#controlTable tr#" + target2 + " select#selectDescriptionScript_1").attr("id", "selectDescriptionScript_" + (count + 1));
    $("table#controlTable tr#" + target1 + " td#tdDescriptionLanguage_" + (count + 1)).find("span").remove();
    $("table#controlTable tr#" + target2 + " td#tdDescriptionScript_" + (count + 1)).find("span").remove();

    // Reset parametters.
    $("table#controlTable tr#" + target1 + " select#selectDescriptionLanguage_" + (count + 1)).attr("value", "none");
    $("table#controlTable tr#" + target2 + " select#selectDescriptionScript_" + (count + 1)).attr("value", "none");
}

function addContactAbbreviation(text1) {
    var count = $("table#controlTable tr[id^='trContactAbbreviationOne']").length;

    var id = "_" + count;

    // var abbrev = $("table#controlTable tr#trContactAbbreviationOne"+id+" input#textContactAbbreviation"+id).attr("value");
    var full = $("table#controlTable tr#trContactAbbreviationTwo" + id + " textarea#textContactFullName" + id).attr("value");

    if (/*(abbrev == null || abbrev == "")
     &&*/ full == null || full == "") {
        alertEmptyFields(text1);
        return;
    }

    var target1 = "trContactAbbreviationOne_" + (count + 1);
    var target2 = "trContactAbbreviationTwo_" + (count + 1);
    var lastId = "table#controlTable tr#trContactAbbreviationTwo_" + count;
    var tr2HTML = "<tr id=\"" + target1 + "\">";
    tr2HTML += $("table#controlTable tr#trContactAbbreviationOne_1").clone().html();
    tr2HTML += "</tr>\n";
    tr2HTML += "<tr id=\"" + target2 + "\">" + $("table#controlTable tr#trContactAbbreviationTwo_1").clone().html();
    tr2HTML += "</tr>";
    $(lastId).after(tr2HTML);
    //update rest of new elements
    $("table#controlTable tr#" + target1 + " td#tdContactAbbreviation_1").attr("id", "tdContactAbbreviation_" + (count + 1));
    $("table#controlTable tr#" + target1 + " label[for='textContactAbbreviation_1']").attr("for", "textContactAbbreviation_" + (count + 1));
    $("table#controlTable tr#" + target1 + " input#textContactAbbreviation_1").attr("id", "textContactAbbreviation_" + (count + 1));
    $("table#controlTable tr#" + target2 + " td#tdContactFullName_1").attr("id", "tdContactFullName_" + (count + 1));
    $("table#controlTable tr#" + target2 + " label[for='textContactFullName_1']").attr("for", "textContactFullName_" + (count + 1));
    $("table#controlTable tr#" + target2 + " textarea#textContactFullName_1").attr("id", "textContactFullName_" + (count + 1));

    // Reset parametters and enable fields.
    $("table#controlTable tr#" + target1 + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#controlTable tr#" + target1 + " textarea").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#controlTable tr#" + target2 + " input[type='text']").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#controlTable tr#" + target2 + " textarea").each(function () {
        $(this).val(""); // Clean all input_text.
    });
}

function relationAddNewResourceRelation(text1) {
    var counter = $("table[id^='resourceRelationTable_']").length;

    var web = $("table#resourceRelationTable_" + counter + " textarea#textWebsiteOfResource").attr("value");
    var title = $("table#resourceRelationTable_" + counter + " textarea#textTitleOfRelatedMaterial").attr("value");

    if ((web == null || web == "")
            && (title == null || title == "")) {
        alertEmptyFields(text1);
        return;
    }

    var clone = $("table[id^='resourceRelationTable_" + counter + "']").clone();
    clone = "<table id='" + ("resourceRelationTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='resourceRelationTable_" + counter + "']").after(clone);
    $("table#resourceRelationTable_" + (counter + 1) + " p#undefined_w_required").remove();
    // Reset parametters.
    $("table#resourceRelationTable_" + (counter + 1) + " textarea").each(function () {
        $(this).val(""); // Clean textarea.
    });
    $("table#resourceRelationTable_" + (counter + 1) + " select#selectTypeOfYourRelation").attr("value", "creator");
    $("table#resourceRelationTable_" + (counter + 1) + " select#selectTitleOfRelatedMaterialLang").attr("value", "none");
    $("table#resourceRelationTable_" + (counter + 1) + " tr#trRelationsDescriptionOfRelation select#selectLanguageDescriptionOfRelation").attr("value", "none");
}

function relationAddNewInstitutionRelation(text1) {
    var counter = $("table[id^='institutionRelationTable_']").length;

    var web = $("table#institutionRelationTable_" + counter + " textarea#textWebsiteOfDescription").attr("value");
    var typeOfTheRelation = $("table#institutionRelationTable_" + counter + " select#selectTypeOftheRelation").attr("value");
    var title = $("table#institutionRelationTable_" + counter + " textarea#textTitleOfRelatedInstitution").attr("value");

    if ((web == null || web == "" || typeOfTheRelation == "none")
            && (title == null || title == "" || typeOfTheRelation == "none")) {
        alertEmptyFields(text1);
        return;
    }

    var clone = $("table[id^='institutionRelationTable_" + counter + "']").clone();
    clone = "<table id='" + ("institutionRelationTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='institutionRelationTable_" + counter + "']").after(clone);
    $("table#institutionRelationTable_" + (counter + 1) + " p#undefined_w_required").remove();
    // Reset parametters.
    $("table#institutionRelationTable_" + (counter + 1) + " textarea").each(function () {
        $(this).val(""); // Clean all input_text.
    });
    $("table#institutionRelationTable_" + (counter + 1) + " select#selectTypeOftheRelation").attr("value", "none");
    $("table#institutionRelationTable_" + (counter + 1) + " select#selectTitleOfRelatedInstitutionLang").attr("value", "none");
    $("table#institutionRelationTable_" + (counter + 1) + " select#selectLanguageInstitutionDescriptionOfRelation").attr("value", "none");
}

function alertEmptyFields(text1) {
    displayAlertDialog(text1);
}

function alertFillFieldsBeforeChangeTab(text1) {
    displayAlertDialog(text1);
}

// Copy content functions.
function personResponsibleForDescriptionChanged() {
    $("#textPesonResponsible").attr("value", $("#textYIPersonInstitutionResposibleForTheDescription").val());
}

function idOfInstitutionChanged(text, text1, index) {
    var countrycode = "(AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BA|BW|BV|BR|IO|BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|IL|IT|JM|JP|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|FM|MD|MC|MN|MS|MA|MZ|MM|NA|NR|NP|NL|AN|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|RO|RU|RW|SH|KN|LC|PM|VC|WS|SM|ST|SA|SN|CS|SC|SL|SG|SK|SI|SB|SO|ZA|GS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW|RS|ME|EU|EUR|IM|XX)";
    var pattern = countrycode + "-[a-zA-Z0-9:\/\\-]{1,11}";
    var identifier = $("#textYIIdentifierOfTheInstitution").val();
    var matched = identifier.match(pattern);
    var repeat = true;
    var check = true;

    if (index != undefined) {
        var furtherId = $("#otherRepositorId_" + index).val();
        $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function (i, v) {
            var otherFurtherId = $("#otherRepositorId_" + i).val();
            if (identifier == otherFurtherId && check && identifier != "") {
                displayAlertDialog("\"" + identifier + "\" " + text);
                $("#otherRepositorId_" + i).attr("value", "");
                if ($("#selectOtherRepositorIdCodeISIL_" + index).val() == "yes") {
                    $("#selectOtherRepositorIdCodeISIL_" + index).attr("value", "no");
                    $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                        $(this).removeAttr("disabled");
                    });
                    $("#selectYICodeISIL").removeAttr("disabled");
                    var id = $("#recordIdHidden").val();
                    $("#textYIIdUsedInAPE").attr("value", id);
                    $("#textIdentityIdUsedInAPE").attr("value", id);
                    $("#textDescriptionIdentifier").attr("value", id);
                }
                check = false;
            } else {
                repeat = false;
            }

            if (i != index) {
                if ((furtherId == otherFurtherId
                        || furtherId == identifier) && check && furtherId != "") {
                    displayAlertDialog("\"" + furtherId + "\" " + text);
                    $("#otherRepositorId_" + index).attr("value", "");
                    if ($("#selectOtherRepositorIdCodeISIL_" + index).val() == "yes") {
                        $("#selectOtherRepositorIdCodeISIL_" + index).attr("value", "no");
                        $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                            $(this).removeAttr("disabled");
                        });
                        $("#selectYICodeISIL").removeAttr("disabled");
                        var id = $("#recordIdHidden").val();
                        $("#textYIIdUsedInAPE").attr("value", id);
                        $("#textIdentityIdUsedInAPE").attr("value", id);
                        $("#textDescriptionIdentifier").attr("value", id);
                    }
                    check = false;
                } else {
                    repeat = false;
                }
            }
        });
    } else {
        $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function (i) {
            var otherFurtherId = $("#otherRepositorId_" + (i)).val();
            if (identifier == otherFurtherId && check && identifier != "") {
                displayAlertDialog("\"" + identifier + "\" " + text);
                $("#textYIIdentifierOfTheInstitution").attr("value", "");
                if ($("#selectYICodeISIL").val() == "yes") {
                    $("#selectYICodeISIL").attr("value", "no");
                    $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                        $(this).removeAttr("disabled");
                    });
                    var id = $("#recordIdHidden").val();
                    $("#textYIIdUsedInAPE").attr("value", id);
                    $("#textIdentityIdUsedInAPE").attr("value", id);
                    $("#textDescriptionIdentifier").attr("value", id);
                }
                check = false;
            } else {
                repeat = false;
            }
        });
    }

    $("#textIdentityIdentifierOfTheInstitution").attr("value", $("#textYIIdentifierOfTheInstitution").val());

    if (index == undefined /*&& repeat*/) {
        if ($("#selectYICodeISIL").val() == "yes") {
            if (matched == null || matched != identifier) {
                $("#selectYICodeISIL").attr("value", "no");
                displayAlertDialog("\"" + identifier + "\" " + text1);
                $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                    $(this).removeAttr("disabled");
                });
                var id = $("#recordIdHidden").val();
                $("#textYIIdUsedInAPE").attr("value", id);
                $("#textIdentityIdUsedInAPE").attr("value", id);
                $("#textDescriptionIdentifier").attr("value", id);
            } else {
                $("#textYIIdUsedInAPE").attr("value", $("#textYIIdentifierOfTheInstitution").val());
                $("#textIdentityIdUsedInAPE").attr("value", $("#textYIIdentifierOfTheInstitution").val());
                $("#textDescriptionIdentifier").attr("value", $("#textYIIdentifierOfTheInstitution").val());
            }
        }
    } else {
        if ($("#selectOtherRepositorIdCodeISIL_" + index).val() == "yes" && index != undefined && repeat) {
            var furtherId = $("#otherRepositorId_" + index).val();
            var matched1 = furtherId.match(pattern);
            if (matched1 == null || matched1 != furtherId) {
                $("#selectOtherRepositorIdCodeISIL_" + index).attr("value", "no");
                displayAlertDialog("\"" + furtherId + "\" " + text1);
                $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                    $(this).removeAttr("disabled");
                });
                $("#selectYICodeISIL").removeAttr("disabled");
                var id = $("#recordIdHidden").val();
                $("#textYIIdUsedInAPE").attr("value", id);
                $("#textIdentityIdUsedInAPE").attr("value", id);
                $("#textDescriptionIdentifier").attr("value", id);
            } else {
                $("#textYIIdUsedInAPE").attr("value", $("#otherRepositorId_" + index).val());
                $("#textIdentityIdUsedInAPE").attr("value", $("#otherRepositorId_" + index).val());
                $("#textDescriptionIdentifier").attr("value", $("#otherRepositorId_" + index).val());
            }
        }
        //		 }
    }
}

function firstIdAndNoISIL() {
    var idUsedInAPE = $("#textYIIdUsedInAPE").val();

    if (idUsedInAPE == null || idUsedInAPE == "") {
        var id = $("#recordIdHidden").val();
        $("#textYIIdUsedInAPE").attr("value", id);
        $("#textIdentityIdUsedInAPE").attr("value", id);
        $("#textDescriptionIdentifier").attr("value", id);
    }
}

function nameOfInstitutionChanged(text, institutionName) {
    $("#textNameOfTheInstitution").attr("value", $("#textYINameOfTheInstitution").val());

    checkNameOfInstitution(text, institutionName);
}

function checkNameOfInstitution(text, institutionName) {
    // Check if value for the field "Name of the institution",  in tab "Your Institution", is the same as
    // the current institution name.
    if (institutionName != escapeDoubleQuote(escapeApostrophe($("#textYINameOfTheInstitution")))) {
        // Check if any of the values for the field "Name of the institution", in tab "Identity", is the same as
        // the current institution name.
        var exists = false;
        $("table[id^='identityTableNameOfTheInstitution']").each(function (i) {
            if (institutionName == escapeDoubleQuote(escapeApostrophe($("table#identityTableNameOfTheInstitution_" + (i + 1) + " #textNameOfTheInstitution")))) {
                exists = true;
            }
        });
        if (!exists) {
            alert(text);
            return false;
        }
    }
    return true;
}

function nameOfInstitutionLanguageChanged() {
    $("#noti_languageList").attr("value", $("#selectYINOTISelectLanguage").val());
}

function parallelNameOfInstitutionChanged() {
    $("#textParallelNameOfTheInstitution").attr("value", $("#textYIParallelNameOfTheInstitution").val());
}

function parallelNameOfInstitutionLanguageChanged() {
    $("#pnoti_languageList").attr("value", $("#selectYIPNOTISelectLanguage").val());
}

function streetOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #textContactStreetOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress" + id + " #textYIStreet").val());
}

function contactStreetOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    $("table#yiTableVisitorsAddress" + id + " #textYIStreet").attr("value", $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #textContactStreetOfTheInstitution").val());
}

function streetOfInstitutionLanguageChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (($("table#yiTableVisitorsAddress" + id + " #selectYIVASelectLanguage").val() != "noneidOfInstitutionChanged()") && ($("table#yiTableVisitorsAddress" + id + " #selectYIVASelectLanguage").val() != null)) {
        $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #selectLanguageVisitorAddress").attr("value", $("table#yiTableVisitorsAddress" + id + " #selectYIVASelectLanguage").val());
        if (id == "_1") {
            $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #selectLanguageVisitorAddress").attr("disabled", "disabled");
        }
    } else {
        if (id == "_1") {
            $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #selectLanguageVisitorAddress").removeAttr("disabled");
        }
    }
}

function contactCityOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    $("table#yiTableVisitorsAddress" + id + " #textYICity").attr("value", $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #textContactCityOfTheInstitution").val());
}

function cityOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #textContactCityOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress" + id + " #textYICity").val());
}

function countryOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #textContactCountryOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress" + id + " #textYICountry").val());
}

function contactCountryOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    $("table#yiTableVisitorsAddress" + id + " #textYICountry").attr("value", $("table#contactTable_1 table#contactTableVisitorsAddress" + id + " #textContactCountryOfTheInstitution").val());
}
function contactAddressCityChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    var value = $("table#contactTable_1 table#contactTablePostalAddress" + id + " #textContactPACity").val();
    $("table#yiTablePostalAddress" + id + " #textYIPACity").attr("value", value);
}
function contactAddressStreetChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    var value = $("table#contactTable_1 table#contactTablePostalAddress" + id + " #textContactPAStreet").val();
    $("table#yiTablePostalAddress" + id + " #textYIPAStreet").attr("value", value);
}
function contactAddressLanguageChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    var value = $("table#contactTable_1 table#contactTablePostalAddress" + id + " #selectContactLanguagePostalAddress").val();
    $("table#yiTablePostalAddress" + id + " #selectYIPASelectLanguage").attr("value", value);
}
function postalAddressStreetChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (($("table#yiTablePostalAddress" + id + " #textYIPAStreet").val() != "") && ($("table#yiTablePostalAddress" + id + " #textYIPAStreet").val() != null)) {
        $("table#contactTable_1 table#contactTablePostalAddress" + id + " #textContactPAStreet").attr("value", $("table#yiTablePostalAddress" + id + " #textYIPAStreet").val());
    } else {
        $("table#contactTable_1 table#contactTablePostalAddress" + id + " #textContactPAStreet").val("");
    }
}
function postalAddressCityChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (($("table#yiTablePostalAddress" + id + " #textYIPACity").val() != "") && ($("table#yiTablePostalAddress" + id + " #textYIPACity").val() != null)) {
        $("table#contactTable_1 table#contactTablePostalAddress" + id + " #textContactPACity").attr("value", $("table#yiTablePostalAddress" + id + " #textYIPACity").val());
    } else {
        $("table#contactTable_1 table#contactTablePostalAddress" + id + " #textContactPACity").val("");
    }
}

function postalAddressLanguageChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    var value = $("table#yiTablePostalAddress" + id + " #selectYIPASelectLanguage").val();
    $("table#contactTable_1 table#contactTablePostalAddress" + id + " #selectContactLanguagePostalAddress option").each(function () {
        if ($(this).val() == value) {
            $(this).attr("selected", "selected");
        }
    });
}

function latitudeOfInstitutionChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));
    var currentTab = getCurrentTab();
    var latitudeValue = $("table#yiTableVisitorsAddress" + currentTab + " #textYILatitude").val();

    $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress" + parentId + " #textContactLatitudeOfTheInstitution").attr("value", latitudeValue);

    $("table#contactTable" + currentTab + " table[id^=contactTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#contactTable" + currentTab + " table#" + id + " #textContactLatitudeOfTheInstitution").attr("value", latitudeValue);
    });
    $("table[id^=yiTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#" + id + " #textYILatitude").attr("value", latitudeValue);
    });
}
function longitudeOfInstitutionChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));
    var currentTab = getCurrentTab();
    var longitudeValue = $("table#yiTableVisitorsAddress" + currentTab + " #textYILongitude").val();

    $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress" + parentId + " #textContactLongitudeOfTheInstitution").attr("value", longitudeValue);

    $("table#contactTable" + currentTab + " table[id^=contactTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#contactTable" + currentTab + " table#" + id + " #textContactLongitudeOfTheInstitution").attr("value", longitudeValue);
    });
    $("table[id^=yiTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#" + id + " #textYILongitude").attr("value", longitudeValue);
    });
}

function latitudeOfRepoChanged() {
    var currentTab = getCurrentTab();
    var latitudeValue = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_1 #textContactLatitudeOfTheInstitution").val();

    $("table#contactTable" + currentTab + " table[id^=contactTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#contactTable" + currentTab + " table#" + id + " #textContactLatitudeOfTheInstitution").attr("value", latitudeValue);
    });
}
function longitudeOfRepoChanged() {
    var currentTab = getCurrentTab();
    var longitudeValue = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_1 #textContactLongitudeOfTheInstitution").val();

    $("table#contactTable" + currentTab + " table[id^=contactTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#contactTable" + currentTab + " table#" + id + " #textContactLongitudeOfTheInstitution").attr("value", longitudeValue);
    });
}

function continentOfInstitutionChanged() {
    $("table#contactTable_1 #selectContinentOfTheInstitution").attr("value", $("table#yiTableOthers #selectYIContinent").val());
}

function telephoneOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        if (($("table#yiTableOthers #textYITelephone" + id).val() != "") && ($("table#yiTableOthers #textYITelephone" + id).val() != null)) {
            if (id == null || id == "") {
                $("table#contactTable_1 tr#trTelephoneOfTheInstitution_1 #textContactTelephoneOfTheInstitution_1").attr("value", $("table#yiTableOthers #textYITelephone").val());
                //			$("table#contactTable_1 tr#trTelephoneOfTheInstitution #textContactTelephoneOfTheInstitution").attr("disabled","disabled");
            } else {
                $("table#contactTable_1 tr#trTelephoneOfTheInstitution" + id + " #textContactTelephoneOfTheInstitution" + id).attr("value", $("table#yiTableOthers #textYITelephone" + id).val());
            }
        } else {
            if (id == null || id == "") {
                //			$("table#contactTable_1 tr#trTelephoneOfTheInstitution #textContactTelephoneOfTheInstitution").removeAttr("disabled");
                $("table#contactTable_1 tr#trTelephoneOfTheInstitution_1 #textContactTelephoneOfTheInstitution_1").val("");
            } else {
                $("table#contactTable_1 tr#trTelephoneOfTheInstitution" + id + " #textContactTelephoneOfTheInstitution" + id).val("");
            }
        }
    } else {
        if (($("table#yiTableOthers #textYITelephone").val() != "") && ($("table#yiTableOthers #textYITelephone").val() != null)) {
            $("table#contactTable_1 tr#trTelephoneOfTheInstitution_1 #textContactTelephoneOfTheInstitution_1").attr("value", $("table#yiTableOthers #textYITelephone").val());
//			$("table#contactTable_1 tr#trTelephoneOfTheInstitution #textContactTelephoneOfTheInstitution").attr("disabled","disabled");
        } else {
//			$("table#contactTable_1 tr#trTelephoneOfTheInstitution #textContactTelephoneOfTheInstitution").removeAttr("disabled");
            $("table#contactTable_1 tr#trTelephoneOfTheInstitution_1 #textContactTelephoneOfTheInstitution_1").val("");
        }
    }
}

function emailOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        if (($("table#yiTableOthers #textYIEmailAddress" + id).val() != "") && ($("table#yiTableOthers #textYIEmailAddress" + id).val() != null)) {
            if (id == null || id == "") {
                $("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution_1").attr("value", $("table#yiTableOthers #textYIEmailAddress").val());
                //		$("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution").attr("disabled","disabled");
            } else {
                $("table#contactTable_1 tr#trEmailOfTheInstitution" + id + " #textContactEmailOfTheInstitution" + id).attr("value", $("table#yiTableOthers #textYIEmailAddress" + id).val());
            }
        } else {
            if (id == null || id == "") {
                //			$("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution").removeAttr("disabled");
                $("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution_1").val("");
            } else {
                $("table#contactTable_1 tr#trEmailOfTheInstitution" + id + " #textContactEmailOfTheInstitution" + id).val("");
            }
        }
    } else {
        if (($("table#yiTableOthers #textYIEmailAddress").val() != "") && ($("table#yiTableOthers #textYIEmailAddress").val() != null)) {
            $("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution_1").attr("value", $("table#yiTableOthers #textYIEmailAddress").val());
        } else {
            //		$("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution").removeAttr("disabled");
            $("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution_1").val("");
        }
    }
}
function emailOfInstitutionLangChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#contactTable_1 tr#trEmailOfTheInstitution" + id + " #selectEmailLanguageOfTheInstitution" + id).attr("value", $("table#yiTableOthers #selectTextYILangEmail" + id).val());
    } else {
        $("table#contactTable_1 tr#trEmailOfTheInstitution #selectEmailLanguageOfTheInstitution_1").attr("value", $("table#yiTableOthers #selectTextYILangEmail").val());
    }
}
function emailOfInstitutionLinkChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#contactTable_1 tr#trLanguageEmailOfTheInstitution" + id + " #textContactLinkTitleForEmailOfTheInstitution" + id).attr("value", $("table#yiTableOthers #textYIEmailLinkTitle" + id).val());

    } else {
        $("table#contactTable_1 tr#trLanguageEmailOfTheInstitution #textContactLinkTitleForEmailOfTheInstitution_1").attr("value", $("table#yiTableOthers #textYIEmailLinkTitle").val());
    }
}

function webOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        if (($("table#yiTableOthers #textYIWebpage" + id).val() != "") && ($("table#yiTableOthers #textYIWebpage" + id).val() != null)) {
            if (id == null || id == "") {
                $("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution_1").attr("value", $("table#yiTableOthers #textYIWebpage").val());
                //  $("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution").attr("disabled","disabled");
            } else {
                $("table#contactTable_1 tr#trWebOfTheInstitution" + id + " #textContactWebOfTheInstitution" + id).attr("value", $("table#yiTableOthers #textYIWebpage" + id).val());
            }
        } else {
            if (id == null || id == "") {
                //$("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution").removeAttr("disabled");
                $("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution_1").val("");
            } else {
                $("table#contactTable_1 tr#trWebOfTheInstitution" + id + " #textContactWebOfTheInstitution" + id).val("");
            }
        }
    } else {
        if (($("table#yiTableOthers #textYIWebpage").val() != "") && ($("table#yiTableOthers #textYIWebpage").val() != null)) {
            $("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution_1").attr("value", $("table#yiTableOthers #textYIWebpage").val());
            //  $("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution").attr("disabled","disabled");
        } else {
            //$("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution").removeAttr("disabled");
            $("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution_1").val("");
        }
    }
}

function openingHoursOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#accessAndServicesTable_1 tr#trASOpeningTimes_" + id + " #textOpeningTimes" + id).attr("value", $("table#yiTableOthers #textYIOpeningTimes" + id).val());
    } else {
        $("table#accessAndServicesTable_1 tr#trASOpeningTimes_1 #textOpeningTimes_1").attr("value", $("table#yiTableOthers #textYIOpeningTimes").val());
    }
}

function openingHoursLinkOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#accessAndServicesTable_1 tr#tr2ASOpeningTimes_" + id + " #linkOpeningTimes" + id).attr("value", $("table#yiTableOthers #linkYIOpeningTimes" + id).val());
    } else {
        $("table#accessAndServicesTable_1 tr#tr2ASOpeningTimes_1 #linkOpeningTimes_1").attr("value", $("table#yiTableOthers #linkYIOpeningTimes").val());
    }
}

function closingHoursOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#accessAndServicesTable_1 tr#trASClosingDates" + id + " #textClosingDates" + id).attr("value", $("table#yiTableOthers #yourInstitutionClosingDates" + id).val());
    } else {
        $("table#accessAndServicesTable_1 tr#trASClosingDates_1 #textClosingDates_1").attr("value", $("table#yiTableOthers #yourInstitutionClosingDates").val());
    }
}

function accessibleToThePublicChanged() {
    $("table#accessAndServicesTable_1 #selectASAccesibleToThePublic").attr("value", $("table#yiTableOthers #selectAccessibleToThePublic").val());
}

function futherAccessInformationChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#accessAndServicesTable_1 #textASAccessRestrictions" + id).attr("value", $("table#yiTableOthers #futherAccessInformation" + id).val());
    } else {
        $("table#accessAndServicesTable_1 #textASAccessRestrictions_1").attr("value", $("table#yiTableOthers #futherAccessInformation").val());
    }
}

function facilitiesForDisabledPeopleAvailableChanged() {
    $("table#accessAndServicesTable_1 #selectASFacilitiesForDisabledPeopleAvailable").attr("value", $("table#yiTableOthers #selectFacilitiesForDisabledPeopleAvailable").val());
}

function futherInformationOnExistingFacilitiesChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#accessAndServicesTable_1 #textASAccessibility" + id).attr("value", $("table#yiTableOthers #futherInformationOnExistingFacilities" + id).val());
    } else {
        $("table#accessAndServicesTable_1 #textASAccessibility_1").attr("value", $("table#yiTableOthers #futherInformationOnExistingFacilities").val());
    }
}

function webOfInstitutionLinkChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#contactTable_1 tr#trLanguageWebpageOfTheInstitution" + id + " #textContactLinkTitleForWebOfTheInstitution" + id).attr("value", $("table#yiTableOthers #textYIWebpageLinkTitle" + id).val());
    } else {
        $("table#contactTable_1 tr#trLanguageWebpageOfTheInstitution #textContactLinkTitleForWebOfTheInstitution_1").attr("value", $("table#yiTableOthers #textYIWebpageLinkTitle").val());
    }
}
function webLangOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#contactTable_1 tr#trWebOfTheInstitution" + id + " #selectWebpageLanguageOfTheInstitution" + id).attr("value", $("table#yiTableOthers #selectTextYILangWebpage" + id).val());
    } else {
        $("table#contactTable_1 tr#trWebOfTheInstitution #selectWebpageLanguageOfTheInstitution_1").attr("value", $("table#yiTableOthers #selectTextYILangWebpage").val());
    }
}
function contactEmailOfInstitutionLinkChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));
    if (id == "_1") {
        $("table#yiTableOthers #textYIEmailLinkTitle").attr("value", $("table#contactTable_1 tr#trLanguageEmailOfTheInstitution #textContactLinkTitleForEmailOfTheInstitution_1").val());
    } else {
        $("table#yiTableOthers #textYIEmailLinkTitle" + id).attr("value", $("table#contactTable_1 tr#trLanguageEmailOfTheInstitution" + id + " #textContactLinkTitleForEmailOfTheInstitution" + id).val());
    }
}

function contactWebOfInstitutionLinkChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #textYIWebpageLinkTitle").attr("value", $("table#contactTable_1 tr#trLanguageWebpageOfTheInstitution #textContactLinkTitleForWebOfTheInstitution_1").val());
    } else {
        $("table#yiTableOthers #textYIWebpageLinkTitle" + id).attr("value", $("table#contactTable_1 tr#trLanguageWebpageOfTheInstitution" + id + " #textContactLinkTitleForWebOfTheInstitution" + id).val());
    }
}

function aSOpeningHoursOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #textYIOpeningTimes").attr("value", $("table#accessAndServicesTable_1 tr#trASOpeningTimes_1 #textOpeningTimes_1").val());
    } else {
        $("table#yiTableOthers #textYIOpeningTimes" + id).attr("value", $("table#accessAndServicesTable_1 tr#trASOpeningTimes" + id + " #textOpeningTimes" + id).val());
    }
}

function aSOpeningHoursOfInstitutionLangChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #selectTextYIOpeningTimes").attr("value", $("table#accessAndServicesTable_1 tr#trASOpeningTimes_1 #selectLanguageOpeningTimes_1").val());
    } else {
        $("table#yiTableOthers #selectTextYIOpeningTimes" + id).attr("value", $("table#accessAndServicesTable_1 tr#trASOpeningTimes" + id + " #selectLanguageOpeningTimes" + id).val());
    }
}

function aSOpeningHoursOfInstitutionHrefChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #linkYIOpeningTimes").attr("value", $("table#accessAndServicesTable_1 tr#tr2ASOpeningTimes_1 #linkOpeningTimes_1").val());
    } else {
        $("table#yiTableOthers #linkYIOpeningTimes" + id).attr("value", $("table#accessAndServicesTable_1 tr#tr2ASOpeningTimes" + id + " #linkOpeningTimes" + id).val());
    }
}

function aSClosingHoursOfInstitutionChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #yourInstitutionClosingDates").attr("value", $("table#accessAndServicesTable_1 tr#trASClosingDates_1 #textClosingDates_1").val());
    } else {
        $("table#yiTableOthers #yourInstitutionClosingDates" + id).attr("value", $("table#accessAndServicesTable_1 tr#trASClosingDates" + id + " #textClosingDates" + id).val());
    }
}

function aSClosingHoursOfInstitutionLangChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #selectTextYIClosingTimes").attr("value", $("table#accessAndServicesTable_1 tr#trASClosingDates_1 #selectLanguageClosingDates_1").val());
    } else {
        $("table#yiTableOthers #selectTextYIClosingTimes" + id).attr("value", $("table#accessAndServicesTable_1 tr#trASClosingDates" + id + " #selectLanguageClosingDates" + id).val());
    }
}

function aSAccessibleToThePublicChanged() {
    $("table#yiTableOthers #selectAccessibleToThePublic").attr("value", $("table#accessAndServicesTable_1 #selectASAccesibleToThePublic").val());
}

function aSFutherAccessInformationChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #futherAccessInformation").attr("value", $("table#accessAndServicesTable_1 #textASAccessRestrictions_1").val());
    } else {
        $("table#yiTableOthers #futherAccessInformation" + id).attr("value", $("table#accessAndServicesTable_1 #textASAccessRestrictions" + id).val());
    }
}

function aSFutherAccessInformationLangChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #selectFutherAccessInformation").attr("value", $("table#accessAndServicesTable_1 #selectASARSelectLanguage_1").val());
    } else {
        $("table#yiTableOthers #selectFutherAccessInformation" + id).attr("value", $("table#accessAndServicesTable_1 #selectASARSelectLanguage" + id).val());
    }
}

function aSFacilitiesForDisabledPeopleAvailableChanged() {
    $("table#yiTableOthers #selectFacilitiesForDisabledPeopleAvailable").attr("value", $("table#accessAndServicesTable_1 #selectASFacilitiesForDisabledPeopleAvailable").val());
}

function aSFutherInformationOnExistingFacilitiesChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #futherInformationOnExistingFacilities").attr("value", $("table#accessAndServicesTable_1 #textASAccessibility_1").val());
    } else {
        $("table#yiTableOthers #futherInformationOnExistingFacilities" + id).attr("value", $("table#accessAndServicesTable_1 #textASAccessibility" + id).val());
    }
}

function aSFutherInformationOnExistingFacilitiesLangChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id == "_1") {
        $("table#yiTableOthers #selectFutherAccessInformationOnExistingFacilities").attr("value", $("table#accessAndServicesTable_1 #selectASASelectLanguage_1").val());
    } else {
        $("table#yiTableOthers #selectFutherAccessInformationOnExistingFacilities" + id).attr("value", $("table#accessAndServicesTable_1 #selectASASelectLanguage" + id).val());
    }
}

function controlPersonResponsibleForDescriptionChanged() {
    $("#textYIPersonInstitutionResposibleForTheDescription").attr("value", $("#textPesonResponsible").val());
}

function linkToYourHolndingsGuideChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#resourceRelationTable" + id + " #textWebsiteOfResource").attr("value", $("#textReferencetoyourinstitutionsholdingsguide" + id).val());
    } else {
        $("table#resourceRelationTable_1 #textWebsiteOfResource").attr("value", $("#textReferencetoyourinstitutionsholdingsguide").val());
    }
}

function linkToYourHolndingsGuideTitleChanged(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#resourceRelationTable" + id + " #textTitleOfRelatedMaterial").attr("value", $("#textYIHoldingsGuideLinkTitle" + id).val());
    } else {
        $("table#resourceRelationTable_1 #textTitleOfRelatedMaterial").attr("value", $("#textYIHoldingsGuideLinkTitle").val());
    }
}

function relationsLinkToYourHolndingsGuideChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));

    var id = $("table#resourceRelationTable" + parentId + " #textWebsiteOfResource").val();

    if (parentId == "_1") {
        $("table#yiTableOthers #textReferencetoyourinstitutionsholdingsguide").attr("value", id);
    } else {
        $("table#yiTableOthers #textReferencetoyourinstitutionsholdingsguide" + parentId).attr("value", id);
    }
}

function relationsLinkToYourHolndingsGuideTitleChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));

    if (parentId == "_1") {
        $("#textYIHoldingsGuideLinkTitle").attr("value", $("table#resourceRelationTable_1 #textTitleOfRelatedMaterial").val());
    } else {
        $("#textYIHoldingsGuideLinkTitle" + parentId).attr("value", $("table#resourceRelationTable" + parentId + " #textTitleOfRelatedMaterial").val());
    }
}

function codeISILChanged(text1, index) {
    var countrycode = "(AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BA|BW|BV|BR|IO|BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|IL|IT|JM|JP|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|FM|MD|MC|MN|MS|MA|MZ|MM|NA|NR|NP|NL|AN|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|RO|RU|RW|SH|KN|LC|PM|VC|WS|SM|ST|SA|SN|CS|SC|SL|SG|SK|SI|SB|SO|ZA|GS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW|RS|ME|EU|EUR|IM|XX)";
    var pattern = countrycode + "-[a-zA-Z0-9:\/\\-]{1,11}";
    var identifier = $("#textYIIdentifierOfTheInstitution").val();
    var matched = identifier.match(pattern);
    if (index == undefined) {
        if ($("#selectYICodeISIL").val() == "yes") {
            if (matched == null) {
                $("#selectYICodeISIL").attr("value", "no");
                displayAlertDialog("\"" + identifier + "\" " + text1);
                $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                    $(this).removeAttr("disabled");
                });
            } else {
                $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                    $(this).attr("disabled", "disabled");
                });
            }
        } else {
            $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                $(this).removeAttr("disabled");
            });
        }
    } else {
        if ($("#selectOtherRepositorIdCodeISIL_" + index).val() == "yes" && index != undefined) {
            var furtherId = $("#otherRepositorId_" + index).val();
            var matched1 = furtherId.match(pattern);
            var check = index;
            if (matched1 == null || matched1 != furtherId) {
                $("#selectOtherRepositorIdCodeISIL_" + index).attr("value", "no");
                displayAlertDialog("\"" + furtherId + "\" " + text1);
                $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                    $(this).removeAttr("disabled");
                });
                $("#selectYICodeISIL").removeAttr("disabled");
            } else {
                $("#selectYICodeISIL").attr("disabled", "disabled");
                $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function (i, v) {
                    if (i != check) {
                        $(this).attr("disabled", "disabled");
                    }
                });
            }
        } else {
            $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
                $(this).removeAttr("disabled");
            });
            $("#selectYICodeISIL").removeAttr("disabled");
        }
    }
    var id = "";
    if (index == undefined && $("#selectYICodeISIL").val() == "yes") {
        if (matched == null) {
            $("#selectYICodeISIL").attr("value", "no");
            displayAlertDialog("\"" + identifier + "\" " + text);
        } else {
            id = $("#textYIIdentifierOfTheInstitution").val();
        }
    } else if ($("#selectOtherRepositorIdCodeISIL_" + index).val() == "yes" && index != undefined) {
        var furtherId = $("#otherRepositorId_" + index).val();
        var matched1 = furtherId.match(pattern);
        if (matched1 == null || matched1 != furtherId) {
            $("#selectOtherRepositorIdCodeISIL_" + index).attr("value", "no");
            displayAlertDialog("\"" + furtherId + "\" " + text);
        } else {
            id = $("#otherRepositorId_" + index).val();
        }
    } else {
        id = $("#recordIdHidden").val();
    }
    $("#textYIIdUsedInAPE").attr("value", id);
    $("#textIdentityIdUsedInAPE").attr("value", id);
    $("#textDescriptionIdentifier").attr("value", id);
    $("#textIdentityIdentifierOfTheInstitution").attr("value", $("#textYIIdentifierOfTheInstitution").val());
}

function loadDisableSelectsForFurtheIds(text) {
    $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function (index) {
        if ($(this).attr("value") == "yes") {
            codeISILChanged(text, index);
        }
    });

    // To check the field "Identifier of the institution".
    codeISILChangedOnLoad();
}

function codeISILChangedOnLoad() {
    var idOfInstitution = $("#textYIIdentifierOfTheInstitution").val();
    var idUsedInAPE = $("#textYIIdUsedInAPE").val();

    if (idOfInstitution == idUsedInAPE && idUsedInAPE != null && idUsedInAPE != "") {
        $("#selectYICodeISIL").attr("value", "yes");

        $("select[id^='selectOtherRepositorIdCodeISIL_']").each(function () {
            $(this).attr("disabled", "disabled");
        });
    }
}

function contactTelephoneChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));

    var id = $("table#contactTable_1 tr#trTelephoneOfTheInstitution" + parentId + " #textContactTelephoneOfTheInstitution" + parentId).val();
    if (parentId == "_1") {
        $("table#yiTableOthers #textYITelephone").attr("value", id);
    }
}
function contactEmailChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));

    if (parentId == "_1") {
        var id = $("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution" + parentId).val();
        $("table#yiTableOthers #textYIEmailAddress").attr("value", id);
    } else {
        var id = $("table#contactTable_1 tr#trEmailOfTheInstitution" + parentId + " #textContactEmailOfTheInstitution" + parentId).val();
        $("table#yiTableOthers #textYIEmailAddress" + parentId).attr("value", id);
    }
}
function contactEmailLanguageChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));

    if (parentId == "_1") {
        var id = $("table#contactTable_1 tr#trEmailOfTheInstitution #selectEmailLanguageOfTheInstitution" + parentId).val();
        $("table#yiTableOthers #selectTextYILangEmail").attr("value", id);
    } else {
        var id = $("table#contactTable_1 tr#trEmailOfTheInstitution" + parentId + " #selectEmailLanguageOfTheInstitution" + parentId).val();
        $("table#yiTableOthers #selectTextYILangEmail" + parentId).attr("value", id);
    }

}
function contactWebpageChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));

    if (parentId == "_1") {
        var id = $("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution" + parentId).val();
        $("table#yiTableOthers #textYIWebpage").attr("value", id);
    } else {
        var id = $("table#contactTable_1 tr#trWebOfTheInstitution" + parentId + " #textContactWebOfTheInstitution" + parentId).val();
        $("table#yiTableOthers #textYIWebpage" + parentId).attr("value", id);
    }
}
function contactWebpageLangChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));

    if (parentId == "_1") {
        var id = $("table#contactTable_1 tr#trWebOfTheInstitution #selectWebpageLanguageOfTheInstitution" + parentId).val();
        $("table#yiTableOthers #selectTextYILangWebpage").attr("value", id);
    } else {
        var id = $("table#contactTable_1 tr#trWebOfTheInstitution" + parentId + " #selectWebpageLanguageOfTheInstitution" + parentId).val();
        $("table#yiTableOthers #selectTextYILangWebpage" + parentId).attr("value", id);
    }
}
function contactLatitudeChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));
    var currentTab = getCurrentTab();
    var latitudeValue = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_1 #textContactLatitudeOfTheInstitution").val();

    $("table#yiTableVisitorsAddress" + parentId + " #textYILatitude").attr("value", latitudeValue);

    $("table#contactTable" + currentTab + " table[id^=contactTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#contactTable" + currentTab + " table#" + id + " #textContactLatitudeOfTheInstitution").attr("value", latitudeValue);
    });
    $("table[id^=yiTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#" + id + " #textYILatitude").attr("value", latitudeValue);
    });
}
function contactLongitudeChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));
    var currentTab = getCurrentTab();
    var longitudeValue = $("table#contactTable" + currentTab + " table#contactTableVisitorsAddress_1 #textContactLongitudeOfTheInstitution").val();

    $("table#yiTableVisitorsAddress" + parentId + " #textYILongitude").attr("value", longitudeValue);

    $("table#contactTable" + currentTab + " table[id^=contactTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#contactTable" + currentTab + " table#" + id + " #textContactLongitudeOfTheInstitution").attr("value", longitudeValue);
    });
    $("table[id^=yiTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#" + id + " #textYILongitude").attr("value", longitudeValue);
    });
}
function contactStreetLanguageChanged(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));

    var id = $("table#contactTable_1 table#contactTableVisitorsAddress" + parentId + " #selectLanguageVisitorAddress").val();
    $("table#yiTableVisitorsAddress" + parentId + " #selectYIVASelectLanguage").attr("value", id);
}
function escapeDate(name) {
    var date = name.val();
    if (date.indexOf("\\") > -1) {
        date = escape(date);
    }
    return date;
}
function escapeApostrophe(name) {
    //this function escape the character ' 
    var apostrophe = name.val();
    var index = apostrophe.indexOf("\'");
    while (index > -1) {
        var subString = apostrophe.substring(index, (index + 1));
        subString = escape(subString);
        var start = apostrophe.substring(0, index);
        var end = apostrophe.substring((index + 1));
        apostrophe = start + subString + end;
        index = apostrophe.indexOf("\'");
    }
    return apostrophe;
}
function escapeDoubleQuote(name) {
    //To escape the character "
    var doubleQuote = name;
    var indexQuote = doubleQuote.indexOf("\"");
    while (indexQuote > -1) {
        var subString = doubleQuote.substring(indexQuote, (indexQuote + 1));
        subString = escape(subString);
        var start = doubleQuote.substring(0, indexQuote);
        var end = doubleQuote.substring((indexQuote + 1));
        doubleQuote = start + subString + end;
        indexQuote = doubleQuote.indexOf("\"");
    }
    return doubleQuote;
}
function selectTitleRelatedLangChange(name) {
    var parentId = name.attr("id");
    parentId = parentId.substring(parentId.lastIndexOf("_"));
    var relation = $("table#resourceRelationTable" + parentId + " tr select#selectTitleOfRelatedMaterialLang").val();

    if (parentId == "_1") {
        $("#selectYIReferencetoHoldingsguide").attr("value", relation);
    } else {
        $("#selectYIReferencetoHoldingsguide" + parentId).attr("value", relation);
    }
}
function selectYIReferencetoHoldingsguideChange(name) {
    var id = name.attr("id");
    id = id.substring(id.lastIndexOf("_"));

    if (id.indexOf("_") != "-1") {
        $("table#resourceRelationTable" + id + " tr select#selectTitleOfRelatedMaterialLang").attr("value", $("#selectYIReferencetoHoldingsguide" + id).val());
    } else {
        $("table#resourceRelationTable_1 tr select#selectTitleOfRelatedMaterialLang").attr("value", $("#selectYIReferencetoHoldingsguide").val());
    }
}

function disableCoordinates() {
    var counter = $("table[id^='yiTableVisitorsAddress_']").length;
    for (var i = 1; i <= counter; i++) {
        $("table#yiTableVisitorsAddress_" + i + " input#textYILatitude").attr("disabled", "disabled");
        $("table#yiTableVisitorsAddress_" + i + " input#textYILongitude").attr("disabled", "disabled");
    }
    $("table[id^=contactTableVisitorsAddress]").each(function () {
        var id = $(this).attr("id");
        $("table#" + id + " #textContactLatitudeOfTheInstitution").attr("disabled", "disabled");
        $("table#" + id + " #textContactLongitudeOfTheInstitution").attr("disabled", "disabled");
    });
    $("table#yiTableVisitorsAddress_1 input#textYILatitude").removeAttr("disabled");
    $("table#yiTableVisitorsAddress_1 input#textYILongitude").removeAttr("disabled");
    $("table#contactTableVisitorsAddress_1 input#textContactLatitudeOfTheInstitution").removeAttr("disabled");
    $("table#contactTableVisitorsAddress_1 input#textContactLongitudeOfTheInstitution").removeAttr("disabled");

}

$(document).ready(function () {
    $(document).scrollTop($("#logo").offset().top);
    disableCoordinates();
    changeSelectOptions();
});

function changeSelectOptions() {
    var targetWidth = $("textarea").css("width");
    $("select[size]").css("width", targetWidth);
    $("select[size] option").css("width", targetWidth);
}

/**
 * Function that copy the country name from "Your Institution" table to "Contact"
 * table if it's necesary.
 */
function copyCountryName() {
    if ($("table#contactTable_1 #textContactCountryOfTheInstitution").attr("value") == "") {
        $("table#contactTable_1 #textContactCountryOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress_1 textarea#textYICountry").attr("value"));
    }
}


function selectTypeOfInstitutionOptionsIntoYITab() {
    $("#selectTypeOfTheInstitution option").each(function () {
        var value = $(this).val();
        if (value != undefined && value.length > 0) {
            if ($(this).is(':selected')) {
                $("select#textYISelectParallelNameOfTheInstitution option[value='" + value + "']").attr("selected", "true");
            } else {
                $("select#textYISelectParallelNameOfTheInstitution option[value='" + value + "']").removeAttr("selected");
            }
        }
    });
}

function selectTypeOfInstitutionOptionsIntoIdTab() {
    $("#textYISelectParallelNameOfTheInstitution option").each(function () {
        var value = $(this).val();
        if (value != undefined && value.length > 0) {
            if ($(this).is(':selected')) {
                $("select#selectTypeOfTheInstitution option[value='" + value + "']").attr("selected", "true");
            } else {
                $("select#selectTypeOfTheInstitution option[value='" + value + "']").removeAttr("selected");
            }
        }
    });
}

/**
 * This function remove the special characters <, >, % when the user put them in the institution's name
 */
function checkName(text, id) {
    var name = $(id).val();
    var indexPercentage = name.indexOf("\%");
    var indexLessThan = name.indexOf("\<");
    var indexGreaterThan = name.indexOf("\>");
    var indexBackslash = name.indexOf("\\");
    var indexColon = name.indexOf("\:");
    var showAlert = true;
    while (indexPercentage > -1 || indexLessThan > -1 || indexGreaterThan > -1 || indexBackslash > -1 || indexColon > -1) {
        if (showAlert) {
            displayAlertDialog(text);
            showAlert = false;
        }
        name = name.replace("\%", '');
        name = name.replace("\<", '');
        name = name.replace("\>", '');
        name = name.replace("\\", '');
        name = name.replace("\:", '');
        $(id).attr("value", name);
        indexPercentage = name.indexOf("\%");
        indexLessThan = name.indexOf("\<");
        indexGreaterThan = name.indexOf("\>");
        indexBackslash = name.indexOf("\\");
        indexColon = name.indexOf("\:");
    }
    nameOfInstitutionChanged(text, name);
}
/**
 * Check if in some autform's values put special character 
 * @param text
 */
function checkAutforms(text) {

    $("textarea#textYINameOfTheInstitution").on("input", function () {
        checkName(text, $(this));
    });
    $("table[id^='identityTableNameOfTheInstitution_']").each(function () {
        var id = $(this).attr("id");
        $("table#" + id + " textarea#textNameOfTheInstitution").on("input", function () {
            checkName(text, $(this));
        });
    });
}
function checkAutformIdentity() {
    var errorTab = 0;
    var found = false;
    $("table[id^='identityTableNameOfTheInstitution_']").each(function (index) {
        if (!found) {
            var id = $(this).attr("id");
            var autform = $("table#" + id + " textarea#textNameOfTheInstitution").val();
            var indexPercentage = autform.indexOf("\%");
            var indexLessThan = autform.indexOf("\<");
            var indexGreaterThan = autform.indexOf("\>");
            if (indexPercentage > -1 || indexLessThan > -1 || indexGreaterThan > -1) {
                errorTab = index + 1;
                found = true;
            }
        }
    });
    return errorTab;
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
}

/**
 * Exit button function, ask the user to save
 * the contents or leaves the form without
 * saving them
 */
function clickExitAction() {
    // Display the dialog box.
    displayExitDialog($("#DlgContent").text(), $("input#btnYes").val(), $("input#btnNo").val());
}

/**
 * Exit button function, leaves the form 
 * without saving the contents
 **/
function clickExitWithoutSaveAction() {
    var Dlg = document.getElementById("dialog-saveOnQuit");
    Dlg.style.visibility = "hidden";
    location.href = "removeInvalidEAG2012.action";
}

function shareEagWithWikimediaReadMore(){
    var message = "test";
    if($(".ui-dialog").length>0){
        $(".ui-dialog").remove();
    }
    $("body").unbind("click");
    var dialog = $("#shareEagWithWikimediaDialog").dialog({
        closeOnEscape : true,
        width: 500,
        resizable: false,
        buttons: {
            "OK" : function() {
                $("body").unbind("click");
                $(this).dialog("close");
            }
        }
    });
    setTimeout("putClickOutsideCloseDialog();","100");
}

function shareEagWithWikimediaCheckedClick(){
    $("#shareEagWithWikimediaLicence option[value=2]").attr('disabled','disabled');

    var shareEagWithWikimediaChecked = false;
    $("#yiTableOthers input[type='radio']").each(function (index, element) {
        if ($(element).is(':checked')) {
            $("#shareEagWithWikimediaLicenceTable").show();
            $("#licenceRightsForEagTr_1").show();
            $("#licenceRightsForEagTr_2").show();
            $("#licenceRightsForEagTr_3").show();
            $("#licenceRightsForEagTr_4").show();
            $("#licenceRightsForEagTr_5").show();
            if (index==0){
                $("#shareEagWithWikimediaLicence").val("2");
                $("#shareEagWithWikimediaLicence").attr('disabled','disabled');
            }
            else {
                var currentVal = $("#shareEagWithWikimediaLicence").val();
                if (currentVal == "2") {
                    $("#shareEagWithWikimediaLicence").val("1");
                }
                $("#shareEagWithWikimediaLicence").removeAttr('disabled');
            }
            $("#licenceRightsForEagRightsStmt").html($( "#shareEagWithWikimediaLicence option:selected" ).text());
            $("#shareEagWithWikimediaError_required").hide();
        }
    });
}

$( document ).ready(function() {
    shareEagWithWikimediaCheckedClick();

    $( "#shareEagWithWikimediaLicence" ).change(function() {
        $("#licenceRightsForEagRightsStmt").html($( "#shareEagWithWikimediaLicence option:selected" ).text());
    });
});
