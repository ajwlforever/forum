function follow(btn,entityType,entityId) {
    $.post(
        CONTEXT_PATH+"/follow",
        {
            "entityType":entityType,
            "entityId":entityId
        },
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0){
                if(data.isFollowed==1) {
                    //关注成功
                    window.location.reload();
                }else {
                    alert(data.msg);
                }

            }else{
                alert(data.msg);
            }
        }
    );
}