package com.example.yangjiyu.myapplication;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import engine.CpComm;
import typeConversion.ByteUtil;

/**
 * Created by yangjiyu on 2017/12/13.
 */

public class ComStruc {

    public static class Stu_Open_Window implements Serializable {
        public byte getWinId() {
            return winId;
        }

        public void setWinId(byte winId) {
            this.winId = winId;
        }

        private byte winId;

        public byte getInputId() {
            return inputId;
        }

        public void setInputId(byte inputId) {
            this.inputId = inputId;
        }

        private byte inputId;

        public byte getSig() {
            return sig;
        }

        public void setSig(byte sig) {
            this.sig = sig;
        }

        private byte sig;

        public byte getHigh_startX() {
            return high_startX;
        }

        public void setHigh_startX(byte high_startX) {
            this.high_startX = high_startX;
        }

        private byte high_startX;

        public byte getLow_startX() {
            return low_startX;
        }

        public void setLow_startX(byte low_startX) {
            this.low_startX = low_startX;
        }

        private byte low_startX;

        public byte getHigh_startY() {
            return high_startY;
        }

        public void setHigh_startY(byte high_startY) {
            this.high_startY = high_startY;
        }

        private byte high_startY;

        public byte getLow_startY() {
            return low_startY;
        }

        public void setLow_startY(byte low_startY) {
            this.low_startY = low_startY;
        }

        private byte low_startY;

        public byte getWidth_high_X() {
            return width_high_X;
        }

        public void setWidth_high_X(byte width_high_X) {
            this.width_high_X = width_high_X;
        }

        private byte width_high_X;

        public byte getWidth_low_X() {
            return width_low_X;
        }

        public void setWidth_low_X(byte width_low_X) {
            this.width_low_X = width_low_X;
        }

        private byte width_low_X;

        public byte getWidth_high_Y() {
            return width_high_Y;
        }

        public void setWidth_high_Y(byte width_high_Y) {
            this.width_high_Y = width_high_Y;
        }

        private byte width_high_Y;

        public byte getWidth_low_Y() {
            return width_low_Y;
        }

        public void setWidth_low_Y(byte width_low_Y) {
            this.width_low_Y = width_low_Y;
        }

        private byte width_low_Y;
    }

