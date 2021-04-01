 function like(btn, entityType, entityId, entityUserId, postId) {

    $.post(
        CONTEXT_PATH+"/like",
        {
            "entityType":entityType,
            "entityId":entityId,
            "entityUserId":entityUserId,
            "postId":postId
        },
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0){

                if(data.likeStatus==1){
                    // todo 点赞后图标变颜色
                    // var $child  = $(btn).children("svg");
                    // $child.addClass("operation_active");
                    // var $svg =  $(btn).children(".tt-icon").html();
                    // alert($svg);
                    // $child.remove();
                    // $(btn).children(".tt-text").prepend($svg);
                    //$(btn).children("svg path").css({fill:"#e74c3c"});
                    $(btn).children(".tt-text").css({color:"#e74c3c"});
                }else {
                    $(btn).children(".tt-text").css({color:"#666f74"});
                }
                $(btn).children(".tt-text").text(data.likeCount);
            }else{
                //点赞失败
                alert(data.msg);
            }
        }
    );
 }
 function dislike(btn, entityType, entityId, entityUserId, postId) {

     $.post(
         CONTEXT_PATH+"/dislike",
         {
             "entityType":entityType,
             "entityId":entityId,
             "entityUserId":entityUserId,
             "postId":postId
         },
         function (data) {
             data = $.parseJSON(data);
             if(data.code == 0){

                 if(data.dislikeStatus==1){
                     // todo 点赞后图标变颜色
                     // var $child  = $(btn).children("svg");
                     // $child.addClass("operation_active");
                     // var $svg =  $(btn).children(".tt-icon").html();
                     // alert($svg);
                     // $child.remove();
                     // $(btn).children(".tt-text").prepend($svg);
                     //$(btn).children("svg path").css({fill:"#e74c3c"});
                     $(btn).children(".tt-text").css({color:"#e74c3c"});
                 }else {
                     $(btn).children(".tt-text").css({color:"#666f74"});
                 }
                 $(btn).children(".tt-text").text(data.dislikeCount);

             }else{
                 //点踩失败
                 alert(data.msg);
             }
         }
     );
 }
 function follow(btn, entityType, entityId) {
        $.post(
            CONTEXT_PATH+"/follow",
            {
                "entityType":entityType,
                "entityId":entityId
            },
            function (data) {
                data = $.parseJSON(data);
                if(data.code == 0){
                    if(data.isFollowed==1){
                        //关注成功
                        $(btn).children(".tt-text").css({color:"#e74c3c"});
                    }else{
                        $(btn).children(".tt-text").css({color:"#666f74"});
                    }
                    $(btn).children(".tt-text").text(data.followCount);

                }else{
                    alert(data.msg);
                }
            }
        );
 }
