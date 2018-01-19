package com.example.yangjiyu.myapplication;

/**
 * Created by yangjiyu on 2018/1/15.
 */

public class StuAlarmEvent {
    public static class EventInfo{
        public short usCubeId;
        public byte  byEventType;                //事件编码
        public byte  dwEventID;                  //事件ID来源
        public byte   byUserPri;                  //用户级别
        public byte   [] byDescr = new byte[128];   //事件描述信息
        public short   wYear;
        public byte   byMonth;   /* scope is 1 - 12 */
        public byte   byDay;     /* scope is 1 - 31 */
        public byte   byHour;    /* scope is 0 - 23 */
        public byte   byMinute;  /* scope is 0 - 59 */
        public byte   bySecond;  /* scope is 0 - 59 */
        public byte   byWeek;    /* scope is 1 - 7  */
        public byte   bySlotID;          //单板槽位号
        public byte   abyPara[];        //事件参数信息
    }
    public static class AlarmInfo{
        public short usCubeId;
        public byte  byEventType;                //事件编码
        public byte  dwAlarmID;                  //事件ID来源
        public byte  byGrade;
        public byte  byType;
        public byte   byUserPri;                  //用户级别
        public byte    [] byDescr = new byte[128];   //事件描述信息
        public short   wYear;
        public byte   byMonth;   /* scope is 1 - 12 */
        public byte   byDay;     /* scope is 1 - 31 */
        public byte   byHour;    /* scope is 0 - 23 */
        public byte   byMinute;  /* scope is 0 - 59 */
        public byte   bySecond;  /* scope is 0 - 59 */
        public byte   byWeek;    /* scope is 1 - 7  */
        public byte   bySlotID;          //单板槽位号
        public byte   abyPara[];        //事件参数信息
    }
}
