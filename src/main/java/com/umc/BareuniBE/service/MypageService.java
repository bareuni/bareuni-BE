package com.umc.BareuniBE.service;

import com.umc.BareuniBE.dto.BookingRes;
import com.umc.BareuniBE.dto.CommunityRes;
import com.umc.BareuniBE.dto.HospitalRes;
import com.umc.BareuniBE.dto.ReviewRes;
import com.umc.BareuniBE.entities.Review;
import com.umc.BareuniBE.entities.User;
import com.umc.BareuniBE.global.BaseException;
import com.umc.BareuniBE.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static com.umc.BareuniBE.global.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
public class MypageService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public MypageService(CommunityRepository communityRepository, UserRepository userRepository, ScrapRepository scrapRepository, ReviewRepository reviewRepository, BookingRepository bookingRepository){
        this.communityRepository = communityRepository;
        this.scrapRepository = scrapRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
}

    // 작성한 글 목록 조회 (최신순)
    public List<CommunityRes.CommunityListRes> getMyCommunityList(Long userId, Pageable page) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USERS_EMPTY_USER_ID));
        List<Object[]> communities = communityRepository.MyCommunityList(user, page);

        return communities.stream()
                .map(communityData -> {
                    CommunityRes.CommunityListRes communityRes = new CommunityRes.CommunityListRes();
                    communityRes.setCommunityIdx(communityData[0]);
                    communityRes.setCreatedAt( communityData[1]);
                    communityRes.setUpdatedAt( communityData[2]);
                    communityRes.setContent( communityData[3]);
                    communityRes.setUser(userRepository.findById(((BigInteger) communityData[4]).longValue()).orElse(null));
                    communityRes.setLike(communityData[5]);

                    return communityRes;
                })
                .collect(Collectors.toList());
    }

    // 치과 저장 목록 조회
    public List<HospitalRes.HospitalListRes> getMyHospitalList(Long userId, Pageable page) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USERS_EMPTY_USER_ID));

        List<Object[]> scrap = scrapRepository.MyScrapList(user, page);

        return scrap.stream()
                .map(hospitalData -> {
                    HospitalRes.HospitalListRes hospitalRes = new HospitalRes.HospitalListRes();
                    hospitalRes.setScrapIdx(hospitalData[0]);
                    hospitalRes.setCreatedAt(hospitalData[1]);
                    hospitalRes.setUpdatedAt( hospitalData[2]);
                    hospitalRes.setUser(userRepository.findById(((Long) hospitalData[3]).longValue()).orElse(null));
                    hospitalRes.setHospital( hospitalData[4]);

                    return hospitalRes;
                })
                .collect(Collectors.toList());
    }

    // 작성한 리뷰 목록 조회 (최신순)

    public List<ReviewRes.ReviewListRes> getMyReviewList(Long userId, Pageable page) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USERS_EMPTY_USER_ID));

        List<Review> reviews = reviewRepository.findReviewByUser(page, user);

        return reviews.stream()
                .map(reviewData -> {
                    ReviewRes.ReviewListRes reviewListRes = new ReviewRes.ReviewListRes();
                    reviewListRes.setReviewIdx(reviewData.getReviewIdx());
                    reviewListRes.setCreatedAt(reviewData.getCreatedAt());
                    reviewListRes.setUpdatedAt(reviewData.getUpdatedAt());
                    reviewListRes.setUser(user);
                    reviewListRes.setContent(reviewData.getContent());
                    reviewListRes.setReceipt(reviewData.isReceipt());
                    reviewListRes.setTotalScore(reviewData.getTotalScore());

                    return reviewListRes;
                })
                .collect(Collectors.toList());
    }

    // 예약 내역 조회 (다가오는 예약 날짜 순?)
    public List<BookingRes.BookingListRes> getMyBookingList(Long userId, Pageable page) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USERS_EMPTY_USER_ID));

        List<Object[]> bookings = bookingRepository.MyBookingList(user, page);

        return bookings.stream()
                .map(bookingData -> {
                    BookingRes.BookingListRes bookingListRes = new BookingRes.BookingListRes();
                    bookingListRes.setBookingIdx(bookingData[0]);
                    bookingListRes.setCreatedAt(bookingData[1]);
                    bookingListRes.setUpdatedAt( bookingData[2]);
                    bookingListRes.setUser(userRepository.findById((Long) bookingData[3]).orElse(null));
                    bookingListRes.setHospital(bookingData[4]);
                    bookingListRes.setMethod(bookingData[5]);
                    bookingListRes.setBookingDate(bookingData[6]);

                    return bookingListRes;
                })
                .collect(Collectors.toList());
    }


}