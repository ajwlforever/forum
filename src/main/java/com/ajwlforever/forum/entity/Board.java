package com.ajwlforever.forum.entity;

/**
 * 板块的实体类
 * author:ajwlforever
 * id int(11) AI PK
 * board_name varchar(100)
 * board_desc varchar(500)
 */
public class Board {
    private int id;
    private String boardName;
    private String boardDesc;

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", boardName='" + boardName + '\'' +
                ", boardDesc='" + boardDesc + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public Board setId(int id) {
        this.id = id;
        return this;
    }

    public String getBoardName() {
        return boardName;
    }

    public Board setBoardName(String boardName) {
        this.boardName = boardName;
        return this;
    }

    public String getBoardDesc() {
        return boardDesc;
    }

    public Board setBoardDesc(String boardDesc) {
        this.boardDesc = boardDesc;
        return this;
    }
}
