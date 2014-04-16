var loaded = 0;
var feedId = 0;
$("#feeds").ready(load_feeds());
function load_feeds() {
	$.ajax({
		url : '/jbrss/rss/do.list.json',
		async : false,
		type : 'get',
		data : {},
		success : function(data) {
			var htmlRet = "";
			if (data.errorCode == 0) {
				$.each(data.data, function(i, feed) {
					htmlRet += '<li data-theme="c"><a href="#" onClick=load_feed(' + feed.id + '); data-transition="slide">' + feed.feedname
							+ '</a></li>';
				});
				$("#feeds").html(htmlRet);
			}
		}
	});
}

function load_feed(fid) {
	$("#feeds_collapsable").trigger('collapse');
	$("#messages_collapsable").trigger('expand');
	loaded = 10;
	feedId = fid;
	load();
}

function create_main_post(title, text, date, link) {
	var ret = "";
	ret += '<div class="column1-unit">'+'<a href=' + link +'> <h1>' + title + '</h1></a>';
	ret += '<h3>' + new Date(date) + '</h3>';
	ret += '<p>' + text + '</p>';
	ret += '</div> <hr class="clear-contentunit" />';
	return ret;
}

function userLogin() {
    if (!$("#loginbtn").hasClass('ui-disabled'))
        $("#loginbtn").addClass('ui-disabled');
	var user = $("#j_username").val();
	var pass = $("#j_password").val();
	if (user == null || user.length == 0) {
		alert('Не указан логин');
	} else if (pass == null || pass.length == 0) {
		alert('Не введен пароль');
	} else {

		$
				.ajax({
					url : '/jbrss/login/do.login.json',
					async : false,
					type : 'get',
					data : {
						user : user,
						pass : pass
					},
					success : function(data) {
				    	$("#loginbtn").removeClass('ui-disabled');
						if (data.logged == true) {
						    setCookie("JSESSIONID",data.session);
							window.location.assign("/jbrss/ui/main");
						} else {
							if (parseInt(data.errorCode) == 1000) {
								alert('Вы сделали слишком много попыток зайти на ресурс. Аккаунт временно заблокирован. Попробуйте еще раз через некоторое время.');
							} else if (parseInt(data.errorCode) == 1001) {
								alert('Ваш аккаунт заблокирован, обратитесь в поддержку для получения полной информации.');
							} else {
								alert('Неверный логин или пароль');
								$('#j_password').val('');
							}
						}
					}
				});
	}
}

function setCookie(key, value) {
                var expires = new Date();
                expires.setTime(expires.getTime() + (1 * 24 * 60 * 60 * 1000));
                document.cookie = key + '=' + value +';path=/'+ ';expires=' + expires.toUTCString();
            }

function update() {
if (!$("#updatebtn").hasClass('ui-disabled'))
        $("#updatebtn").addClass('ui-disabled');
        $.ajax({
                url : '/jbrss/rss/do.update.json',
                async : true,
                type : 'get',
                data : {},
                success : function(data) {
                    loading = false;
                    $("#updatebtn").removeClass('ui-disabled');
                    if (data.errorCode == 0) {
                        alert("Обновление завершено, загружено "+data.data+" новых записей");
                    } else {
                        alert("Ошибка обновления: "+data.errorMessage);
                    }
                }
            });
}

function more(){
    load();
}

function load(){
$.ajax({
		url : '/jbrss/rss/do.get.feed.json',
		async : false,
		type : 'get',
		data : {
			id : feedId,
			count : loaded
		},
		success : function(data) {
			var htmlRet = "";
			if (data.errorCode == 0) {
				$.each(data.data, function(i, post) {
					htmlRet += create_main_post(post.posttitle, post.posttext, post.postdate, post.postlink);
				});
				$("#messages").html(htmlRet);
			}
		}
	});
	loaded += 10;
}

function add(){
var url = $("#url").val();
    if (learnRegExp(url)){
        $.ajax({
        		url : '/jbrss/rss/do.add.json',
            	async : false,
        		type : 'get',
        		data : {
        			url : url
        		},
        		success : function(data) {
        	            if (data.data)
        	                window.location.assign("/jbrss/ui/main");
        	            else
        	                alert("Ошибка при добавлении: "+data.errorMessage);
        			}
        		});
    }else{
        alert("Неверный URL");
    }
}

function learnRegExp(s) {
      var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
      return regexp.test(s);
 }

 $("#user").ready(loadCaptcha());

 var captcha = "";
 function loadCaptcha(){
     if (!$("#loadcapthca").hasClass('ui-disabled'))
             $("#loadcapthca").addClass('ui-disabled');
       $.ajax({
       				url : '/jbrss/captcha/do.get.json',
       				async : true,
       				type : 'get',
       				data : {
       				},
       				success : function(data) {
       				   	$("#loadcapthca").removeClass('ui-disabled');
       					if (data.errorCode == 0) {
                                captcha = data.cid;
                                $("#captcha_image").attr("src",data.image);
       					} else {
                                alert("Не получилось загрузить капчу: "+data.errorMessage);
       					}
       				}
       			});
 }

 function userReg() {
 	var user = $("#user").val();
 	var pass = $("#pass").val();
 	var capval = $("#captcha_val").val();
 	if (user == null || user.length == 0) {
 		alert('Не указан логин');
 	} else if (pass == null || pass.length == 0) {
 		alert('Не введен пароль');
 	} else {
 		$.ajax({
 					url : '/jbrss/login/do.register.json',
 					async : true,
 					type : 'get',
 					data : {
 						user : user,
 						pass : pass,
 						captcha : captcha,
 						capval : capval
 					},
 					success : function(data) {
 						if (data.logged == true) {
 						    setCookie("JSESSIONID",data.session);
 							window.location.assign("/jbrss/ui/main");
 						} else {
 							if (parseInt(data.errorCode) == 1000) {
 								alert('Вы сделали слишком много попыток зайти на ресурс. Аккаунт временно заблокирован. Попробуйте еще раз через некоторое время.');
 							} else if (parseInt(data.errorCode) == 1001) {
 								alert('Ваш аккаунт заблокирован, обратитесь в поддержку для получения полной информации.');
 							} else {
 								alert(data.message);
 								$('#j_password').val('');
 							}
 						}
 					}
 				});
 	}
 }

 function doSearch(){
    var val = $("#search_input").val();
    if (val.length > 0){
        $.ajax({
        		url : '/jbrss/rss/do.search.json',
        		async : false,
    		    type : 'post',
    		    data : {val:val},
    		    success : function(data) {
    			        var htmlRet = "";
    			        $("#feeds_collapsable").trigger('collapse');
                        $("#messages_collapsable").trigger('expand');
                        if (data.errorCode == 0) {
                	        $.each(data.data, function(i, post) {
                            htmlRet += create_main_post(post.posttitle, post.posttext, post.postdate, post.postlink);
                        });
                        $("#messages").html(htmlRet);
                    }
    		    }
    	    });
    	}
 }