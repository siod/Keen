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

function deleteData(oId,divId) {
	$.post('/user/mark',{ id: oId }).success($(divId).remove());
}
