package com.staylog.staylog.global.event;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreatedEvent {
    long commentId; // 댓글 PK
    long userId; // 댓글 작성자 PK
    long boardId; // 댓글이 작성된 원글 PK
}
