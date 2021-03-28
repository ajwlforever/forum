jQuery.cookie = function(name, value, options) {
    if (typeof value != 'undefined') {
        options = options || {};
        if (value === null) {
            value = '';
            options = $.extend({}, options);
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString();
        }
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};
$(function () {

        $("#login").click(login);
    }
);
// todo 输入框长度 决定登录框able
function login() {

    var  code = $("#code").val();
    var username = $("#username").val();
    var pwd = $("#passsword").val();
    var c = $.cookie("kaptch");
    var rememberMe = $("#settingsCheckBox01").val();
    console.log("rememberMe"+rememberMe);
    console.log("c"+c);

    if(code != c) {
        $("#code-error").show();
    }else{
        $("#code-error").hide();
        //登录
        $.post(
            CONTEXT_PATH+"/login",
            {
                "username":username,
                "password":pwd,
                "rememberMe":rememberMe
            },
            function (data) {
                data = $.parseJSON(data);
                if(data.code==0) {
                    // 登录成功
                    window.location = CONTEXT_PATH+"/index";
                }else alert(data.msg);

            }
        );
    }
}
function refresh_kaptcha()
{
    var path = CONTEXT_PATH +"/kaptcha?refresh="+ Math.random();
    console.log(path);
    $("#kaptcha").attr("src",path);

}
