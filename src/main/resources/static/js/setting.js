$(
    function () {
        $('.change-info').click(changeInfo);
    }
);

function changeInfo() {
    var nickName = $("#settingsUserName").val();
    var email = $("#settingsUserEmail").val();
    var password = $("#settingsUserPassword").val();
    var location = $("#settingsUserLocation").val();
    var website = $("#settingsUserWebsite").val();
    var about = $("#settingsUserAbout").val();
    //todo 修改用户数据核验 前端
    $.post(
        CONTEXT_PATH+"/user/setting",
        {
            "nickName":nickName,
            "email":email,
            "password":password,
            "location":location,
            "website":website,
            "about":about
        },
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 1){
                alert(data.Msg);
            }else{
                // 数据修改成功！;
                alert(data.Msg+"\n"+
                    data.nickNameMsg+"\n"+
                    data.emailMsg+"\n"+
                    data.passwordMsg+"\n"+
                    data.infoMsg+"\n"
                );
                window.location.reload();
            }

        }
    );
}