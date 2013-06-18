$("#last_posts").ready(load_news());
$("#main_posts").ready(load_main_posts());
$("#feeds_navigation").ready(load_feeds());

function load_news() {
	// $.ajax({
	// url : '/rss/do.list.json',
	// async : false,
	// type : 'post',
	// dataType : 'jsonp',
	// data : {
	// user : user,
	// pass : pass
	// },
	// success : function(data) {
	// }
	// });
}
function load_main_posts() {
	if ($("#feed_id").val() != null) {
		load_feed($("#feed_id").val());
	}
}
function load_feeds() {
	$.ajax({
		url : '/jbrss/rss/do.list.json',
		async : false,
		type : 'post',
		dataType : 'jsonp',
		data : {},
		success : function(data) {
			if (data.errorCode == 0) {
				var htmlRet = "";
				$.each(data.data, function(i, feed) {
					htmlRet += '<li><a href="/jbrss/feed?id=' + feed.id + '">' + feed.feedname + '</a></li>';
				});
				$("#feeds_navigation").html(htmlRet);
			}
		}
	});
}
function load_feed(feedId) {
	$.ajax({
		url : '/jbrss/rss/do.get.feed.json',
		async : false,
		type : 'post',
		dataType : 'jsonp',
		data : {
			id : feedId,
			count : 10
		},
		success : function(data) {
			var htmlRet = "";
			$.each(data.data, function(i, post) {
				htmlRet += create_main_post(post.posttitle, post.posttext, post.postdate, post.postlink);
			});
			$("#main_posts").html(htmlRet);
		}
	});
}
function create_last_post(root, title, date, link) {
	/*
	 * <h3>MultiFlex-1</h3> <p>Released: 01.05.2006<br />OK for operational
	 * use, but very heavy code.<br /><a
	 * href="http://www.1-2-3-4.info/webtemplates/">Download latest update</a></p>
	 */

}
function create_main_post(title, text, date, link) {
	var ret = "";
	ret += '<div class="column1-unit"> <h1>' + title + '</h1>';
	ret += '<h3>' + date + '</h3>';
	ret += '<p>' + text + '</p>';
	ret += '<a href=' + link + '>Ссылка</a>';
	ret += '</div> <hr class="clear-contentunit" />';
	return ret;
}