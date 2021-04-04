function message(bnt) {
    var $in = $(bnt).children("input");
    // alert($in);
    console.log($in);
    if($in.val() ==null){
        window.location = CONTEXT_PATH+"/messages";
    }else {
        window.location = CONTEXT_PATH+"/message/"+$in.val();
    }
}
function sendMessage() {
    var content = $("#message").val();
    var conversationId = $("#cd").val();
    if(content.length>0&&content.length<=16){
        $.post(
            CONTEXT_PATH+"/create_message",
            {
                'content':content,
                'conversationId':conversationId
            },
            function (data) {
                data = $.parseJSON(data);
                if(data.code==0){
                    alert(data.msg);
                    window.location.reload();
                }else {
                    alert(data.msg);
                }
            }
        );
    }
}