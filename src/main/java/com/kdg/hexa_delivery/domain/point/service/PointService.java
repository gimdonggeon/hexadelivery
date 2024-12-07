package com.kdg.hexa_delivery.domain.point.service;

import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.point.entity.Point;
import com.kdg.hexa_delivery.domain.point.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
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
        List<Point> pointList = pointRepository.findAllByUserId(userId);

        if(pointList == null || pointList.isEmpty()) {
            return 0;
        }

        // 만료된 포인트 제거
        changeStatus(pointList);
        pointList = pointList.stream().filter(point -> point.getStatus().equals(Status.NORMAL)).toList();

        // 포인트 사용
        // 사용하려는 포인트보다 적은 포인트가 있으면 있는 포인트만 사용
        Integer usedPoint = 0;

        for(Point point : pointList) {
            if(pointDiscount > point.getPointPresentAmount()){
                // 현재 포인트 제거
                point.discountPointAmount(point.getPointPresentAmount());
                point.updateStatus2Delete();
                // 총 사용 포인트에 저장
                usedPoint += point.getPointPresentAmount();
                // 사용하려는 포인트에서 차감
                pointDiscount -= point.getPointPresentAmount();
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
    public Integer getMyPoint(Long userId) {
        // 포인트 조회
        return pointRepository.findByUserIdToPointAmount(userId, LocalDateTime.now());
    }


    // 만료된 포인트 제거 메서드
    private void changeStatus(List<Point> points){
        // 포인트 만료기간 상태 변경
        LocalDateTime now = LocalDateTime.now();
        for (Point point : points) {
            if(now.isAfter(point.getExpirationTime())){
                point.updateStatus2Delete();
            }
        }
    }
}
