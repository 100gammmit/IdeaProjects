package com.spdrtr.nklcb;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleElementTest {
    @Test
    @DisplayName("채용보상금 String타입에서 int타입으로 추출")
    void rewardStringToInt() {
        int reward = Integer.parseInt("채용보상금 1,000,000원".replaceAll("[^0-9]", ""));

        assertThat(reward).isEqualTo(1000000);
    }

    @Test
    @DisplayName("이미지url 어트리뷰트에서 슬라이싱으로 url만 추출")
    void getImageUrl() {
        String iu = "background-image: url(\"https://image.wanted.co.kr/optimize?src=https%3A%2F%2Fstatic.wanted.co.kr%2Fimages%2Fcompany%2F8567%2Fxzxxblm2ffsn1xdb__400_400.jpg&w=400&q=75\");";

        assertThat(iu.substring(iu.indexOf("(")+2, iu.indexOf(")")-1)).isEqualTo("https://image.wanted.co.kr/optimize?src=https%3A%2F%2Fstatic.wanted.co.kr%2Fimages%2Fcompany%2F8567%2Fxzxxblm2ffsn1xdb__400_400.jpg&w=400&q=75");
    }

    @Test
    String solution(String[] participant, String[] completion) {
        String answer = "";
        List<String> pc = Arrays.stream(participant).toList();
        List<String> cp = Arrays.stream(completion).toList();
        for(String part : pc) {
            int i = 0;
            for(String comp : cp) {
                if(part == comp) {
                    pc.remove(i);
                    cp.remove(i);
                }
                i++;
            }
        }
        return pc.get(0);
    }

}
