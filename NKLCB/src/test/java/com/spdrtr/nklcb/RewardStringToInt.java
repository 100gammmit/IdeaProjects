package com.spdrtr.nklcb;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RewardStringToInt {
    private int reward = Integer.parseInt("채용보상금 1,000,000원".replaceAll("[^0-9]", ""));

    @Test
    void parseFine() {
        assertThat(reward).isEqualTo(1000000);
    }

}
