
function getContextPath() {
   return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

function arrayToTable(tableData) {
    const membersColMap = new Map([['Id', 'id'], ['Name', 'name'], ['Email', 'email'], ['Phone #', 'phoneNumber'], ['REST URL', 'restUrl']]);
    var table = $('<table></table>');

    var header = $("<tr></tr>");
    for (const [key, value] of membersColMap) {
        header.append("<th>" + key + "</th>");
    }

    table.append(header);


    for (var i = 0; i < tableData.length; i++) {
        var row = $("<tr></tr>");
        for (const [key, value] of membersColMap) {
            if (value == 'restUrl') {
                var mbmrUrl = "rest/members/" + tableData[i]['id'];
                row.append("<td><a href=\"" + getContextPath() + mbmrUrl + "\">" + mbmrUrl + "</td>");
            } else {
                row.append("<td>" + tableData[i][value] + "</td>");
            }
        }
        table.append(row);
    }
    table.append("<tfoot><tr><td rowspan=\"" + membersColMap.size + "\"><a href=\"" + getContextPath() + "/rest/members\">/rest/members</td></tr></tfoot>");

    return table;
}


function refreshTable() {
 $.ajax({
    type: "GET",
    url: "/api/members",
    dataType: 'json',
    success: function (data) {
        $('#members_container').empty();
        $('#members_container').append(arrayToTable(data));
    }
 });
 }

 function registerMember() {
  var formData = {};
  const fields = ['name', 'phoneNumber', 'email'];
  for (var field of fields) {
    formData[field] = $("#" + field + "_in").val();
    $("#"+ field + "_err").html('');
  }

  $.ajax({
       url: "/api/member", // Replace with your server-side script URL
       type: "POST",
       data: JSON.stringify(formData),
       contentType: "application/json; charset=utf-8",
       dataType: "json",
       success: function(response) {
         $('#valid_msg').html("<span class=\"success_class\">Registered!</span>");
         refreshTable();
       },
       error: function(xhr, status, error) {
            var err = JSON.parse(xhr.responseText);
            if (err.validationError != null) {
                $('#valid_msg').html("<span class=\"error_class\">" + err.validationError + "</span>");
            }
            if (err.violations != null) {
                for(var efield in err.violations) {
                    $('#' + efield + "_err").html("<span class=\"error_class\">" + err.violations[efield] + "</span>");
               }
            }
       }
     });
 }

$(document).ready(function() {
   refreshTable();
   $('#reg_btn').click(registerMember);
});

