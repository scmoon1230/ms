package kr.co.ucp.cmns;

import org.springframework.stereotype.Component;

@Component("geoUtil")
public class GeoUtils {
	// 경위도
    // Haversine 공식을 사용하여 두 지점 간의 거리 계산
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0; // 지구 반지름 (단위: km)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c;
        return distance;
    }

    // 두 지점 간의 방위각 계산
    public static double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
        double dLon = lon2 - lon1;

        double x = Math.cos(Math.toRadians(lat2)) * Math.sin(Math.toRadians(dLon));
        double y = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) -
                   Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(dLon));

        double bearing = Math.atan2(x, y);
        bearing = Math.toDegrees(bearing);
        bearing = (bearing + 360) % 360;

        return bearing;
    }

    // 특정위치에서 방위각 방향으로 일정거리에 있는 좌표 구하기
    // 위도,경도,거리,방위각에 대한 좌표
    public static double[] calculateNewCoordinates(double lat, double lon, double distance, double bearing) {
        double R = 6371.0; // 지구 반지름 (단위: km)

        double d = distance / R; // 거리를 반지름으로 나누어 각도로 변환
        double brng = Math.toRadians(bearing);

        double lat1 = Math.toRadians(lat);
        double lon1 = Math.toRadians(lon);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1) * Math.sin(d) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d) * Math.cos(lat1),
                                       Math.cos(d) - Math.sin(lat1) * Math.sin(lat2));

        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);

        return new double[]{lat2, lon2};
    }
}
