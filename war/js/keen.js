function getURLParameter(name) {
    return decodeURI(
        (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
    );
}


function checkType() {
	if (getUrlParam("image") ==1) {
		$("#music").hide();
		$("#music-header").click(function() {
			$("#music").show(20);
		});
		$("#video").hide();
		$("#video-header").click(function() {
			$("#video").show(20);
		});

	} else if (getUrlParam("music") == 1) {
		$("#image").hide();
		$("#image-header").click(function() {
			$("#image").show(20);
		});
		$("#video").hide();
		$("#video-header").click(function() {
			$("#video").show(20);
		});

	} else {
		//video
		$("#image").hide();
		$("#image-header").click(function() {
			$("#image").show(20);
		});
		$("#music").hide();
		$("#music-header").click(function() {
			$("#music").show(20);
		});

	}
}
