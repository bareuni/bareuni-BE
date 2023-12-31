package com.umc.BareuniBE.dto;

import lombok.*;

public class CommunityReq {
//    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CommunityCreateReq {
        private String content;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CommentCreateReq {
        private String comment;
    }

}
