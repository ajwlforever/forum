var myEditor = null;
ClassicEditor
    .create( document.querySelector( '#editor' ) )
    .then(editor => {
        myEditor = editor;
    })
    .catch( error => {
        console.error(error);
    } );

$(function () {
    $(".create-post").click(function () {
        //创建帖子
        var title =  $("#inputTopicTitle").val();
        var type = 1;
        var content = myEditor.getData();
        var boardName = $("#board-select option:selected").val();
        var tags = $("#inputTopicTags").val();
        // alert(title);
        // alert(content);
        // alert(boardName);
        // alert(tags);
        $.post(
            CONTEXT_PATH+"/post/create",
            {
                'boardName':boardName,
                'title':title,
                'content':content,
                'type':type,
                'tags':tags
            },
            function (data) {
                data = $.parseJSON(data);
                if(data.code==0)
                {
                    alert("发布成功成功");
                    //todo 跳转
                    window.location = CONTEXT_PATH+"/";

                }else{
                    alert(data.msg);
                }
            }
        );
    });
});