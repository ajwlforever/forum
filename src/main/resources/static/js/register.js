$(function () {
    $("#confirm-password").blur(passwordCheck);
    $("#password").blur(passwordCheck);
    $(".register").click(register);
});

function register() {
    var username = $("#username").val();
    var email = $("#email").val();
    var pwd1 = $("#password").val();
    var pwd2 = $("#confirm-password").val();
    if(pwd1 != pwd2) {
        $(".password-erro").show();
    }else{
        if(pwd1.length<8&&pwd1.length>16)
        {
            $(".passwor-error").show();
        }
        else{
            if(username.length == 0 || email.length == 0)
                alert("用户名或邮箱不得为空");
            else
            {
                var myreg = /^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
                if(!myreg.test(email)){
                     $(".email-error").text("邮箱格式错误！");
                }else

            //可以注册pos-
            //alert("can register");
            $.post(
                CONTEXT_PATH+"/register",
                {
                    "username":username,
                    "email":email,
                    "password":pwd1
                },
                function (data) {
                    data = $.parseJSON(data);
                    if(data.code==0)
                    {
                        console.log("注册成功");
                        alert(data.msg);
                        window.location=CONTEXT_PATH+"/login";  //回到登录页

                    }else
                    {
                        console.log(data);
                        alert(data.msg);
                        $(".email-error").text(data.usernameMsg);
                        $(".username-error").text(data.emailMsg);
                    }

                }
            );
            }
        }
    }
}
function  passwordCheck() {
    var pwd1 = $("#password").val();
    var pwd2 = $("#confirm-password").val();
    if(pwd1 != pwd2) {
        $(".password-erro").show();
    }else
        $(".password-erro").hide();
    if(pwd1.length<8&&pwd1.length>16)
    {
        $(".passwor-error").show();
    }
    else{
        $(".passwor-error").hide();
    }

}