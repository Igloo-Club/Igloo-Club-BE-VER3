package com.igloo_club.nungil_v3.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class CookieUtil {

        /**
         * 요청값(이름, 값, 만료기간)을 바탕으로 HttpOnly 쿠키를 추가하는 메서드이다.
         */
        public static void addHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAge) {
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            cookie.setMaxAge(maxAge);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);
        }

        public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            cookie.setMaxAge(maxAge);

            response.addCookie(cookie);
        }
        public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return;
            }

            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        public static String serialize(Object obj) {
            return Base64.getUrlEncoder()
                    .encodeToString(SerializationUtils.serialize(obj));
        }

        public static <T> T deserialize(Cookie cookie, Class<T> cls) {
            return cls.cast(
                    SerializationUtils.deserialize(
                            Base64.getUrlDecoder().decode(cookie.getValue())
                    )
            );
        }

    }
