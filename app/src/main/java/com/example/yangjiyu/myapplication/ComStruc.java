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


}
