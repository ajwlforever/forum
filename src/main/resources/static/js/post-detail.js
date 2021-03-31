$(function () {
    if(getQueryVariable("current")!=(false)){
        //$('html , body').animate({scrollTop: $("#pign-show").offset().top}, 0);

        window.scrollTo({
            top: $('#pign-show').offset().top,
            behavior: 'smooth'
        });
    }

});
function  showReply(id) {
    var reply = ".create-reply"+id;
    $(reply).toggle();
}
function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}
function create_reply(postId,fatherId) {
     var content = $("#reply-input"+postId+fatherId).val();
     if(content.length>0 && content.length<160) {
        $.post(
            CONTEXT_PATH+"/post/reply/create",
            {
                "postId":postId,
                "fatherId":fatherId,
                "content":content
            },
            function (data) {
                data = $.parseJSON(data);
                if(data.code == 0){
                    alert(data.msg);
                    window.location.reload();
                }else
                    alert(data.msg);
            }

        );
     }

}