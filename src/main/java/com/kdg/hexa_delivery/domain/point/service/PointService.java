package com.kdg.hexa_delivery.domain.point.service;

import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.point.dto.PointResponseDto;
import com.kdg.hexa_delivery.domain.point.dto.TotalPointResponseDto;
import com.kdg.hexa_delivery.domain.point.entity.Point;
import com.kdg.hexa_delivery.domain.point.repository.PointRepository;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @org.springframework.core.annotation.Order(2)
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void clearPoints() {
        log.info("Clear points");
        List<Point> points = pointRepository.findAlByNORMAL();

        // 포인트 만료기간 상태 변경
        LocalDate now = LocalDate.now();
        for (Point point : points) {
            if(now.isAfter(point.getExpirationTime())){
                point.updateStatus2Delete();
            }
        }

        pointRepository.saveAll(points);
    }

    /**
     * 포인트 적립 메서드
     *
     * @param order 주문 정보
     */
    @Transactional
    public void addPoint(Order order) {
        // 포인트 3% 계산
        Integer pointAmount = (int) (order.getTotalPrice()*0.03);

        // 포인트 생성
        Point point = new Point(order, order.getUser(), pointAmount);

        // 포인트 저장
        pointRepository.save(point);
    }

    /**
     * 포인트 사용 메서드
     *
     * @param pointDiscount 차감하려는 포인트
     * @param userId 유저 id
     */
    @Transactional
    public Integer usePoint(Integer pointDiscount, Long userId) {
        List<Point> pointList = pointRepository.findAllByUserId(userId, LocalDate.now());

        if(pointList == null || pointList.isEmpty()) {
            return 0;
        }

        // 포인트 사용
        // 사용하려는 포인트보다 적은 포인트가 있으면 있는 포인트만 사용
        int usedPoint = 0;

        for(Point point : pointList) {
            if(pointDiscount > point.getPointPresentAmount()){
                // 총 사용 포인트에 저장
                usedPoint += point.getPointPresentAmount();
                // 사용하려는 포인트에서 차감
                pointDiscount -= point.getPointPresentAmount();
                // 현재 포인트 제거
                point.discountPointAmount(point.getPointPresentAmount());
                point.updateStatus2Delete();
            }
            else if(pointDiscount < point.getPointPresentAmount()){
                // 현재 포인트 제거
                point.discountPointAmount(pointDiscount);
                // 총 사용 포인트에 저장
                usedPoint += pointDiscount;
                break;
            }
            else {
                point.updateStatus2Delete();
                usedPoint += pointDiscount;
                break;
            }
        }

        return usedPoint;
    }

    /**
     * 유저 전체 포인트 조회 메서드
     *
     * @param userId  유저의 Id
     *
     * @return 유저의 전체 포인트
     */
    public TotalPointResponseDto getMyPoint(Long userId) {
        // 포인트 조회
        return new TotalPointResponseDto(pointRepository.findByUserIdToPointAmount(userId, LocalDate.now()));
    }


    /**
     * 유저 포인트 목록 조회 메서드
     *
     * @param userId  유저의 Id
     *
     * @return 유저의 전체 포인트 목록
     */
    public List<PointResponseDto> getMyPointList(Long userId) {
        List<Point> points = pointRepository.findAllByUserId(userId, LocalDate.now());

        if(points == null || points.isEmpty()) {
            throw new NotFoundException(ExceptionType.POINT_NOT_FOUND);
        }

        return points.stream().map(PointResponseDto::toDto).toList();
    }
}
