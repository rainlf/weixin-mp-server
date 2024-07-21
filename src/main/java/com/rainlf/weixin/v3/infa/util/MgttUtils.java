package com.rainlf.weixin.v3.infa.util;

import java.util.Random;
import java.util.UUID;

/**
 * @author rain
 * @date 7/21/2024 5:35 PM
 */
public class MgttUtils {
    private static final Random random = new Random();

    public static String getMjUUID() {
        return "mj-" + UUID.randomUUID().toString().replace("-", "");
    }

    public static Integer getRandAward() {
        int randomInte = random.nextInt(100) + 1;
        if (randomInte <= 50) {
            return 1;
        } else if (randomInte <= 70) {
            return 2;
        } else if (randomInte <= 90) {
            return 3;
        } else if (randomInte <= 99) {
            return 5;
        }
        return 100;
    }
}
