var loaded = 0;
var feedId = 0;
//$("#load_marker").ready(load_main());

$(document).on("pageshow", load_main())

function load_main() {
    var url = document.URL;
    if (url.indexOf("feed") > 0) {
        load_feed(url.substring(url.indexOf("#") + 6));
    }
    load_feeds();
}


function load_feeds() {
    $.ajax({
        url: 'http://terranz.ath.cx/jbrss/rss/do.list.json',
        async: false,
        type: 'get',
        dataType: "jsonp",
        jsonp: "callback",
        data: {},
        success: function(data) {
            var htmlRet = "";
            if (data.errorCode == 0) {
                $.each(data.data, function(i, feed) {
                    htmlRet += '<li data-theme="c"><a  id=' + feed.id + ' href="#" onClick=open_feed(' + feed.id + '); data-transition="slide">' + feed.feedname
                            + '</a></li>';
                });
                $("#feeds").html(htmlRet);
                var url = document.URL;
                if (url.indexOf("feed") > 0) {
                    load_feed(url.substring(url.indexOf("#") + 6));
                }
            }
        }
    });
}

function open_feed(fid) {
    window.location.href = "http://terranz.ath.cx/jbrss/ui/main#feed=" + fid;
    load_feed(fid);
}

function load_feed(fid) {
    loaded = 10;
    feedId = fid;
    load();
    $("#feeds_collapsable").trigger('collapse');
    $("#messages_collapsable").trigger('expand');
}

