package com.spdrtr.nklcb.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Scheduling {
    /**
     * 현재 시간을 "yyyy.MM.dd kk:mm:ss E요일" 형식으로 포멧하여 반환하는 매서드
     * @return String(현재시간)
     */
    public static String getNowDateTime24() {
        //long 타입으로 System.currentTimeMillis() 시스템에서 현재 시간 데이터를 받음
        long time = System.currentTimeMillis();
        //시간 데이터를 포멧하는 양식 설정
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.MM.dd kk:mm:ss E요일");
        //위에서 설정한 양식대로 시간 데이터 String 타입으로 변환
        String str = dayTime.format(new Date(time));
        return str;
    }
}
