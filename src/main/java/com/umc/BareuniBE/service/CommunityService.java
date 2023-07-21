package com.umc.BareuniBE.service;

import com.umc.BareuniBE.dto.CommunityReq;
import com.umc.BareuniBE.dto.CommunityRes;
import com.umc.BareuniBE.entities.Community;
import com.umc.BareuniBE.entities.User;
import com.umc.BareuniBE.global.BaseException;
import com.umc.BareuniBE.repository.CommunityRepository;
import com.umc.BareuniBE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.umc.BareuniBE.global.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    private static final int PAGE_POST_COUNT = 10; // 한 페이지 당 게시글 수

    public CommunityRes.CommunityDetailRes createCommunity(CommunityReq.CommunityCreateReq request) throws BaseException {
        User user = userRepository.findById(request.getUserIdx())
                .orElseThrow(() ->  new BaseException(USERS_EMPTY_USER_ID));

        Community newCommunity = Community.builder()
                .user(user)
                .content(request.getContent())
                .build();
        Community community = communityRepository.saveAndFlush(newCommunity);
        return new CommunityRes.CommunityDetailRes(community);
    }

    public List<CommunityRes.CommunityListRes> getCommunityList(Pageable page) {
        List<Object[]> communities = communityRepository.findAllCommunity_Pagination(PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSort()));
        return communities.stream()
                .map(communityData -> {
                    CommunityRes.CommunityListRes communityRes = new CommunityRes.CommunityListRes();
                    communityRes.setCommunityIdx(communityData[0]);
                    communityRes.setCreatedAt(communityData[1]);
                    communityRes.setUpdatedAt(communityData[2]);
                    communityRes.setContent(communityData[3]);
                    communityRes.setUser(userRepository.findById(((BigInteger)communityData[4]).longValue()).orElse(null));
                    communityRes.setLike(communityData[5]);

                    return communityRes;
                })
                .collect(Collectors.toList());
    }

}