function create_main_post(title, text, date, link, id) {
    var ret = "";
    ret += '<div class="column1-unit"> <a href=# onClick = "window.open(\'' + link + '\');"> <h1>' + title + '</h1></a>';
    ret += '<h3>' + new Date(date) + '</h3>';
    ret += '<p>' + text + '</p>';
    ret += '<a data-transition="slide" href="#" onClick = "Android.share(' + id + ');"> Поделиться </a>';
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
        $.ajax({
            url: 'http://terranz.ath.cx/jbrss/login/do.login.json',
            async: false,
            type: 'get',
            dataType: "jsonp",
            jsonp: "callback",
            data: {
                user: user,
                pass: pass
            },
            success: function(data) {
                $("#loginbtn").removeClass('ui-disabled');
                if (data.logged == true) {
                    setCookie("JSESSIONID", data.session);
                    window.location.assign("http://terranz.ath.cx/jbrss/ui/main");
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
    document.cookie = key + '=' + value + ';path=/' + ';expires=' + expires.toUTCString();
}

function update() {
    if (!$("#updatebtn").hasClass('ui-disabled'))
        $("#updatebtn").addClass('ui-disabled');
    $.ajax({
        url: 'http://terranz.ath.cx/jbrss/rss/do.update.json',
        async: true,
        type: 'get',
        dataType: "jsonp",
        jsonp: "callback",
        data: {},
        success: function(data) {
            loading = false;
            $("#updatebtn").removeClass('ui-disabled');
            if (data.errorCode == 0) {
                alert("Обновление завершено, загружено " + data.data + " новых записей");
            } else {
                alert("Ошибка обновления: " + data.errorMessage);
            }
        }
    });
}

function more() {
    load();
}

function load() {
    $.ajax({
        url: 'http://terranz.ath.cx/jbrss/rss/do.get.feed.json',
        async: false,
        type: 'get',
        dataType: "jsonp",
        jsonp: "callback",
        data: {
            id: feedId,
            count: loaded
        },
        success: function(data) {
            var htmlRet = "";
            if (data.errorCode == 0) {
                $.each(data.data, function(i, post) {
                    htmlRet += create_main_post(post.posttitle, post.posttext, post.postdate, post.postlink, post.id);
                });
                $("#messages").html(htmlRet);
            }
        }
    });
    loaded += 10;
}

function add() {
    var url = $("#url").val();
    if (learnRegExp(url)) {
        $.ajax({
            url: 'http://terranz.ath.cx/jbrss/rss/do.add.json',
            async: false,
            type: 'get',
            dataType: "jsonp",
            jsonp: "callback",
            data: {
                url: url
            },
            success: function(data) {
                if (data.data) {
                    window.location.assign("http://terranz.ath.cx/jbrss/ui/main");
                    load_feeds();
                } else
                    alert("Ошибка при добавлении: " + data.errorMessage);
            }
        });
    } else {
        alert("Неверный URL");
    }
}

function learnRegExp(s) {
    var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
    return regexp.test(s);
}

$("#user").ready(loadCaptcha());

var captcha = "";
function loadCaptcha() {
    if (!$("#loadcapthca").hasClass('ui-disabled'))
        $("#loadcapthca").addClass('ui-disabled');
    $.ajax({
        url: 'http://terranz.ath.cx/jbrss/captcha/do.get.json',
        async: true,
        type: 'get',
        dataType: "jsonp",
        jsonp: "callback",
        data: {
        },
        success: function(data) {
            $("#loadcapthca").removeClass('ui-disabled');
            if (data.errorCode == 0) {
                captcha = data.cid;
                $("#captcha_image").attr("src", data.image);
            } else {
                alert("Не получилось загрузить капчу: " + data.errorMessage);
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
            url: 'http://terranz.ath.cx/jbrss/login/do.register.json',
            async: true,
            type: 'get',
            dataType: "jsonp",
            jsonp: "callback",
            data: {
                user: user,
                pass: pass,
                captcha: captcha,
                capval: capval
            },
            success: function(data) {
                if (data.logged == true) {
                    setCookie("JSESSIONID", data.session);
                    window.location.assign("http://terranz.ath.cx/jbrss/ui/main");
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

function doSearch() {
    var val = $("#search_input").val();
    if (val.length > 0) {
        $.ajax({
            url: 'http://terranz.ath.cx/jbrss/rss/do.search.json',
            async: false,
            type: 'post',
            dataType: "jsonp",
            jsonp: "callback",
            data: {val: val},
            success: function(data) {
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

function saveSettings() {
    $.ajax({
        url: 'http://terranz.ath.cx/jbrss/settings/do.set.json',
        async: true,
        type: 'get',
        dataType: "jsonp",
        jsonp: "callback",
        data: {
            key: $("#key").val(),
            val: $("#val").val(),
        },
        success: function(data) {
            window.location.assign("http://terranz.ath.cx/jbrss/ui/main");
        }
    });
}

function settings() {
    window.location.assign("http://terranz.ath.cx/jbrss/ui/setting");
}

$("#feedscontainer").ready(loadFeedsToSettings());
function loadFeedsToSettings() {
    $.ajax({
        url: 'http://terranz.ath.cx/jbrss/rss/do.list.json',
        async: false,
        type: 'get',
        dataType: "jsonp",
        jsonp: "callback",
        data: {},
        success: function(data) {
            var htmlRet = "";
            if (data.errorCode == 0) {
                $.each(data.data, function(i, feed) {
                    htmlRet += '<li data-theme="c"><a href="#" onClick=delete_feed(' + feed.id + '); data-transition="slide">' + feed.feedname
                            + '</a></li>';
                });
                $("#feedslist").html(htmlRet);
            }
        }
    });
}

function delete_feed(id) {
    $("#sure .sure-do").text("Да").on("click.sure", function() {
        $.ajax({
            url: 'http://terranz.ath.cx/jbrss/rss/do.del.json',
            async: false,
            type: 'get',
            dataType: "jsonp",
            jsonp: "callback",
            data: {id: id},
            success: function(data) {
                var htmlRet = "";
                if (data.errorCode == 0) {
                    loadFeedsToSettings();
                    load_feeds();
                }
            }
        });
        $(this).off("click.sure");
    });
    $.mobile.changePage("#sure");
}