    public static class ExchangeInterfaceStatus{
        public CpComm.stuDlpQInterfaceStatus ExchangeInterfaceStatus(Vector<Byte> vecResponse) throws IOException
        {

            CpComm.stuDlpQInterfaceStatus stInfo = new CpComm.stuDlpQInterfaceStatus();
            Iterator<Byte> iterBegin = vecResponse.iterator();

            stInfo.ucTemperature = iterBegin.next();
            stInfo.ucHumidity = iterBegin.next();
            byte []b=new byte[2];
            b[0]=iterBegin.next();
            b[1]=iterBegin.next();
            stInfo.usFan1= ByteUtil.bntohs(b);
            stInfo.ucPower1=iterBegin.next();
            stInfo.ucPower2=iterBegin.next();

            return stInfo;
        }
    }
    public static class ExchangeInterfaceVersion{
        private String GetVersion(int nM, int nS,int nModify)
        {
            String strVer="";
            String strTemp="";
            strVer = strVer+String.format("%d.", nM);

            if ((int)nS<10 )
                strTemp +=String.format("0%d", nS);
            else
                strTemp +=String.format("%d", nS);
            strVer += strTemp;

            strTemp="";
            if ((int)nModify<10 )
                strTemp +=String.format(".0%d", nModify);
            else
                strTemp +=String.format(".%d",  nModify);

            strVer += strTemp;

            if (0==nM && 0==nS && 0==nModify)
            {
                strVer = "";
            }
            return strVer;
        }
        private String GetVCL3Version(int nM, int nS)
        {
            String strTemp="";
            if ((int)nS<10)
                strTemp+=String.format("%c.0%x",nM, nS);
            else
                strTemp+=String.format("%c.%x", nM, nS);
            if (0==nM && 0==nS)
            {
                strTemp = "";
            }
            return strTemp;
        }
        public CpComm.stuDlpQInterfaceVersion ExchangeInterfaceVersion(Vector<Byte> vecResponse) throws IOException
        {

            CpComm.stuDlpQInterfaceVersion stInfo = new CpComm.stuDlpQInterfaceVersion();
            Iterator<Byte> iterBegin = vecResponse.iterator();

            byte ucMainver;
            byte ucSubver;
            byte ucModifyver;

            byte ucFPGver;
            byte ucFPGAver;

            byte ucIndex = 0;
            byte charVersion=0;
            byte HW_MODEL_LEN = 32;

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            String string = GetVersion(ucMainver,ucSubver,ucModifyver);
            stInfo.stMain.strMcu = string;

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stMain.strFpga = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucFPGver=iterBegin.next();
            ucFPGAver=iterBegin.next();
            stInfo.stMain.strPcb = GetVCL3Version(ucFPGver,ucFPGAver);

            for(ucIndex = 0;ucIndex <HW_MODEL_LEN ;ucIndex++)
            {
                charVersion = iterBegin.next();
                stInfo.stMain.strType += String.format("%c", charVersion);
            }


            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSIn1.strMcu = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSIn1.strFpga = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucFPGver=iterBegin.next();
            ucFPGAver=iterBegin.next();
            stInfo.stSIn1.strPcb = GetVCL3Version(ucFPGver,ucFPGAver);

            for(ucIndex = 0;ucIndex <HW_MODEL_LEN ;ucIndex++)
            {
                charVersion = iterBegin.next();
                stInfo.stSIn1.strType += String.format("%c", charVersion);
            }

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSIn2.strMcu = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSIn2.strFpga = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucFPGver=iterBegin.next();
            ucFPGAver=iterBegin.next();
            stInfo.stSIn2.strPcb = GetVCL3Version(ucFPGver,ucFPGAver);

            for(ucIndex = 0;ucIndex <HW_MODEL_LEN ;ucIndex++)
            {
                charVersion = iterBegin.next();
                stInfo.stSIn2.strType += String.format("%c", charVersion);
            }

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSIn3.strMcu = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSIn3.strFpga = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucFPGver=iterBegin.next();
            ucFPGAver=iterBegin.next();
            stInfo.stSIn3.strPcb = GetVCL3Version(ucFPGver,ucFPGAver);

            for(ucIndex = 0;ucIndex <HW_MODEL_LEN ;ucIndex++)
            {
                charVersion = iterBegin.next();
                stInfo.stSIn3.strType += String.format("%c", charVersion);
            }

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSIn4.strMcu = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSIn4.strFpga = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucFPGver=iterBegin.next();
            ucFPGAver=iterBegin.next();
            stInfo.stSIn4.strPcb = GetVCL3Version(ucFPGver,ucFPGAver);

            for(ucIndex = 0;ucIndex <HW_MODEL_LEN ;ucIndex++)
            {
                charVersion = iterBegin.next();
                stInfo.stSIn4.strType += String.format("%c", charVersion);
            }

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSOut1.strMcu = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSOut1.strFpga = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucFPGver=iterBegin.next();
            ucFPGAver=iterBegin.next();
            stInfo.stSOut1.strPcb = GetVCL3Version(ucFPGver,ucFPGAver);

            for(ucIndex = 0;ucIndex <HW_MODEL_LEN ;ucIndex++)
            {
                charVersion = iterBegin.next();
                stInfo.stSOut1.strType += String.format("%c", charVersion);
            }

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSOut2.strMcu = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSOut2.strFpga = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucFPGver=iterBegin.next();
            ucFPGAver=iterBegin.next();
            stInfo.stSOut2.strPcb = GetVCL3Version(ucFPGver,ucFPGAver);

            for(ucIndex = 0;ucIndex <HW_MODEL_LEN ;ucIndex++)
            {
                charVersion = iterBegin.next();
                stInfo.stSOut2.strType += String.format("%c", charVersion);
            }

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSOut3.strMcu = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucMainver=iterBegin.next();
            ucSubver=iterBegin.next();
            ucModifyver=iterBegin.next();
            stInfo.stSOut3.strFpga = GetVersion(ucMainver,ucSubver,ucModifyver);

            ucFPGver=iterBegin.next();
            ucFPGAver=iterBegin.next();
            stInfo.stSOut3.strPcb = GetVCL3Version(ucFPGver,ucFPGAver);

            for(ucIndex = 0;ucIndex <HW_MODEL_LEN ;ucIndex++)
            {
                charVersion = iterBegin.next();
                stInfo.stSOut3.strType += String.format("%c", charVersion);
            }

            return stInfo;
        }
    }
}
