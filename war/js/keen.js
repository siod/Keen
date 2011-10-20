function getURLParameter(name) {
    return decodeURI(
        (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
    );
}


function checkType() {
	if (getURLParameter("image") ==1) {
		$("#music").hide();
		$("#video").hide();
	} else if (getURLParameter("music") == 1) {
		$("#image").hide();
		$("#video").hide();
	} else if (getURLParameter("video") == 1) {
		$("#image").hide();
		$("#music").hide();
	}
	$.each(["#image","#music","#video"],function(index,value) {
		$(value+ "-header").click(function() {
			if ($(value).is(":hidden"))
				$(value).show(20);
			else
				$(value).hide();
		});
	});

}

function setupFileUpload() {
$(function() {
  /* activate the plugin */
  //$('#fileupload').fileupload();

  /* generate an App Engine url on each click */

  /*
  $('input:file', '#fileupload').button().click(function() {
    $.getJSON('/upload', function (response) {
      $('#fileupload').fileupload('add',{url: response.url});
    });
  });
*/

  $('#fileupload').fileupload({
	add: function (e, data) {
		var url;
		$.ajax({
		  url: '/upload',
		  async: false,
		  dataType: 'json',
		  success: function (json) {
			url = json.url;
			}
		});
		//data.url = $.getJSON('/upload', function (response) {
		//});
		var that = this;
		$('#fileupload form').prop('action', url);
		data.url = url;
		//= url.url;
		/* configure the plugin with the /_ah/upload url */
		$.blueimpUI.fileupload.prototype.options.add.call(that, e, data);
	}
  });

});
};

var page;
function deleteData(oId,divId) {
	$.post('/'+page,{ id: oId }).success($(divId).remove());
}


function search(event){
	var searchStr = $('#searchBox').val();
	//searchStr.replace(/([ !"#$%&'()*+,.\/:;<=>?@[\\\]^`{|}~])/g,'\\\\$1');
	var rStr = new RegExp(searchStr,"i");
	//var searchStr = document.getElementById('searchBox').value;
	//$("imageTableDiv").hide();
	//console.log("in search value = " + rStr);
	for (x in imageList) {
		if(rStr == "" || 
		   imageList[x].title.match(rStr) ||
		   imageList[x].artist.match(rStr) ||
		   imageList[x].comment.match(rStr) ||
		   imageList[x].tags.match(rStr)){
			$('#' + imageList[x].id).show();
			//console.log('showing ' + imageList[x].id);
			//document.getElementById(imageList[x].id).style.display = '';
		} else{
			$('#' + imageList[x].id).hide();
			//console.log('hiding ' + imageList[x].id);
			//document.getElementById(imageList[x].id).style.display = 'none';
		}
	}
}
