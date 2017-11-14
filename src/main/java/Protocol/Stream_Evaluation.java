/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Protocol;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Jan.Adamczyk
 */
public class Stream_Evaluation extends ArrayList<Integer> {
    
       /**
     *
     */
    public enum ExceptionChar {

        /**
         *
         */
        EXC_EOT(0x04),
        /**
         *
         */
        EXC_ENQ(0x05),
        /**
         *
         */
        EXC_ACK(0x06),
        /**
         *
         */
        EXC_NAK(0x15),
        /**
         *
         */
        EXC_SYN(0x16),
        /**
         *
         */
        EXC_ETB(0x17),
        /**
         *
         */
        EXC_CAN(0x18),
        /**
         *
         */
        EXC_ETB_OFFSET(0xE0);

        private final int value;

        private ExceptionChar(int value) {
            this.value = value;
        }

        /**
         *
         * @return
         */
        public int getValue() {
            return value;
        }
    }

    private static final int HEADERLENGTH = 3;
    private static final int CHECKSUMLENGTH = 1;

    /**
     *
     * @return
     */
    public Stream_Evaluation removeETBs() {
        if (this.size() > 1) {
            Iterator<Integer> iterator = this.iterator();
            int position = 0;
            while (iterator.hasNext()) {
                Integer element = iterator.next();
                if (element == ExceptionChar.EXC_ETB.getValue()) {
                    iterator.remove();
                    if (iterator.hasNext()) {
                        Integer corrected = (iterator.next() & ((~(ExceptionChar.EXC_ETB_OFFSET.getValue())) & 0xff));
                        this.set(position, corrected);
                    } else {
//                        JOptionPane.showMessageDialog(null, "Communication Error, unexpected end of Stream while EXC Bytes were available");
                        this.clear();
                    }
                }
                position++;
            }
            return this;
        } else {
            return new Stream_Evaluation();
        }
    }

    /**
     *
     * @param bytesFolded
     * @return
     */
    public Boolean checkCorrectness(ArrayList<Integer> bytesFolded) {
        if (this.isEmpty()) {
            return false;
        }
        return (checkChecksum(bytesFolded) && headerEvaluation());
    }

    /**
     *
     * @param bytesFolded
     * @return
     */
    public Boolean checkChecksum(ArrayList<Integer> bytesFolded) {
        int receivedChecksum = (this.get(this.size() - 1) & 0xff);
        int calculatedChecksum = 0;

        for (int i = 0; i < HEADERLENGTH; i++) {
            calculatedChecksum += this.get(i);
        }

        for (int i = HEADERLENGTH; i < (this.size() - CHECKSUMLENGTH); i++) {
            int byte1 = (this.get(i) & 0xff);
            calculatedChecksum += byte1;
            i++;
            int byte2 = this.get(i);
            calculatedChecksum += byte2;

            bytesFolded.add((byte2 << 8) + (byte1));
        }

        calculatedChecksum = ((calculatedChecksum & 0xff) % 256);
        return (receivedChecksum == calculatedChecksum);
    }

    /**
     *
     * @return
     */
    public Boolean headerEvaluation() {
        //PROT_Service (New format)
        int type = this.get(0) & 0xff;
        if(type != 0x26){
            return false;
        }
        
        int header = (this.get(2) << 8) + (this.get(1) & 0xff);
        int channelNum = ((header & 0xfff0) >> 4);
        int streamLength = (channelNum * 2) + CHECKSUMLENGTH;

        return (this.size() == (streamLength + HEADERLENGTH));
    }
    
//    public Boolean headerEvaluation() {
//        //PROT_FW (old format)
//        int header = (this.get(1) << 8) + (this.get(0) & 0xff);
//        int channelNum = ((header & 0xfc00) >> 10);
//        int streamLength = (channelNum * 2) + CHECKSUMLENGTH;
////        int format = ((header & 0x0380) >> 7);
////        int cmd = (header & 0x003f);
////        int extended = ((header & 0x0040) >> 6);
//
//        return (this.size() == (streamLength + HEADERLENGTH));
//    }

    /**
     *
     */
    public void removeHeader() {
        this.removeRange(0, (HEADERLENGTH - 1));
    }
}
