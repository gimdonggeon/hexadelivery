package com.kdg.hexa_delivery.domain.SocialLogin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.domain.user.repository.UserRepository;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.WrongAccessException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

@Service
@Slf4j
public class KakaoLoginService {

    private final KakaoApi kakaoApi;
    private final UserRepository userRepository;

    @Autowired
    public KakaoLoginService(KakaoApi kakaoApi, UserRepository userRepository) {
        this.kakaoApi = kakaoApi;
        this.userRepository = userRepository;
    }

    public String getAccessToken(String code) throws IOException, ParseException {
        String accessToken = "";
        String refreshToken = "";
        String requestUrl = "https://kauth.kakao.com/oauth/token";

        try {
            //java와 url 간의 연결 관련 설정
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //필수 헤더 세팅
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true); //OutputStream 으로 POST 데이터를 넘겨주겠다는 옵션 ->  OutputStream Byte 기반 최상위 출력 추상클래스

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            //필수 쿼리 파라미터 세팅
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(kakaoApi.getKakaoApiKey());
            sb.append("&redirect_uri=").append(kakaoApi.getKakaoRedirectUri());
            sb.append("&code=").append(code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getAccessToken] responseCode = {}", responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("responseBody = {}", result);

            JsonElement element = JsonParser.parseString(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return accessToken;
    }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String postURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(postURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return userInfo;
    }

    @Cacheable(value = "KakaoUserId", key = "#accessToken")
    public Long cacheKakaoUserIdWithEmail (String email, String accessToken) {
        User user = userRepository.findByEmailOrElseThrow(email);
        return user.getId();
    }

    @CacheEvict(value = "KakaoUserId", key = "#accessToken")
    public void kakaoLogout(String accessToken) throws WrongAccessException, IOException {
        String reqUrl = "https://kapi.kakao.com/v1/user/logout";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        log.info("[KakaoApi.kakaoLogout] responseCode : {}", responseCode);

        BufferedReader br;
        if (responseCode >= 200 && responseCode <= 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            throw new WrongAccessException(ExceptionType.WRONG_TOKEN);
        }

        String line = "";
        StringBuilder responseSb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            responseSb.append(line);
        }
        String result = responseSb.toString();
        log.info("kakao logout - responseBody = {}", result);

    }
}
